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
    private ApplicationUI applicationUIComponent;

    private boolean canWriteToFile = true;

    public ButtonsUI(Data dataComponent, MainActivity mainActivityComponent, TableUI tableComponent, WriteToFile writeFileComponent, ReadFromFile readFileComponent, ApplicationUI applicationUIComponent)
    {
        this.dataComponent = dataComponent;
        this.mainActivityComponent = mainActivityComponent;
        this.tableComponent = tableComponent;
        this.writeFileComponent = writeFileComponent;
        this.readFileComponent = readFileComponent;
        this.applicationUIComponent = applicationUIComponent;

        applicationUIComponent.mainLabel.setOnClickListener(this);
        applicationUIComponent.addButton.setOnClickListener(this);
        applicationUIComponent.deleteButton.setOnClickListener(this);
        applicationUIComponent.backupButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnAdd:
                hideKeyboard();

                if (applicationUIComponent.addButton.getText() == "Adauga")
                {
                    if (applicationUIComponent.dateText.length() > 0 && applicationUIComponent.hoursAtWorkText.length() > 2 && applicationUIComponent.hoursAtWorkText.getText().toString().contains("-") && applicationUIComponent.breakText.length() > 0 && applicationUIComponent.moneyPerHourText.length() > 0)
                    {
                        tableComponent.addRow(false, null, null, null, null, null);
                        dataComponent.addMoney(applicationUIComponent.hoursAtWorkText.getText().toString(), applicationUIComponent.breakText.getText().toString(), applicationUIComponent.moneyPerHourText.getText().toString());

                        tableComponent.displayCalculations();
                        tableComponent.clearFields();

                        if(canWriteToFile)
                        {
                            writeFileComponent.saveToFile();
                        }
                    }
                }
                else
                {
                    for (int i = applicationUIComponent.tableGUI.getChildCount() - 1; i >= 0; i--)
                    {
                        if (((ColorDrawable) applicationUIComponent.tableGUI.getChildAt(i).getBackground()).getColor() == Color.rgb(230, 100, 100))
                        {
                            dataComponent.substractMoney(i);

                            if (applicationUIComponent.hoursAtWorkText.length() > 2 && applicationUIComponent.hoursAtWorkText.getText().toString().contains("-"))
                            {
                                ((TextView) ((TableRow) applicationUIComponent.tableGUI.getChildAt(i)).getChildAt(1)).setText(applicationUIComponent.hoursAtWorkText.getText());
                            }
                            if (applicationUIComponent.breakText.length() > 0)
                            {
                                ((TextView) ((TableRow) applicationUIComponent.tableGUI.getChildAt(i)).getChildAt(2)).setText(applicationUIComponent.breakText.getText());
                            }
                            if (applicationUIComponent.moneyPerHourText.length() > 0)
                            {
                                ((TextView) ((TableRow) applicationUIComponent.tableGUI.getChildAt(i)).getChildAt(3)).setText(applicationUIComponent.moneyPerHourText.getText());
                            }

                            dataComponent.addMoney(((TextView) ((TableRow) applicationUIComponent.tableGUI.getChildAt(i)).getChildAt(1)).getText().toString(), ((TextView) ((TableRow) applicationUIComponent.tableGUI.getChildAt(i)).getChildAt(2)).getText().toString(), ((TextView) ((TableRow) applicationUIComponent.tableGUI.getChildAt(i)).getChildAt(3)).getText().toString());
                            ((TextView) ((TableRow) applicationUIComponent.tableGUI.getChildAt(i)).getChildAt(4)).setText(String.valueOf(dataComponent.calculateTotalMoney(((TextView) ((TableRow) applicationUIComponent.tableGUI.getChildAt(i)).getChildAt(1)).getText().toString(), ((TextView) ((TableRow) applicationUIComponent.tableGUI.getChildAt(i)).getChildAt(2)).getText().toString(), ((TextView) ((TableRow) applicationUIComponent.tableGUI.getChildAt(i)).getChildAt(3)).getText().toString())));
                        }
                    }

                    tableComponent.displayCalculations();
                    tableComponent.clearFields();

                    if(canWriteToFile)
                    {
                        writeFileComponent.saveToFile();
                    }
                }
                break;

            case R.id.btnRemove:
                hideKeyboard();

                if (applicationUIComponent.tableGUI.getChildCount() > 1)
                {
                    tableComponent.deleteRow();

                    if (!tableComponent.areRowsSelected())
                    {
                        applicationUIComponent.addButton.setText("Adauga");
                        applicationUIComponent.deleteButton.setEnabled(false);
                    }

                    if(canWriteToFile)
                    {
                        writeFileComponent.saveToFile();
                    }
                }
                break;

            case R.id.btnBackup:
                hideKeyboard();

                File customBackupFile = new File(mainActivityComponent.getApplicationInfo().dataDir + "/backup " + applicationUIComponent.dateText.getText().toString() + ".txt");
                File customCurrentMonthFile = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/ManagerActivitate/" + applicationUIComponent.dateText.getText().toString() + ".txt");

                if (customCurrentMonthFile.exists())
                {
                    tableComponent.deleteAllRows();
                    readFileComponent.writeDataFromFileToTable(customCurrentMonthFile);
                    canWriteToFile = false;
                }
                else
                {
                    if (customBackupFile.exists())
                    {
                        tableComponent.deleteAllRows();
                        readFileComponent.writeDataFromFileToTable(customBackupFile);
                        canWriteToFile = false;
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