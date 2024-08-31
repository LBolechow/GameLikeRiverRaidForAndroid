package com.example.riverraid;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Flight {
int x,y, width, height;
Bitmap flight1;

Flight(int screenX, Resources res)
{
    flight1 = BitmapFactory.decodeResource(res, R.drawable.jet);

    width = flight1.getWidth();
    height = flight1.getHeight();

    width = 150;
    height = 150;

    flight1 = Bitmap.createScaledBitmap(flight1, width, height, false);
    x = screenX/2;
    y = 1950;
}
Bitmap getFlight()
{
   return flight1;
}
    Rect getCollisonShape(int x, int y)
    {
        return new Rect(x, y, x+width, y + height);
    }

}
