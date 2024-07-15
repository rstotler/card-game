package com.jbs.cardgame.entity.board;

import java.util.Random;

import com.jbs.cardgame.screen.Point;

public class BoardSlot {
    public Point location;

    public int color;

    public BoardSlot(Point location) {
        this.location = location;
        
        color = new Random().nextInt(50) + 10;
    }
}
