package com.example.tickbattle.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.example.tickbattle.Config;
import com.example.tickbattle.Icons;
import com.example.tickbattle.R;
import com.example.tickbattle.objects.OnSelectBlockListener;

public class Block extends FrameLayout {
    private int x;
    private int y;

    private ExtendedButton btn;
    private TextView units;

    private TextView leftArrow;
    private TextView rightArrow;
    private TextView upArrow;
    private TextView downArrow;
    private OnSelectBlockListener selectBlockListener;

    public Block(@NonNull Context context, OnSelectBlockListener selectBlockListener, int x, int y) {
        super(context);
        this.x = x;
        this.y = y;
        this.selectBlockListener = selectBlockListener;

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
        leftArrow.setText(Icons.LEFT_ARROW);
        leftArrow.setTypeface(typeface);
        leftArrow.setElevation(1000);
        leftArrow.setY(Config.BLOCK_SIZE / 2f - 20);
        leftArrow.setX(20);
        leftArrow.setVisibility(View.INVISIBLE);

        rightArrow = new TextView(btn.getContext());
        rightArrow.setText(Icons.RIGHT_ARROW);
        rightArrow.setTypeface(typeface);
        rightArrow.setElevation(1000);
        rightArrow.setY(Config.BLOCK_SIZE / 2f - 20);
        rightArrow.setX(Config.BLOCK_SIZE + 6);
        rightArrow.setVisibility(View.INVISIBLE);

        upArrow = new TextView(btn.getContext());
        upArrow.setText(Icons.UP_ARROW);
        upArrow.setTypeface(typeface);
        upArrow.setElevation(1000);
        upArrow.setY(5);
        upArrow.setX(Config.BLOCK_SIZE / 2f + 14);
        upArrow.setVisibility(View.INVISIBLE);

        downArrow = new TextView(btn.getContext());
        downArrow.setText(Icons.DOWN_ARROW);
        downArrow.setTypeface(typeface);
        downArrow.setElevation(1000);
        downArrow.setY(Config.BLOCK_SIZE - 55);
        downArrow.setX(Config.BLOCK_SIZE / 2f + 14);
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

    private void selectBlock() {
        if (selectBlockListener != null) {
            selectBlockListener.onSelectBlock(x, y);
        }
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
            setWidth(Config.BLOCK_SIZE);
            setHeight(Config.BLOCK_SIZE);
            setBackgroundColor(Color.YELLOW);
            setTextSize(Config.INCON_FONT_SIZE);
            setTextAlignment(TEXT_ALIGNMENT_GRAVITY);
            setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Block.this.selectBlock();
        }
    }
}