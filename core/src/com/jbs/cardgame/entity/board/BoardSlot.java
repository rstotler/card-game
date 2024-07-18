package com.jbs.cardgame.entity.board;

import java.util.Random;

import com.jbs.cardgame.entity.Card;
import com.jbs.cardgame.screen.utility.Point;

public class BoardSlot {
    public static final int PADDING = 4;

    public Point location;
    public boolean isPlayable;

    public Card card;

    public int color;

    public BoardSlot(Point location) {
        this.location = location;
        isPlayable = true;
        
        card = null;

        color = new Random().nextInt(50) + 10;
    }
}
