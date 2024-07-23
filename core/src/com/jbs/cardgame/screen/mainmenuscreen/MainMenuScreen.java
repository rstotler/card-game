package com.jbs.cardgame.screen.mainmenuscreen;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.jbs.cardgame.Settings;
import com.jbs.cardgame.screen.Screen;
import com.jbs.cardgame.screen.utility.*;

public class MainMenuScreen extends Screen {
    public SpriteBatch spriteBatchMask;
    public FrameBuffer frameBufferBackgroundMask;

    public BitmapFont fontMainMenu;
    
    public Texture textureBackground;
    public Texture textureBackgroundMask;
    public Texture textureBackgroundMaskImage;

    public String currentState;
    public float currentStatePercent;

    public ArrayList<Button> menuButtonList;

    public boolean clickNewGameCheck = false;

    public MainMenuScreen() {
        super();

        spriteBatchMask = new SpriteBatch();
        frameBufferBackgroundMask = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        
        fontMainMenu = new BitmapFont(Gdx.files.internal("fonts/Code_New_Roman_44.fnt"), Gdx.files.internal("fonts/Code_New_Roman_44.png"), false);

        textureBackground = new Texture("images/mainmenu/Background.png");
        textureBackgroundMask = new Texture("images/mainmenu/BackgroundMask.png");
        textureBackgroundMaskImage = new Texture("images/mainmenu/BackgroundMaskImage.png");

        currentState = "Intro Fade Stage 1";
        currentStatePercent = -0.5f;

        Button buttonNewGame = new Button("New Game", fontMainMenu);
        Button buttonLoadGame = new Button("Load Game", fontMainMenu);
        buttonLoadGame.displayMod.y -= 42;
        Button buttonQuitGame = new Button("Quit", fontMainMenu);
        buttonQuitGame.displayMod.y -= 42 * 2;

        menuButtonList = new ArrayList<>();
        menuButtonList.add(buttonNewGame);
        menuButtonList.add(buttonLoadGame);
        menuButtonList.add(buttonQuitGame);

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
        if(mouse.hoverButton != null) {
            if(mouse.hoverButton.label.equals("New Game")) {
                clickNewGameCheck = true;
            }
            
            else if(mouse.hoverButton.label.equals("Quit")) {
                System.exit(0);
            }
        }
    }

    public void leftClickUp() {

    }

    public String update() {
        mouse.updateLocation();
        updateMouse();

        if(currentState.equals("Intro Fade Stage 1")) {
            if(currentStatePercent < 1) {
                currentStatePercent += .0055f;
                if(currentStatePercent >= 1) {
                    currentStatePercent = 0.0f;
                    currentState = "Intro Fade Stage 2";
                }
            }
        } else if(currentState.equals("Intro Fade Stage 2")) {
            if(currentStatePercent < 1) {
                currentStatePercent += .0085f;
                if(currentStatePercent >= 1) {
                    currentStatePercent = 1.0f;
                    currentState = "";
                }
            }
        }

        if(clickNewGameCheck) {
            return "Click New Game";
        }

        return "";
    }

    public void updateMouse() {
        mouse.hoverButton = null;
        for(Button menuButton : menuButtonList) {
            if(menuButton.getDisplayRect().rectCollide(mouse.rect)) {
                mouse.hoverButton = menuButton;
                break;
            }
        }
    }

    public void render() {

        // Background Image //
        float backgroundAlpha = currentStatePercent;
        if(currentState.equals("Intro Fade Stage 2")) {
            backgroundAlpha = 1.0f;
        }
        spriteBatch.begin();
        spriteBatch.setColor(1, 1, 1, backgroundAlpha);
        spriteBatch.draw(textureBackground, 0, 0);
        spriteBatch.setColor(1, 1, 1, 1);
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

        for(Button menuButton : menuButtonList) {
            boolean hoverCheck = mouse.hoverButton == menuButton;
            float menuButtonLabelAlpha = 0.0f;
            if(currentState.equals("Intro Fade Stage 2")
            || currentState.equals("")) {
                menuButtonLabelAlpha = currentStatePercent;
            }
            menuButton.render(spriteBatch, null, hoverCheck, menuButtonLabelAlpha);
        }

        renderDebugData();
    }

    public void renderDebugData() {
        spriteBatch.setProjectionMatrix(cameraDebug.combined);
        spriteBatch.begin();

        int debugDataYLoc = Settings.SCREEN_HEIGHT - 4;

        font.setColor(Color.WHITE);
        font.draw(spriteBatch, "FPS: " + String.valueOf(Gdx.graphics.getFramesPerSecond()), Settings.SCREEN_WIDTH - 75, debugDataYLoc);
        
        font.draw(spriteBatch, "Mouse X: " + mouse.rect.location.x + ", Y: " + mouse.rect.location.y + ", Button: " + (mouse.hoverButton != null), 3, debugDataYLoc);
        debugDataYLoc -= 15;

        font.draw(spriteBatch, "Zoom: " + camera.zoom + ", Ratio: " + Settings.SIZE_RATIO_X + " " + Settings.SIZE_RATIO_Y, 3, debugDataYLoc);
        debugDataYLoc -= 15;
        
        spriteBatch.end();
    }

    public void dispose() {
        spriteBatchMask.dispose();
        frameBufferBackgroundMask.dispose();

        fontMainMenu.dispose();

        textureBackground.dispose();
        textureBackgroundMask.dispose();
        textureBackgroundMaskImage.dispose();

        spriteBatch.dispose();
        shapeRenderer.dispose();
        font.dispose();
    }
}
