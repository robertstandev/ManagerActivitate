package com.example.manageractivitate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewTreeObserver;
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
    private final AskForWritePermission askPermissionComponent;
    private final Data dataComponent;
    private final ErrorBuilder errorBuilderComponent;
    private final ApplicationUI applicationUIComponent;

    public WriteToFile(AskForWritePermission askPermissionComponent, Data dataComponent, ErrorBuilder errorBuilderComponent, ApplicationUI applicationUIComponent)
    {
        this.askPermissionComponent = askPermissionComponent;
        this.dataComponent = dataComponent;
        this.errorBuilderComponent = errorBuilderComponent;
        this.applicationUIComponent = applicationUIComponent;
    }

    private String getLoadedMonth()
    {
        if(dataComponent.getLoadedDate().equals(dataComponent.getCurrentMonth()))
        {
            return dataComponent.getCurrentMonth();
        }
        else
        {
            return dataComponent.getLoadedDate();
        }
    }

    public void saveToFile()
    {
        askPermissionComponent.askForWritePermission();
        askPermissionComponent.verifyIfFolderExist();

        writeFile(dataComponent.getSavedFile(getLoadedMonth()));
        writeFile(dataComponent.getSavedBackupFile(getLoadedMonth()));
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
        //make sure the table has finished updating , else it will give you incorrect data and after that send the screenshot and save commands
        applicationUIComponent.tableGUI.getViewTreeObserver().addOnGlobalLayoutListener(tableGUILayoutListener);
    }

    ViewTreeObserver.OnGlobalLayoutListener tableGUILayoutListener = new ViewTreeObserver.OnGlobalLayoutListener()
    {
        @Override
        public void onGlobalLayout()
        {
            ViewTreeObserver observer = applicationUIComponent.tableGUI.getViewTreeObserver();
            observer.removeOnGlobalLayoutListener(this);

            ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();

            bitmapArray.add(getBitmapFromView(applicationUIComponent.totalStats));
            bitmapArray.add(getBitmapFromView(applicationUIComponent.tableGUI));

            writeScreenShotToFile(bitmapArray);
        }
    };

    private Bitmap getBitmapFromView(View view)
    {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();

        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        }
        else
        {
            canvas.drawColor(Color.WHITE);
        }

        view.draw(canvas);

        return bitmap;
    }

    private Bitmap combineImages(ArrayList<Bitmap> listOfBitmapsToStitch)
    {
        int width = 0, height = 0;

        for (Bitmap bitmap : listOfBitmapsToStitch)
        {
            width = Math.max(width, bitmap.getWidth());
            height = height + bitmap.getHeight();
        }
        Bitmap bitmapResult = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

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

    private void writeScreenShotToFile(ArrayList<Bitmap> bitmapArray)
    {
        askPermissionComponent.askForWritePermission();
        askPermissionComponent.verifyIfFolderExist();

        try
        {
            FileOutputStream outputStream = new FileOutputStream(dataComponent.getSavedImage(getLoadedMonth()));

            combineImages(bitmapArray).compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        }
        catch (IOException e)
        {
            errorBuilderComponent.errorConstructor("creare imagine");
        }
    }
}