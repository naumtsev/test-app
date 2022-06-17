package ru.hse.tickbattle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.List;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ru.hse.Game;

import ru.hse.GameObject;
import ru.hse.GameServiceGrpc;
import ru.hse.Room;
import ru.hse.RoomServiceGrpc;
import ru.hse.tickbattle.controllers.GameController;
import ru.hse.tickbattle.views.GameMapView;
import ru.hse.tickbattle.controllers.MoveController;
import ru.hse.tickbattle.views.ScoreBoardView;

public class GameActivity extends Activity  {
    private GameServiceGrpc.GameServiceStub stub;
    private ManagedChannel channel;

    private FrameLayout gameMapLayout;
    private LinearLayout menuLayout;
    private ConstraintLayout moveControllerLayout;
    private ConstraintLayout scoreBoardLayout;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);

        getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);


        this.setContentView(R.layout.game_map);


        System.out.println("\n\n\n");

        String login = Context.getLogin();
        System.out.println("Login:" + login);
        String gameID = getIntent().getExtras().getString("game_id");
        System.out.println("GAME:" + gameID);

        System.out.println("\n\n\n");

        channel = ManagedChannelBuilder.forAddress(Context.getServerAddress(), Integer.parseInt(gameID)).usePlaintext().build();

        Game.JoinToGameRequest req = Game.JoinToGameRequest.newBuilder().setLogin(login).setGameId(gameID).build();

        StreamObserver<Game.GameEvent> clientObserver = new StreamObserver<Game.GameEvent>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onNext(Game.GameEvent value) {
                processGameEvent(value);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("ERROR\nERROR\n");
            }

            @Override
            public void onCompleted() {
                // close game
            }

        };

        stub = GameServiceGrpc.newStub(channel);
        stub.joinToGame(req, clientObserver);


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




    private GameObject.GameStateResponse getGameState() {

        // --------------------------------
        GameObject.GameStateResponse.Builder gameStateResponseBuilder = GameObject.GameStateResponse.newBuilder();
        // state
        gameStateResponseBuilder.setGameState(GameObject.GameStateResponse.GameState.IN_PROGRESS);


        // playerList
        List<GameObject.GamePlayerInfo> playerInfoList = new ArrayList<>();

        // player1
        GameObject.Player player1 = GameObject.Player.newBuilder().setColor("#9A57E0").setLogin("admin").build();
        GameObject.GamePlayerInfo gamePlayerInfo1 = GameObject.GamePlayerInfo.newBuilder().setAlive(true).setCountArmy(134).setPlayer(player1).build();

        playerInfoList.add(gamePlayerInfo1);

        gameStateResponseBuilder.setPlayer(player1);

        //player2
        GameObject.Player player2 = GameObject.Player.newBuilder().setColor("#38FF2E").setLogin("enemy").build();
        GameObject.GamePlayerInfo gamePlayerInfo2 = GameObject.GamePlayerInfo.newBuilder().setAlive(false).setCountArmy(0).setPlayer(player2).build();
        playerInfoList.add(gamePlayerInfo2);

        // player3
        GameObject.Player player3 = GameObject.Player.newBuilder().setColor("#FFC373").setLogin("diedplayer").build();
        GameObject.GamePlayerInfo gamePlayerInfo3 = GameObject.GamePlayerInfo.newBuilder().setAlive(true).setCountArmy(24).setPlayer(player2).build();
        playerInfoList.add(gamePlayerInfo3);

        gameStateResponseBuilder.addAllGamePlayerInfo(playerInfoList);
        //
        GameObject.GameMap.Builder gameMapBuilder = GameObject.GameMap.newBuilder();
        gameMapBuilder.setWidth(15);
        gameMapBuilder.setHeight(20);

        GameObject.BlockList.Builder blockListBuilder = GameObject.BlockList.newBuilder();
        for(int i = 0; i < gameMapBuilder.getHeight(); i += 1){
            for (int j = 0; j < gameMapBuilder.getWidth(); j += 1) {
                GameObject.Block.Builder blockBuilder  = GameObject.Block.newBuilder();



                GameObject.EmptyBlock.Builder emptyBlockBuilder = GameObject.EmptyBlock.newBuilder();
                emptyBlockBuilder.setCountArmy(i* gameMapBuilder.getWidth() + j);
                blockBuilder.setX(j);
                blockBuilder.setY(i);
                if (i % 6 < 4 && j > 3 && j % 9 < 4) {
                    blockBuilder.setHidden(true);
                } else {
                    blockBuilder.setHidden(false);
                }

                if (i % 3 < 2 && (i + j) % 14 < 5) {
                    emptyBlockBuilder.setOwner(player1);
                } else if(i % 7 < 3 && j > 4) {
                    emptyBlockBuilder.setOwner(player2);
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

    private void processGameEvent(Game.GameEvent event) {
        switch (event.getEventCase()) {
            case PLAYERLOSTEVENT:
                break;
            case GAMEFINISHEDEVENT:
                break;
            case GAMESTATEUPDATEDEVENT:
                System.out.println("GAMESTATE UPDATED");
                break;

        }

    }
}