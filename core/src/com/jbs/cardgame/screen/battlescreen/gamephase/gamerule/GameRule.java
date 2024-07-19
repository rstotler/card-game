package com.jbs.cardgame.screen.battlescreen.gamephase.gamerule;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.jbs.cardgame.entity.board.BoardSlot;
import com.jbs.cardgame.entity.board.GameBoard;
import com.jbs.cardgame.screen.battlescreen.gamephase.FlipChecks;

public class GameRule {
    public static BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/Code_New_Roman_72.fnt"), Gdx.files.internal("fonts/Code_New_Roman_72.png"), false);
    public static GlyphLayout fontLayout = new GlyphLayout();

    public String toString() {
        return getClass().toString().substring(getClass().toString().lastIndexOf(".") + 1);
    }

    public void checkSurroundingCards(FlipChecks flipCheckGamePhase, GameBoard gameBoard, BoardSlot centerBoardSlot) {
    }
}
