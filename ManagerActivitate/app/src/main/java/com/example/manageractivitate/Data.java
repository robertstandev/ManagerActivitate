package com.example.manageractivitate;

import android.os.Environment;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Data
{
    private MainActivity mainActivityComponent;
    private ErrorBuilder errorBuilderComponent;
    private ApplicationUI applicationUIComponent;

    private double totalHoursAtWork = 0.0;
    private double totalBreak = 0.0;
    private double totalMoney = 0.0;

    private String loadedDate;

    public Data (MainActivity mainActivityComponent, ErrorBuilder errorBuilderComponent, ApplicationUI applicationUIComponent)
    {
        this.mainActivityComponent = mainActivityComponent;
        this.errorBuilderComponent = errorBuilderComponent;
        this.applicationUIComponent = applicationUIComponent;

        loadedDate = getCurrentMonth();
    }

    public void addMoney(String hoursAtWorkTime, String breakTime, String moneyPerHour)
    {
        try
        {
            totalMoney = totalMoney + calculateTotalMoney(hoursAtWorkTime, breakTime, moneyPerHour);
            totalHoursAtWork = totalHoursAtWork + calculateHoursAtWork(hoursAtWorkTime);
            totalBreak = totalBreak + convertUserInputFormatToDouble(breakTime);

        }
        catch (Exception e)
        {
            errorBuilderComponent.errorConstructor("la calcul");
        }
    }

    public void substractMoney(int i)
    {
        View columnHoursAtWork = ((TableRow) applicationUIComponent.tableGUI.getChildAt(i)).getChildAt(1);
        View columnBreakHours = ((TableRow) applicationUIComponent.tableGUI.getChildAt(i)).getChildAt(2);
        View columnMoneyPerHour = ((TableRow) applicationUIComponent.tableGUI.getChildAt(i)).getChildAt(3);

        totalMoney = totalMoney - calculateTotalMoney(((TextView) columnHoursAtWork).getText().toString(), ((TextView) columnBreakHours).getText().toString(), ((TextView) columnMoneyPerHour).getText().toString());
        totalHoursAtWork = totalHoursAtWork - calculateHoursAtWork(((TextView) columnHoursAtWork).getText().toString());
        totalBreak = totalBreak - convertUserInputFormatToDouble(((TextView) columnBreakHours).getText().toString());
    }

    private double calculateHoursAtWork(String myValues)
    {
        String[] parts = myValues.split("\\-");
        Double inputStartHours = parts[0].contains(":") ? convertUserInputFormatToDouble(parts[0]) : Double.valueOf(parts[0]);
        Double inputEndHours = parts[1].contains(":") ? convertUserInputFormatToDouble(parts[1]) : Double.valueOf(parts[1]);

        return inputStartHours >= inputEndHours ? ((24 - inputStartHours) + inputEndHours) : inputEndHours - inputStartHours;
    }

    public double calculateTotalMoney(String oreMuncite, String orePauza, String baniPeOra)
    {
        String[] parts = String.valueOf(calculateHoursAtWork(oreMuncite) - convertUserInputFormatToDouble(orePauza)).split("\\.");
        double intBeforeLine = Double.valueOf(parts[0]);
        double intAfterLine = Double.valueOf(parts[1]) / 10;

        return (intBeforeLine * Double.valueOf(baniPeOra)) + (Double.valueOf(baniPeOra) * intAfterLine);
    }

    private double convertUserInputFormatToDouble(String input)
    {
        double result;
        if (input.contains(":"))
        {
            String[] parts = input.split("\\:");
            result = parts[1].equals("00") || parts[1].equals("0") ? Double.valueOf(parts[0]) : Double.valueOf(parts[0]) + 1d / (60 / Integer.valueOf(parts[1]));
        }
        else
        {
            result = Double.valueOf(input);
        }

        return result;
    }

    public void resetData()
    {
        totalMoney = 0;
        totalHoursAtWork = 0;
        totalBreak = 0;
    }

    public double getTotalMoney()
    {
        return this.totalMoney;
    }

    public double getTotalHourAtWork()
    {
        return this.totalHoursAtWork;
    }

    public double getTotalBreak()
    {
        return this.totalBreak;
    }

    public String convertDoubleToUserFormat(double input)
    {
        String[] parts = String.valueOf(input).split("\\.");

        return input % 1 == 0 ? parts[0] : parts[0] + ":" + String.valueOf(((Integer.valueOf(parts[1]) * 60) / 10)).substring(0, 2);
    }

    public File getSavedFile(String month)
    {
        return new File(getSDCardLocation(month) + ".txt");
    }

    public File getSavedImage(String month)
    {
        return new File(getSDCardLocation(month) + ".png");
    }

    private String getSDCardLocation(String month)
    {
        return  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)  + "/ManagerActivitate/" + month;
    }

    public File getSavedBackupFile(String month)
    {
        return new File(mainActivityComponent.getApplicationInfo().dataDir + "/backup " + month + ".txt");
    }

    public String getCurrentMonth()
    {
        return new SimpleDateFormat("MM-yyyy", Locale.getDefault()).format(new Date());
    }

    public String getLoadedDate()
    {
        return this.loadedDate;
    }

    public void setLoadedDate(String dateToSet)
    {
        this.loadedDate = dateToSet;
    }
}