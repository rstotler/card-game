package com.jbs.cardgame.screen.battlescreen.gamephase;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.jbs.cardgame.entity.Card;
import com.jbs.cardgame.entity.board.BoardSlot;
import com.jbs.cardgame.entity.board.GameBoard;

public class Deal extends GamePhase {
    public void update() {

    }

    public void render(OrthographicCamera camera, ShapeRenderer shapeRenderer, GameBoard gameBoard) {
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(camera.combined);
        
        shapeRenderer.setColor(40/255f, 0/255f, 0/255f, 1f);
        int xLoc = (gameBoard.getWidth() / 2) - (Card.WIDTH / 2);
        int yLoc = (gameBoard.getHeight() / 2) - (Card.HEIGHT / 2);
        shapeRenderer.rect(xLoc, yLoc, Card.WIDTH, Card.HEIGHT);

        shapeRenderer.end();
    }
}
