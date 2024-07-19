package com.jbs.cardgame.entity;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.jbs.cardgame.Settings;
import com.jbs.cardgame.entity.battleplayer.BattlePlayer;
import com.jbs.cardgame.screen.ImageManager;
import com.jbs.cardgame.screen.utility.Point;
import com.jbs.cardgame.screen.utility.RGBColor;

public class Card {
    public static final int WIDTH = 80;
    public static final int HEIGHT = 120;
    public static final float MOVE_SPEED = 70.0f;

    public static FrameBuffer frameBufferCard = new FrameBuffer(Pixmap.Format.RGBA8888, Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT, false);
    public static BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/Code_New_Roman_18.fnt"), Gdx.files.internal("fonts/Code_New_Roman_18.png"), false);
    
    public int[] powerRating; // 0 - Top, 1 - Right, 2 - Bottom, 3 - Left

    public Point currentLocation;
    public Point targetLocation;
    public Point selectedCardOffset;

    public BattlePlayer currentOwnerInBattle;

    public Card() {
        powerRating = new int[4];
        for(int i = 0; i < 4; i++) {
            powerRating[i] = new Random().nextInt(10) + 1;
        }

        currentLocation = new Point(0, 0);
        targetLocation = new Point(0, 0);
        selectedCardOffset = new Point(0, 0);

        currentOwnerInBattle = null;
    }

    public void bufferCardImage(OrthographicCamera cameraTop, ImageManager imageManager, SpriteBatch spriteBatch, RGBColor cardColor) {
        frameBufferCard.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        spriteBatch.setProjectionMatrix(cameraTop.combined);
        spriteBatch.begin();
        int srcFunc = spriteBatch.getBlendSrcFunc();
        int destFunc = spriteBatch.getBlendDstFunc();
        spriteBatch.setShader(imageManager.shaderProgramColorChannel);

        imageManager.shaderProgramColorChannel.setUniformf("target_r", cardColor.r/255f);
        imageManager.shaderProgramColorChannel.setUniformf("target_g", cardColor.g/255f);
        imageManager.shaderProgramColorChannel.setUniformf("target_b", cardColor.b/255f);
        imageManager.shaderProgramColorChannel.setUniformf("target_alpha", 1.0f);

        spriteBatch.draw(imageManager.cardBackTexture, 0, 0);
        spriteBatch.setShader(null);
        spriteBatch.setBlendFunction(srcFunc, destFunc);
        spriteBatch.draw(imageManager.cardBorderTexture, 0, 0);

        // Power Rating //
        font.setColor(Color.WHITE);
        for(int i = 0; i < 4; i++) {
            Point attackRatingNumLoc = new Point(WIDTH - 25, HEIGHT - 7);
            if(i == 1) {
                attackRatingNumLoc.x += 9;
                attackRatingNumLoc.y -= 15;
            } else if(i == 2) {
                attackRatingNumLoc.y -= 30;
            } else if(i == 3) {
                attackRatingNumLoc.x -= 9;
                attackRatingNumLoc.y -= 15;
            }
            String powerRatingString = String.valueOf(powerRating[i]);
            if(powerRating[i] == 10) {
                powerRatingString = "A";
            }
            font.draw(spriteBatch, powerRatingString, attackRatingNumLoc.x, attackRatingNumLoc.y);
        }

        spriteBatch.end();
        frameBufferCard.end();
    }

    public boolean isStrongerThan(Card targetCard, int attackDirectionIndex) {
        int attackPower = powerRating[attackDirectionIndex];
        int defensePower = targetCard.powerRating[getOppositeDirectionIndex(attackDirectionIndex)];

        return attackPower > defensePower;
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

    public int getOppositeDirectionIndex(int targetIndex) {
        int oppositeInt = targetIndex + 2;
        if(oppositeInt >= 4) {
            oppositeInt = oppositeInt % 4;
        }

        return oppositeInt;
    }
}
