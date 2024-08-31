package com.example.riverraid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;
import java.util.Random;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isPlaying;
    final private int screenX, screenY;
    final private Paint paint;
    private Flight flight;
    private Explosion explosion;
    private SharedPreferences prefs;
    private Enemy enemy;
    private int score = 0;
    private GameActivity activity;
    private boolean isOver = false;
    private List<Missile> missiles;
    private List<Enemy> enemies;
    private Background background1, background2;
    Random rand = new Random();

    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);
        this.activity = activity;
        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);
        this.screenX = screenX;
        this.screenY = screenY;
        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.BLACK);
        missiles = new ArrayList<>();
        enemies = new ArrayList<>();
        flight = new Flight(screenX, getResources());
        explosion = new Explosion(getResources());
        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());
        background2.y = screenY;

    }

    @Override
    public void run() {
        while (isPlaying) {
            update();
            draw();
            sleep();


        }

    }


    private void update() {
        List<Enemy> trash = new ArrayList<>();
        background1.y -= 5;
        background2.y -= 5;
        if (background1.y + background1.background.getHeight() < 0) {
            background1.y = screenY;
        }
        if (background2.y + background2.background.getHeight() < 0) {
            background2.y = screenY;
        }
        ctrlMissile();
        createEnemy();
        ctrlEnemy();
        for (Enemy enemy : enemies) {
            if (Rect.intersects(enemy.getCollisonShape(enemy.x, enemy.y), flight.getCollisonShape(flight.x, flight.y))) {
                explosion.x = flight.x;
                explosion.y = flight.y;
                flight.x = -500;
                flight.y = -500;
                isOver = true;
            }
        }
        for (Enemy enemy : enemies) {
            for (Missile missile : missiles) {
                if (Rect.intersects(enemy.getCollisonShape(enemy.x, enemy.y), missile.getCollisonShape(missile.x, missile.y))) {
                    score++;
                    enemy.y = 5000;
                    missile.x = -5000;
                    trash.add(enemy);
                }
            }

        }
        for (Enemy enemy : trash) {
            enemies.remove(enemy);
        }
        if (trash.size() > 10) {
            trash.clear();
        }


    }

    private void ctrlMissile() {
        List<Missile> trash = new ArrayList<>();
        for (Missile missile : missiles) {
            if (missile.y < 0) {
                trash.add(missile);
            }

            missile.y -= 25;
        }
        for (Missile missile : trash) {
            missiles.remove(missile);
        }
        if (trash.size() > 10) {
            trash.clear();
        }
    }

    private void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);
            canvas.drawBitmap(flight.getFlight(), flight.x, flight.y, paint);
            canvas.drawText(score + "", screenX / 2f, 164, paint);
            if (isOver) {
                canvas.drawBitmap(explosion.getExplosion(), explosion.x, explosion.y, paint);
                canvas.drawText("Game Over", screenX / 5, screenY / 2f, paint);
                getHolder().unlockCanvasAndPost(canvas);
                isPlaying = false;
                saveHighScore();
                exit();
                return;
            }

            for (Missile missile : missiles) {
                canvas.drawBitmap(missile.getMissile(), missile.x, missile.y, paint);
            }
            for (Enemy enemy : enemies) {
                canvas.drawBitmap(enemy.getEnemy(), enemy.x, enemy.y, paint);
            }
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void sleep() {
        try {
            thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void exit()
    {
        try {
            Thread.sleep(2000);
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                if(event.getX() < screenX /2 && event.getY() > screenY / 2)
                {
                    flight.x -= 85;
                    if (flight.x < 0)
                    {
                        flight.x = 0;
                    }
                }
                if (event.getX() > screenX / 2 && event.getY() > screenY / 2)
                {
                    flight.x += 85;
                    if (flight.x > screenX - flight.height)
                    {
                        flight.x = screenX - flight.height;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (event.getY() < screenY / 2)
                {
                    if (missiles.size() < 4) {
                        Missile missile = new Missile(getResources());
                        missile.x = flight.x + 70;
                        missile.y = flight.y;
                        missiles.add(missile);
                    }

                }
                break;
        }

        return true;
    }
public void createEnemy()
{
    if (enemies.size() < rand.nextInt(4))
    {
        enemy = new Enemy(getResources());
        enemy.x = rand.nextInt(screenX - 100);
        enemy.y = 0;
        enemies.add(enemy);
    }
}
    private void ctrlEnemy()
    {
        for (Enemy enemy: enemies)
        {
            if (enemy.y > screenY)
            {
                explosion.x = flight.x;
                explosion.y = flight.y;
                flight.x = -500;
                flight.y = -500;
                isOver = true;
            }
            if (score <= 10)
            {
                enemy.y += 6;
            }
            else if ( 10 < score  &&  score <= 25)
            {
                enemy.y += 8;
            }
            else if ( 25 < score  &&  score <= 50)
            {
                enemy.y += 10;
            }
            else if ( 50 < score)
            {
                enemy.y += 13;
            }

        }

    }
    private void saveHighScore()
    {
        if(prefs.getInt("highscore", 0) < score)
        {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore", score);
            editor.apply();
        }
    }



}
