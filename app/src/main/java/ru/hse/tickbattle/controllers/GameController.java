package ru.hse.tickbattle.controllers;


import android.graphics.Color;

import ru.hse.Game;
import ru.hse.tickbattle.UIConfig;
import ru.hse.tickbattle.Icons;
import ru.hse.tickbattle.objects.Direction;
import ru.hse.tickbattle.objects.OnMoveListener;
import ru.hse.tickbattle.objects.OnSelectBlockListener;
import ru.hse.tickbattle.views.BlockView;
import ru.hse.tickbattle.views.GameMapView;
import ru.hse.tickbattle.views.ScoreBoardView;

public class GameController implements OnSelectBlockListener, OnMoveListener {
    private GameMapView gameMapView;
    private SelectedBlock selectedBlock;
    private Game.PlayerList players;
    private Game.GameMap gameMap;
    private Game.Player player;
    private ScoreBoardView scoreBoardView;
    private int w, h;

    public GameController() {
    }

    public void init(GameMapView gameMapView, ScoreBoardView scoreBoardView, Game.GameStateResponse gameStateResponse) {
        this.gameMapView = gameMapView;
        this.scoreBoardView = scoreBoardView;
        gameMapView.init(gameStateResponse.getGameMap().getWidth(), gameStateResponse.getGameMap().getHeight());
        updateGame(gameStateResponse);
    }


    @Override
    public void onMove(Direction direction) {
        if (selectedBlock == null) {
            return;
        }

        int tox = selectedBlock.x;
        int toy = selectedBlock.y;

        switch (direction) {
            case UP:
                toy -= 1;
                break;
            case DOWN:
                toy += 1;
                break;
            case LEFT:
                tox -= 1;
                break;
            case RIGHT:
                tox += 1;
                break;
        }

        if (checkCords(tox, toy)) {
            selectedBlock.x = tox;
            selectedBlock.y = toy;
        }

        drawMap();
    }

    @Override
    public void onSelectBlock(int x, int y) {
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

    static class SelectedBlock {
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


    public void updateGame(Game.GameStateResponse gameStateResponse) {
        this.players = gameStateResponse.getPlayerList();
        this.gameMap = gameStateResponse.getGameMap();
        this.player = gameStateResponse.getPlayer();

        w = this.gameMap.getWidth();
        h = this.gameMap.getHeight();
        drawMap();
    }

    public void drawMap() {
        scoreBoardView.updateScoreBoard(players);

        for (int i = 0; i < h; i += 1) {
            for(int j = 0; j < w; j += 1) {
                Game.Block block = gameMap.getBlockList().getBlock(i * w + j);

                BlockView blockView = gameMapView.getBlock(j, i);
                resetBlock(blockView);

                if (!block.getIsHidden()) {
                    switch (block.getBlockCase()) {
                        case FARMBLOCK:
                            setFarmBlock(blockView, block.getFarmBlock());
                            break;
                        case WALLBLOCK:
                            setWallBlock(blockView, block.getWallBlock());
                            break;
                        case EMPTYBLOCK:
                            setEmptyBlock(blockView, block.getEmptyBlock());
                            break;
                        case CASTLEBLOCK:
                            setCastleBlock(blockView, block.getCastleBlock());
                            break;
                        case BLOCK_NOT_SET:
                            blockView.setBlockColor(Color.GREEN);
                            break;
                    }
                }
            }
        }

        drawSelectedBlock();
    }

    private boolean checkCords(int x, int y) {
        return 0 <= x && x < w && 0 <= y && y < h;
    }

    private void drawSelectedBlock() {
        if (selectedBlock == null) {
            return;
        }

        BlockView blockView = gameMapView.getBlock(selectedBlock.x, selectedBlock.y);
        switch (selectedBlock.getClickNumber()) {
            case 1:
                blockView.setBlockColor(Color.RED);
                break;
            case 2:
                blockView.setBlockColor(Color.YELLOW);
                break;
        }


        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, 1, -1};

        for(int i = 0; i < 4; i += 1) {
            int tox = selectedBlock.x + dx[i];
            int toy = selectedBlock.y + dy[i];
            if(checkCords(tox, toy)) {
                BlockView neighboringBlockView = gameMapView.getBlock(tox, toy);
                neighboringBlockView.setAlpha(0.5f);
            }
        }

    }

    private void resetBlock(BlockView blockView) {
        blockView.setAlpha(1);
        blockView.setUnitText("");
        blockView.setBlockText("");
        blockView.setVisableLeftArrow(false);
        blockView.setVisableRightArrow(false);
        blockView.setVisableDownArrow(false);
        blockView.setVisableUpArrow(false);
        blockView.setBlockColor(UIConfig.HIDDEN_BLOCK_COLOR);
    }


    private void setFarmBlock(BlockView blockView, Game.FarmBlock farmBlock) {
        if (farmBlock.hasOwner()) {
           blockView.setBlockColor(Color.parseColor(farmBlock.getOwner().getColor()));
        } else {
            blockView.setBlockColor(UIConfig.NEUTRAL_BLOCK_COLOR);
        }


        if (farmBlock.getCountArmy() != 0) {
            blockView.setUnitText(String.valueOf(farmBlock.getCountArmy()));
        }

        blockView.setBlockText(Icons.FARM);
    }

    private void setCastleBlock(BlockView blockView, Game.CastleBlock castleBlock) {
        if (castleBlock.hasOwner()) {
            blockView.setBlockColor(Color.parseColor(castleBlock.getOwner().getColor()));
        } else {
            blockView.setBlockColor(UIConfig.NEUTRAL_BLOCK_COLOR);
        }

        if (castleBlock.getCountArmy() != 0) {
            blockView.setUnitText(String.valueOf(castleBlock.getCountArmy()));
        }

        blockView.setBlockText(Icons.CASTLE);
    }


    private void setWallBlock(BlockView blockView, Game.WallBlock wallBlock) {
        blockView.setBlockText(Icons.WALL);
    }


    private void setEmptyBlock(BlockView blockView, Game.EmptyBlock emptyBlock) {
        if (emptyBlock.hasOwner()) {
            blockView.setBlockColor(Color.parseColor(emptyBlock.getOwner().getColor()));
        } else {
            blockView.setBlockColor(UIConfig.NEUTRAL_BLOCK_COLOR);
        }


        if (emptyBlock.getCountArmy() != 0) {
            blockView.setUnitText(String.valueOf(emptyBlock.getCountArmy()));
        }
    }

}
