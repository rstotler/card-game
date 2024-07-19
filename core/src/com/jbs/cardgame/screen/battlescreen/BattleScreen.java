package com.jbs.cardgame.screen.battlescreen;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.jbs.cardgame.Settings;
import com.jbs.cardgame.entity.Card;
import com.jbs.cardgame.entity.battleplayer.BattlePlayer;
import com.jbs.cardgame.entity.board.BoardSlot;
import com.jbs.cardgame.entity.board.GameBoard;
import com.jbs.cardgame.screen.ImageManager;
import com.jbs.cardgame.screen.Screen;
import com.jbs.cardgame.screen.battlescreen.gamephase.*;
import com.jbs.cardgame.screen.utility.Point;
import com.jbs.cardgame.screen.utility.Rect;

public class BattleScreen extends Screen {
    public OrthographicCamera cameraTop;
    public OrthographicCamera cameraDebug;
    
    public ImageManager imageManager;
    
    public GamePhase gamePhase;
    public GameBoard gameBoard;

    public ArrayList<BattlePlayer> battlePlayerList;
    public BattlePlayer currentTurnBattlePlayer;

    public BattleScreen() {
        super();

        cameraTop = new OrthographicCamera();
        cameraTop.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cameraDebug = new OrthographicCamera();
        cameraDebug.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
        imageManager = new ImageManager();

        gamePhase = new Deal();
        gameBoard = new GameBoard();

        battlePlayerList = new ArrayList<>();
        battlePlayerList.add(new BattlePlayer(true));
        battlePlayerList.add(new BattlePlayer(false));
        battlePlayerList.add(new BattlePlayer(false));
        currentTurnBattlePlayer = battlePlayerList.get(0);

        centerCamera();
        initInputAdapter();

        loadDebugGame();
    }

    public void loadDebugGame() {
        for(int i = 0; i < 7; i++) {
            int slotX = new Random().nextInt(gameBoard.boardSlot.length);
            int slotY = new Random().nextInt(gameBoard.boardSlot[0].length);
            BoardSlot randomSlot = gameBoard.boardSlot[slotX][slotY];

            if(randomSlot.card == null && randomSlot.isPlayable) {
                Card randomCard = new Card();
                BattlePlayer randomPlayer = battlePlayerList.get(new Random().nextInt(battlePlayerList.size() - 1) + 1);
                randomPlayer.placeCardOnGameBoard(gamePhase, gameBoard, randomCard, new Point(slotX, slotY), currentTurnBattlePlayer, true);
            }
        }
    }

