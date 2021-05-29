package com.stanrobert.manageractivitate;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ApplicationUI
{
    EditText dateText;
    EditText hoursAtWorkText;
    EditText breakText;
    EditText moneyPerHourText;
    TextView mainLabel;
    Button addButton;
    Button deleteButton;
    Button backupButton;
    TableLayout tableGUI;
    TableRow tableRow;
    TextView columnText1, columnText2, columnText3, columnText4, columnText5;
    TextView totalMoneyLabel, totalHoursLabel, totalBreakLabel;

    public ApplicationUI(MainActivity mainActivityComponent)
    {
        dateText = (EditText) mainActivityComponent.findViewById(R.id.dateField);
        dateText.setText(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));

        hoursAtWorkText = (EditText) mainActivityComponent.findViewById(R.id.workHoursField);

        breakText = (EditText) mainActivityComponent.findViewById(R.id.breakField);

        moneyPerHourText = (EditText) mainActivityComponent.findViewById(R.id.moneyPerHourField);
        moneyPerHourText.setText("12");

        totalMoneyLabel = (TextView) mainActivityComponent.findViewById(R.id.totalMoney);
        totalMoneyLabel.setText("Bani:\n0 RON");

        totalHoursLabel = (TextView) mainActivityComponent.findViewById(R.id.totalHours);
        totalHoursLabel.setText("Ore La Munca:\n0 h");

        totalBreakLabel = (TextView) mainActivityComponent.findViewById(R.id.totalBreak);
        totalBreakLabel.setText("Ore Pauza:\n0 h");

        mainLabel = (TextView) mainActivityComponent.findViewById(R.id.lblMain);

        addButton = (Button) mainActivityComponent.findViewById(R.id.btnAdd);
        addButton.setText("Adauga");

        deleteButton = (Button) mainActivityComponent.findViewById(R.id.btnRemove);
        deleteButton.setEnabled(false);

        backupButton = (Button) mainActivityComponent.findViewById(R.id.btnBackup);

        tableGUI = (TableLayout) mainActivityComponent.findViewById(R.id.tableLayoutId);
        tableRow = (TableRow) mainActivityComponent.findViewById(R.id.tableRowId);
    }
}