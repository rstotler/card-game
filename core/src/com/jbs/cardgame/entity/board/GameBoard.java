package com.jbs.cardgame.entity.board;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.jbs.cardgame.entity.Card;
import com.jbs.cardgame.screen.Point;

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
                boardSlot[x][y] = new BoardSlot(new Point(x, y));
            }
        }
    }

    public void render(OrthographicCamera camera, ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(camera.combined);
        
        for(int y = 0; y < cardsHeight; y++) {
            for(int x = 0; x < cardsWidth; x++) {
                BoardSlot targetSlot = boardSlot[x][y];
                int xLoc = targetSlot.location.x * Card.WIDTH;
                int yLoc = targetSlot.location.y * Card.HEIGHT;

                shapeRenderer.setColor(0/255f, targetSlot.color/255f, 0/255f, 1f);
                shapeRenderer.rect(xLoc, yLoc, Card.WIDTH, Card.HEIGHT);
            }
        }

        shapeRenderer.end();
    }

    public Point getSize() {
        int width = Card.WIDTH * boardSlot.length;
        int height = Card.HEIGHT * boardSlot[0].length;
        return new Point(width, height);
    }
}
