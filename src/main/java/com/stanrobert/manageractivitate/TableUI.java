package com.stanrobert.manageractivitate;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TableUI
{
    private ErrorBuilder errorBuilderComponent;
    private Data dataComponent;
    private MainActivity mainActivityComponent;
    private WriteToFile writeFileComponent;

    public TableUI(Data dataComponent, MainActivity mainActivityComponent, ErrorBuilder errorBuilderComponent, WriteToFile writeFileComponent)
    {
        this.dataComponent = dataComponent;
        this.mainActivityComponent = mainActivityComponent;
        this.errorBuilderComponent = errorBuilderComponent;
        this.writeFileComponent = writeFileComponent;
    }

    private TextView createColumns()
    {
        TextView column = new TextView(mainActivityComponent);
        column.setTextSize(15);
        column.setTextColor(Color.BLACK);
        column.setGravity(Gravity.CENTER);
        column.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        column.setLayoutParams(new TableRow.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT, 1.0f));
        return column;
    }

    public void addRow(boolean fromFile, String column1, String column2, String column3, String column4, String column5)
    {
        try
        {
            mainActivityComponent.tableRow = new TableRow(mainActivityComponent);

            mainActivityComponent.columnText1 = createColumns();
            mainActivityComponent.columnText2 = createColumns();
            mainActivityComponent.columnText3 = createColumns();
            mainActivityComponent.columnText4 = createColumns();
            mainActivityComponent.columnText5 = createColumns();

            if (!fromFile)
            {
                mainActivityComponent.columnText1.setText(mainActivityComponent.dateText.getText());
                mainActivityComponent.columnText2.setText(mainActivityComponent.hoursAtWorkText.getText());
                mainActivityComponent.columnText3.setText(mainActivityComponent.breakText.getText());
                mainActivityComponent.columnText4.setText(mainActivityComponent.moneyPerHourText.getText());
                mainActivityComponent.columnText5.setText(String.valueOf(dataComponent.calculateTotalMoney(mainActivityComponent.hoursAtWorkText.getText().toString(), mainActivityComponent.breakText.getText().toString(), mainActivityComponent.moneyPerHourText.getText().toString())));
            }
            else
            {
                mainActivityComponent.columnText1.setText(column1);
                mainActivityComponent.columnText2.setText(column2);
                mainActivityComponent.columnText3.setText(column3);
                mainActivityComponent.columnText4.setText(column4);
                mainActivityComponent.columnText5.setText(column5);
            }

            mainActivityComponent.tableRow.addView(mainActivityComponent.columnText1);
            mainActivityComponent.tableRow.addView(mainActivityComponent.columnText2);
            mainActivityComponent.tableRow.addView(mainActivityComponent.columnText3);
            mainActivityComponent.tableRow.addView(mainActivityComponent.columnText4);
            mainActivityComponent.tableRow.addView(mainActivityComponent.columnText5);
            mainActivityComponent.tableRow.setBackgroundColor(Color.rgb(218, 232, 252));
            mainActivityComponent.tableRow.setPadding(5, 0, 5, 0);

            mainActivityComponent.tableRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (((ColorDrawable) view.getBackground()).getColor() == Color.rgb(218, 232, 252))
                    {
                        view.setBackgroundColor(Color.rgb(230, 100, 100));
                        mainActivityComponent.addButton.setText("Modifica");
                        mainActivityComponent.deleteButton.setEnabled(true);
                    }
                    else
                    {
                        view.setBackgroundColor(Color.rgb(218, 232, 252));
                        if (!areRowsSelected())
                        {
                            mainActivityComponent.addButton.setText("Adauga");
                            mainActivityComponent.deleteButton.setEnabled(false);
                        }
                    }
                }
            });
            mainActivityComponent.tableGUI.addView(mainActivityComponent.tableRow);
        }
        catch (Exception e)
        {
            errorBuilderComponent.errorConstructor("creare rand");
        }
    }

    public void deleteRow()
    {
        try
        {
            for (int i = mainActivityComponent.tableGUI.getChildCount() - 1; i >= 0; i--)
            {
                if (((ColorDrawable) mainActivityComponent.tableGUI.getChildAt(i).getBackground()).getColor() == Color.rgb(230, 100, 100))
                {
                    dataComponent.substractMoney(i);
                    mainActivityComponent.tableGUI.removeViewAt(i);
                }
            }
            displayCalculations();
        }
        catch (Exception e)
        {
            errorBuilderComponent.errorConstructor("la calcul");
        }
        writeFileComponent.saveToFile();
    }

    public void deleteAllRows()
    {
        while (mainActivityComponent.tableGUI.getChildCount() > 1)
        {
            mainActivityComponent.tableGUI.removeView(mainActivityComponent.tableGUI.getChildAt(mainActivityComponent.tableGUI.getChildCount() - 1));
        }
        dataComponent.resetData();
    }

    public void displayCalculations()
    {
        mainActivityComponent.totalMoneyLabel.setText("Total Bani:\n" + dataComponent.getTotalMoney() + "RON");
        mainActivityComponent.totalHoursLabel.setText("Ore La Munca:\n" + dataComponent.convertDoubleToUserFormat(dataComponent.getTotalHourAtWork()) + "h");
        mainActivityComponent.totalBreakLabel.setText("Ore Pauza:\n" + dataComponent.convertDoubleToUserFormat(dataComponent.getTotalBreak()) + "h");
    }

    public boolean areRowsSelected()
    {
        for (int i = 0; i < mainActivityComponent.tableGUI.getChildCount(); i++)
        {
            if (((ColorDrawable) mainActivityComponent.tableGUI.getChildAt(i).getBackground()).getColor() == Color.rgb(230, 100, 100))
            {
                return true;
            }
        }
        return false;
    }

    public void clearFields()
    {
        mainActivityComponent.dateText.setText("");
        mainActivityComponent.hoursAtWorkText.setText("");
        mainActivityComponent.breakText.setText("");
    }
}