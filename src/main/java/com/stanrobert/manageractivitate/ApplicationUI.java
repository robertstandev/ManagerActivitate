package com.stanrobert.manageractivitate;

import android.os.Build;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    LinearLayout totalStats;

    public ApplicationUI(MainActivity mainActivityComponent) {
        this.dateText = mainActivityComponent.findViewById(R.id.dateField);
        this.dateText.setText(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));

        this.hoursAtWorkText = mainActivityComponent.findViewById(R.id.workHoursField);

        this.breakText = mainActivityComponent.findViewById(R.id.breakField);

        this.moneyPerHourText = mainActivityComponent.findViewById(R.id.moneyPerHourField);
        this.moneyPerHourText.setText("12");

        this.totalMoneyLabel = mainActivityComponent.findViewById(R.id.totalMoney);
        this.totalMoneyLabel.setText("Bani:\n0 RON");

        this.totalHoursLabel = mainActivityComponent.findViewById(R.id.totalHours);
        this.totalHoursLabel.setText("Ore La Munca:\n0 h");

        this.totalBreakLabel = mainActivityComponent.findViewById(R.id.totalBreak);
        this.totalBreakLabel.setText("Ore Pauza:\n0 h");

        this.mainLabel = mainActivityComponent.findViewById(R.id.lblMain);

        this.addButton = mainActivityComponent.findViewById(R.id.btnAdd);
        this.addButton.setText("Adauga");

        this.deleteButton = mainActivityComponent.findViewById(R.id.btnRemove);
        this.deleteButton.setEnabled(false);

        this.backupButton = mainActivityComponent.findViewById(R.id.btnBackup);

        this.tableGUI = mainActivityComponent.findViewById(R.id.tableLayoutId);
        this.tableRow = mainActivityComponent.findViewById(R.id.tableRowId);

        this.totalStats = mainActivityComponent.findViewById(R.id.totalStats);

        //exista un bug in meizu,lge si samsung cand folosesti android:inputType="datetime" (in activity_main.xml)
        //asa ca atunci cand apesi pe textboxurile de pus data, ore la munca si pauza nu vor aparea ":" si "-" deci nu poti folosi aplicatia
        //in cazul asta fac ca sa se schimbe inputType-ul
        if (Build.MANUFACTURER.toLowerCase(Locale.ENGLISH).equals("meizu") || Build.MANUFACTURER.toLowerCase(Locale.ENGLISH).equals("samsung") || Build.MANUFACTURER.toLowerCase(Locale.ENGLISH).equals("lge"))
        {
            this.dateText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
            this.hoursAtWorkText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
            this.breakText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
        }
    }
}