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
    private ApplicationUI applicationUIComponent;

    public TableUI(Data dataComponent, MainActivity mainActivityComponent, ErrorBuilder errorBuilderComponent, WriteToFile writeFileComponent, ApplicationUI applicationUIComponent)
    {
        this.dataComponent = dataComponent;
        this.mainActivityComponent = mainActivityComponent;
        this.errorBuilderComponent = errorBuilderComponent;
        this.writeFileComponent = writeFileComponent;
        this.applicationUIComponent = applicationUIComponent;
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
            applicationUIComponent.tableRow = new TableRow(mainActivityComponent);

            applicationUIComponent.columnText1 = createColumns();
            applicationUIComponent.columnText2 = createColumns();
            applicationUIComponent.columnText3 = createColumns();
            applicationUIComponent.columnText4 = createColumns();
            applicationUIComponent.columnText5 = createColumns();

            if (!fromFile)
            {
                applicationUIComponent.columnText1.setText(applicationUIComponent.dateText.getText());
                applicationUIComponent.columnText2.setText(applicationUIComponent.hoursAtWorkText.getText());
                applicationUIComponent.columnText3.setText(applicationUIComponent.breakText.getText());
                applicationUIComponent.columnText4.setText(applicationUIComponent.moneyPerHourText.getText());
                applicationUIComponent.columnText5.setText(String.valueOf(dataComponent.calculateTotalMoney(applicationUIComponent.hoursAtWorkText.getText().toString(), applicationUIComponent.breakText.getText().toString(), applicationUIComponent.moneyPerHourText.getText().toString())));
            }
            else
            {
                applicationUIComponent.columnText1.setText(column1);
                applicationUIComponent.columnText2.setText(column2);
                applicationUIComponent.columnText3.setText(column3);
                applicationUIComponent.columnText4.setText(column4);
                applicationUIComponent.columnText5.setText(column5);
            }

            applicationUIComponent.tableRow.addView(applicationUIComponent.columnText1);
            applicationUIComponent.tableRow.addView(applicationUIComponent.columnText2);
            applicationUIComponent.tableRow.addView(applicationUIComponent.columnText3);
            applicationUIComponent.tableRow.addView(applicationUIComponent.columnText4);
            applicationUIComponent.tableRow.addView(applicationUIComponent.columnText5);
            applicationUIComponent.tableRow.setBackgroundColor(Color.rgb(218, 232, 252));
            applicationUIComponent.tableRow.setPadding(5, 0, 5, 0);

            applicationUIComponent.tableRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (((ColorDrawable) view.getBackground()).getColor() == Color.rgb(218, 232, 252))
                    {
                        view.setBackgroundColor(Color.rgb(230, 100, 100));
                        applicationUIComponent.addButton.setText("Modifica");
                        applicationUIComponent.deleteButton.setEnabled(true);
                    }
                    else
                    {
                        view.setBackgroundColor(Color.rgb(218, 232, 252));
                        if (!areRowsSelected())
                        {
                            applicationUIComponent.addButton.setText("Adauga");
                            applicationUIComponent.deleteButton.setEnabled(false);
                        }
                    }
                }
            });
            applicationUIComponent.tableGUI.addView(applicationUIComponent.tableRow);
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
            for (int i = applicationUIComponent.tableGUI.getChildCount() - 1; i >= 0; i--)
            {
                if (((ColorDrawable) applicationUIComponent.tableGUI.getChildAt(i).getBackground()).getColor() == Color.rgb(230, 100, 100))
                {
                    dataComponent.substractMoney(i);
                    applicationUIComponent.tableGUI.removeViewAt(i);
                }
            }
            displayCalculations();
        }
        catch (Exception e)
        {
            errorBuilderComponent.errorConstructor("la calcul");
        }
    }

    public void deleteAllRows()
    {
        while (applicationUIComponent.tableGUI.getChildCount() > 1)
        {
            applicationUIComponent.tableGUI.removeView(applicationUIComponent.tableGUI.getChildAt(applicationUIComponent.tableGUI.getChildCount() - 1));
        }
        dataComponent.resetData();
    }

    public void displayCalculations()
    {
        applicationUIComponent.totalMoneyLabel.setText("Total Bani:\n" + dataComponent.getTotalMoney() + "RON");
        applicationUIComponent.totalHoursLabel.setText("Ore La Munca:\n" + dataComponent.convertDoubleToUserFormat(dataComponent.getTotalHourAtWork()) + "h");
        applicationUIComponent.totalBreakLabel.setText("Ore Pauza:\n" + dataComponent.convertDoubleToUserFormat(dataComponent.getTotalBreak()) + "h");
    }

    public boolean areRowsSelected()
    {
        for (int i = 0; i < applicationUIComponent.tableGUI.getChildCount(); i++)
        {
            if (((ColorDrawable) applicationUIComponent.tableGUI.getChildAt(i).getBackground()).getColor() == Color.rgb(230, 100, 100))
            {
                return true;
            }
        }
        return false;
    }

    public void clearFields()
    {
        applicationUIComponent.dateText.setText("");
        applicationUIComponent.hoursAtWorkText.setText("");
        applicationUIComponent.breakText.setText("");
    }
}