package com.example.tickbattle.views;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
;import ua.org.tenletters.widget.DiagonalScrollView;

public class GameMap extends DiagonalScrollView {
    private float startX;
    private float startY;
    private int startPointerID;
    GridLayout grid;
    private float currentScale = 1f;
    private final ScaleGestureDetector scaleDetector;

    public GameMap(Context context) {
        super(context);


        FrameLayout.LayoutParams mainLayoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.setLayoutParams(mainLayoutParams);

        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());


        int w = 15;
        int h = 10;

        grid = new GridLayout(this.getContext());
        grid.setColumnCount(h);
        grid.setRowCount(w);
        grid.setUseDefaultMargins(true);

        for(int i = 0; i < h; i += 1) {
            for(int j = 0; j < w; j += 1) {
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
                this.startX = X;
                this.startY = Y;
                this.startPointerID = pointerID;
                System.out.println("DOWN" + String.valueOf(event.getX()));
                break;
            case MotionEvent.ACTION_MOVE:
                if (pointerID != this.startPointerID) break;
                float coef = 0.1f;
                float dx = (X - startX);
                float dy = (Y - startY);

                if( Math.abs(dx) <= 80 && Math.abs(dy) <= 80) break;

                float new_x = grid.getX() + dx * coef;
                float new_y = grid.getY() + dy * coef;

                grid.setX(new_x);
                grid.setY(new_y);
                break;
        }
        super.dispatchTouchEvent(event);
        return true;
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
            if(currentScale * scaleFactor < 0.3) return true;
            if(currentScale * scaleFactor > 1.5) return true;

            currentScale *= scaleFactor;
            grid.setScaleX(currentScale);
            grid.setScaleY(currentScale);
            System.out.println("SCALE: " + String.valueOf(currentScale));
            return true;
        }
    }

}
