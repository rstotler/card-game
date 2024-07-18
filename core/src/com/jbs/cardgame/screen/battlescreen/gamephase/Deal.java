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
import com.jbs.cardgame.screen.utility.Point;

public class Deal extends GamePhase {
    public final int DEAL_AMOUNT = 7;
    public final boolean FAST_DEAL = false;

    public float dealPercent;
    public float dealCount;

    public Deal() {
        dealPercent = 0.0f;
        dealCount = 0;
    }

    public String update(ArrayList<BattlePlayer> battlePlayerList, BattlePlayer currentBattlePlayer) {

        // Switch Player Or End GamePhase //
        if(dealCount >= DEAL_AMOUNT) {
            if(battlePlayerList.contains(currentBattlePlayer)
            && battlePlayerList.indexOf(currentBattlePlayer) < battlePlayerList.size() - 1) {
                dealCount = 0;
                return "Next Player";
            } else {
                return "End GamePhase";
            }
        }

        // Deal //
        else if(dealCount < DEAL_AMOUNT) {
            if(FAST_DEAL) {
                dealPercent = 1;
            } else {
                dealPercent += .065;
            }
            if(dealPercent >= 1) {
                dealPercent = 0;
                dealCount += 1;

                currentBattlePlayer.drawCardToHand();
            }
        }

        return "";
    }

    public void render(OrthographicCamera camera, OrthographicCamera cameraTop, SpriteBatch spriteBatch, ImageManager imageManager, Mouse mouse, GameBoard gameBoard, ArrayList<BattlePlayer> battlePlayerList, BattlePlayer currentBattlePlayer) {
        spriteBatch.setProjectionMatrix(cameraTop.combined);
        spriteBatch.begin();

        // Deck //
        int deckX = (Settings.SCREEN_WIDTH / 2) - Card.WIDTH;
        int deckY = (Settings.SCREEN_HEIGHT / 2) - Card.HEIGHT;
        Point deckLocation = new Point(deckX, deckY);
        int currentBattlePlayerIndex = battlePlayerList.indexOf(currentBattlePlayer);
        if(!(currentBattlePlayerIndex == battlePlayerList.size() - 1
        && dealCount >= DEAL_AMOUNT - 1)) {
            spriteBatch.draw(imageManager.cardBackTexture, deckX, deckY, Card.WIDTH * 2, Card.HEIGHT * 2, 0, 0, 1, 1);
            spriteBatch.draw(imageManager.cardBorderTexture, deckX, deckY, Card.WIDTH * 2, Card.HEIGHT * 2, 0, 0, 1, 1);
        }
        
        // Card Being Dealt //
        if(dealCount < DEAL_AMOUNT) {
            Point dealDestination = BattlePlayer.getPlayerScreenLocation(currentBattlePlayerIndex, battlePlayerList.size());
            Point dealingCardLocation = Point.getPointAlongLine(deckLocation, dealDestination, dealPercent);
            spriteBatch.draw(imageManager.cardBackTexture, dealingCardLocation.x, dealingCardLocation.y, Card.WIDTH * 2, Card.HEIGHT * 2, 0, 0, 1, 1);
            spriteBatch.draw(imageManager.cardBorderTexture, dealingCardLocation.x, dealingCardLocation.y, Card.WIDTH * 2, Card.HEIGHT * 2, 0, 0, 1, 1);
        }
        
        spriteBatch.end();
    }
}
