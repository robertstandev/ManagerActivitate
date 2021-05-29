package com.stanrobert.manageractivitate;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity
{
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
        ApplicationUI applicationUIComponent = new ApplicationUI(this);
        ErrorBuilder errorBuilderComponent = new ErrorBuilder(this, applicationUIComponent);
        Data dataComponent = new Data(this, errorBuilderComponent, applicationUIComponent);
        WriteToFile writeFileComponent = new WriteToFile(askPermissionComponent, dataComponent, errorBuilderComponent , applicationUIComponent);
        TableUI tableComponent = new TableUI(dataComponent,this, errorBuilderComponent, applicationUIComponent);
        ReadFromFile readFileComponent = new ReadFromFile(askPermissionComponent,dataComponent,tableComponent,errorBuilderComponent);
        ButtonsUI buttonsComponent = new ButtonsUI(dataComponent,this, tableComponent,writeFileComponent, readFileComponent, applicationUIComponent);

        readFileComponent.loadFromFile();
    }
}