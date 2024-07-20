package com.jbs.cardgame.component;

import com.badlogic.gdx.Gdx;
import com.jbs.cardgame.Settings;
import com.jbs.cardgame.entity.Card;
import com.jbs.cardgame.entity.board.BoardSlot;
import com.jbs.cardgame.screen.utility.Point;
import com.jbs.cardgame.screen.utility.Rect;

public class Mouse {
    public boolean leftClick;

    public Rect rect;

    public BoardSlot hoverBoardSlot;
    public boolean hoverHandCheck;
    public Card hoverHandCard;
    public Card selectedHandCard;

    public Mouse() {
        leftClick = false;

        rect = new Rect(new Point(0, 0), 1, 1);

        hoverBoardSlot = null;
        hoverHandCheck = false;
        hoverHandCard = null;
        selectedHandCard = null;
    }

    public void updateLocation() {
        rect.location.x = (int) (Gdx.input.getX() * Settings.SIZE_RATIO_X);
        rect.location.y = (int) ((Gdx.graphics.getBackBufferHeight() - Gdx.input.getY()) * Settings.SIZE_RATIO_Y);
    }
}
