package com.jbs.cardgame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.jbs.cardgame.component.Mouse;

public class Screen {
    public OrthographicCamera camera;
    public Mouse mouse;

    public SpriteBatch spriteBatch;
    public ShapeRenderer shapeRenderer;
    
    public BitmapFont font;

    public Screen() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        mouse = new Mouse();
        
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        font = new BitmapFont(Gdx.files.internal("fonts/Code_New_Roman_18.fnt"), Gdx.files.internal("fonts/Code_New_Roman_18.png"), false);
    }

    public void moveCamera(int moveX, int moveY) {
        camera.position.add(moveX * camera.zoom, moveY * camera.zoom, 0);
        camera.update();
    }

    public void changeZoomLevel(int zoomDirection) {
        if(zoomDirection < 0
        && camera.zoom > 1.0) {
            camera.zoom -= .5f;
        }
        else if(zoomDirection > 0
        && camera.zoom < 1.5) {
            camera.zoom += .5f;
        }
        camera.update();
    }

    public void handleInput() {}
    public String update() {return "";}
    public void render() {}
    public void dispose() {}
}
