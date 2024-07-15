package com.jbs.cardgame.screen.battlescreen.gamephase;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.jbs.cardgame.entity.Card;
import com.jbs.cardgame.entity.board.GameBoard;
import com.jbs.cardgame.screen.Point;

public class Deal extends GamePhase {
    public final int DEAL_AMOUNT = 7;

    public float dealPercent;
    public float dealCount;

    public Deal() {
        dealPercent = 0.0f;
        dealCount = 0;
    }

    public void update() {
        if(dealCount < DEAL_AMOUNT) {
            dealPercent += .065;
            if(dealPercent >= 1) {
                dealPercent = 0;
                dealCount += 1;
            }
        }
    }

    public void render(OrthographicCamera camera, OrthographicCamera cameraTop, ShapeRenderer shapeRenderer, GameBoard gameBoard) {
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(cameraTop.combined);
        
        // Deck //
        shapeRenderer.setColor(40/255f, 0/255f, 0/255f, 1f);
        int deckX = (Gdx.graphics.getWidth() / 4) - (Card.WIDTH / 2);
        int deckY = (Gdx.graphics.getHeight() / 4) - (Card.HEIGHT / 2);
        Point deckLocation = new Point(deckX, deckY);
        shapeRenderer.rect(deckX, deckY, Card.WIDTH, Card.HEIGHT);

        // Card Being Dealt //
        shapeRenderer.setColor(55/255f, 0/255f, 0/255f, 1f);
        int dealDestinationX = deckX;
        int dealDestinationY = 0;
        Point dealDestination = new Point(dealDestinationX, dealDestinationY);
        Point dealingCardLocation = Point.getPointAlongLine(deckLocation, dealDestination, dealPercent);
        shapeRenderer.rect(dealingCardLocation.x, dealingCardLocation.y, Card.WIDTH, Card.HEIGHT);

        shapeRenderer.end();
    }
}
