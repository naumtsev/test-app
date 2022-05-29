package com.example.tickbattle.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.TextView;
;import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.example.tickbattle.Config;
import com.example.tickbattle.R;
import com.example.tickbattle.Icons;
import com.example.tickbattle.objects.OnSelectBlockListener;

import ua.org.tenletters.widget.DiagonalScrollView;


public class GameMap extends DiagonalScrollView {
    GridLayout grid;
    private float currentScale = 1f;
    private final ScaleGestureDetector scaleDetector;
    public GameMap(Context context, OnSelectBlockListener selectBlockListener) {
        super(context);


        FrameLayout.LayoutParams mainLayoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.setLayoutParams(mainLayoutParams);

        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());

        int w = 10;
        int h = 10;

        grid = new GridLayout(this.getContext());
        grid.setColumnCount(h);
        grid.setRowCount(w);
        grid.setUseDefaultMargins(true);

        for (int i = 0; i < h; i += 1) {
            for (int j = 0; j < w; j += 1) {
                grid.addView(new Block(grid.getContext(), selectBlockListener,  j, i));
            }
        }

        addView(grid);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        scaleDetector.onTouchEvent(event);
        super.dispatchTouchEvent(event);
        return true;
    }


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = Math.abs(detector.getScaleFactor());
            if (currentScale * scaleFactor < 0.3) return true;
            if (currentScale * scaleFactor > 1.5) return true;

            currentScale *= scaleFactor;
            grid.setScaleX(currentScale);
            grid.setScaleY(currentScale);
            return true;
        }
    }
}
