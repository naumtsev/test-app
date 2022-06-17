package ru.hse.tickbattle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.internal.ClientStream;
import io.grpc.stub.ClientCallStreamObserver;
import io.grpc.stub.StreamObserver;
import ru.hse.Game;
import ru.hse.GameObject;
import ru.hse.Room;
import ru.hse.RoomServiceGrpc;


public class RoomActivity extends Activity {
    private RoomServiceGrpc.RoomServiceStub stub;
    private ManagedChannel channel;
    private LinearLayout roomLogLayout;
    private LinearLayout roomButtonsLayout;
    private LinearLayout playersLayout;
    private TextView numberPlayersToStartView;
    int numberPlayersToStart = 0;
    private final ArrayList<GameObject.Player> players = new ArrayList<>();

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


        this.setContentView(R.layout.room);

        Context.setLogin(UUID.randomUUID().toString().substring(0, 6));


        String roomName = "";


        channel = ManagedChannelBuilder.forAddress(Context.getServerAddress(), Context.getRoomPort()).usePlaintext().build();

        Room.JoinToRoomRequest req = Room.JoinToRoomRequest.newBuilder().setLogin(Context.getLogin()).setRoomName(roomName).build();

        StreamObserver<Room.RoomEvent> clientObserver = new StreamObserver<Room.RoomEvent>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onNext(Room.RoomEvent value) {
                processRoomEvent(value);
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

        stub = RoomServiceGrpc.newStub(channel);
        stub.joinToRoom(req, clientObserver);


        TextView roomNameTextView = findViewById(R.id.roomText);
        roomNameTextView.setText(roomName);


        roomLogLayout = findViewById(R.id.roomLogLayout);
        roomButtonsLayout = findViewById(R.id.roomButtonsLayout);
        playersLayout = findViewById(R.id.roomPlayersLayout);
        numberPlayersToStartView = findViewById(R.id.numberPlayersToStartTextView);

        roomButtonsLayout.addView(new LeaveButton(roomButtonsLayout.getContext()));
        roomButtonsLayout.addView(new Button(roomButtonsLayout.getContext()));
    }

    private void processRoomEvent(Room.RoomEvent value) {

        System.out.println(value.getEventCase());
        switch (value.getEventCase()) {
            case JOINTOROOMRESPONSE:
                processJoinToRoomResponse(value.getJoinToRoomResponse());
                break;
            case OTHERPLAYERJOINEDEVENT:
                processOtherPlayerJoinedEvent(value.getOtherPlayerJoinedEvent());
                break;
            case OTHERPLAYERDISCONNECTEDEVENT:
                processOtherPlayerDisconnectedEvent(value.getOtherPlayerDisconnectedEvent());
                break;
            case GAMESTARTEDEVENT:
                processGameStartedEvent(value.getGameStartedEvent());
                break;
        }
    }

    private void processJoinToRoomResponse(Room.JoinToRoomResponse res) {
        boolean success = res.getSuccess();
        System.out.println(res.getSuccess());
        ArrayList<GameObject.Player> playerList = new ArrayList<>(res.getPlayerList());
        numberPlayersToStart = res.getNumberPlayersToStart();
        if (success) {
            synchronized (players) {
                players.addAll(playerList);
            }
            runOnUiThread(() -> addToLog("Connected successfully...", Color.GREEN));

        } else {
            String comment = res.getComment();
            synchronized (players) {
                players.clear();
            }
            runOnUiThread(() -> roomLogLayout.addView(generateView(roomLogLayout.getContext(), comment, Color.RED)));

            runOnUiThread(() -> channel.shutdownNow());
            runOnUiThread(() -> addToLog("Connection failed", Color.RED));
        }
        runOnUiThread(this::draw);

    }

    private TextView generateView(android.content.Context context, String text, int color) {
        TextView tv = new TextView(context);
        tv.setText(text);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        tv.setTextColor(color);
        return tv;
    }


    private void processOtherPlayerJoinedEvent(Room.OtherPlayerJoinedEvent res) {
        GameObject.Player player = res.getPlayer();

        synchronized (players) {
            players.add(player);
        }

        String text = player.getLogin() + " joined";
        runOnUiThread(() -> addToLog(text, Color.WHITE));
        runOnUiThread(this::draw);
    }

    private void processOtherPlayerDisconnectedEvent(Room.OtherPlayerDisconnectedEvent res) {
        synchronized (players) {
            for (int i = players.size() - 1; i >= 0; i -= 1) {
                if (players.get(i).getLogin().equals(res.getPlayerLogin())) {
                    players.remove(i);
                }
            }
        }

        String text = res.getPlayerLogin() + " disconnected";
        runOnUiThread(() -> addToLog(text, Color.WHITE));
        runOnUiThread(this::draw);

    }


    private void processGameStartedEvent(Room.GameStartedEvent res) {
        String text = "GAME STARTED";
        runOnUiThread(() -> addToLog(text, Color.GREEN));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        runOnUiThread(() ->{
            Intent intent = new Intent(this, GameActivity.class);


            Bundle b = new Bundle();
            b.putString("game_id", res.getGameId());
            intent.putExtras(b);
            startActivity(intent);
            }
        );
    }

    private void addToLog(String text, int color) {
        TextView tv = generateView(roomLogLayout.getContext(), text, color);
        tv.setTypeface(ResourcesCompat.getFont(roomLogLayout.getContext(), R.font.fa_thin_100));
        roomLogLayout.addView(tv);
    }

    @SuppressLint("SetTextI18n")
    private void draw() {
        playersLayout.removeAllViews();
        synchronized (players) {
            for (GameObject.Player player : players) {
                String login = player.getLogin();
                int color = Color.parseColor(player.getColor());
                TextView tv = generateView(playersLayout.getContext(), login, color);
                tv.setPadding(15, 15, 15, 15);
                playersLayout.addView(tv);
            }
            numberPlayersToStartView.setText(players.size() + "/" + numberPlayersToStart);
        }


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


    private class LeaveButton extends androidx.appcompat.widget.AppCompatButton implements View.OnClickListener {
        public LeaveButton(android.content.Context context) {
            super(context);
            setOnClickListener(this);
            setText("Leave");
        }


        @Override
        public void onClick(View v) {

        }
    }

}