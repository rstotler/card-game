package com.jbs.cardgame.entity;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.jbs.cardgame.Settings;
import com.jbs.cardgame.screen.ImageManager;
import com.jbs.cardgame.screen.Point;

public class Card {
    public static final int WIDTH = 80;
    public static final int HEIGHT = 120;
    public static final float MOVE_SPEED = 70.0f;

    public static FrameBuffer frameBufferCard = new FrameBuffer(Pixmap.Format.RGBA8888, Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT, false);

    public int[] powerRating; // 0 - Top, 1 - Right, 2 - Bottom, 3 - Left

    public Point currentLocation;
    public Point targetLocation;
    public Point selectedCardOffset;

    public int color;

    public Card() {
        powerRating = new int[4];
        for(int i = 0; i < 4; i++) {
            powerRating[i] = new Random().nextInt(10) + 1;
        }

        currentLocation = new Point(0, 0);
        targetLocation = new Point(0, 0);
        selectedCardOffset = new Point(0, 0);

        color = new Random().nextInt(35) + 15;
    }

    public void bufferCardImage(OrthographicCamera cameraTop, ImageManager imageManager, SpriteBatch spriteBatch) {
        frameBufferCard.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        spriteBatch.setProjectionMatrix(cameraTop.combined);
        spriteBatch.begin();
        spriteBatch.draw(imageManager.cardBackTexture, 0, 0);

        //font.setColor(Color.WHITE);
        //font.draw(spriteBatch, "1234567890", 50, 50, 150, 10, false);
        
        spriteBatch.end();
        frameBufferCard.end();
    }

    public void updateLocation() {
        float diffX = targetLocation.x - currentLocation.x;
        float diffY = targetLocation.y - currentLocation.y;
        float slope = diffY / diffX;
        float updateXMove = -1.0f;
        float updateYMove = -1.0f;

        // Longer Left & Right //
        if(Math.abs(diffX) >= Math.abs(diffY)) {
            updateXMove = MOVE_SPEED;
            updateYMove = Math.abs(slope) * MOVE_SPEED;

            if(diffX < 0) {
                updateXMove *= -1;
            }
            if(diffY < 0) {
                updateYMove *= -1;
            }
        }
        
        // Longer Top & Bottom //
        else {
            slope = diffX / diffY;
            updateYMove = MOVE_SPEED;
            updateXMove = Math.abs(slope) * MOVE_SPEED;

            if(diffY < 0) {
                updateYMove *= -1;
            }
            if(diffX < 0) {
                updateXMove *= -1;
            }
        }

        if(Math.abs(diffX) < MOVE_SPEED && Math.abs(diffY) < MOVE_SPEED) {
            currentLocation.x = targetLocation.x;
            currentLocation.y = targetLocation.y;
        } else {
            currentLocation.x += updateXMove;
            currentLocation.y += updateYMove;
        }
    }
}
