package com.example.tickbattle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.tickbattle.objects.Direction;
import com.example.tickbattle.objects.OnMoveListener;
import com.example.tickbattle.objects.OnSelectBlockListener;
import com.example.tickbattle.views.GameMap;
import com.example.tickbattle.views.MoveController;

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
        ConstraintLayout moveControllerLayout = findViewById(R.id.moveControllerLayout);


        gameMapLayout.addView(new GameMap(gameMapLayout.getContext(), new OnSelectBlockListener() {
            @Override
            public void onSelectBlock(int x, int y) {
                System.out.println(String.valueOf(x) + " " + String.valueOf(y) + " are selected");
            }
        }));


        moveControllerLayout.addView(new MoveController(moveControllerLayout.getContext(), new OnMoveListener() {
            @Override
            public void onMove(Direction direction) {
                System.out.println(direction.toString());
            }
        }));



        menuLayout.addView(new Button(menuLayout.getContext()));

        menuLayout.addView(new Button(menuLayout.getContext()));

        menuLayout.addView(new Button(menuLayout.getContext()));

        menuLayout.addView(new Button(menuLayout.getContext()));

////        this.setContentView(new GameSurface(this));
    }

}