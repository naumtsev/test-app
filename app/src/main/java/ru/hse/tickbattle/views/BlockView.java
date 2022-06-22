package ru.hse.tickbattle.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import ru.hse.tickbattle.Icons;
import ru.hse.tickbattle.R;
import ru.hse.tickbattle.UIConfig;
import ru.hse.tickbattle.objects.OnSelectBlockListener;

public class BlockView extends ConstraintLayout {
    private int x;
    private int y;

    private TextView units;

    private TextView leftArrow;
    private TextView rightArrow;
    private TextView upArrow;
    private TextView downArrow;
    private OnSelectBlockListener selectBlockListener;
    private Button btn;

    public BlockView(Context context) {
        super(context);

        View root = inflate(context, R.layout.block_view, this);

        units = root.findViewById(R.id.unitText);
        leftArrow = root.findViewById(R.id.leftArrow);
        upArrow = root.findViewById(R.id.upArrow);
        downArrow = root.findViewById(R.id.downArrow);
        rightArrow = root.findViewById(R.id.rightArrow);
        btn = root.findViewById(R.id.blockButton);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }



    public BlockView(@NonNull Context context, OnSelectBlockListener selectBlockListener, int x, int y) {
        super(context);
        this.x = x;
        this.y = y;
        this.selectBlockListener = selectBlockListener;

        View root = inflate(context, R.layout.block_view, this);

        units = root.findViewById(R.id.unitText);
        leftArrow = root.findViewById(R.id.leftArrow);
        upArrow = root.findViewById(R.id.upArrow);
        downArrow = root.findViewById(R.id.downArrow);
        rightArrow = root.findViewById(R.id.rightArrow);
        btn = root.findViewById(R.id.blockButton);

        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBlockListener.onSelectBlock(x, y);
            }
        });

        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.fa_thin_100);
        Typeface typefaceNumbers = ResourcesCompat.getFont(getContext(), R.font.numbers);
        Typeface typefaceArrows = ResourcesCompat.getFont(getContext(), R.font.fa_solid_900);

        btn.setTypeface(typeface);
        btn.setTextSize(UIConfig.ICON_FONT_SIZE);

        units.setTextColor(Color.BLACK);
        units.setTypeface(typefaceNumbers);

        leftArrow.setText(Icons.LEFT_ARROW);
        leftArrow.setVisibility(View.INVISIBLE);
        leftArrow.setTextColor(Color.BLACK);
        leftArrow.setTextSize(UIConfig.ARROW_SIZE);
        leftArrow.setTypeface(typefaceArrows);

        rightArrow.setText(Icons.RIGHT_ARROW);
        rightArrow.setVisibility(View.INVISIBLE);
        rightArrow.setTextColor(Color.BLACK);
        rightArrow.setTextSize(UIConfig.ARROW_SIZE);
        rightArrow.setTypeface(typefaceArrows);

        upArrow.setText(Icons.UP_ARROW);
        upArrow.setVisibility(View.INVISIBLE);
        upArrow.setTextColor(Color.BLACK);
        upArrow.setTextSize(UIConfig.ARROW_SIZE);
        upArrow.setTypeface(typefaceArrows);

        downArrow.setText(Icons.DOWN_ARROW);
        downArrow.setVisibility(View.INVISIBLE);
        downArrow.setTextColor(Color.BLACK);
        downArrow.setTextSize(UIConfig.ARROW_SIZE);
        downArrow.setTypeface(typefaceArrows);
    }

    public void setBorderColor(int color) {
        this.setBackgroundColor(color);
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

    public Button getButton() {
        return btn;
    }
}