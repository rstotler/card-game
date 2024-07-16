package com.jbs.cardgame.component;

import com.badlogic.gdx.Gdx;
import com.jbs.cardgame.entity.Card;
import com.jbs.cardgame.screen.Point;
import com.jbs.cardgame.screen.Rect;

public class Mouse {
    public Rect rect;

    public Card selectedHandCard;

    public void update() {
        rect = new Rect(new Point(Gdx.input.getX(), Gdx.graphics.getBackBufferHeight() - Gdx.input.getY()), 1, 1);

        selectedHandCard = null;
    }
}
