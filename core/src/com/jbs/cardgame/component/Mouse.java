package com.jbs.cardgame.component;

import com.badlogic.gdx.Gdx;
import com.jbs.cardgame.entity.Card;
import com.jbs.cardgame.screen.Point;
import com.jbs.cardgame.screen.Rect;

public class Mouse {
    public Rect rect;

    public Card hoverHandCard;
    public Card selectedHandCard;

    public void update() {
        rect = new Rect(new Point(Gdx.input.getX(), Gdx.graphics.getBackBufferHeight() - Gdx.input.getY()), 1, 1);

        hoverHandCard = null;
        selectedHandCard = null;
    }

    public void leftClickDown() {
        if(hoverHandCard != null) {
            selectedHandCard = hoverHandCard;
            System.out.println("DOWN");
        }
    }

    public void leftClickUp() {
        if(selectedHandCard != null) {
            selectedHandCard = null;
            System.out.println("UP");
        }
    }
}
