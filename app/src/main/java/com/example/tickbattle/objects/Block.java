package com.example.tickbattle.objects;


import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.tickbattle.Config;
import com.example.tickbattle.GameObject;
import com.example.tickbattle.GameSurface;

public class Block extends GameObject {
    int id;
    int backgroundColor;
    protected GameSurface gameSurface;

    public Block(GameSurface gameSurface, int id, int backgroundColor, int x, int y) {
        super(Config.BLOCK_SIZE, Config.BLOCK_SIZE, x, y);
        this.id = id;
        this.backgroundColor = backgroundColor;
        this.gameSurface = gameSurface;
    }

    public int getId() {
        return id;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(backgroundColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(x, y, x + width, y + width, paint);
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void update() {
    }
}
