package com.stanrobert.manageractivitate;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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

    public void saveToFile(String month)
    {
        askPermissionComponent.askForWritePermission();
        askPermissionComponent.verifyIfFolderExist();

        writeFile(dataComponent.getSavedFile(month));
        writeFile(dataComponent.getSavedBackupFile(month));
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

    public void takeScreenShot()
    {
        askPermissionComponent.askForWritePermission();
        askPermissionComponent.verifyIfFolderExist();

        ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();
        bitmapArray.add(getBitmapFromView(applicationUIComponent.totalStats));
        bitmapArray.add(getBitmapFromView(applicationUIComponent.tableGUI));

        try
        {
            FileOutputStream outputStream = new FileOutputStream(dataComponent.getSavedImage(dataComponent.getCurrentMonth()));

            combineImages(bitmapArray).compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        }
        catch (IOException e)
        {
            errorBuilderComponent.errorConstructor("creare imagine");
        }
    }

    private Bitmap getBitmapFromView(View view)
    {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(canvas);

        return bitmap;
    }

    private Bitmap combineImages(ArrayList<Bitmap> listOfBitmapsToStitch)
    {
        Bitmap bitmapResult = null;

        int width = 0, height = 0;

        for (Bitmap bitmap : listOfBitmapsToStitch)
        {
            width = Math.max(width, bitmap.getWidth());
            height = height + bitmap.getHeight();
        }
        bitmapResult = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas comboImageCanvas = new Canvas(bitmapResult);
        comboImageCanvas.drawColor(Color.WHITE);

        int currentHeight = 0;
        for (Bitmap bitmap : listOfBitmapsToStitch)
        {
            comboImageCanvas.drawBitmap(bitmap, (comboImageCanvas.getWidth() - bitmap.getWidth()) / 2, currentHeight, null);
            currentHeight = currentHeight + bitmap.getHeight();
        }

        return bitmapResult;
    }
}