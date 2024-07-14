package com.jbs.cardgame.screen;

public class Rect {
    public int x;
    public int y;
    public int width;
    public int height;

    public Rect(int width, int height) {
        x = 0;
        y = 0;
        this.width = width;
        this.height = height;
    }

    public Rect(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}