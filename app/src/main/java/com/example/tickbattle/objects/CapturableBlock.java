package com.example.tickbattle.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.example.tickbattle.Config;
import com.example.tickbattle.GameSurface;
import com.example.tickbattle.Player;
import com.example.tickbattle.R;


public class CapturableBlock extends Block {
    Player owner;

    int countUnits;

    CapturableBlock(@NonNull GameSurface gameSurface, int id, int x, int y, int countUnits, @Nullable Player owner) {
        super(gameSurface, id, Color.WHITE, x, y);
        this.owner = owner;
        this.countUnits = countUnits;
    }

    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);
        Typeface typeface = ResourcesCompat.getFont(gameSurface.getContext(), R.font.fa_thin_100);
        Paint paint = new Paint();
        paint.setTypeface(typeface);
        paint.setColor(Color.BLACK);
        paint.setTextSize(Config.INCON_FONT_SIZE);
        paint.setTextAlign(Paint.Align.CENTER);

        String symbol = "\uF6FD";
        Rect textBounds = new Rect();
        paint.getTextBounds(symbol, 0, symbol.length(), textBounds);

        Rect blockRect = new Rect(x, y, x + width, y + height);
        canvas.drawText(symbol, blockRect.centerX(), blockRect.centerY() - textBounds.exactCenterY(), paint);
    }

    @Override
    public void update() {
        if (owner == null) {
            super.setBackgroundColor(Color.WHITE);
        } else {
            super.setBackgroundColor(owner.getColor());
        }
    }

}