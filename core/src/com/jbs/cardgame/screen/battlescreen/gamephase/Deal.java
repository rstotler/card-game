package com.jbs.cardgame.screen.battlescreen.gamephase;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jbs.cardgame.Settings;
import com.jbs.cardgame.component.Mouse;
import com.jbs.cardgame.entity.Card;
import com.jbs.cardgame.entity.battleplayer.BattlePlayer;
import com.jbs.cardgame.entity.board.GameBoard;
import com.jbs.cardgame.screen.ImageManager;
import com.jbs.cardgame.screen.battlescreen.BattleScreen;
import com.jbs.cardgame.screen.utility.Point;

public class Deal extends GamePhase {
    public final int DEAL_AMOUNT = 7;
    public final float DEAL_SPEED = .15f;
    public final boolean FAST_DEAL = true;

    public float dealPercent;
    public float dealCount;

    public Deal() {
        super();

        nextGamePhase = new PlayCard();
        
        dealPercent = 0.0f;
        dealCount = 0;
    }

    public String update(BattleScreen battleScreen) {

        // Fast Deal //
        if(FAST_DEAL) {
            for(int i = 0; i < DEAL_AMOUNT; i++) {
                battleScreen.currentTurnBattlePlayer.drawCardToHand();
                dealCount += 1;
            }
        }

        // Switch Player Or End GamePhase //
        if(dealCount >= DEAL_AMOUNT) {
            if(battleScreen.battlePlayerList.contains(battleScreen.currentTurnBattlePlayer)
            && battleScreen.battlePlayerList.indexOf(battleScreen.currentTurnBattlePlayer) < battleScreen.battlePlayerList.size() - 1) {
                dealCount = 0;
                return "Next Player";
            } else {
                return "End GamePhase";
            }
        }

        // Deal //
        else if(dealCount < DEAL_AMOUNT) {
            dealPercent += DEAL_SPEED;
            if(dealPercent >= 1) {
                dealPercent = 0;
                dealCount += 1;

                battleScreen.currentTurnBattlePlayer.drawCardToHand();
            }
        }

        return "";
    }

    @SuppressWarnings("unused")
    public void render(OrthographicCamera camera, OrthographicCamera cameraTop, SpriteBatch spriteBatch, ImageManager imageManager, Mouse mouse, GameBoard gameBoard, ArrayList<BattlePlayer> battlePlayerList, BattlePlayer currentTurnBattlePlayer) {
        spriteBatch.setProjectionMatrix(cameraTop.combined);
        spriteBatch.begin();

        // Deck //
        int deckX = (Settings.SCREEN_WIDTH / 2) - Card.WIDTH;
        int deckY = (Settings.SCREEN_HEIGHT / 2) - Card.HEIGHT;
        Point deckLocation = new Point(deckX, deckY);
        int currentTurnBattlePlayerIndex = battlePlayerList.indexOf(currentTurnBattlePlayer);

        if(!(currentTurnBattlePlayerIndex == battlePlayerList.size() - 1
        && dealCount >= DEAL_AMOUNT - 1)
        && FAST_DEAL == false) {
            spriteBatch.draw(imageManager.cardBackTexture, deckX, deckY, Card.WIDTH * 2, Card.HEIGHT * 2, 0, 0, 1, 1);
            spriteBatch.draw(imageManager.cardBorderTexture, deckX, deckY, Card.WIDTH * 2, Card.HEIGHT * 2, 0, 0, 1, 1);
        }
        
        // Card Being Dealt //
        if(dealCount < DEAL_AMOUNT
        && FAST_DEAL == false) {
            Point dealDestination = BattlePlayer.getPlayerScreenLocation(currentTurnBattlePlayerIndex, battlePlayerList.size());
            Point dealingCardLocation = Point.getPointAlongLine(deckLocation, dealDestination, dealPercent);
            spriteBatch.draw(imageManager.cardBackTexture, dealingCardLocation.x, dealingCardLocation.y, Card.WIDTH * 2, Card.HEIGHT * 2, 0, 0, 1, 1);
            spriteBatch.draw(imageManager.cardBorderTexture, dealingCardLocation.x, dealingCardLocation.y, Card.WIDTH * 2, Card.HEIGHT * 2, 0, 0, 1, 1);
        }
        
        spriteBatch.end();
    }
}
