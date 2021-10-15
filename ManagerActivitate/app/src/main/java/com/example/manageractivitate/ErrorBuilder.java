package com.example.manageractivitate;

import android.graphics.Color;
import android.widget.Toast;

public class ErrorBuilder
{
    private MainActivity mainActivityComponent;
    private ApplicationUI applicationUIComponent;

    public ErrorBuilder(MainActivity mainActivityComponent, ApplicationUI applicationUIComponent)
    {
        this.mainActivityComponent = mainActivityComponent;
        this.applicationUIComponent = applicationUIComponent;
    }

    public void errorConstructor(String text)
    {
        applicationUIComponent.mainLabel.setText("Eroare " + text);
        applicationUIComponent.mainLabel.setTextColor(Color.RED);
        Toast.makeText(mainActivityComponent, "Eroare " + text, Toast.LENGTH_SHORT).show();
    }
}