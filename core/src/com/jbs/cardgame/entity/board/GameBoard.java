package com.jbs.cardgame.entity.board;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.jbs.cardgame.entity.Card;
import com.jbs.cardgame.screen.Point;
import com.jbs.cardgame.screen.Rect;

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
                BoardSlot targetBoardSlot = boardSlot[x][y];
                int xLoc = targetBoardSlot.location.x * (Card.WIDTH + (BoardSlot.PADDING * 2));
                int yLoc = targetBoardSlot.location.y * (Card.HEIGHT + (BoardSlot.PADDING * 2));

                shapeRenderer.setColor(0/255f, targetBoardSlot.color/255f, 0/255f, 1f);
                shapeRenderer.rect(xLoc, yLoc, Card.WIDTH + (BoardSlot.PADDING * 2), Card.HEIGHT + (BoardSlot.PADDING * 2));

                if(targetBoardSlot.card != null) {
                    shapeRenderer.setColor(targetBoardSlot.card.color/255f, 0/255f, 0/255f, 1f);
                    shapeRenderer.rect(xLoc + BoardSlot.PADDING, yLoc + BoardSlot.PADDING, Card.WIDTH, Card.HEIGHT);
                }
            }
        }

        shapeRenderer.end();
    }

    public boolean placeCardCheck(Card targetCard, Point targetLocation) {
        if(targetLocation.x > 0 && targetLocation.y > 0) {
            int slotX = targetLocation.x / ((Card.WIDTH + (BoardSlot.PADDING * 2)) * 2);
            int slotY = targetLocation.y / ((Card.HEIGHT + (BoardSlot.PADDING * 2)) * 2);
            
            if(slotX < boardSlot.length && slotY < boardSlot[0].length) {
                BoardSlot targetBoardSlot = boardSlot[slotX][slotY];

                if(targetBoardSlot.isPlayable
                && targetBoardSlot.card == null) {
                    targetBoardSlot.card = targetCard;
                    return true;
                }
            }
        }

        return false;
    }

    public Rect getSize() {
        int width = (Card.WIDTH + (BoardSlot.PADDING * 2)) * boardSlot.length;
        int height = (Card.HEIGHT + (BoardSlot.PADDING * 2)) * boardSlot[0].length;
        return new Rect(new Point(0, 0), width, height);
    }
}
