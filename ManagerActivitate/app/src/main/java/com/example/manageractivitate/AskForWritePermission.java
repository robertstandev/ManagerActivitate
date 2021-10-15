package com.example.manageractivitate;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.File;

public class AskForWritePermission
{
    private final MainActivity mainActivityComponent;
    private static final int REQUEST_WRITE_STORAGE = 112;

    public AskForWritePermission(MainActivity mainActivityComponent)
    {
        this.mainActivityComponent = mainActivityComponent;
    }

    public void askForWritePermission()
    {
        boolean hasPermission = ContextCompat.checkSelfPermission(mainActivityComponent, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (!hasPermission)
        {
            ActivityCompat.requestPermissions(mainActivityComponent, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
        }
    }

    public void verifyIfFolderExist()
    {
        File dir = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/ManagerActivitate");
        if (!dir.exists())
        {
            dir.mkdirs();
        }
    }
}