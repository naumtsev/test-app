package com.example.tickbattle;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.example.tickbattle.models.Block;

public class MountainBlock extends Block {
    MountainBlock(@NonNull GameSurface gameSurface, int id, int x, int y) {
        super(gameSurface, id, Color.WHITE, x, y);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        Typeface typeface = ResourcesCompat.getFont(gameSurface.getContext(), R.font.fa_thin_100);
        Paint paint = new Paint();
        paint.setTypeface(typeface);
        paint.setColor(Color.BLACK);
        paint.setTextSize(Config.INCONS_FONT_SIZE);
        paint.setTextAlign(Paint.Align.CENTER);

        String symbol = "\uF6FD";
        Rect textBounds = new Rect();
        paint.getTextBounds(symbol, 0, symbol.length(), textBounds);

        Rect blockRect = new Rect(x, y, x + width, y + height);
        canvas.drawText(symbol, blockRect.centerX(), blockRect.centerY() - textBounds.exactCenterY(), paint);
    }

    @Override
    public void update() {
    }
}