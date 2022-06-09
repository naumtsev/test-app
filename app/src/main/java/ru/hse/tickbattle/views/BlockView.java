package ru.hse.tickbattle.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import ru.hse.tickbattle.Config;
import ru.hse.tickbattle.Icons;
import ru.hse.tickbattle.R;
import ru.hse.tickbattle.objects.OnSelectBlockListener;

public class BlockView extends FrameLayout {
    private final int x;
    private final int y;

    private final ExtendedButton btn;
    private final TextView units;

    private final TextView leftArrow;
    private final TextView rightArrow;
    private final TextView upArrow;
    private final TextView downArrow;
    private final OnSelectBlockListener selectBlockListener;

    public BlockView(@NonNull Context context, OnSelectBlockListener selectBlockListener, int x, int y) {
        super(context);
        this.x = x;
        this.y = y;
        this.selectBlockListener = selectBlockListener;


        btn = new ExtendedButton(this.getContext());

        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.fa_thin_100);
        Typeface typefaceNumbers = ResourcesCompat.getFont(getContext(), R.font.numbers);

        btn.setTypeface(typeface);
        btn.setText(String.valueOf(btn.getHeight()));

        units = new TextView(this.getContext());
        units.setTextSize(30);
        units.setTypeface(typefaceNumbers);
        units.setTextColor(Color.BLACK);


        LayoutParams lp_text = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp_text.setMargins(10, -15, 0, 0);
        units.setLayoutParams(lp_text);
        units.setElevation(1000);

        leftArrow = new TextView(btn.getContext());
        leftArrow.setText(Icons.LEFT_ARROW);
        leftArrow.setTypeface(typeface);
        leftArrow.setElevation(1000);
        leftArrow.setY(Config.BLOCK_SIZE / 2f - 20);
        leftArrow.setX(20);
        leftArrow.setVisibility(View.INVISIBLE);
        leftArrow.setTextColor(Color.BLACK);

        rightArrow = new TextView(btn.getContext());
        rightArrow.setText(Icons.RIGHT_ARROW);
        rightArrow.setTypeface(typeface);
        rightArrow.setElevation(1000);
        rightArrow.setY(Config.BLOCK_SIZE / 2f - 20);
        rightArrow.setX(Config.BLOCK_SIZE + 6);
        rightArrow.setVisibility(View.INVISIBLE);
        rightArrow.setTextColor(Color.BLACK);

        upArrow = new TextView(btn.getContext());
        upArrow.setText(Icons.UP_ARROW);
        upArrow.setTypeface(typeface);
        upArrow.setElevation(1000);
        upArrow.setY(5);
        upArrow.setX(Config.BLOCK_SIZE / 2f + 14);
        upArrow.setVisibility(View.INVISIBLE);
        upArrow.setTextColor(Color.BLACK);

        downArrow = new TextView(btn.getContext());
        downArrow.setText(Icons.DOWN_ARROW);
        downArrow.setTypeface(typeface);
        downArrow.setElevation(1000);
        downArrow.setY(Config.BLOCK_SIZE - 55);
        downArrow.setX(Config.BLOCK_SIZE / 2f + 14);
        downArrow.setVisibility(View.INVISIBLE);
        downArrow.setTextColor(Color.BLACK);


        addView(btn);
        addView(leftArrow);
        addView(rightArrow);
        addView(upArrow);
        addView(downArrow);
        addView(units);

    }

    public void setBlockText(@NonNull String text) {
        btn.setText(text);
    }

    public void setUnitText(@NonNull String text) {
        units.setText(text);
    }

    public void setBlockColor(int color) {
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

    public void setVisableDownArrow(boolean visable) {
        if (visable) {
            downArrow.setVisibility(View.VISIBLE);
        } else {
            downArrow.setVisibility(View.INVISIBLE);
        }
    }

    public void setVisableUpArrow(boolean visable) {
        if (visable) {
            upArrow.setVisibility(View.VISIBLE);
        } else {
            upArrow.setVisibility(View.INVISIBLE);
        }
    }

    class ExtendedButton extends androidx.appcompat.widget.AppCompatButton implements View.OnClickListener {
        public ExtendedButton(Context context) {
            super(context);

            setSoundEffectsEnabled(false);
            setWidth(Config.BLOCK_SIZE);
            setHeight(Config.BLOCK_SIZE);
            setTextSize(Config.INCON_FONT_SIZE);
            setTextAlignment(TEXT_ALIGNMENT_GRAVITY);
            setOnClickListener(this);
            setTextColor(Color.BLACK);
        }

        @Override
        public void onClick(View v) {
            BlockView.this.selectBlock();
        }
    }
}