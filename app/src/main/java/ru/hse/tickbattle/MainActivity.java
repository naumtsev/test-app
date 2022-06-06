package ru.hse.tickbattle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.concurrent.Executors;

import io.grpc.okhttp.OkHttpChannelBuilder;
import io.grpc.stub.StreamObserver;
import ru.hse.LoginServiceGrpc;
import ru.hse.Services;
import ru.hse.tickbattle.R;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import ru.hse.tickbattle.objects.GameController;
import ru.hse.tickbattle.views.GameMap;
import ru.hse.tickbattle.views.MoveController;

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

        System.out.println("WRITE BEGIN\n\n");
        ManagedChannel channel = ManagedChannelBuilder.forAddress("192.168.0.156", 8080)
                .usePlaintext()
                .build();

        LoginServiceGrpc.LoginServiceStub stub = LoginServiceGrpc.newStub(channel);

        Services.LoginRequest request = Services.LoginRequest
                .newBuilder().setName("Android").setPassword("ANTON").build();
//
        stub.login(request, new StreamObserver<Services.LoginResponse>() {
            @Override
            public void onNext(Services.LoginResponse value) {
                System.out.println(value.getSuccess());
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("\n\n\n" + t.getMessage());
                System.out.println("ERROR\n\n\n");

            }

            @Override
            public void onCompleted() {
                channel.shutdownNow();
            }
        });
//
//        System.out.println(res.getSuccess());
        System.out.println("WRITE END\n\n");

        FrameLayout gameMapLayout = findViewById(R.id.gameMapLayout);
        LinearLayout menuLayout = findViewById(R.id.menuLayout);
        ConstraintLayout moveControllerLayout = findViewById(R.id.moveControllerLayout);

        GameController gameController = new GameController();
        GameMap gameMap = new GameMap(gameMapLayout.getContext(), gameController);
        MoveController moveController = new MoveController(moveControllerLayout.getContext(), gameController);

        gameController.init(gameMap);


        // add blocks
        gameMapLayout.addView(gameMap);

        // add moveController
        moveControllerLayout.addView(moveController);

        // add buttons to menu
        menuLayout.addView(new Button(menuLayout.getContext()));
        menuLayout.addView(new Button(menuLayout.getContext()));
        menuLayout.addView(new Button(menuLayout.getContext()));
        menuLayout.addView(new Button(menuLayout.getContext()));



        ////        this.setContentView(new GameSurface(this));
    }

}