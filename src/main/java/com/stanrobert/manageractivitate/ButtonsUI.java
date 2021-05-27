package com.stanrobert.manageractivitate;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TableRow;
import android.widget.TextView;
import java.io.File;

public class ButtonsUI implements View.OnClickListener
{
    private Data dataComponent;
    private MainActivity mainActivityComponent;
    private TableUI tableComponent;
    private WriteToFile writeFileComponent;
    private ReadFromFile readFileComponent;

    public ButtonsUI(Data dataComponent, MainActivity mainActivityComponent, TableUI tableComponent, WriteToFile writeFileComponent, ReadFromFile readFileComponent)
    {
        this.dataComponent = dataComponent;
        this.mainActivityComponent = mainActivityComponent;
        this.tableComponent = tableComponent;
        this.writeFileComponent = writeFileComponent;
        this.readFileComponent = readFileComponent;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnAdd:
                hideKeyboard();

                if (mainActivityComponent.addButton.getText() == "Adauga")
                {
                    if (mainActivityComponent.dateText.length() > 0 && mainActivityComponent.hoursAtWorkText.length() > 2 && mainActivityComponent.hoursAtWorkText.getText().toString().contains("-") && mainActivityComponent.breakText.length() > 0 && mainActivityComponent.moneyPerHourText.length() > 0)
                    {
                        tableComponent.addRow(false, null, null, null, null, null);
                        dataComponent.addMoney(mainActivityComponent.hoursAtWorkText.getText().toString(), mainActivityComponent.breakText.getText().toString(), mainActivityComponent.moneyPerHourText.getText().toString());

                        tableComponent.displayCalculations();
                        tableComponent.clearFields();

                        writeFileComponent.saveToFile();
                    }
                }
                else
                {
                    for (int i = mainActivityComponent.tableGUI.getChildCount() - 1; i >= 0; i--)
                    {
                        if (((ColorDrawable) mainActivityComponent.tableGUI.getChildAt(i).getBackground()).getColor() == Color.rgb(230, 100, 100))
                        {
                            dataComponent.substractMoney(i);

                            if (mainActivityComponent.hoursAtWorkText.length() > 2 && mainActivityComponent.hoursAtWorkText.getText().toString().contains("-"))
                            {
                                ((TextView) ((TableRow) mainActivityComponent.tableGUI.getChildAt(i)).getChildAt(1)).setText(mainActivityComponent.hoursAtWorkText.getText());
                            }
                            if (mainActivityComponent.breakText.length() > 0)
                            {
                                ((TextView) ((TableRow) mainActivityComponent.tableGUI.getChildAt(i)).getChildAt(2)).setText(mainActivityComponent.breakText.getText());
                            }
                            if (mainActivityComponent.moneyPerHourText.length() > 0)
                            {
                                ((TextView) ((TableRow) mainActivityComponent.tableGUI.getChildAt(i)).getChildAt(3)).setText(mainActivityComponent.moneyPerHourText.getText());
                            }

                            dataComponent.addMoney(((TextView) ((TableRow) mainActivityComponent.tableGUI.getChildAt(i)).getChildAt(1)).getText().toString(), ((TextView) ((TableRow) mainActivityComponent.tableGUI.getChildAt(i)).getChildAt(2)).getText().toString(), ((TextView) ((TableRow) mainActivityComponent.tableGUI.getChildAt(i)).getChildAt(3)).getText().toString());
                            ((TextView) ((TableRow) mainActivityComponent.tableGUI.getChildAt(i)).getChildAt(4)).setText(String.valueOf(dataComponent.calculateTotalMoney(((TextView) ((TableRow) mainActivityComponent.tableGUI.getChildAt(i)).getChildAt(1)).getText().toString(), ((TextView) ((TableRow) mainActivityComponent.tableGUI.getChildAt(i)).getChildAt(2)).getText().toString(), ((TextView) ((TableRow) mainActivityComponent.tableGUI.getChildAt(i)).getChildAt(3)).getText().toString())));
                        }
                    }

                    tableComponent.displayCalculations();
                    tableComponent.clearFields();
                    writeFileComponent.saveToFile();
                }
                break;

            case R.id.btnRemove:
                hideKeyboard();

                if (mainActivityComponent.tableGUI.getChildCount() > 1)
                {
                    tableComponent.deleteRow();
                    if (!tableComponent.areRowsSelected())
                    {
                        mainActivityComponent.addButton.setText("Adauga");
                        mainActivityComponent.deleteButton.setEnabled(false);
                    }
                }
                break;

            case R.id.btnBackup:
                hideKeyboard();

                File backupFile = new File(mainActivityComponent.getApplicationInfo().dataDir + "/backup " + mainActivityComponent.dateText.getText().toString() + ".txt");
                File currentMonthFile = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/ManagerActivitate/" + mainActivityComponent.dateText.getText().toString() + ".txt");
                if (currentMonthFile.exists())
                {
                    tableComponent.deleteAllRows();
                    readFileComponent.writeDataFromFileToTable(currentMonthFile);
                }
                else
                {
                    if (backupFile.exists())
                    {
                        tableComponent.deleteAllRows();
                        readFileComponent.writeDataFromFileToTable(backupFile);
                    }
                }
                break;
            case R.id.lblMain:
                hideKeyboard();
                break;
        }
    }

    private void hideKeyboard()
    {
        InputMethodManager inputManager = (InputMethodManager) mainActivityComponent.getSystemService(mainActivityComponent.getApplicationContext().INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow((null == mainActivityComponent.getCurrentFocus()) ? null : mainActivityComponent.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}