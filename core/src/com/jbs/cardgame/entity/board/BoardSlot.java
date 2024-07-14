package com.jbs.cardgame.entity.board;

import java.util.Random;

public class BoardSlot {
    public int x;
    public int y;
    
    public int color;

    public BoardSlot(int x, int y) {
        this.x = x;
        this.y = y;

        color = new Random().nextInt(50) + 10;
    }
}
