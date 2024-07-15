package com.jbs.cardgame.screen.battlescreen;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.jbs.cardgame.entity.Card;
import com.jbs.cardgame.entity.battleplayer.BattlePlayer;
import com.jbs.cardgame.entity.board.GameBoard;
import com.jbs.cardgame.screen.Screen;
import com.jbs.cardgame.screen.battlescreen.gamephase.Deal;
import com.jbs.cardgame.screen.battlescreen.gamephase.GamePhase;

public class BattleScreen extends Screen {
    public OrthographicCamera cameraTop;
    public OrthographicCamera cameraDebug;
    
    public GamePhase gamePhase;
    public GameBoard gameBoard;

    public ArrayList<BattlePlayer> battlePlayerList;

    public BattleScreen() {
        super();

        cameraTop = new OrthographicCamera();
        cameraTop.setToOrtho(false, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        cameraDebug = new OrthographicCamera();
        cameraDebug.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        gamePhase = new Deal();
        gameBoard = new GameBoard();

        battlePlayerList = new ArrayList<>();
        battlePlayerList.add(new BattlePlayer(true));

        centerCamera();
        initInputAdapter();
    }

    public void initInputAdapter() {
        Gdx.input.setInputProcessor(new InputAdapter() {

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

            @Override
            public boolean scrolled(float amountX, float amountY) {
                changeZoomLevel((int) amountY);
                return true;
            }

            @Override
            public boolean touchDragged(int moveX, int moveY, int pointer) {
                int moveDiffX = (mouse.oldLocation.x - moveX) / 2;
                int moveDiffY = (moveY - mouse.oldLocation.y) / 2;
                moveCamera(moveDiffX, moveDiffY);
                return true;
            }
        });
    }

    public void handleInput() {
    }

    public void update() {
        mouse.update();
        gamePhase.update();
    }

    public void render() {
        ScreenUtils.clear(0/255f, 0/255f, 15/255f, 1);

        gameBoard.render(camera, shapeRenderer);
        gamePhase.render(camera, cameraTop, shapeRenderer, gameBoard);

        renderDebugData();
    }

    public void renderDebugData() {
        spriteBatch.setProjectionMatrix(cameraDebug.combined);
        spriteBatch.begin();
        font.setColor(Color.WHITE);

        font.draw(spriteBatch, "FPS: " + String.valueOf(Gdx.graphics.getFramesPerSecond()), 1205, 767);
        
        spriteBatch.end();
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
