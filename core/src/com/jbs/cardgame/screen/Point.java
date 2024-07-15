package com.jbs.cardgame.screen;

public class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Point getPointAlongLine(Point startPoint, Point endPoint, float percent) {
        int diffX = endPoint.x - startPoint.x;
        int diffY = endPoint.y - startPoint.y;
        int xMod = (int) (diffX * percent);
        int yMod = (int) (diffY * percent);

        return new Point(startPoint.x + xMod, startPoint.y + yMod);
    }
}
