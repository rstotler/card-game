package com.jbs.cardgame.component;

import com.badlogic.gdx.Gdx;

public class Mouse {
    public int oldX;
    public int oldY;

    public void update() {
        oldX = Gdx.input.getX();
        oldY = Gdx.input.getY();
    }
}
