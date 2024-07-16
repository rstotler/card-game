package com.jbs.cardgame.entity;

import java.util.Random;

import com.jbs.cardgame.screen.Point;

public class Card {
    public static final int WIDTH = 80;
    public static final int HEIGHT = 120;

    public Point handLocation;
    public Point selectedCardOffset;

    public int color;

    public Card() {
        handLocation = new Point(0, 0);
        selectedCardOffset = new Point(0, 0);

        color = new Random().nextInt(35) + 15;
    }
}
