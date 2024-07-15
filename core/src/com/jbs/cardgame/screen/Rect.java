package com.jbs.cardgame.screen;

public class Rect {
    public Point location;
    public int width;
    public int height;

    public Rect(int width, int height) {
        location = new Point(0, 0);
        this.width = width;
        this.height = height;
    }

    public Rect(Point location, int width, int height) {
        this.location = location;
        this.width = width;
        this.height = height;
    }
}
