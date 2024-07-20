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
import com.jbs.cardgame.screen.battlescreen.gamephase.gamerule.*;
import com.jbs.cardgame.screen.utility.Point;
import com.jbs.cardgame.screen.utility.Rect;

public class BattleScreen extends Screen {
    public OrthographicCamera cameraTop;
    public OrthographicCamera cameraDebug;
    
    public ImageManager imageManager;
    
    public GamePhase gamePhase;
    public GameBoard gameBoard;

    public ArrayList<GameRule> gameRuleList;
    public ArrayList<BattlePlayer> battlePlayerList;
    public BattlePlayer currentTurnBattlePlayer;

    public BattleScreen() {
        super();

        cameraTop = new OrthographicCamera();
        cameraTop.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cameraDebug = new OrthographicCamera();
        cameraDebug.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
        imageManager = new ImageManager();
        initInputAdapter();

        initBattle();
        centerCamera();
    }

    public void initBattle() {
        gameRuleList = new ArrayList<>();
        gameRuleList.add(new Same());
        gameRuleList.add(new Plus());
        gameRuleList.add(new Combo());

        battlePlayerList = new ArrayList<>();
        battlePlayerList.add(new BattlePlayer(true));
        battlePlayerList.add(new BattlePlayer(false));
        battlePlayerList.add(new BattlePlayer(false));
        currentTurnBattlePlayer = battlePlayerList.get(0);

        gamePhase = new Deal();
        gameBoard = new GameBoard(7, 5, 3, 7);

        loadDebugBattle();
    }

