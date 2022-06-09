package ru.hse.tickbattle.views;

import android.content.Context;
import android.os.Build;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ru.hse.Game;

public class ScoreBoardView extends ConstraintLayout {
    private Game.PlayerList playerList;
    private GridLayout gridLayout;
    public ScoreBoardView(Context context, Game.PlayerList playerList) {
        super(context);
        updateScoreBoard(playerList);

        LayoutParams layoutParams = new LayoutParams(300, 400);
        setLayoutParams(layoutParams);
    }

    public void updateScoreBoard(@NonNull Game.PlayerList playerList) {
        this.playerList = playerList;

        List<Game.Player> players = playerList.getPlayerList();
        Collections.sort(players, new Comparator<Game.Player>() {
            @Override
            public int compare(Game.Player o1, Game.Player o2) {
                return o1.getCountArmy() - o2.getCountArmy();
            }
        });




    }
}
