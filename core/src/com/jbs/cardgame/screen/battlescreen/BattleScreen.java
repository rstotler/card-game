package com.jbs.cardgame.screen.battlescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.jbs.cardgame.entity.Card;
import com.jbs.cardgame.entity.board.GameBoard;
import com.jbs.cardgame.screen.Screen;

public class BattleScreen extends Screen {
    public OrthographicCamera cameraDebug;

    public GameBoard gameBoard;

    public BattleScreen() {
        super();

        cameraDebug = new OrthographicCamera();
        cameraDebug.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        gameBoard = new GameBoard();
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
            public boolean touchDragged (int moveX, int moveY, int pointer) {
                int moveDiffX = (mouse.oldX - moveX) / 2;
                int moveDiffY = (moveY - mouse.oldY) / 2;
                moveCamera(moveDiffX, moveDiffY);
                return true;
            }
        });
    }

    public void handleInput() {
    }

    public void update() {
        mouse.update();
    }

    public void render() {
        ScreenUtils.clear(0/255f, 0/255f, 15/255f, 1);

        gameBoard.render(camera, shapeRenderer);

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

    public void dispose() {
        spriteBatch.dispose();
        shapeRenderer.dispose();
    }
}
