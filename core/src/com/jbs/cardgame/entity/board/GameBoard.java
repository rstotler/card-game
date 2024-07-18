package com.jbs.cardgame.entity.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.jbs.cardgame.Settings;
import com.jbs.cardgame.entity.Card;
import com.jbs.cardgame.screen.ImageManager;
import com.jbs.cardgame.screen.utility.Point;
import com.jbs.cardgame.screen.utility.RGBColor;
import com.jbs.cardgame.screen.utility.Rect;

public class GameBoard {
    public int cardsWidth;
    public int cardsHeight;

    public BoardSlot[][] boardSlot;

    public BitmapFont fontCard;

    public GameBoard() {
        cardsWidth = 5;
        cardsHeight = 4;

        fontCard = new BitmapFont(Gdx.files.internal("fonts/Code_New_Roman_18.fnt"), Gdx.files.internal("fonts/Code_New_Roman_18.png"), false);

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

    public void render(OrthographicCamera camera, OrthographicCamera cameraTop, ImageManager imageManager, SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
        for(int y = 0; y < cardsHeight; y++) {
            for(int x = 0; x < cardsWidth; x++) {
                BoardSlot targetBoardSlot = boardSlot[x][y];
                int xLoc = targetBoardSlot.location.x * (Card.WIDTH + (BoardSlot.PADDING * 2));
                int yLoc = targetBoardSlot.location.y * (Card.HEIGHT + (BoardSlot.PADDING * 2));

                // Render BoardSlot //
                shapeRenderer.begin(ShapeType.Filled);
                shapeRenderer.setProjectionMatrix(camera.combined);
                shapeRenderer.setColor(0/255f, targetBoardSlot.color/255f, 0/255f, 1f);
                shapeRenderer.rect(xLoc, yLoc, Card.WIDTH + (BoardSlot.PADDING * 2), Card.HEIGHT + (BoardSlot.PADDING * 2));
                shapeRenderer.end();

                // Render BoardSlot Card //
                if(targetBoardSlot.card != null) {
                    RGBColor cardColor = new RGBColor(0, 0, 0);
                    if(targetBoardSlot.card.currentOwnerInBattle != null) {
                        cardColor = targetBoardSlot.card.currentOwnerInBattle.cardColor;
                    }

                    targetBoardSlot.card.bufferCardImage(cameraTop, imageManager, spriteBatch, cardColor);

                    spriteBatch.setProjectionMatrix(camera.combined);
                    spriteBatch.begin();
                    spriteBatch.draw(Card.frameBufferCard.getColorBufferTexture(), targetBoardSlot.card.currentLocation.x, targetBoardSlot.card.currentLocation.y, Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT, 0, 0, 1, 1);
                    spriteBatch.end();
                }
            }
        }
    }

    public void flipSurroundingCardsCheck(BoardSlot centerBoardSlot) {
        for(int i = 0; i < 4; i++) {
            Card centerCard = centerBoardSlot.card;
            centerCard.flipCheck(this, centerBoardSlot, i);
        }
    }

    public Rect getSize() {
        int width = (Card.WIDTH + (BoardSlot.PADDING * 2)) * boardSlot.length;
        int height = (Card.HEIGHT + (BoardSlot.PADDING * 2)) * boardSlot[0].length;
        return new Rect(new Point(0, 0), width, height);
    }
}
