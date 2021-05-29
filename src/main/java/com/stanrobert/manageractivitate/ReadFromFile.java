package com.stanrobert.manageractivitate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadFromFile
{
    private AskForWritePermission askPermissionComponent;
    private Data dataComponent;
    private ErrorBuilder errorBuilderComponent;
    private TableUI tableComponent;

    public ReadFromFile(AskForWritePermission askPermissionComponent, Data dataComponent, TableUI tableComponent, ErrorBuilder errorBuilderComponent)
    {
        this.askPermissionComponent = askPermissionComponent;
        this.dataComponent = dataComponent;
        this.tableComponent = tableComponent;
        this.errorBuilderComponent = errorBuilderComponent;
    }

    public void loadFromFile()
    {
        askPermissionComponent.askForWritePermission();
        askPermissionComponent.verifyIfFolderExist();

        if (dataComponent.getSavedFile(dataComponent.getCurrentMonth()).exists())
        {
            writeDataFromFileToTable(dataComponent.getSavedFile(dataComponent.getCurrentMonth()));
        }
        else
        {
            if (dataComponent.getSavedBackupFile(dataComponent.getCurrentMonth()).exists())
            {
                writeDataFromFileToTable(dataComponent.getSavedBackupFile(dataComponent.getCurrentMonth()));
            }
        }
    }

    public void writeDataFromFileToTable(File file)
    {
        String line;
        String[] parts;
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            while ((line = br.readLine()) != null)
            {
                parts = line.split("\\ ----- ");
                tableComponent.addRow(true, parts[0], parts[1], parts[2], parts[3], parts[4]);
                dataComponent.addMoney(parts[1], parts[2], parts[3]);
            }
            br.close();
            tableComponent.displayCalculations();
        }
        catch (IOException e)
        {
            errorBuilderComponent.errorConstructor("incarcare fisier");
        }
    }
}