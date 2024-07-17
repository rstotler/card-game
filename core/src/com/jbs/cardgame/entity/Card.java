package com.jbs.cardgame.entity;

import java.util.Random;

import com.jbs.cardgame.screen.Point;

public class Card {
    public static final int WIDTH = 80;
    public static final int HEIGHT = 120;
    public static final float MOVE_SPEED = 50.0f;

    public Point currentLocation;
    public Point targetLocation;
    public Point selectedCardOffset;

    public int color;

    public Card() {
        currentLocation = new Point(0, 0);
        targetLocation = new Point(0, 0);
        selectedCardOffset = new Point(0, 0);

        color = new Random().nextInt(35) + 15;
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
