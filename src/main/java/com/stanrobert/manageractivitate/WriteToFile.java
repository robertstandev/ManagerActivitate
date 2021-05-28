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
    private ErrorBuilder errorBuilderComponent;
    private ApplicationUI applicationUIComponent;

    public WriteToFile(AskForWritePermission askPermissionComponent, Data dataComponent, ErrorBuilder errorBuilderComponent, ApplicationUI applicationUIComponent)
    {
        this.askPermissionComponent = askPermissionComponent;
        this.dataComponent = dataComponent;
        this.errorBuilderComponent = errorBuilderComponent;
        this.applicationUIComponent = applicationUIComponent;
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
            for (int i = 1; i <= (applicationUIComponent.tableGUI.getChildCount() - 1); i++)
            {
                View columnData = ((TableRow) applicationUIComponent.tableGUI.getChildAt(i)).getChildAt(0);
                View columnHoursAtWork = ((TableRow) applicationUIComponent.tableGUI.getChildAt(i)).getChildAt(1);
                View columnBreakHours = ((TableRow) applicationUIComponent.tableGUI.getChildAt(i)).getChildAt(2);
                View columnMoneyPerHour = ((TableRow) applicationUIComponent.tableGUI.getChildAt(i)).getChildAt(3);
                View columnMoneyPerDay = ((TableRow) applicationUIComponent.tableGUI.getChildAt(i)).getChildAt(4);

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