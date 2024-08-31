package com.example.riverraid;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Missile {
    int x,y, width, height;
    Bitmap missile;

    Missile(Resources res)
    {
        missile = BitmapFactory.decodeResource(res, R.drawable.missile);
        width = missile.getWidth();
        height = missile.getHeight();
        width /=10;
        height /=10;

        missile = Bitmap.createScaledBitmap(missile, width, height, false);

    }
    Bitmap getMissile()
    {
        return missile;

    }
    Rect getCollisonShape(int x, int y)
    {
        return new Rect(x, y, x+width, y + height);
    }
}
