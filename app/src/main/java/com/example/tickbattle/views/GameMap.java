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

import com.example.tickbattle.R;

import ua.org.tenletters.widget.DiagonalScrollView;

enum DIRECTION {
    LEFT,
    RIGHT,
    UP,
    DOWN
}


public class GameMap extends DiagonalScrollView {
    GridLayout grid;
    private float currentScale = 1f;
    private final ScaleGestureDetector scaleDetector;

    public GameMap(Context context) {
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
                grid.addView(new Block(grid.getContext(), j, i));
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

    class Block extends FrameLayout {
        private int BLOCK_SIZE = 200;
        private int x;
        private int y;

        private ExtendedButton btn;
        private TextView units;

        private TextView leftArrow;
        private TextView rightArrow;
        private TextView upArrow;
        private TextView downArrow;


        public Block(@NonNull Context context, int x, int y) {
            super(context);
            this.x = x;
            this.y = y;

            btn = new ExtendedButton(this.getContext());

            Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.fa_thin_100);

            btn.setTypeface(typeface);
            btn.setText(String.valueOf(btn.getHeight()));

            units = new TextView(btn.getContext());
            units.setTextSize(15);
            units.setTypeface(typeface);


            LayoutParams lp_text = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp_text.setMargins(10, 0, 0, 0);
            units.setLayoutParams(lp_text);
            units.setElevation(1000);

            leftArrow = new TextView(btn.getContext());
            leftArrow.setText("\uF060");
            leftArrow.setTypeface(typeface);
            leftArrow.setElevation(1000);
            leftArrow.setY(BLOCK_SIZE / 2f - 20);
            leftArrow.setX(20);
            leftArrow.setVisibility(View.INVISIBLE);

            rightArrow = new TextView(btn.getContext());
            rightArrow.setText("\uF061");
            rightArrow.setTypeface(typeface);
            rightArrow.setElevation(1000);
            rightArrow.setY(BLOCK_SIZE / 2f - 20);
            rightArrow.setX(BLOCK_SIZE + 6);
            rightArrow.setVisibility(View.INVISIBLE);

            upArrow = new TextView(btn.getContext());
            upArrow.setText("\uF062");
            upArrow.setTypeface(typeface);
            upArrow.setElevation(1000);
            upArrow.setY(5);
            upArrow.setX(BLOCK_SIZE / 2f + 14);
            upArrow.setVisibility(View.INVISIBLE);

            downArrow = new TextView(btn.getContext());
            downArrow.setText("\uF063");
            downArrow.setTypeface(typeface);
            downArrow.setElevation(1000);
            downArrow.setY(BLOCK_SIZE - 55);
            downArrow.setX(BLOCK_SIZE / 2f + 14);
            downArrow.setVisibility(View.INVISIBLE);

            addView(btn);
            addView(leftArrow);
            addView(rightArrow);
            addView(upArrow);
            addView(downArrow);
            addView(units);
        }

        void setBlockText(@NonNull String text) {
            btn.setText(text);
        }

        void setUnitText(@NonNull String text) {
            units.setText(text);
        }

        void setBlockColor(int color) {
            btn.setBackgroundColor(color);
        }

        private void processClick() {
            // process (x, y)
        }


        public void setVisableRightArrow(boolean visable) {
            if (visable) {
                rightArrow.setVisibility(View.VISIBLE);
            } else {
                rightArrow.setVisibility(View.INVISIBLE);
            }
        }

        public void setVisableLeftArrow(boolean visable) {
            if (visable) {
                leftArrow.setVisibility(View.VISIBLE);
            } else {
                leftArrow.setVisibility(View.INVISIBLE);
            }
        }

        public void setVisableUpArrow(boolean visable) {
            if (visable) {
                leftArrow.setVisibility(View.VISIBLE);
            } else {
                leftArrow.setVisibility(View.INVISIBLE);
            }
        }

        public void setVisableDownArrow(boolean visable) {
            if (visable) {
                downArrow.setVisibility(View.VISIBLE);
            } else {
                downArrow.setVisibility(View.INVISIBLE);
            }
        }


        class ExtendedButton extends androidx.appcompat.widget.AppCompatButton implements View.OnClickListener {
            public ExtendedButton(Context context) {
                super(context);

                setSoundEffectsEnabled(false);
                setWidth(BLOCK_SIZE);
                setHeight(BLOCK_SIZE);
                setBackgroundColor(Color.YELLOW);
                setTextSize(25);
                setTextAlignment(TEXT_ALIGNMENT_GRAVITY);
                setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Block.this.processClick();
            }
        }
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
