package com.stanrobert.manageractivitate;

import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class ButtonsUI implements View.OnClickListener
{
    private Data dataComponent;
    private MainActivity mainActivityComponent;
    private TableUI tableComponent;
    private WriteToFile writeFileComponent;
    private ReadFromFile readFileComponent;
    private ApplicationUI applicationUIComponent;

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
                    tableComponent.createRow();
                }
                else
                {
                    tableComponent.modifyRow();
                }

                tableComponent.displayCalculations();
                tableComponent.clearFields();

                writeFileComponent.saveToFile();
                writeFileComponent.takeScreenShot();

                break;

            case R.id.btnRemove:
                hideKeyboard();

                tableComponent.deleteRow();

                writeFileComponent.saveToFile();
                writeFileComponent.takeScreenShot();

                break;

            case R.id.btnBackup:
                hideKeyboard();
                loadPreviousMonth();

                break;
            case R.id.lblMain:
                hideKeyboard();

                break;
        }
    }

    private void loadPreviousMonth()
    {

        if (dataComponent.getSavedFile(applicationUIComponent.dateText.getText().toString()).exists())
        {
            prepareForLoad();
            readFileComponent.writeDataFromFileToTable(dataComponent.getSavedFile(applicationUIComponent.dateText.getText().toString()));
        }
        else if (dataComponent.getSavedBackupFile(applicationUIComponent.dateText.getText().toString()).exists())
        {
            prepareForLoad();
            readFileComponent.writeDataFromFileToTable(dataComponent.getSavedBackupFile(applicationUIComponent.dateText.getText().toString()));
        }
    }

    private void prepareForLoad()
    {
        dataComponent.setLoadedDate(applicationUIComponent.dateText.getText().toString());
        tableComponent.deleteAllRows();
    }

    private void hideKeyboard()
    {
        InputMethodManager inputManager = (InputMethodManager) mainActivityComponent.getSystemService(mainActivityComponent.getApplicationContext().INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow((null == mainActivityComponent.getCurrentFocus()) ? null : mainActivityComponent.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}