    public void initInputAdapter() {
        Gdx.input.setInputProcessor(new InputAdapter() {

            // Keyboard Input //
            @Override
            public boolean keyDown(int keyCode) {
                String key = Input.Keys.toString(keyCode);

                // Toggle Full Screen //
                if(key.equals("F4")) {
                    boolean isFullScreen = Gdx.graphics.isFullscreen();
                    Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();

                    if(isFullScreen) {
                        Gdx.graphics.setWindowedMode(1280, 768);
                    } else {
                        Gdx.graphics.setFullscreenMode(currentMode);
                    }

                    Settings.SIZE_RATIO_X = 1280.0f / Gdx.graphics.getWidth();
                    Settings.SIZE_RATIO_Y = 768.0f / Gdx.graphics.getHeight();

                    battlePlayerList.get(0).updateHandLocations();
                }

                // (Debug) Reset BattleScreen //
                else if(key.equals("Space")) {
                    gamePhase = new Deal();
                    gameBoard = new GameBoard();

                    battlePlayerList = new ArrayList<>();
                    battlePlayerList.add(new BattlePlayer(true));
                    battlePlayerList.add(new BattlePlayer(false));
                    battlePlayerList.add(new BattlePlayer(false));
                    currentTurnBattlePlayer = battlePlayerList.get(0);

                    centerCamera();
                    loadDebugGame();
                }

                else if(key.equals("Escape")) {
                    System.exit(0);
                }

                return true;
            }

            @Override
            public boolean keyUp(int keyCode) {
                return true;
            }

            // Mouse Input //
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if(button == 0) {
                    leftClickDown();
                }
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if(button == 0) {
                    leftClickUp();
                }
                return true;
            }

            @Override
            public boolean scrolled(float amountX, float amountY) {
                changeZoomLevel((int) amountY);
                return true;
            }

            @Override
            public boolean touchDragged(int moveX, int moveY, int pointer) {
                if(mouse.leftClick) {
                    if(mouse.selectedHandCard == null) {
                        moveY = Gdx.graphics.getHeight() - moveY;
                        int moveDiffX = (int) (mouse.rect.location.x - (moveX * Settings.SIZE_RATIO_X)) / 2;
                        int moveDiffY = (int) (mouse.rect.location.y - (moveY * Settings.SIZE_RATIO_Y)) / 2;
                        moveCamera(moveDiffX, moveDiffY);
                    }
                }
                return true;
            }
        });
    }

    public void leftClickDown() {
        mouse.leftClick = true;

        if(mouse.hoverHandCard != null) {
            mouse.selectedHandCard = mouse.hoverHandCard;
            mouse.hoverHandCard = null;

            // Set Selected Card Offset //
            mouse.selectedHandCard.selectedCardOffset.x = mouse.selectedHandCard.currentLocation.x - mouse.rect.location.x;
            mouse.selectedHandCard.selectedCardOffset.y = mouse.selectedHandCard.currentLocation.y - mouse.rect.location.y + (BattlePlayer.HAND_Y_OFFSET * -1);
        }
    }

    public void leftClickUp() {
        mouse.leftClick = false;

        // Place Selected Card Into BoardSlot OR Return Card To Hand //
        if(mouse.selectedHandCard != null) {
            int clickX = (int) (mouse.rect.location.x - 640 + ((camera.position.x * 2) / camera.zoom));
            int clickY = (int) (mouse.rect.location.y - 384 + ((camera.position.y * 2) / camera.zoom));
            
            if(clickX >= 0 && clickY >= 0) {
                int slotX = (int) (clickX / (((Card.WIDTH + (BoardSlot.PADDING * 2)) * 2) / camera.zoom));
                int slotY = (int) (clickY / (((Card.HEIGHT + (BoardSlot.PADDING * 2)) * 2) / camera.zoom));
                Point targetSlot = new Point(slotX, slotY);
                
                // Place Card On Board //
                if(battlePlayerList.get(0).placeCardOnGameBoard(gamePhase, gameBoard, mouse.selectedHandCard, targetSlot, currentTurnBattlePlayer, false)) {
                    battlePlayerList.get(0).removeCardFromHand(mouse.selectedHandCard);
                    battlePlayerList.get(0).updateHandLocations();

                    gamePhase = new FlipChecks(battlePlayerList.get(0));
                    FlipChecks.initFlipSurroundingCards(this, gameBoard.boardSlot[slotX][slotY]);
                }
                
                // Return Card To Hand //
                else {
                    float diffX = mouse.selectedHandCard.targetLocation.x - (mouse.rect.location.x + mouse.selectedHandCard.selectedCardOffset.x);
                    float diffY = (mouse.selectedHandCard.targetLocation.y - BattlePlayer.HAND_Y_OFFSET) - (mouse.rect.location.y + mouse.selectedHandCard.selectedCardOffset.y);
                    
                    if(Math.abs(diffX) < Card.MOVE_SPEED && Math.abs(diffY) < Card.MOVE_SPEED) {
                        mouse.selectedHandCard.currentLocation.x = mouse.selectedHandCard.targetLocation.x;
                        mouse.selectedHandCard.currentLocation.y = mouse.selectedHandCard.targetLocation.y;
                    } else {
                        mouse.selectedHandCard.currentLocation.x = mouse.rect.location.x + mouse.selectedHandCard.selectedCardOffset.x;
                        mouse.selectedHandCard.currentLocation.y = mouse.rect.location.y + mouse.selectedHandCard.selectedCardOffset.y;
                    }
                }
            }
            
            mouse.selectedHandCard = null;
        }
    }

    public void handleInput() {
    }

    public void update() {
        mouse.updateLocation();
        updateMouse();
        updateHand();

        // Update GamePhase //
        if(gamePhase != null) {
            String gamePhaseReturnStatus = gamePhase.update(this);
            if(gamePhaseReturnStatus.equals("Next Player")) {
                setNextPlayer();
            } else if(gamePhaseReturnStatus.equals("End GamePhase")) {
                setNextPlayer();
                gamePhase = gamePhase.nextGamePhase;
            }
        }
    }

    public void updateMouse() {
        BattlePlayer player = battlePlayerList.get(0);

        // Set HoverCard //
        mouse.hoverHandCard = null;
        if(mouse.selectedHandCard == null) {
            for(int i = 0; i < player.hand.size(); i++) {
                Card handCard = battlePlayerList.get(0).hand.get(i);
    
                Rect handCardRect = new Rect(handCard.currentLocation, Card.WIDTH * 2, Card.HEIGHT * 2);
                if(mouse.rect.rectCollide(handCardRect)
                && handCard.currentLocation.equals(handCard.targetLocation)) {
                    mouse.hoverHandCard = handCard;
                    break;
                }
            }
        }
    }

    public void updateHand() {
        for(Card handCard : battlePlayerList.get(0).hand) {
            if(!handCard.currentLocation.equals(handCard.targetLocation)) {
                handCard.updateLocation();
            }
        }
    }

    public void render() {
        ScreenUtils.clear(0/255f, 0/255f, 15/255f, 1);

        gameBoard.render(camera, cameraTop, imageManager, spriteBatch, shapeRenderer, gamePhase);
        if(gamePhase != null) {
            gamePhase.render(camera, cameraTop, spriteBatch, imageManager, mouse, gameBoard, battlePlayerList, currentTurnBattlePlayer);
        }
        renderPlayerHand();

        renderDebugData();
    }

    public void renderPlayerHand() {

        // Hand //
        for(int i = battlePlayerList.get(0).hand.size() - 1; i >= 0 ; i--) {
            Card handCard = battlePlayerList.get(0).hand.get(i);

            if(mouse.hoverHandCard != handCard
            && mouse.selectedHandCard != handCard) {
                handCard.bufferCardImage(cameraTop, imageManager, spriteBatch, battlePlayerList.get(0).cardColor, null);

                spriteBatch.setProjectionMatrix(cameraTop.combined);
                spriteBatch.begin();
                spriteBatch.draw(Card.frameBufferCard.getColorBufferTexture(), handCard.currentLocation.x, handCard.currentLocation.y, Settings.SCREEN_WIDTH * 2, Settings.SCREEN_HEIGHT * 2, 0, 0, 1, 1);
                spriteBatch.end();
            }
        }

        // Hover Over Hand Card //
        if(mouse.hoverHandCard != null
        && mouse.selectedHandCard == null) {
            mouse.hoverHandCard.bufferCardImage(cameraTop, imageManager, spriteBatch, battlePlayerList.get(0).cardColor, null);

            spriteBatch.setProjectionMatrix(cameraTop.combined);
            spriteBatch.begin();
            spriteBatch.draw(Card.frameBufferCard.getColorBufferTexture(), mouse.hoverHandCard.currentLocation.x, 0, Settings.SCREEN_WIDTH * 2, Settings.SCREEN_HEIGHT * 2, 0, 0, 1, 1);
            spriteBatch.end();
        }

        // Card Selected From Hand Or GameBoard //
        if(mouse.selectedHandCard != null) {
            int selectedCardX = mouse.rect.location.x + mouse.selectedHandCard.selectedCardOffset.x;
            int selectedCardY = mouse.rect.location.y + mouse.selectedHandCard.selectedCardOffset.y;

            mouse.selectedHandCard.bufferCardImage(cameraTop, imageManager, spriteBatch, battlePlayerList.get(0).cardColor, null);
            
            spriteBatch.setProjectionMatrix(cameraTop.combined);
            spriteBatch.begin();
            spriteBatch.draw(Card.frameBufferCard.getColorBufferTexture(), selectedCardX, selectedCardY, Settings.SCREEN_WIDTH * 2, Settings.SCREEN_HEIGHT * 2, 0, 0, 1, 1);
            spriteBatch.end();
        }
    }

    public void renderDebugData() {
        spriteBatch.setProjectionMatrix(cameraDebug.combined);
        spriteBatch.begin();

        font.setColor(Color.WHITE);
        font.draw(spriteBatch, "FPS: " + String.valueOf(Gdx.graphics.getFramesPerSecond()), 1205, 767);
        
        font.draw(spriteBatch, "Mouse X: " + mouse.rect.location.x + ", Y: " + mouse.rect.location.y, 3, 765);
        
        String gamePhaseString = "None";
        if(gamePhase != null) {
            gamePhaseString = gamePhase.toString();
        }
        font.draw(spriteBatch, "GamePhase: " + gamePhaseString, 3, 750);
        font.draw(spriteBatch, "Current Player: " + battlePlayerList.indexOf(currentTurnBattlePlayer), 3, 735);
        
        spriteBatch.end();
    }

    public void setNextPlayer() {
        int currentPlayerIndex = battlePlayerList.indexOf(currentTurnBattlePlayer);
        if(currentPlayerIndex == battlePlayerList.size() - 1) {
            currentTurnBattlePlayer = battlePlayerList.get(0);
        } else {
            currentTurnBattlePlayer = battlePlayerList.get(currentPlayerIndex + 1);
        }
    }

    public void centerCamera() {
        int xLoc = gameBoard.getSize().width / 2;
        int yLoc = gameBoard.getSize().height / 2;
        camera.position.set(xLoc, yLoc, 0);
        camera.update();
    }

    public void dispose() {
        imageManager.dispose();
        Card.frameBufferCard.dispose();
        Card.font.dispose();

        spriteBatch.dispose();
        shapeRenderer.dispose();
        font.dispose();
    }
}
