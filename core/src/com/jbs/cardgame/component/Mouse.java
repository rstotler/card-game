package com.jbs.cardgame.component;

import com.badlogic.gdx.Gdx;
import com.jbs.cardgame.entity.Card;
import com.jbs.cardgame.screen.Point;
import com.jbs.cardgame.screen.Rect;

public class Mouse {
    public boolean leftClick;

    public Rect rect;

    public Card hoverHandCard;
    public Card selectedHandCard;

    public Mouse() {
        leftClick = false;

        rect = new Rect(new Point(0, 0), 1, 1);

        hoverHandCard = null;
        selectedHandCard = null;
    }

    public void updateLocation() {
        rect.location.x = Gdx.input.getX();
        rect.location.y = Gdx.graphics.getBackBufferHeight() - Gdx.input.getY();
    }
}
