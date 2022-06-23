package ru.hse.tickbattle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ru.hse.Game;

import ru.hse.GameObject;
import ru.hse.GameServiceGrpc;
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

    private GameController gameController;
    private GameMapView gameMapView;
    private MoveController moveController;
    private ScoreBoardView scoreBoardView;
    private TextView advertTextView;


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

        gameMapLayout = findViewById(R.id.gameMapLayout);
        menuLayout = findViewById(R.id.menuLayout);
        moveControllerLayout = findViewById(R.id.moveControllerLayout);
        scoreBoardLayout = findViewById(R.id.scoreBoardLayout);
        advertTextView = findViewById(R.id.advertTextView);
        advertTextView.setAlpha(0);

        channel = ManagedChannelBuilder.forAddress(Context.getServerAddress(), Integer.parseInt(gameID)).usePlaintext().build();

        gameController = new GameController(channel);
        gameMapView = new GameMapView(gameMapLayout.getContext(), gameController);
        moveController = new MoveController(moveControllerLayout.getContext(), gameController);
        scoreBoardView = new ScoreBoardView(scoreBoardLayout.getContext());



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


    private void processGameEvent(Game.GameEvent event) {
        System.out.println("Get event " + event.getEventCase());

        switch (event.getEventCase()) {
            case PLAYERLOSTEVENT:
                break;
            case GAMEFINISHEDEVENT:
                processGameFinishedEvent(event.getGameFinishedEvent());
                break;
            case GAMESTATERESPONSE:
                processGameStateUpdatedEvent(event.getGameStateResponse());
                break;
        }

    }


    private void processGameFinishedEvent(Game.GameFinishedEvent event) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        runOnUiThread(() -> {
            advertTextView.setText(event.getReason());
            gameMapLayout.setVisibility(View.GONE);
            advertTextView.setAlpha(1);
            moveControllerLayout.setVisibility(View.GONE);
        });

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        runOnUiThread(() -> {
            advertTextView.setAlpha(0);
        });

        //  switch to choose room
    }


    private void processGameStateUpdatedEvent(GameObject.GameStateResponse event) {
        runOnUiThread(()-> {
                if (!gameController.getInitializated()) {
                    System.out.println("Init state");
                    gameController.init(gameMapView, scoreBoardView, event);
                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.start_sound);
                    mediaPlayer.start();
                } else {
                    System.out.println("update state");

                    gameController.updateGame(event);
                }
        });
    }
}