package com.jbs.cardgame.screen.gamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.ScreenUtils;
import com.jbs.cardgame.Settings;
import com.jbs.cardgame.screen.Screen;

public class GameScreen extends Screen {
    public GameScreen() {
        super();

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

                    Settings.SIZE_RATIO_X = 1280.0f / Gdx.graphics.getWidth();
                    Settings.SIZE_RATIO_Y = 768.0f / Gdx.graphics.getHeight();
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
                    leftClickDown();
                }
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if(button == 0) {
                    leftClickUp();
                }
                return true;
            }

            @Override
            public boolean scrolled(float amountX, float amountY) {
                return true;
            }

            @Override
            public boolean touchDragged(int moveX, int moveY, int pointer) {
                return true;
            }
        });
    }

    public void leftClickDown() {

    }

    public void leftClickUp() {

    }

    public void render() {
        ScreenUtils.clear(2/255f, 2/255f, 2/255f, 1);
    }

    public void dispose() {

    }
}
