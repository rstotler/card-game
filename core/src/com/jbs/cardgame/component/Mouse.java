package com.jbs.cardgame.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.jbs.cardgame.Settings;
import com.jbs.cardgame.entity.Card;
import com.jbs.cardgame.entity.board.BoardSlot;
import com.jbs.cardgame.screen.utility.Point;
import com.jbs.cardgame.screen.utility.Rect;

public class Mouse {
    public boolean leftClick;

    public Rect rect;

    public BoardSlot selectedBoardSlot;
    public BoardSlot hoverBoardSlot;

    public Card selectedHandCard;
    public Card hoverHandCard;
    public boolean hoverHandCheck;

    public boolean defaultClickLevel;

    public Mouse() {
        leftClick = false;

        rect = new Rect(new Point(0, 0), 1, 1);

        selectedBoardSlot = null;
        hoverBoardSlot = null;

        selectedHandCard = null;
        hoverHandCard = null;
        hoverHandCheck = false;

        defaultClickLevel = false;
    }

    public void updateLocation() {
        rect.location.x = (int) (Gdx.input.getX() * Settings.SIZE_RATIO_X);
        rect.location.y = (int) ((Gdx.graphics.getBackBufferHeight() - Gdx.input.getY()) * Settings.SIZE_RATIO_Y);
    }

    public Point getScreenLocation(OrthographicCamera camera) {
        int mouseX = (int) (rect.location.x - (Settings.SCREEN_WIDTH / 2) + ((camera.position.x * 2) / camera.zoom));
        int mouseY = (int) (rect.location.y - (Settings.SCREEN_HEIGHT / 2) + ((camera.position.y * 2) / camera.zoom));
        return new Point(mouseX, mouseY);
    }

    public BoardSlot getHoverBoardSlot(OrthographicCamera camera, BoardSlot[][] boardSlot) {
        Point mouseLocation = getScreenLocation(camera);
        int slotX = (int) (mouseLocation.x / (((Card.WIDTH + (BoardSlot.PADDING * 2)) * 2) / camera.zoom));
        int slotY = (int) (mouseLocation.y / (((Card.HEIGHT + (BoardSlot.PADDING * 2)) * 2) / camera.zoom));

        if(slotX >= 0 && slotX < boardSlot.length
        && slotY >= 0 && slotY < boardSlot[0].length) {
            return boardSlot[slotX][slotY];
        }

        return null;
    }
}
