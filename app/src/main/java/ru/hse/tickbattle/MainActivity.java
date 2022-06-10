package ru.hse.tickbattle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Random;

import ru.hse.Game;
import ru.hse.LoginServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import ru.hse.tickbattle.controllers.GameController;
import ru.hse.tickbattle.views.GameMapView;
import ru.hse.tickbattle.controllers.MoveController;
import ru.hse.tickbattle.views.ScoreBoardView;

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

//        System.out.println("WRITE BEGIN\n\n");
//        ManagedChannel channel = ManagedChannelBuilder.forAddress("192.168.0.156", 8080)
//                .usePlaintext()
//                .build();
//
//        LoginServiceGrpc.LoginServiceStub stub = LoginServiceGrpc.newStub(channel);


        FrameLayout gameMapLayout = findViewById(R.id.gameMapLayout);
        LinearLayout menuLayout = findViewById(R.id.menuLayout);
        ConstraintLayout moveControllerLayout = findViewById(R.id.moveControllerLayout);
        ConstraintLayout scoreBoardLayout = findViewById(R.id.scoreBoardLayout);



        GameController gameController = new GameController();
        GameMapView gameMapView = new GameMapView(gameMapLayout.getContext(), gameController);
        MoveController moveController = new MoveController(moveControllerLayout.getContext(), gameController);
        ScoreBoardView scoreBoardView = new ScoreBoardView(scoreBoardLayout.getContext());


        gameController.init(gameMapView, scoreBoardView, getGameState());

        // add blocks
        gameMapLayout.addView(gameMapView);

        // add moveController
        moveControllerLayout.addView(moveController);

        // add buttons to menu
        menuLayout.addView(new Button(menuLayout.getContext()));
        menuLayout.addView(new Button(menuLayout.getContext()));
        menuLayout.addView(new Button(menuLayout.getContext()));
        menuLayout.addView(new Button(menuLayout.getContext()));

        // add scoreboard
        scoreBoardLayout.addView(scoreBoardView);

        // this.setContentView(new GameSurface(this));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }




    private Game.GameStateResponse getGameState() {

        // --------------------------------
        Game.GameStateResponse.Builder gameStateResponseBuilder = Game.GameStateResponse.newBuilder();
        // state
        gameStateResponseBuilder.setGameState(Game.GameStateResponse.GameState.IN_PROGRESS);

        // player
        Game.Player.Builder playerBuilder1 = Game.Player.newBuilder();
        playerBuilder1.setColor("#9A57E0").setCountArmy(1555).setIsAlive(true).setLogin("admin");
        gameStateResponseBuilder.setPlayer(playerBuilder1.build());

        // playerList

        Game.PlayerList.Builder playerListBuilder = Game.PlayerList.newBuilder();
        playerListBuilder.addPlayer(playerBuilder1);

        Game.Player.Builder playerBuilder2 = Game.Player.newBuilder();
        playerBuilder2.setColor("#38FF2E").setCountArmy(43).setIsAlive(true).setLogin("enemy");
        playerListBuilder.addPlayer(playerBuilder2);

        playerBuilder2.setColor("#FFC373").setCountArmy(0).setIsAlive(false).setLogin("diedplayer");
        playerListBuilder.addPlayer(playerBuilder2);


        gameStateResponseBuilder.setPlayerList(playerListBuilder);
        //
        Game.GameMap.Builder gameMapBuilder = Game.GameMap.newBuilder();
        gameMapBuilder.setWidth(15);
        gameMapBuilder.setHeight(20);

        Game.BlockList.Builder blockListBuilder = Game.BlockList.newBuilder();
        for(int i = 0; i < gameMapBuilder.getHeight(); i += 1){
            for (int j = 0; j < gameMapBuilder.getWidth(); j += 1) {
                Game.Block.Builder blockBuilder  = Game.Block.newBuilder();



                Game.EmptyBlock.Builder emptyBlockBuilder = Game.EmptyBlock.newBuilder();
                emptyBlockBuilder.setCountArmy(i* gameMapBuilder.getWidth() + j);
                blockBuilder.setX(j);
                blockBuilder.setY(i);
                if (i % 6 < 4 && j > 3 && j % 9 < 4) {
                    blockBuilder.setIsHidden(true);
                } else {
                    blockBuilder.setIsHidden(false);
                }

                if (i % 3 < 2 && (i + j) % 14 < 5) {
                    emptyBlockBuilder.setOwner(playerBuilder1.build());
                } else if(i % 7 < 3 && j > 4) {
                    emptyBlockBuilder.setOwner(playerBuilder2.build());
                }
                blockBuilder.setEmptyBlock(emptyBlockBuilder.build());

                blockListBuilder.addBlock(blockBuilder.build());
            }
        }
        gameMapBuilder.setBlockList(blockListBuilder.build());
        gameStateResponseBuilder.setGameMap(gameMapBuilder.build());
        // --------------------------------

        return gameStateResponseBuilder.build();
    }
}