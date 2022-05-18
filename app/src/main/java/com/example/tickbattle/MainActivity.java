package com.example.tickbattle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.example.tickbattle.views.GameMap;

public class MainActivity extends Activity  {
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);


        this.setContentView(R.layout.game_map);


        FrameLayout gameMapLayout = findViewById(R.id.gameMapLayout);

        LinearLayout menuLayout = findViewById(R.id.menuLayout);

        gameMapLayout.addView(new GameMap(gameMapLayout.getContext()));

        menuLayout.addView(new Button(menuLayout.getContext()));

        menuLayout.addView(new Button(menuLayout.getContext()));

        menuLayout.addView(new Button(menuLayout.getContext()));

        menuLayout.addView(new Button(menuLayout.getContext()));



////        this.setContentView(new GameSurface(this));
    }

}