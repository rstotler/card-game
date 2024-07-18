package com.jbs.cardgame.screen.battlescreen.gamephase;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jbs.cardgame.component.Mouse;
import com.jbs.cardgame.entity.battleplayer.BattlePlayer;
import com.jbs.cardgame.entity.board.GameBoard;
import com.jbs.cardgame.screen.ImageManager;

public class FlipChecks extends GamePhase {
    public FlipChecks() {
        super();
    }
    
    public String update(ArrayList<BattlePlayer> battlePlayerList, BattlePlayer currentTurnBattlePlayer) {
        return "";
    }

    public void render(OrthographicCamera camera, OrthographicCamera cameraTop, SpriteBatch spriteBatch, ImageManager imageManager, Mouse mouse, GameBoard gameBoard, ArrayList<BattlePlayer> battlePlayerList, BattlePlayer currentBattlePlayer) {
        spriteBatch.setProjectionMatrix(cameraTop.combined);
        spriteBatch.begin();

        spriteBatch.end();
    }

}
