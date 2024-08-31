package com.example.riverraid;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

public class Enemy {
    int x,y, width, height;
    Bitmap enemy, enemy2;
    Random rand = new Random();
    int a;
   Enemy(Resources res)
   {
       enemy = BitmapFactory.decodeResource(res, R.drawable.enemy3);

       width = enemy.getWidth();
       height = enemy.getHeight();

       width = 150;
       height = 150;

       enemy = Bitmap.createScaledBitmap(enemy, width, height, false);
   }
    Bitmap getEnemy()
    {
            return enemy;
    }
    Rect getCollisonShape(int x, int y)
    {
        return new Rect(x, y, x + width, y + height);
    }

}
