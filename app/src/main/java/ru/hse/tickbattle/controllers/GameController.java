package ru.hse.tickbattle.controllers;


import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.BoringLayout;
import android.view.View;

import java.util.List;

import io.grpc.ManagedChannel;
import ru.hse.Game;
import ru.hse.GameObject;
import ru.hse.GameServiceGrpc;
import ru.hse.tickbattle.Context;
import ru.hse.tickbattle.Player;
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
    private List<GameObject.GamePlayerInfo> players;
    private GameObject.GameMap gameMap;
    private GameObject.Player player;
    private ScoreBoardView scoreBoardView;
    private List<GameObject.Move> playerMoveList;
    private int w, h;
    private Boolean initializated = false;
    private GameServiceGrpc.GameServiceFutureStub stub;
    public GameController(ManagedChannel channel) {
        stub = GameServiceGrpc.newFutureStub(channel);
    }

    public synchronized Boolean getInitializated() {
        return initializated;
    }

    public synchronized void init(GameMapView gameMapView, ScoreBoardView scoreBoardView, GameObject.GameStateResponse gameStateResponse) {
        this.gameMapView = gameMapView;
        this.scoreBoardView = scoreBoardView;
        gameMapView.init(gameStateResponse.getGameMap().getWidth(), gameStateResponse.getGameMap().getHeight());
        updateGame(gameStateResponse);
        initializated = true;
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
            GameObject.BlockCoordinate start = GameObject.BlockCoordinate.newBuilder().setX(selectedBlock.x).setY(selectedBlock.y).build();
            GameObject.BlockCoordinate end = GameObject.BlockCoordinate.newBuilder().setX(tox).setY(toy).build();
            Game.AttackRequest.Builder req = Game.AttackRequest.newBuilder().setStart(start).setEnd(end);
            GameObject.Player player = GameObject.Player.newBuilder().setLogin(Context.getLogin()).setColor("").build();

            req.setPlayer(player);
            req.setIs50(selectedBlock.getClickNumber() != 1);
            selectedBlock.clickNumber = 1;
            selectedBlock.x = tox;
            selectedBlock.y = toy;

            
            
            try {
                Game.PlayerMovesResponse res = stub.attackBlock(req.build()).get();
                this.playerMoveList = res.getPlayerMoveList();
            } catch (Exception ignored) {
            }
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


    public synchronized void updateGame(GameObject.GameStateResponse gameStateResponse) {
        this.players = gameStateResponse.getGamePlayerInfoList();
        this.gameMap = gameStateResponse.getGameMap();
        this.player = gameStateResponse.getPlayer();
        this.playerMoveList = gameStateResponse.getPlayerMoveList();

        w = this.gameMap.getWidth();
        h = this.gameMap.getHeight();
        drawMap();
    }

    public void drawMap() {
        scoreBoardView.updateScoreBoard(players);

        for (int i = 0; i < h; i += 1) {
            for(int j = 0; j < w; j += 1) {
                GameObject.Block block = gameMap.getBlockList().getBlock(i * w + j);

                BlockView blockView = gameMapView.getBlock(j, i);
                resetBlock(blockView);

                if (!block.getHidden() || block.getBlockCase().equals(GameObject.Block.BlockCase.WALLBLOCK) || block.getBlockCase().equals(GameObject.Block.BlockCase.FARMBLOCK)) {
                    switch (block.getBlockCase()) {
                        case FARMBLOCK:
                            if (block.getHidden()) {
                                blockView.setBlockText(Icons.WALL);
                                blockView.setUnitText("?");
                                break;
                            }

                            setFarmBlock(blockView, block.getFarmBlock());
                            break;
                        case WALLBLOCK:
                            if (!block.getHidden()) {
                                blockView.setBlockColor(UIConfig.NEUTRAL_BLOCK_COLOR);
                            } else {
                                blockView.setUnitText("?");
                            }
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
        drawArrows();
        drawSelectedBlock();
    }

    private boolean checkCords(int x, int y) {
        return 0 <= x && x < w && 0 <= y && y < h;
    }

    private void drawArrows() {
       for(int i = 0; i < playerMoveList.size(); i+=1) {
           GameObject.Move move = playerMoveList.get(i);

           GameObject.BlockCoordinate start = move.getStart();
           GameObject.BlockCoordinate end = move.getEnd();

           BlockView block = gameMapView.getBlock(start.getX(), start.getY());

           int dx = end.getX() - start.getX();
           int dy = end.getY() - start.getY();

           if(dx == 1) {
               block.setVisableRightArrow(true);
           } else if(dx == -1) {
               block.setVisableLeftArrow(true);
           } else if(dy == -1) {
               block.setVisableUpArrow(true);
           } else if(dy == 1) {
               block.setVisableDownArrow(true);
           }
       }
    }

    private void drawSelectedBlock() {
        if (selectedBlock == null) {
            return;
        }

        BlockView blockView = gameMapView.getBlock(selectedBlock.x, selectedBlock.y);
        switch (selectedBlock.getClickNumber()) {
            case 1:
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
                neighboringBlockView.setAlpha(0.7f);
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


    private void setFarmBlock(BlockView blockView, GameObject.FarmBlock farmBlock) {
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

    private void setCastleBlock(BlockView blockView, GameObject.CastleBlock castleBlock) {
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


    private void setWallBlock(BlockView blockView, GameObject.WallBlock wallBlock) {
        blockView.setBlockText(Icons.WALL);
    }


    private void setEmptyBlock(BlockView blockView, GameObject.EmptyBlock emptyBlock) {
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
