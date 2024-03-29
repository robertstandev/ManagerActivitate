package com.example.manageractivitate;

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
    private ApplicationUI applicationUIComponent;

    public TableUI(Data dataComponent, MainActivity mainActivityComponent, ErrorBuilder errorBuilderComponent, ApplicationUI applicationUIComponent)
    {
        this.dataComponent = dataComponent;
        this.mainActivityComponent = mainActivityComponent;
        this.errorBuilderComponent = errorBuilderComponent;
        this.applicationUIComponent = applicationUIComponent;
    }

    private TextView createRowUIItem()
    {
        TextView column = new TextView(mainActivityComponent);
        column.setTextSize(15);
        column.setTextColor(Color.BLACK);
        column.setGravity(Gravity.CENTER);
        column.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        column.setLayoutParams(new TableRow.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT, 1.0f));
        return column;
    }

    public void rowBuilder(String[] columnTextToAdd)
    {
        try
        {
            applicationUIComponent.tableRow = new TableRow(mainActivityComponent);

            for(int i = 0 ; i < 5 ; i++)
            {
                applicationUIComponent.columnText = createRowUIItem();
                applicationUIComponent.columnText.setText(columnTextToAdd[i]);
                applicationUIComponent.tableRow.addView(applicationUIComponent.columnText);
            }

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
                        checkRowSelection();
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

    public void createRow()
    {
        rowBuilder
        (
                new String[]
                {
                        applicationUIComponent.dateText.getText().toString(),
                        applicationUIComponent.hoursAtWorkText.getText().toString(),
                        applicationUIComponent.breakText.getText().toString(),
                        applicationUIComponent.moneyPerHourText.getText().toString(),
                        String.valueOf(dataComponent.calculateTotalMoney(applicationUIComponent.hoursAtWorkText.getText().toString(), applicationUIComponent.breakText.getText().toString(), applicationUIComponent.moneyPerHourText.getText().toString()))
                }
        );
        dataComponent.addMoney(applicationUIComponent.hoursAtWorkText.getText().toString(), applicationUIComponent.breakText.getText().toString(), applicationUIComponent.moneyPerHourText.getText().toString());
    }

    public boolean canCreateRow()
    {
        return (applicationUIComponent.dateText.length() > 0 && applicationUIComponent.hoursAtWorkText.length() > 2 && applicationUIComponent.hoursAtWorkText.getText().toString().contains("-") && applicationUIComponent.breakText.length() > 0 && applicationUIComponent.moneyPerHourText.length() > 0);
    }

    public void modifyRow()
    {
        for (int i = applicationUIComponent.tableGUI.getChildCount() - 1; i >= 0; i--)
        {
            if (((ColorDrawable) applicationUIComponent.tableGUI.getChildAt(i).getBackground()).getColor() == Color.rgb(230, 100, 100))
            {
                dataComponent.substractMoney(i);

                if (applicationUIComponent.hoursAtWorkText.length() > 2 && applicationUIComponent.hoursAtWorkText.getText().toString().contains("-"))
                {
                    ((TextView) ((TableRow) applicationUIComponent.tableGUI.getChildAt(i)).getChildAt(1)).setText(applicationUIComponent.hoursAtWorkText.getText());
                }
                if (applicationUIComponent.breakText.length() > 0)
                {
                    ((TextView) ((TableRow) applicationUIComponent.tableGUI.getChildAt(i)).getChildAt(2)).setText(applicationUIComponent.breakText.getText());
                }
                if (applicationUIComponent.moneyPerHourText.length() > 0)
                {
                    ((TextView) ((TableRow) applicationUIComponent.tableGUI.getChildAt(i)).getChildAt(3)).setText(applicationUIComponent.moneyPerHourText.getText());
                }

                dataComponent.addMoney(((TextView) ((TableRow) applicationUIComponent.tableGUI.getChildAt(i)).getChildAt(1)).getText().toString(), ((TextView) ((TableRow) applicationUIComponent.tableGUI.getChildAt(i)).getChildAt(2)).getText().toString(), ((TextView) ((TableRow) applicationUIComponent.tableGUI.getChildAt(i)).getChildAt(3)).getText().toString());
                ((TextView) ((TableRow) applicationUIComponent.tableGUI.getChildAt(i)).getChildAt(4)).setText(String.valueOf(dataComponent.calculateTotalMoney(((TextView) ((TableRow) applicationUIComponent.tableGUI.getChildAt(i)).getChildAt(1)).getText().toString(), ((TextView) ((TableRow) applicationUIComponent.tableGUI.getChildAt(i)).getChildAt(2)).getText().toString(), ((TextView) ((TableRow) applicationUIComponent.tableGUI.getChildAt(i)).getChildAt(3)).getText().toString())));
            }
        }
    }

    public void clearUserSelectedRows()
    {
        for (int i = applicationUIComponent.tableGUI.getChildCount() - 1; i >= 1; i--)
        {
            applicationUIComponent.tableGUI.getChildAt(i).setBackgroundColor(Color.rgb(218, 232, 252));
        }
        checkRowSelection();
    }

    public void deleteRow()
    {
        if (applicationUIComponent.tableGUI.getChildCount() > 1)
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
            checkRowSelection();
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

    private void checkRowSelection()
    {
        if (areAllRowsDeselected())
        {
            applicationUIComponent.addButton.setText("Adauga");
            applicationUIComponent.deleteButton.setEnabled(false);
        }
    }

    public boolean areAllRowsDeselected()
    {
        for (int i = 0; i < applicationUIComponent.tableGUI.getChildCount(); i++)
        {
            if (((ColorDrawable) applicationUIComponent.tableGUI.getChildAt(i).getBackground()).getColor() == Color.rgb(230, 100, 100))
            {
                return false;
            }
        }
        return true;
    }

    public void clearFields()
    {
        applicationUIComponent.dateText.setText("");
        applicationUIComponent.hoursAtWorkText.setText("");
        applicationUIComponent.breakText.setText("");
    }
}