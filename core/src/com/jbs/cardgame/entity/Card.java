package com.jbs.cardgame.entity;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.jbs.cardgame.entity.board.BoardSlot;
import com.jbs.cardgame.screen.Point;

public class Card {
    public static final int WIDTH = 80;
    public static final int HEIGHT = 120;
    public static final float MOVE_SPEED = 70.0f;

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

    public void render(OrthographicCamera camera, SpriteBatch spriteBatch, ShapeRenderer shapeRenderer, BitmapFont font) {
        
        // Card //
        // shapeRenderer.begin(ShapeType.Filled);
        // shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.setColor(color/255f, 0/255f, 0/255f, 1f);
        shapeRenderer.rect(currentLocation.x + BoardSlot.PADDING, currentLocation.y + BoardSlot.PADDING, Card.WIDTH, Card.HEIGHT);
        // shapeRenderer.end();

        // Power Rating //
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        font.setColor(Color.WHITE);
        font.draw(spriteBatch, "Test", currentLocation.x + BoardSlot.PADDING, currentLocation.y + BoardSlot.PADDING);
        spriteBatch.end();
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
