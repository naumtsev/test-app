package ru.hse.tickbattle.objects;


import android.graphics.Color;

import ru.hse.GameEvents;
import ru.hse.tickbattle.Icons;
import ru.hse.tickbattle.views.Block;
import ru.hse.tickbattle.views.GameMap;

public class GameController implements OnSelectBlockListener, OnMoveListener{
    private GameMap gameMap;
    private SelectedBlock selectedBlock;
    private int w, h;

    public GameController() {
    }

    public void init(GameMap gameMap) {
        this.gameMap = gameMap;
        w = 20;
        h = 20;
        gameMap.init(w, h);
    }


    @Override
    public void onMove(Direction direction) {
        if (selectedBlock == null) {
            return;
        }

        int tox = selectedBlock.x;
        int toy = selectedBlock.y;

        if (direction == Direction.LEFT) {
            tox -= 1;
        } else if(direction == Direction.RIGHT) {
            tox += 1;
        } else if(direction == Direction.UP) {
            toy -= 1;
        } else if (direction == Direction.DOWN) {
            toy += 1;
        }


        if(0 <= tox && tox < w && 0 <= toy && toy < h) {
            selectedBlock.x = tox;
            selectedBlock.y = toy;
        }
        drawMap();
    }

    @Override
    public void onSelectBlock(int x, int y) {
        System.out.println(String.valueOf(x) + " " + String.valueOf(y));

        if (selectedBlock != null) {
            System.out.println("SelectBlock" + String.valueOf(selectedBlock.getClickNumber()));
        } else {
            System.out.println("SelectBlock");
        }

        if (selectedBlock == null || selectedBlock.getX() != x || selectedBlock.getY() != y) {
            selectedBlock = new SelectedBlock(x, y);
        } else {
            selectedBlock.click();
            if (selectedBlock.getClickNumber() == 3) {
                selectedBlock = null;
            }
        }
        drawMap();
    }

    class SelectedBlock {
        int x;
        int y;
        private int clickNumber;
        public SelectedBlock(int x, int y) {
            this.x = x;
            this.y = y;
            clickNumber = 1;
        }

        public void click() {
            clickNumber += 1;
        }

        public int getClickNumber() {
            return clickNumber;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }


    public void updateGame(GameEvents.GameStateUpdatedEvent gameStateUpdatedEvent) {
            // update
    }

    public void drawMap() {
        if (selectedBlock != null) {
            Block block = gameMap.getBlock(selectedBlock.x, selectedBlock.y);
            switch (selectedBlock.getClickNumber()) {
                case 1:
                    block.setBlockColor(Color.GREEN);
                    block.setBlockText(Icons.FORT);
                    break;
                case 2:
                    block.setBlockColor(Color.YELLOW);
                    block.setBlockText(Icons.MOUNTAIN);
                    break;
                case 3:
                    block.setBlockText("");
                    block.setBlockColor(Color.RED);
                    break;
            }
        }
    }

}
