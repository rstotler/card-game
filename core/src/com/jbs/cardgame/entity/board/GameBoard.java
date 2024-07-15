package com.jbs.cardgame.entity.board;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.jbs.cardgame.entity.Card;

public class GameBoard {
    public int cardsWidth;
    public int cardsHeight;

    public BoardSlot[][] boardSlot;

    public GameBoard() {
        cardsWidth = 5;
        cardsHeight = 4;

        generateBoard();
    }

    public void generateBoard() {
        boardSlot = new BoardSlot[cardsWidth][cardsHeight];
        for(int y = 0; y < cardsHeight; y++) {
            for(int x = 0; x < cardsWidth; x++) {
                boardSlot[x][y] = new BoardSlot(x, y);
            }
        }
    }

    public void render(OrthographicCamera camera, ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(camera.combined);
        
        for(int y = 0; y < cardsHeight; y++) {
            for(int x = 0; x < cardsWidth; x++) {
                BoardSlot targetSlot = boardSlot[x][y];
                int xLoc = targetSlot.x * Card.WIDTH;
                int yLoc = targetSlot.y * Card.HEIGHT;

                shapeRenderer.setColor(0/255f, targetSlot.color/255f, 0/255f, 1f);
                shapeRenderer.rect(xLoc, yLoc, Card.WIDTH, Card.HEIGHT);
            }
        }

        shapeRenderer.end();
    }

    public int getWidth() {
        return (Card.WIDTH * boardSlot.length);
    }

    public int getHeight() {
        return (Card.HEIGHT * boardSlot[0].length);
    }
}
