package com.jbs.cardgame.entity.board;

import java.util.Random;

import com.jbs.cardgame.entity.Card;
import com.jbs.cardgame.screen.utility.Point;

public class BoardSlot {
    public static final int PADDING = 4;

    public Point location;
    public boolean isPlayable;

    public Card card;
    public String element;

    public int color;

    public BoardSlot(Point location) {
        this.location = location;
        isPlayable = true;
        
        card = null;
        element = "";

        color = new Random().nextInt(50) + 10;
    }

    public BoardSlot getAdjacentBoardSlot(GameBoard gameBoard, int directionIndex) {
        int targetSlotX = location.x;
        int targetSlotY = location.y;
        if(directionIndex == 0) {
            targetSlotY += 1;
        } else if(directionIndex == 1) {
            targetSlotX += 1;
        } else if(directionIndex == 2) {
            targetSlotY -= 1;
        } else if(directionIndex == 3) {
            targetSlotX -= 1;
        }

        if(targetSlotX >= 0 && targetSlotX < gameBoard.boardSlot.length
        && targetSlotY >= 0 && targetSlotY < gameBoard.boardSlot[0].length) {
            return gameBoard.boardSlot[targetSlotX][targetSlotY];
        }

        return null;
    }

    public Point getScreenLocation() {
        int locationX = ((Card.WIDTH + (PADDING * 2)) * location.x) + PADDING;
        int locationY = ((Card.HEIGHT + (PADDING * 2)) * location.y) + PADDING;
        return new Point(locationX, locationY);
    }
}
