package com.example.riverraid;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Explosion {
    int x,y, width, height;
    Bitmap explosion;

    Explosion(Resources res)
    {
        explosion = BitmapFactory.decodeResource(res, R.drawable.explosion);

        width = explosion.getWidth();
        height = explosion.getHeight();

        width = 150;
        height = 150;

        explosion = Bitmap.createScaledBitmap(explosion, width, height, false);

    }
    Bitmap getExplosion()
    {
        {
            return explosion;
        }
    }
}
