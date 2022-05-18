package com.example.tickbattle.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;

public class GameMap extends FrameLayout {
    private float mX;
    private float mY;
    private
    int pointerID;
    GridLayout grid;
    private float scale = 1f;
    private ScaleGestureDetector scaleDetector;

    public GameMap(Context context) {
        super(context);
        int w = 7;
        int h = 5;
        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());

        grid = new GridLayout(this.getContext());

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout
                .LayoutParams.WRAP_CONTENT);
        grid.setLayoutParams(layoutParams);
        grid.setUseDefaultMargins(true);
        grid.setBackgroundColor(Color.BLUE);
        grid.setColumnCount(w);
        grid.setRowCount(h);


        for(int i = 0; i < h; i += 1) {
            for(int j= 0; j < w; j += 1) {
                grid.addView(new ExtendedButton(grid.getContext()));
            }
        }
        addView(grid);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        int pointerIndex = event.getActionIndex();
        int pointerID = event.getPointerId(pointerIndex);
        scaleDetector.onTouchEvent(event);


        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: // первое касание
                this.mX = X;
                this.mY = Y;
                this.pointerID = pointerID;
                break;
            case MotionEvent.ACTION_MOVE:
                if (pointerID != this.pointerID) break;
                float coef = 0.1f;
                float dx = (X - mX);
                float dy = (Y - mY);

                if( Math.abs(dx) <= 80 && Math.abs(dy) <= 80) break;

                float new_x = grid.getX() + dx * coef;
                float new_y = grid.getY() + dy * coef;

                grid.setX(new_x);
                grid.setY(new_y);
                break;
            case MotionEvent.ACTION_UP:
                if(Math.abs(X - mX) <= 80 && Math.abs(Y - mY) <= 80) {
                    System.out.println("KEY UP: " + String.valueOf(Math.abs(X - mX)) + " "  + String.valueOf(Math.abs(Y - mY)));
                }
                break;

        }
        return super.dispatchTouchEvent(event);
    }



    class ExtendedButton extends androidx.appcompat.widget.AppCompatButton implements View.OnClickListener {
        int s = 0;
        public ExtendedButton(Context context) {
            super(context);
            setSoundEffectsEnabled(false);
            setWidth(200);
            setHeight(200);
            setBackgroundColor(Color.YELLOW);
            setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            s += 1;
            setText(String.valueOf(s));
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = Math.abs(detector.getScaleFactor());
            if(scale * scaleFactor < 0.3) return true;
            if(scale * scaleFactor > 1.5) return true;

            scale *= scaleFactor;
            grid.setScaleX(scale);
            grid.setScaleY(scale);
            System.out.println("SCALE: " + String.valueOf(scale));
            return true;
        }
    }

}
