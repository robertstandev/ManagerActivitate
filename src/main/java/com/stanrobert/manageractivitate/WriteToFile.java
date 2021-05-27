package com.stanrobert.manageractivitate;

import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class WriteToFile
{
    private AskForWritePermission askPermissionComponent;
    private Data dataComponent;
    private MainActivity mainActivityComponent;
    private ErrorBuilder errorBuilderComponent;

    public WriteToFile(AskForWritePermission askPermissionComponent, Data dataComponent, MainActivity mainActivityComponent, ErrorBuilder errorBuilderComponent)
    {
        this.askPermissionComponent = askPermissionComponent;
        this.dataComponent = dataComponent;
        this.mainActivityComponent = mainActivityComponent;
        this.errorBuilderComponent = errorBuilderComponent;
    }

    public void saveToFile()
    {
        askPermissionComponent.askForWritePermission();
        askPermissionComponent.verifyIfFolderExist();

        writeFile(dataComponent.getCurrentMonthFile());
        writeFile(dataComponent.getBackupFile());
    }

    public void writeFile(File file)
    {
        String str = "";
        try
        {
            for (int i = 1; i <= (mainActivityComponent.tableGUI.getChildCount() - 1); i++)
            {
                View columnData = ((TableRow) mainActivityComponent.tableGUI.getChildAt(i)).getChildAt(0);
                View columnHoursAtWork = ((TableRow) mainActivityComponent.tableGUI.getChildAt(i)).getChildAt(1);
                View columnBreakHours = ((TableRow) mainActivityComponent.tableGUI.getChildAt(i)).getChildAt(2);
                View columnMoneyPerHour = ((TableRow) mainActivityComponent.tableGUI.getChildAt(i)).getChildAt(3);
                View columnMoneyPerDay = ((TableRow) mainActivityComponent.tableGUI.getChildAt(i)).getChildAt(4);

                str = str + ((TextView) columnData).getText().toString() + " ----- " + ((TextView) columnHoursAtWork).getText().toString() + " ----- " + ((TextView) columnBreakHours).getText().toString() + " ----- " + ((TextView) columnMoneyPerHour).getText().toString() + " ----- " + ((TextView) columnMoneyPerDay).getText().toString() + "\n";
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(str);
            writer.close();
        }
        catch (Exception e)
        {
            errorBuilderComponent.errorConstructor("scriere fisier");
        }
    }
}