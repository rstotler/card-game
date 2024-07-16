package com.jbs.cardgame.screen.battlescreen;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.jbs.cardgame.entity.Card;
import com.jbs.cardgame.entity.battleplayer.BattlePlayer;
import com.jbs.cardgame.entity.board.GameBoard;
import com.jbs.cardgame.screen.Point;
import com.jbs.cardgame.screen.Rect;
import com.jbs.cardgame.screen.Screen;
import com.jbs.cardgame.screen.battlescreen.gamephase.Deal;
import com.jbs.cardgame.screen.battlescreen.gamephase.GamePhase;

public class BattleScreen extends Screen {
    public OrthographicCamera cameraTop;
    public OrthographicCamera cameraDebug;
    
    public GamePhase gamePhase;
    public GameBoard gameBoard;

    public ArrayList<BattlePlayer> battlePlayerList;
    public BattlePlayer currentBattlePlayer;

    public BattleScreen() {
        super();

        cameraTop = new OrthographicCamera();
        cameraTop.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cameraDebug = new OrthographicCamera();
        cameraDebug.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        gamePhase = new Deal();
        gameBoard = new GameBoard();

        battlePlayerList = new ArrayList<>();
        battlePlayerList.add(new BattlePlayer(true));
        battlePlayerList.add(new BattlePlayer(false));
        battlePlayerList.add(new BattlePlayer(false));
        currentBattlePlayer = battlePlayerList.get(0);

        centerCamera();
        initInputAdapter();
    }

    public void initInputAdapter() {
        Gdx.input.setInputProcessor(new InputAdapter() {

            // Keyboard Input //
            @Override
            public boolean keyDown(int keyCode) {
                String key = Input.Keys.toString(keyCode);

                // Toggle Full Screen //
                if(key.equals("F4")) {
                    boolean isFullScreen = Gdx.graphics.isFullscreen();
                    Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();

                    if(isFullScreen) {
                        Gdx.graphics.setWindowedMode(1280, 768);
                    } else {
                        Gdx.graphics.setFullscreenMode(currentMode);
                    }
                }

                else if(key.equals("Escape")) {
                    System.exit(0);
                }

                return true;
            }

            @Override
            public boolean keyUp(int keyCode) {
                return true;
            }

            // Mouse Input //
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if(button == 0) {
                    mouse.leftClickDown();
                }
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if(button == 0) {
                    mouse.leftClickUp();
                }
                return true;
            }

            @Override
            public boolean scrolled(float amountX, float amountY) {
                changeZoomLevel((int) amountY);
                return true;
            }

            @Override
            public boolean touchDragged(int moveX, int moveY, int pointer) {
                if(mouse.selectedHandCard == null) {
                    moveY = Gdx.graphics.getHeight() - moveY;
                    int moveDiffX = (mouse.rect.location.x - moveX) / 2;
                    int moveDiffY = (mouse.rect.location.y - moveY) / 2;
                    moveCamera(moveDiffX, moveDiffY);
                }
                return true;
            }
        });
    }

    public void handleInput() {
    }

    public void update() {
        mouse.update();

        if(gamePhase != null) {
            String gamePhaseReturnStatus = gamePhase.update(battlePlayerList, currentBattlePlayer);
            if(gamePhaseReturnStatus.equals("Next Player")) {
                setNextPlayer();
            } else if(gamePhaseReturnStatus.equals("End GamePhase")) {
                setNextPlayer();
                gamePhase = null;
            }
        }
    }

    public void render() {
        ScreenUtils.clear(0/255f, 0/255f, 15/255f, 1);

        gameBoard.render(camera, shapeRenderer);
        if(gamePhase != null) {
            gamePhase.render(camera, cameraTop, shapeRenderer, mouse, gameBoard, battlePlayerList, currentBattlePlayer);
        }
        renderPlayerHand();

        renderDebugData();
    }

    public void renderPlayerHand() {
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(cameraTop.combined);

        Card hoverHandCard = null;
        for(int i = battlePlayerList.get(0).hand.size() - 1; i >= 0 ; i--) {
            Card handCard = battlePlayerList.get(0).hand.get(i);

            Rect handCardRect = null;
            if(i == 0) {
                handCardRect = new Rect(handCard.handLocation, Card.WIDTH * 2, Card.HEIGHT * 2);
            } else {
                handCardRect = new Rect(new Point(handCard.handLocation.x + ((Card.WIDTH * 2) - BattlePlayer.HAND_OVERLAP_WIDTH), handCard.handLocation.y), BattlePlayer.HAND_OVERLAP_WIDTH, Card.HEIGHT * 2);
            }
            
            if(hoverHandCard == null
            && mouse.rect.rectCollide(handCardRect)) {
                hoverHandCard = handCard;
            } else {
                shapeRenderer.setColor(handCard.color/255f, 0/255f, 0/255f, 1f);
                shapeRenderer.rect(handCard.handLocation.x, handCard.handLocation.y, Card.WIDTH * 2, Card.HEIGHT * 2);
            }
        }

        // Hand Hovered Over Card //
        if(hoverHandCard != null) {
            shapeRenderer.setColor(65/255f, 0/255f, 0/255f, 1f);
            shapeRenderer.rect(hoverHandCard.handLocation.x, 0, Card.WIDTH * 2, Card.HEIGHT * 2);
        }

        shapeRenderer.end();
    }

    public void renderDebugData() {
        spriteBatch.setProjectionMatrix(cameraDebug.combined);
        spriteBatch.begin();
        font.setColor(Color.WHITE);

        font.draw(spriteBatch, "FPS: " + String.valueOf(Gdx.graphics.getFramesPerSecond()), 1205, 767);
        
        spriteBatch.end();
    }

    public void setNextPlayer() {
        int currentPlayerIndex = battlePlayerList.indexOf(currentBattlePlayer);
        if(currentPlayerIndex == battlePlayerList.size() - 1) {
            currentBattlePlayer = battlePlayerList.get(0);
        } else {
            currentBattlePlayer = battlePlayerList.get(currentPlayerIndex + 1);
        }
    }

    public void centerCamera() {
        int xLoc = ((gameBoard.cardsWidth * Card.WIDTH) / 2);
        int yLoc = ((gameBoard.cardsHeight * Card.HEIGHT) / 2);
        camera.position.set(xLoc, yLoc, 0);
        camera.update();
    }

    public void changeZoomLevel(int zoomDirection) {
        if(zoomDirection < 0
        && camera.zoom > .5) {
            camera.zoom -= .5f;
        }
        else if(zoomDirection > 0
        && camera.zoom < 1.5) {
            camera.zoom += .5f;
        }
        camera.update();
    }

    public void dispose() {
        spriteBatch.dispose();
        shapeRenderer.dispose();
    }
}
