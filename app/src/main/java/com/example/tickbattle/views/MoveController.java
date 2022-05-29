package com.example.tickbattle.views;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.example.tickbattle.Icons;
import com.example.tickbattle.R;
import com.example.tickbattle.objects.Direction;
import com.example.tickbattle.objects.OnMoveListener;

public class MoveController extends RelativeLayout {
    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 150;

    private static final int FONT_SIZE = 25;

    private Button leftButton;
    private Button rightButton;
    private Button upButton;
    private Button downButton;
    private OnMoveListener moveListener;

    public MoveController(@NonNull Context context, OnMoveListener moveListener) {
        super(context);
        this.moveListener = moveListener;
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.fa_thin_100);
        leftButton = new Button(getContext());
        leftButton.setText(Icons.LEFT_ARROW);
        leftButton.setWidth(BUTTON_WIDTH);
        leftButton.setHeight(BUTTON_HEIGHT);
        leftButton.setTypeface(typeface);
        leftButton.setTextSize(FONT_SIZE);
        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onMove(Direction.LEFT);
            }
        });

        RelativeLayout.LayoutParams leftBtnParams = new RelativeLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        leftBtnParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        leftBtnParams.addRule(RelativeLayout.CENTER_VERTICAL);
        this.addView(leftButton, leftBtnParams);



        rightButton = new Button(getContext());
        rightButton.setText(Icons.RIGHT_ARROW);
        rightButton.setWidth(BUTTON_WIDTH);
        rightButton.setHeight(BUTTON_HEIGHT);
        rightButton.setTypeface(typeface);
        rightButton.setTextSize(FONT_SIZE);

        RelativeLayout.LayoutParams rightBtnParams = new RelativeLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        rightBtnParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rightBtnParams.addRule(RelativeLayout.CENTER_VERTICAL);
        this.addView(rightButton, rightBtnParams);

        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onMove(Direction.RIGHT);
            }
        });

        upButton = new Button(getContext());
        upButton.setText(Icons.UP_ARROW);
        upButton.setWidth(BUTTON_WIDTH);
        upButton.setHeight(BUTTON_HEIGHT);
        upButton.setTypeface(typeface);
        upButton.setTextSize(FONT_SIZE);

        RelativeLayout.LayoutParams upBtnParams = new RelativeLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        upBtnParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        upBtnParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        this.addView(upButton, upBtnParams);

        upButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onMove(Direction.UP);
            }
        });

        downButton = new Button(getContext());
        downButton.setText(Icons.DOWN_ARROW);
        downButton.setWidth(BUTTON_WIDTH);
        downButton.setHeight(BUTTON_HEIGHT);
        downButton.setTypeface(typeface);
        downButton.setTextSize(FONT_SIZE);

        RelativeLayout.LayoutParams downBtnParams = new RelativeLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        downBtnParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        downBtnParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        this.addView(downButton, downBtnParams);

        downButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onMove(Direction.DOWN);
            }
        });
    }

    private void onMove(Direction direction) {
        if (moveListener != null) {
            moveListener.onMove(direction);
        }
    }
}
