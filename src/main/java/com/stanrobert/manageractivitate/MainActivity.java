package com.stanrobert.manageractivitate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.GridLayout.LayoutParams;
import android.widget.Toast;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_WRITE_STORAGE = 112;

    double totalHoursAtWork = 0.0;
    double totalBreak = 0.0;
    double totalMoney = 0.0;

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
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        mainLabel.setOnClickListener(this);

        addButton = (Button) findViewById(R.id.btnAdd);
        addButton.setOnClickListener(this);
        addButton.setText("Adauga");

        deleteButton = (Button) findViewById(R.id.btnRemove);
        deleteButton.setOnClickListener(this);
        deleteButton.setEnabled(false);

        backupButton = (Button) findViewById(R.id.btnBackup);
        backupButton.setOnClickListener(this);

        tableGUI = (TableLayout) findViewById(R.id.tableLayoutId);
        tableRow = (TableRow) findViewById(R.id.tableRowId);

        loadFromFile();

    }

    private void loadFromFile() {

        askForWritePermission();
        verifyIfFolderExist();

        if (getCurrentMonthFile().exists()) {
            writeDataFromFileToTable(getCurrentMonthFile());
        } else {
            if (getBackupFile().exists()) {
                writeDataFromFileToTable(getBackupFile());
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnAdd:
                hideKeyboard();

                if (addButton.getText() == "Adauga") {
                    if (dateText.length() > 0 && hoursAtWorkText.length() > 2 && hoursAtWorkText.getText().toString().contains("-") && breakText.length() > 0 && moneyPerHourText.length() > 0) {

                        addRow(false, null, null, null, null, null);
                        addMoney(hoursAtWorkText.getText().toString(), breakText.getText().toString(), moneyPerHourText.getText().toString());
                        displayCalculations();
                        clearFields();
                        saveToFile();

                    }
                } else {

                    for (int i = tableGUI.getChildCount() - 1; i >= 0; i--) {
                        if (((ColorDrawable) tableGUI.getChildAt(i).getBackground()).getColor() == Color.rgb(230, 100, 100)) {

                            substractMoney(i);

                            if (hoursAtWorkText.length() > 2 && hoursAtWorkText.getText().toString().contains("-")) {
                                ((TextView) ((TableRow) tableGUI.getChildAt(i)).getChildAt(1)).setText(hoursAtWorkText.getText());
                            }
                            if (breakText.length() > 0) {
                                ((TextView) ((TableRow) tableGUI.getChildAt(i)).getChildAt(2)).setText(breakText.getText());
                            }
                            if (moneyPerHourText.length() > 0) {
                                ((TextView) ((TableRow) tableGUI.getChildAt(i)).getChildAt(3)).setText(moneyPerHourText.getText());
                            }

                            addMoney(((TextView) ((TableRow) tableGUI.getChildAt(i)).getChildAt(1)).getText().toString(), ((TextView) ((TableRow) tableGUI.getChildAt(i)).getChildAt(2)).getText().toString(), ((TextView) ((TableRow) tableGUI.getChildAt(i)).getChildAt(3)).getText().toString());
                            ((TextView) ((TableRow) tableGUI.getChildAt(i)).getChildAt(4)).setText(String.valueOf(calculateTotalMoney(((TextView) ((TableRow) tableGUI.getChildAt(i)).getChildAt(1)).getText().toString(), ((TextView) ((TableRow) tableGUI.getChildAt(i)).getChildAt(2)).getText().toString(), ((TextView) ((TableRow) tableGUI.getChildAt(i)).getChildAt(3)).getText().toString())));
                        }
                    }

                    displayCalculations();
                    clearFields();
                    saveToFile();
                }
                break;

            case R.id.btnRemove:
                hideKeyboard();

                if (tableGUI.getChildCount() > 1) {
                    deleteRow();
                    if (!areRowsSelected()) {
                        addButton.setText("Adauga");
                        deleteButton.setEnabled(false);
                    }
                }
                break;

            case R.id.btnBackup:
                hideKeyboard();

                File backupFile = new File(getApplicationInfo().dataDir + "/backup " + dateText.getText().toString() + ".txt");
                File currentMonthFile = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/ManagerActivitate/" + dateText.getText().toString() + ".txt");
                if (currentMonthFile.exists()) {
                    deleteAllRows();
                    writeDataFromFileToTable(currentMonthFile);
                } else {
                    if (backupFile.exists()) {
                        deleteAllRows();
                        writeDataFromFileToTable(backupFile);
                    }
                }
                break;
            case R.id.lblMain:
                hideKeyboard();
                break;
        }
    }

    private void addMoney(String hoursAtWorkTime, String breakTime, String moneyPerHour) {
        try {
            totalMoney = totalMoney + calculateTotalMoney(hoursAtWorkTime, breakTime, moneyPerHour);
            totalHoursAtWork = totalHoursAtWork + calculateHoursAtWork(hoursAtWorkTime);
            totalBreak = totalBreak + convertUserInputFormatToDouble(breakTime);

        } catch (Exception e) {
            errorConstructor("la calcul");
        }
    }

    private void substractMoney(int i) {
        View columnHoursAtWork = ((TableRow) tableGUI.getChildAt(i)).getChildAt(1);
        View columnBreakHours = ((TableRow) tableGUI.getChildAt(i)).getChildAt(2);
        View columnMoneyPerHour = ((TableRow) tableGUI.getChildAt(i)).getChildAt(3);

        totalMoney = totalMoney - calculateTotalMoney(((TextView) columnHoursAtWork).getText().toString(), ((TextView) columnBreakHours).getText().toString(), ((TextView) columnMoneyPerHour).getText().toString());
        totalHoursAtWork = totalHoursAtWork - calculateHoursAtWork(((TextView) columnHoursAtWork).getText().toString());
        totalBreak = totalBreak - convertUserInputFormatToDouble(((TextView) columnBreakHours).getText().toString());
    }

    private void clearFields() {
        dateText.setText("");
        hoursAtWorkText.setText("");
        breakText.setText("");
    }

    private void addRow(boolean fromFile, String column1, String column2, String column3, String column4, String column5) {
        try {
            tableRow = new TableRow(this);

            columnText1 = createColumns();
            columnText2 = createColumns();
            columnText3 = createColumns();
            columnText4 = createColumns();
            columnText5 = createColumns();

            if (!fromFile) {
                columnText1.setText(dateText.getText());
                columnText2.setText(hoursAtWorkText.getText());
                columnText3.setText(breakText.getText());
                columnText4.setText(moneyPerHourText.getText());
                columnText5.setText(String.valueOf(calculateTotalMoney(hoursAtWorkText.getText().toString(), breakText.getText().toString(), moneyPerHourText.getText().toString())));
            } else {
                columnText1.setText(column1);
                columnText2.setText(column2);
                columnText3.setText(column3);
                columnText4.setText(column4);
                columnText5.setText(column5);
            }

            tableRow.addView(columnText1);
            tableRow.addView(columnText2);
            tableRow.addView(columnText3);
            tableRow.addView(columnText4);
            tableRow.addView(columnText5);
            tableRow.setBackgroundColor(Color.rgb(218, 232, 252));
            tableRow.setPadding(5, 0, 5, 0);

            tableRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (((ColorDrawable) view.getBackground()).getColor() == Color.rgb(218, 232, 252)) {
                        view.setBackgroundColor(Color.rgb(230, 100, 100));
                        addButton.setText("Modifica");
                        deleteButton.setEnabled(true);
                    } else {
                        view.setBackgroundColor(Color.rgb(218, 232, 252));
                        if (!areRowsSelected()) {
                            addButton.setText("Adauga");
                            deleteButton.setEnabled(false);
                        }
                    }
                }
            });
            tableGUI.addView(tableRow);
        } catch (Exception e) {
            errorConstructor("creare rand");
        }
    }

    private TextView createColumns() {
        TextView column = new TextView(this);
        column.setTextSize(15);
        column.setTextColor(Color.BLACK);
        column.setGravity(Gravity.CENTER);
        column.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        column.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f));
        return column;
    }

    private void saveToFile() {
        askForWritePermission();
        verifyIfFolderExist();

        writeFile(getCurrentMonthFile());
        writeFile(getBackupFile());
    }

    private File getCurrentMonthFile(){
        return new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/ManagerActivitate/" + getCurrentMonth() + ".txt");
    }

    private File getBackupFile(){
        return new File(getApplicationInfo().dataDir + "/backup " + getCurrentMonth() + ".txt");
    }

    private String getCurrentMonth(){
        return new SimpleDateFormat("MM-yyyy", Locale.getDefault()).format(new Date());
    }

    private void deleteRow() {
        try {
            for (int i = tableGUI.getChildCount() - 1; i >= 0; i--) {
                if (((ColorDrawable) tableGUI.getChildAt(i).getBackground()).getColor() == Color.rgb(230, 100, 100)) {
                    substractMoney(i);
                    tableGUI.removeViewAt(i);
                }
            }
            displayCalculations();
        } catch (Exception e) {
            errorConstructor("la calcul");
        }
        saveToFile();
    }

    private boolean areRowsSelected() {
        for (int i = 0; i < tableGUI.getChildCount(); i++) {
            if (((ColorDrawable) tableGUI.getChildAt(i).getBackground()).getColor() == Color.rgb(230, 100, 100)) {
                return true;
            }
        }
        return false;
    }

    private void deleteAllRows() {
        while (tableGUI.getChildCount() > 1) {
            tableGUI.removeView(tableGUI.getChildAt(tableGUI.getChildCount() - 1));
        }
        totalMoney = 0;
        totalHoursAtWork = 0;
        totalBreak = 0;
    }

    private double calculateHoursAtWork(String myValues) {
        String[] parts = myValues.split("\\-");
        Double inputStartHours = parts[0].contains(":") ? convertUserInputFormatToDouble(parts[0]) : Double.valueOf(parts[0]);
        Double inputEndHours = parts[1].contains(":") ? convertUserInputFormatToDouble(parts[1]) : Double.valueOf(parts[1]);

        return inputStartHours >= inputEndHours ? ((24 - inputStartHours) + inputEndHours) : inputEndHours - inputStartHours;
    }

    private double calculateTotalMoney(String oreMuncite, String orePauza, String baniPeOra) {
        String[] parts = String.valueOf(calculateHoursAtWork(oreMuncite) - convertUserInputFormatToDouble(orePauza)).split("\\.");
        double intBeforeLine = Double.valueOf(parts[0]);
        double intAfterLine = Double.valueOf(parts[1]) / 10;

        return (intBeforeLine * Double.valueOf(baniPeOra)) + (Double.valueOf(baniPeOra) * intAfterLine);
    }

    private double convertUserInputFormatToDouble(String input) {
        double result;
        if (input.contains(":")) {
            String[] parts = input.split("\\:");
            result = parts[1].equals("00") || parts[1].equals("0") ? Double.valueOf(parts[0]) : Double.valueOf(parts[0]) + 1d / (60 / Integer.valueOf(parts[1]));
        } else {
            result = Double.valueOf(input);
        }

        return result;
    }

    private String convertDoubleToUserFormat(double input) {
        String[] parts = String.valueOf(input).split("\\.");

        return input % 1 == 0 ? parts[0] : parts[0] + ":" + String.valueOf(((Integer.valueOf(parts[1]) * 60) / 10)).substring(0, 2);
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    private void askForWritePermission() {
        boolean hasPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
        }
    }

    private void verifyIfFolderExist() {
        File dir = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/ManagerActivitate");
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private void writeDataFromFileToTable(File file) {
        String line;
        String[] parts;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            while ((line = br.readLine()) != null) {
                parts = line.split("\\ ----- ");
                addRow(true, parts[0], parts[1], parts[2], parts[3], parts[4]);
                addMoney(parts[1], parts[2], parts[3]);
            }
            br.close();
            displayCalculations();
        } catch (IOException e) {
            errorConstructor("incarcare fisier");
        }
    }

    private void writeFile(File file) {
        String str = "";
        try {
            for (int i = 1; i <= (tableGUI.getChildCount() - 1); i++) {
                View columnData = ((TableRow) tableGUI.getChildAt(i)).getChildAt(0);
                View columnHoursAtWork = ((TableRow) tableGUI.getChildAt(i)).getChildAt(1);
                View columnBreakHours = ((TableRow) tableGUI.getChildAt(i)).getChildAt(2);
                View columnMoneyPerHour = ((TableRow) tableGUI.getChildAt(i)).getChildAt(3);
                View columnMoneyPerDay = ((TableRow) tableGUI.getChildAt(i)).getChildAt(4);
                str = str + ((TextView) columnData).getText().toString() + " ----- " + ((TextView) columnHoursAtWork).getText().toString() + " ----- " + ((TextView) columnBreakHours).getText().toString() + " ----- " + ((TextView) columnMoneyPerHour).getText().toString() + " ----- " + ((TextView) columnMoneyPerDay).getText().toString() + "\n";

            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(str);
            writer.close();
        } catch (Exception e) {
            errorConstructor("scriere fisier");
        }
    }

    private void displayCalculations() {
        totalMoneyLabel.setText("Total Bani:\n" + totalMoney + "RON");
        totalHoursLabel.setText("Ore La Munca:\n" + convertDoubleToUserFormat(totalHoursAtWork) + "h");
        totalBreakLabel.setText("Ore Pauza:\n" + convertDoubleToUserFormat(totalBreak) + "h");
    }

    private void errorConstructor(String text) {
        mainLabel.setText("Eroare " + text);
        mainLabel.setTextColor(Color.RED);
        Toast.makeText(this, "Eroare " + text, Toast.LENGTH_SHORT).show();
    }
}
