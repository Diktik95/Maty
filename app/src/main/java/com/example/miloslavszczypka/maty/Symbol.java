package com.example.miloslavszczypka.maty;

import android.graphics.drawable.Drawable;

/**
 * Created by miloslavszczypka on 13.12.17.
 */

public class Symbol {
    public Drawable img;
    public int pos;
    public int sloupec;
    public char nazev;

    public Symbol(Drawable image, int position, int sloupec, char name) {
        img = image;
        pos = position;
        this.sloupec = sloupec;
        nazev = name;
    }
}
