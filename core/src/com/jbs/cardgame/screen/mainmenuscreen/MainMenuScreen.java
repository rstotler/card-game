package com.jbs.cardgame.screen.mainmenuscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.jbs.cardgame.Settings;
import com.jbs.cardgame.screen.Screen;

public class MainMenuScreen extends Screen {
    public SpriteBatch spriteBatchMask;
    public FrameBuffer frameBufferBackgroundMask;

    public Texture textureBackground;
    public Texture textureBackgroundMask;
    public Texture textureBackgroundMaskImage;

    public MainMenuScreen() {
        super();

        spriteBatchMask = new SpriteBatch();
        frameBufferBackgroundMask = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);

        textureBackground = new Texture("images/mainmenu/Background.png");
        textureBackgroundMask = new Texture("images/mainmenu/BackgroundMask.png");
        textureBackgroundMaskImage = new Texture("images/mainmenu/BackgroundMaskImage.png");

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
                        Gdx.graphics.setWindowedMode(Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT);
                    } else {
                        Gdx.graphics.setFullscreenMode(currentMode);
                    }

                    Settings.SIZE_RATIO_X = (Settings.SCREEN_WIDTH + 0.0f) / Gdx.graphics.getWidth();
                    Settings.SIZE_RATIO_Y = (Settings.SCREEN_HEIGHT + 0.0f) / Gdx.graphics.getHeight();
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

    public String update() {
        return "";
    }

    public void render() {

        // Background Image //
        spriteBatch.begin();
        spriteBatch.draw(textureBackground, 0, 0);
        spriteBatch.end();

        // Mask//
        frameBufferBackgroundMask.begin();
        spriteBatchMask.begin();

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatchMask.draw(textureBackgroundMask, -150, -150);
        spriteBatchMask.setBlendFunction(GL20.GL_ONE_MINUS_DST_ALPHA, GL20.GL_ONE_MINUS_DST_COLOR);
        spriteBatchMask.draw(textureBackgroundMaskImage, 0, 0);

        spriteBatchMask.end();
        frameBufferBackgroundMask.end();
        spriteBatch.begin();
        spriteBatch.draw(frameBufferBackgroundMask.getColorBufferTexture(), 0, 0, frameBufferBackgroundMask.getWidth(), frameBufferBackgroundMask.getHeight(), 0, 0, 1, 1);
        spriteBatch.end();

        renderDebugData();
    }

    public void renderDebugData() {
        spriteBatch.setProjectionMatrix(cameraDebug.combined);
        spriteBatch.begin();

        int debugDataYLoc = Settings.SCREEN_HEIGHT - 4;

        font.setColor(Color.WHITE);
        font.draw(spriteBatch, "FPS: " + String.valueOf(Gdx.graphics.getFramesPerSecond()), Settings.SCREEN_WIDTH - 75, debugDataYLoc);
        
        font.draw(spriteBatch, "Mouse X: " + mouse.rect.location.x + ", Y: " + mouse.rect.location.y + ", Zoom: " + camera.zoom + ", Ratio: " + Settings.SIZE_RATIO_X + " " + Settings.SIZE_RATIO_Y, 3, debugDataYLoc);
        debugDataYLoc -= 15;
        
        spriteBatch.end();
    }

    public void dispose() {
        spriteBatchMask.dispose();
        frameBufferBackgroundMask.dispose();
        textureBackground.dispose();
        textureBackgroundMask.dispose();
        textureBackgroundMaskImage.dispose();

        spriteBatch.dispose();
        shapeRenderer.dispose();
        font.dispose();
    }
}
