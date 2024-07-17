package com.jbs.cardgame.screen.battlescreen.gamephase;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.jbs.cardgame.Settings;
import com.jbs.cardgame.component.Mouse;
import com.jbs.cardgame.entity.Card;
import com.jbs.cardgame.entity.battleplayer.BattlePlayer;
import com.jbs.cardgame.entity.board.GameBoard;
import com.jbs.cardgame.screen.Point;

public class Deal extends GamePhase {
    public final int DEAL_AMOUNT = 7;
    public final boolean FAST_DEAL = true;

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

    public void render(OrthographicCamera camera, OrthographicCamera cameraTop, ShapeRenderer shapeRenderer, Mouse mouse, GameBoard gameBoard, ArrayList<BattlePlayer> battlePlayerList, BattlePlayer currentBattlePlayer) {
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(cameraTop.combined);

        // Deck //
        int deckX = (Settings.SCREEN_WIDTH / 2) - Card.WIDTH;
        int deckY = (Settings.SCREEN_HEIGHT / 2) - Card.HEIGHT;
        Point deckLocation = new Point(deckX, deckY);
        int currentBattlePlayerIndex = battlePlayerList.indexOf(currentBattlePlayer);
        if(!(currentBattlePlayerIndex == battlePlayerList.size() - 1
        && dealCount >= DEAL_AMOUNT - 1)) {
            shapeRenderer.setColor(40/255f, 0/255f, 0/255f, 1f);
            shapeRenderer.rect(deckX, deckY, Card.WIDTH * 2, Card.HEIGHT * 2);
        }
        
        // Card Being Dealt //
        if(dealCount < DEAL_AMOUNT) {
            shapeRenderer.setColor(55/255f, 0/255f, 0/255f, 1f);
            Point dealDestination = BattlePlayer.getPlayerScreenLocation(currentBattlePlayerIndex, battlePlayerList.size());
            Point dealingCardLocation = Point.getPointAlongLine(deckLocation, dealDestination, dealPercent);
            shapeRenderer.rect(dealingCardLocation.x, dealingCardLocation.y, Card.WIDTH * 2, Card.HEIGHT * 2);
        }
        
        shapeRenderer.end();
    }
}
