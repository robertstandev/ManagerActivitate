package com.stanrobert.manageractivitate;

import android.graphics.Color;
import android.widget.Toast;

public class ErrorBuilder
{
    private MainActivity mainActivityComponent;

    public ErrorBuilder(MainActivity mainActivityComponent)
    {
        this.mainActivityComponent = mainActivityComponent;
    }

    public void errorConstructor(String text)
    {
        mainActivityComponent.mainLabel.setText("Eroare " + text);
        mainActivityComponent.mainLabel.setTextColor(Color.RED);
        Toast.makeText(mainActivityComponent, "Eroare " + text, Toast.LENGTH_SHORT).show();
    }
}