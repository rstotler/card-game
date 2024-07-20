package com.jbs.cardgame.entity;

import java.util.*;

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
import com.jbs.cardgame.entity.board.BoardSlot;
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
    public String element;    // Fire, Ice, Lightning, Earth, Water, Poison, Dark, Holy
    public int movement;

    public Point currentLocation;
    public Point targetLocation;
    public Point selectedCardOffset;
    public BoardSlot boardSlot;

    public BattlePlayer originalOwnerInBattle;
    public BattlePlayer currentOwnerInBattle;
    public boolean isCurrentOwner;
    public boolean gameRuleFlip;

    public Card() {
        powerRating = new int[4];
        for(int i = 0; i < 4; i++) {
            powerRating[i] = new Random().nextInt(10) + 1;
        }
        element = Card.getElementList().get(new Random().nextInt(Card.getElementList().size()));
        movement = new Random().nextInt(4);

        currentLocation = new Point(0, 0);
        targetLocation = new Point(0, 0);
        selectedCardOffset = new Point(0, 0);
        boardSlot = null;

        originalOwnerInBattle = null;
        currentOwnerInBattle = null;
        isCurrentOwner = false;
        gameRuleFlip = false;
    }

    public Card(int[] powerRatingList) {
        this();
        powerRating = powerRatingList;
    }

    public void bufferCardImage(OrthographicCamera cameraTop, ImageManager imageManager, SpriteBatch spriteBatch, RGBColor cardColor, BoardSlot targetBoardSlot) {
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

            int powerRatingNum = getPowerRating(i, targetBoardSlot);
            if(targetBoardSlot != null
            && !targetBoardSlot.element.equals("")) {
                if(targetBoardSlot.element.equals(element)) {
                    font.setColor(Color.GREEN);
                } else if(!targetBoardSlot.element.equals(element)) {
                    font.setColor(Color.RED);
                }
            }

            String powerRatingString = String.valueOf(powerRatingNum);
            if(powerRatingNum == 10) {
                powerRatingString = "A";
            } else if(powerRatingNum == 11) {
                powerRatingString = "S";
            }

            font.draw(spriteBatch, powerRatingString, attackRatingNumLoc.x, attackRatingNumLoc.y);
        }

        // Element //
        if(!element.equals("")) {
            String elementSubstring = element.substring(0, 1) + element.substring(element.length() - 1);
            font.draw(spriteBatch, elementSubstring, 8, 112);
        }

        font.draw(spriteBatch, String.valueOf(movement), 8, 19);

        spriteBatch.end();
        frameBufferCard.end();
    }

    public boolean isStrongerThan(Card targetCard, int attackDirectionIndex, BoardSlot attackerBoardSlot, BoardSlot defenderBoardSlot) {
        int attackPower = getPowerRating(attackDirectionIndex, attackerBoardSlot);
        int defensePower = targetCard.getPowerRating(getOppositeDirectionIndex(attackDirectionIndex), defenderBoardSlot);

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

    public int getPowerRating(int attackDirectionIndex, BoardSlot targetBoardSlot) {
        int attackPower = powerRating[attackDirectionIndex];
        if(targetBoardSlot != null
        && !targetBoardSlot.element.equals("")) {
            if(targetBoardSlot.element.equals(element)) {
                attackPower += 1;
                if(attackPower == 12) {
                    attackPower = 11;
                }
            } else {
                attackPower -= 1;
            }
        }

        return attackPower;
    }

    public static int getOppositeDirectionIndex(int targetIndex) {
        int oppositeInt = targetIndex + 2;
        if(oppositeInt >= 4) {
            oppositeInt = oppositeInt % 4;
        }

        return oppositeInt;
    }

    public static ArrayList<String> getElementList() {
        return new ArrayList<>(Arrays.asList("Fire", "Ice", "Lightning", "Earth", "Water", "Poison", "Dark", "Holy"));
    }
}
