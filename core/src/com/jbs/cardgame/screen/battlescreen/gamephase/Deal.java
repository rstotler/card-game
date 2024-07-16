package com.jbs.cardgame.screen.battlescreen.gamephase;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.jbs.cardgame.component.Mouse;
import com.jbs.cardgame.entity.Card;
import com.jbs.cardgame.entity.battleplayer.BattlePlayer;
import com.jbs.cardgame.entity.board.GameBoard;
import com.jbs.cardgame.screen.Point;
import com.jbs.cardgame.screen.Rect;

public class Deal extends GamePhase {
    public final int DEAL_AMOUNT = 7;

    public float dealPercent;
    public float dealCount;

    public Deal() {
        dealPercent = 0.0f;
        dealCount = 0;
    }

    public void update(BattlePlayer currentBattlePlayer) {
        if(dealCount < DEAL_AMOUNT) {
            dealPercent += .065;
            if(dealPercent >= 1) {
                dealPercent = 0;
                dealCount += 1;

                currentBattlePlayer.drawCard();
            }
        }
    }

    public void render(OrthographicCamera camera, OrthographicCamera cameraTop, ShapeRenderer shapeRenderer, Mouse mouse, GameBoard gameBoard, ArrayList<BattlePlayer> battlePlayerList) {
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(cameraTop.combined);

        // Deck //
        shapeRenderer.setColor(40/255f, 0/255f, 0/255f, 1f);
        int deckX = (Gdx.graphics.getWidth() / 2) - Card.WIDTH;
        int deckY = (Gdx.graphics.getHeight() / 2) - Card.HEIGHT;
        Point deckLocation = new Point(deckX, deckY);
        shapeRenderer.rect(deckX, deckY, Card.WIDTH * 2, Card.HEIGHT * 2);

        // Card Being Dealt //
        shapeRenderer.setColor(55/255f, 0/255f, 0/255f, 1f);
        int dealDestinationX = deckX;
        int dealDestinationY = 0;
        Point dealDestination = new Point(dealDestinationX, dealDestinationY);
        Point dealingCardLocation = Point.getPointAlongLine(deckLocation, dealDestination, dealPercent);
        shapeRenderer.rect(dealingCardLocation.x, dealingCardLocation.y, Card.WIDTH * 2, Card.HEIGHT * 2);
        
        // Player Hand //
        Card handHoverCard = null;
        for(int i = battlePlayerList.get(0).hand.size() - 1; i >= 0 ; i--) {
            Card handCard = battlePlayerList.get(0).hand.get(i);

            Rect handRect = null;
            if(i == 0) {
                handRect = new Rect(handCard.handLocation, Card.WIDTH * 2, Card.HEIGHT * 2);
            } else {
                handRect = new Rect(new Point(handCard.handLocation.x + ((Card.WIDTH * 2) - BattlePlayer.HAND_OVERLAP_WIDTH), handCard.handLocation.y), BattlePlayer.HAND_OVERLAP_WIDTH, Card.HEIGHT * 2);
            }
            
            if(handHoverCard == null
            && mouse.rect.rectCollide(handRect)) {
                handHoverCard = handCard;
            } else {
                shapeRenderer.setColor(handCard.color/255f, 0/255f, 0/255f, 1f);
                shapeRenderer.rect(handCard.handLocation.x, handCard.handLocation.y, Card.WIDTH * 2, Card.HEIGHT * 2);
            }
        }

        // Hand Hovered Over Card //
        if(handHoverCard != null) {
            shapeRenderer.setColor(65/255f, 0/255f, 0/255f, 1f);
            shapeRenderer.rect(handHoverCard.handLocation.x, 0, Card.WIDTH * 2, Card.HEIGHT * 2);
        }
        
        shapeRenderer.end();
    }
}
