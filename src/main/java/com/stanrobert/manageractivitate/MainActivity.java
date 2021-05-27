package com.stanrobert.manageractivitate;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitializeComponents();
    }

    private void InitializeComponents()
    {
        AskForWritePermission askPermissionComponent = new AskForWritePermission(this);
        ErrorBuilder errorBuilderComponent = new ErrorBuilder(this);
        Data dataComponent = new Data(this,errorBuilderComponent);
        WriteToFile writeFileComponent = new WriteToFile(askPermissionComponent,dataComponent,this,errorBuilderComponent);
        TableUI tableComponent = new TableUI(dataComponent,this,errorBuilderComponent, writeFileComponent);
        ReadFromFile readFileComponent = new ReadFromFile(askPermissionComponent,dataComponent,tableComponent,errorBuilderComponent);
        ButtonsUI buttonsComponent = new ButtonsUI(dataComponent,this,tableComponent,writeFileComponent,readFileComponent);

        InitializeUIAndLoadData(buttonsComponent,readFileComponent);
    }

    private void InitializeUIAndLoadData(ButtonsUI buttonsComponent,ReadFromFile readFileComponent)
    {
        dateText = (EditText) findViewById(R.id.dateField);
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        dateText.setText(currentDate);

        hoursAtWorkText = (EditText) findViewById(R.id.workHoursField);

        breakText = (EditText) findViewById(R.id.breakField);

        moneyPerHourText = (EditText) findViewById(R.id.moneyPerHourField);
        moneyPerHourText.setText("12");

        totalMoneyLabel = (TextView) findViewById(R.id.totalMoney);
        totalMoneyLabel.setText("Bani:\n0 RON");

        totalHoursLabel = (TextView) findViewById(R.id.totalHours);
        totalHoursLabel.setText("Ore La Munca:\n0 h");

        totalBreakLabel = (TextView) findViewById(R.id.totalBreak);
        totalBreakLabel.setText("Ore Pauza:\n0 h");

        mainLabel = (TextView) findViewById(R.id.lblMain);
        mainLabel.setOnClickListener(buttonsComponent);

        addButton = (Button) findViewById(R.id.btnAdd);
        addButton.setOnClickListener(buttonsComponent);
        addButton.setText("Adauga");

        deleteButton = (Button) findViewById(R.id.btnRemove);
        deleteButton.setOnClickListener(buttonsComponent);
        deleteButton.setEnabled(false);

        backupButton = (Button) findViewById(R.id.btnBackup);
        backupButton.setOnClickListener(buttonsComponent);

        tableGUI = (TableLayout) findViewById(R.id.tableLayoutId);
        tableRow = (TableRow) findViewById(R.id.tableRowId);

        readFileComponent.loadFromFile();
    }
}