package ru.hse.tickbattle.views;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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

public class ScoreBoardView extends TableLayout {
    private Game.PlayerList playerList;
    private static int padding = 5;
    public ScoreBoardView(Context context) {
        super(context);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
    }

    public void updateScoreBoard(@NonNull Game.PlayerList playerList) {
        this.playerList = playerList;

        ArrayList<Game.Player> players = new ArrayList<Game.Player>(playerList.getPlayerList());

        Collections.sort(players, new Comparator<Game.Player>() {
            @Override
            public int compare(Game.Player o1, Game.Player o2) {
                return o2.getCountArmy() - o1.getCountArmy();
            }
        });

        this.setPadding(30, 15, 30, 30);
        this.removeAllViews();

        TableRow ScoreboardRow = new TableRow(getContext());
        TextView scoreboardText = new TextView(getContext());
        scoreboardText.setText("Scoreboard");
        scoreboardText.setTextColor(Color.YELLOW);
        scoreboardText.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        lp.span = 3;
        ScoreboardRow.addView(scoreboardText, lp);
        this.addView(ScoreboardRow);

        int padding = 7;
        for (int i = 1; i <= players.size(); i += 1) {
            Game.Player pl = players.get(i - 1);

            TableRow playerRow = new TableRow(this.getContext());
            int color = Color.parseColor(pl.getColor());

            View place = generateView(String.valueOf(i), color);

            View login = generateView(pl.getLogin(), color);

            View army = generateView(String.valueOf(pl.getCountArmy()), color);

            if(!pl.getIsAlive()) {
                float alpha = 0.5f;
                place.setAlpha(alpha);
                login.setAlpha(alpha);
                army.setAlpha(alpha);
            }

            playerRow.addView(place);
            playerRow.addView(login);
            playerRow.addView(army);

            playerRow.setPadding(padding, 0, padding, 0);
            this.addView(playerRow);
        }

    }


    private View generateView(String text, int color) {
        TextView tv = new TextView(this.getContext());
        tv.setText(text);
        tv.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        tv.setTextColor(color);
        tv.setPadding(padding, padding, padding,padding);
        return tv;
    }
}
