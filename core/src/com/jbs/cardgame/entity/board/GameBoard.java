package com.jbs.cardgame.entity.board;

import java.util.Random;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.jbs.cardgame.Settings;
import com.jbs.cardgame.component.Mouse;
import com.jbs.cardgame.entity.Card;
import com.jbs.cardgame.screen.ImageManager;
import com.jbs.cardgame.screen.battlescreen.gamephase.*;
import com.jbs.cardgame.screen.utility.Point;
import com.jbs.cardgame.screen.utility.RGBColor;
import com.jbs.cardgame.screen.utility.Rect;

public class GameBoard {
    public int cardsWidth;
    public int cardsHeight;

    public BoardSlot[][] boardSlot;

    public GameBoard(int cardsWidth, int cardsHeight, int nonPlayableCount, int elementSlotCount) {
        this.cardsWidth = cardsWidth;
        this.cardsHeight = cardsHeight;

        generateBoard(nonPlayableCount, elementSlotCount);
    }

    public void generateBoard(int nonPlayableCount, int elementSlotCount) {
        boardSlot = new BoardSlot[cardsWidth][cardsHeight];
        for(int y = 0; y < cardsHeight; y++) {
            for(int x = 0; x < cardsWidth; x++) {
                boardSlot[x][y] = new BoardSlot(new Point(x, y));
            }
        }

        for(int i = 0; i < nonPlayableCount; i++) {
            int randomX = new Random().nextInt(cardsWidth);
            int randomY = new Random().nextInt(cardsHeight);
            boardSlot[randomX][randomY].isPlayable = false;
        }

        for(int i = 0; i < elementSlotCount; i++) {
            int randomX = new Random().nextInt(cardsWidth);
            int randomY = new Random().nextInt(cardsHeight);
            boardSlot[randomX][randomY].element = Card.getElementList().get(new Random().nextInt(Card.getElementList().size()));
        }
    }

    public void render(OrthographicCamera camera, OrthographicCamera cameraTop, ImageManager imageManager, SpriteBatch spriteBatch, ShapeRenderer shapeRenderer, GamePhase gamePhase, Mouse mouse) {
        for(int y = cardsHeight - 1; y >= 0; y--) {
            for(int x = 0; x < cardsWidth; x++) {
                BoardSlot targetBoardSlot = boardSlot[x][y];
                int xLoc = targetBoardSlot.location.x * (Card.WIDTH + (BoardSlot.PADDING * 2));
                int yLoc = targetBoardSlot.location.y * (Card.HEIGHT + (BoardSlot.PADDING * 2));

                // Render BoardSlot //
                if(targetBoardSlot.isPlayable) {
                    shapeRenderer.begin(ShapeType.Filled);
                    shapeRenderer.setProjectionMatrix(camera.combined);
                    if(mouse.hoverBoardSlot != null && mouse.hoverBoardSlot == targetBoardSlot) {
                        shapeRenderer.setColor(0/255f, 10/255f, 0/255f, 1f);
                    } else {
                        shapeRenderer.setColor(0/255f, targetBoardSlot.color/255f, 0/255f, 1f);
                    }
                    shapeRenderer.rect(xLoc, yLoc, Card.WIDTH + (BoardSlot.PADDING * 2), Card.HEIGHT + (BoardSlot.PADDING * 2));
                    shapeRenderer.end();

                    // Board Slot Element Character //
                    if(!targetBoardSlot.element.equals("")) {
                        spriteBatch.setProjectionMatrix(camera.combined);
                        spriteBatch.begin();
                        String elementSubstring = targetBoardSlot.element.substring(0, 1) + targetBoardSlot.element.substring(targetBoardSlot.element.length() - 1);
                        Card.font.draw(spriteBatch, elementSubstring, xLoc + 37, yLoc + 73);
                        spriteBatch.end();
                    }
                }

                // Render BoardSlot Card OR  //
                BoardSlot mouseHoverBoardSlot = mouse.getHoverBoardSlot(camera, boardSlot);
                boolean movingBoardSlotCard = (mouse.selectedBoardSlot != null && mouse.selectedBoardSlot.card != null
                                              && mouseHoverBoardSlot != null && mouseHoverBoardSlot.isPlayable
                                              && mouseHoverBoardSlot.location.x == x && mouseHoverBoardSlot.location.y == y
                                              && !(mouseHoverBoardSlot.location.x == mouse.selectedBoardSlot.location.x && mouseHoverBoardSlot.location.y == mouse.selectedBoardSlot.location.y));

                if(targetBoardSlot.card != null || movingBoardSlotCard) {

                    // Set Card Color //
                    RGBColor cardColor = new RGBColor(0, 0, 0);
                    if(targetBoardSlot.card != null
                    && targetBoardSlot.card.currentOwnerInBattle != null) {
                        cardColor = targetBoardSlot.card.currentOwnerInBattle.cardColor;
                    }

                    // Buffer Image //
                    if(targetBoardSlot.card != null) {
                        targetBoardSlot.card.bufferCardImage(cameraTop, imageManager, spriteBatch, cardColor, targetBoardSlot);
                    } else {
                        mouse.selectedBoardSlot.card.bufferCardImage(cameraTop, imageManager, spriteBatch, cardColor, mouse.selectedBoardSlot);
                    }
                    
                    spriteBatch.setProjectionMatrix(camera.combined);
                    spriteBatch.begin();
                    
                    // Render Moving Card OR Flipping Cards OR Cards On Board //
                    if(movingBoardSlotCard) {
                        Point selectedBoardCardLocation = targetBoardSlot.getScreenLocation();
                        spriteBatch.setColor(1, 1, 1, .4f);
                        spriteBatch.draw(Card.frameBufferCard.getColorBufferTexture(), selectedBoardCardLocation.x, selectedBoardCardLocation.y, Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT, 0, 0, 1, 1);
                        spriteBatch.setColor(1, 1, 1, 1);
                    } else if(gamePhase != null && gamePhase.toString().equals("FlipChecks")
                    && (((FlipChecks) gamePhase).flipCardList.contains(targetBoardSlot.card)
                    || ((FlipChecks) gamePhase).comboFlipList.contains(targetBoardSlot.card))
                    && targetBoardSlot.card.originalOwnerInBattle != ((FlipChecks) gamePhase).attackingPlayer) {
                        ((FlipChecks) gamePhase).renderFlippingCard(spriteBatch, targetBoardSlot);
                    } else {
                        spriteBatch.draw(Card.frameBufferCard.getColorBufferTexture(), targetBoardSlot.card.currentLocation.x, targetBoardSlot.card.currentLocation.y, Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT, 0, 0, 1, 1);
                    }

                    spriteBatch.end();
                }
            }
        }
    }

    public boolean isFull() {
        for(int y = 0; y < boardSlot[0].length; y++) {
            for(int x = 0; x < boardSlot.length; x++) {
                if(boardSlot[x][y].isPlayable
                && boardSlot[x][y].card == null) {
                    return false;
                }
            }
        }

        return true;
    }

    public Rect getSize() {
        int width = (Card.WIDTH + (BoardSlot.PADDING * 2)) * boardSlot.length;
        int height = (Card.HEIGHT + (BoardSlot.PADDING * 2)) * boardSlot[0].length;
        return new Rect(new Point(0, 0), width, height);
    }
}
