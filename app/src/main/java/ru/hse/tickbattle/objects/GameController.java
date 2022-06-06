package ru.hse.tickbattle.objects;


import ru.hse.tickbattle.views.GameMap;

public class GameController implements OnSelectBlockListener, OnMoveListener{
    private GameMap gameMap;

    public GameController() {

    }

    public void init(GameMap gameMap) {
        this.gameMap = gameMap;
        gameMap.init(20, 20);
    }


    @Override
    public void onMove(Direction direction) {

    }

    @Override
    public void onSelectBlock(int x, int y) {

    }

}
