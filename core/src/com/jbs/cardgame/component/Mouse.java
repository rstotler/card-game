package com.jbs.cardgame.component;

import com.badlogic.gdx.Gdx;
import com.jbs.cardgame.screen.Point;

public class Mouse {
    public Point oldLocation;

    public void update() {
        oldLocation = new Point(Gdx.input.getX(), Gdx.input.getY());
    }
}