    public void loadDebugBattle() {

        // Load Random Cards //
        // for(int i = 0; i < 7; i++) {
        //     int slotX = new Random().nextInt(gameBoard.boardSlot.length);
        //     int slotY = new Random().nextInt(gameBoard.boardSlot[0].length);
        //     BoardSlot randomSlot = gameBoard.boardSlot[slotX][slotY];

        //     if(randomSlot.card == null && randomSlot.isPlayable) {
        //         Card randomCard = new Card();
        //         BattlePlayer randomPlayer = battlePlayerList.get(new Random().nextInt(battlePlayerList.size() - 1) + 1);
        //         randomPlayer.placeCardOnGameBoard(gamePhase, gameBoard, randomCard, new Point(slotX, slotY), null, true);
        //     }
        // }

        // Load Same Rule Check Cards //
        // Card sameCard1 = new Card(new int[] {4, 4, 4, 4});
        // Card sameCard2 = new Card(new int[] {5, 5, 5, 5});
        // Card sameCard3 = new Card(new int[] {6, 6, 6, 6});
        // Card sameCard4 = new Card(new int[] {7, 7, 7, 7});
        // BattlePlayer randomPlayer = battlePlayerList.get(new Random().nextInt(battlePlayerList.size() - 1) + 1);
        // randomPlayer.placeCardOnGameBoard(gamePhase, gameBoard, sameCard1, new Point(1, 0), null, true);
        // battlePlayerList.get(0).placeCardOnGameBoard(gamePhase, gameBoard, sameCard2, new Point(0, 1), null, true);
        // randomPlayer.placeCardOnGameBoard(gamePhase, gameBoard, sameCard3, new Point(2, 1), null, true);
        // randomPlayer.placeCardOnGameBoard(gamePhase, gameBoard, sameCard4, new Point(1, 2), null, true);
        // battlePlayerList.get(0).hand.add(new Card(new int[] {7, 5, 4, 6}));

        // Load Combo Rule Check Cards //
        // BattlePlayer randomPlayer = battlePlayerList.get(new Random().nextInt(battlePlayerList.size() - 1) + 1);
        // Card sameCard1 = new Card(new int[] {1, 1, 1, 1});
        // randomPlayer.placeCardOnGameBoard(gamePhase, gameBoard, sameCard1, new Point(0, 0), null, true);
        // Card sameCard2 = new Card(new int[] {2, 2, 2, 2});
        // randomPlayer.placeCardOnGameBoard(gamePhase, gameBoard, sameCard2, new Point(1, 0), null, true);
        // Card sameCard3 = new Card(new int[] {3, 3, 3, 3});
        // randomPlayer.placeCardOnGameBoard(gamePhase, gameBoard, sameCard3, new Point(2, 0), null, true);
        // Card sameCard4 = new Card(new int[] {4, 4, 4, 4});
        // randomPlayer.placeCardOnGameBoard(gamePhase, gameBoard, sameCard4, new Point(3, 0), null, true);
        // Card sameCard5 = new Card(new int[] {5, 5, 5, 5});
        // randomPlayer.placeCardOnGameBoard(gamePhase, gameBoard, sameCard5, new Point(4, 0), null, true);
        // Card sameCard6 = new Card(new int[] {6, 6, 6, 6});
        // randomPlayer.placeCardOnGameBoard(gamePhase, gameBoard, sameCard6, new Point(4, 1), null, true);
        // Card sameCard7 = new Card(new int[] {7, 7, 1, 7});
        // randomPlayer.placeCardOnGameBoard(gamePhase, gameBoard, sameCard7, new Point(3, 1), null, true);
        // Card sameCard8 = new Card(new int[] {8, 8, 1, 8});
        // randomPlayer.placeCardOnGameBoard(gamePhase, gameBoard, sameCard8, new Point(2, 1), null, true);
        // Card sameCard9 = new Card(new int[] {8, 8, 8, 8});
        // randomPlayer.placeCardOnGameBoard(gamePhase, gameBoard, sameCard9, new Point(2, 3), null, true);
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
                    gameBoard = new GameBoard(7, 5, 3, 7);

                    battlePlayerList = new ArrayList<>();
                    battlePlayerList.add(new BattlePlayer(true));
                    battlePlayerList.add(new BattlePlayer(false));
                    battlePlayerList.add(new BattlePlayer(false));
                    currentTurnBattlePlayer = battlePlayerList.get(0);

                    centerCamera();
                    initBattle();
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
                } else if(button == 1) {
                    rightClickUp();
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
                if(!mouse.hoverHandCheck
                && battlePlayerList.get(0).placeCardOnGameBoard(gamePhase, gameBoard, mouse.selectedHandCard, targetSlot, currentTurnBattlePlayer, false)) {
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

    public void rightClickUp() {
        if(mouse.hoverHandCard != null) {
            for(int i = 0; i < 4; i++) {
                mouse.hoverHandCard.powerRating[i] += 1;
                if(mouse.hoverHandCard.powerRating[i] == 12) {
                    mouse.hoverHandCard.powerRating[i] = 1;
                }
            }
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
        mouse.hoverHandCheck = false;
        mouse.hoverHandCard = null;
        for(int i = 0; i < player.hand.size(); i++) {
            Card handCard = battlePlayerList.get(0).hand.get(i);

            Rect handCardRect = new Rect(handCard.currentLocation, Card.WIDTH * 2, Card.HEIGHT * 2);
            if(mouse.rect.rectCollide(handCardRect)
            && handCard.currentLocation.equals(handCard.targetLocation)) {
                mouse.hoverHandCheck = true;
                if(mouse.selectedHandCard == null) {
                    mouse.hoverHandCard = handCard;
                    break;
                }
            }
        }

        // Set HoverBoardCell //
        mouse.hoverBoardSlot = null;
        if(mouse.hoverHandCard == null) {
            int mouseX = (int) (mouse.rect.location.x - 640 + ((camera.position.x * 2) / camera.zoom));
            int mouseY = (int) (mouse.rect.location.y - 384 + ((camera.position.y * 2) / camera.zoom));
            if(mouseX >= 0 && mouseX < ((((Card.WIDTH + (BoardSlot.PADDING * 2)) * 2) / camera.zoom) * gameBoard.boardSlot.length)) {
                if(mouseY >= 0 && mouseY < ((((Card.HEIGHT + (BoardSlot.PADDING * 2)) * 2) / camera.zoom) * gameBoard.boardSlot[0].length)) {
                    int slotX = mouseX / (int) (((Card.WIDTH + (BoardSlot.PADDING * 2)) * 2) / camera.zoom);
                    int slotY = mouseY / (int) (((Card.HEIGHT + (BoardSlot.PADDING * 2)) * 2) / camera.zoom);
                    if(slotX >= 0 && slotX < gameBoard.boardSlot.length
                    && slotY >= 0 && slotY < gameBoard.boardSlot[0].length) {
                        mouse.hoverBoardSlot = gameBoard.boardSlot[slotX][slotY];
                    }
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

        gameBoard.render(camera, cameraTop, imageManager, spriteBatch, shapeRenderer, gamePhase, mouse);
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
        
        font.draw(spriteBatch, "Mouse X: " + mouse.rect.location.x + ", Y: " + mouse.rect.location.y + ", Zoom: " + camera.zoom, 3, 765);
        
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
        GameRule.font.dispose();

        spriteBatch.dispose();
        shapeRenderer.dispose();
        font.dispose();
    }
}
