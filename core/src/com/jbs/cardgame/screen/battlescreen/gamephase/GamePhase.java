package com.jbs.cardgame.screen.battlescreen.gamephase;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jbs.cardgame.component.Mouse;
import com.jbs.cardgame.entity.battleplayer.BattlePlayer;
import com.jbs.cardgame.entity.board.GameBoard;
import com.jbs.cardgame.screen.ImageManager;
import com.jbs.cardgame.screen.battlescreen.BattleScreen;

public class GamePhase {
    public GamePhase nextGamePhase;

    public GamePhase() {
        nextGamePhase = null;
    }

    public String toString() {
        return getClass().toString().substring(getClass().toString().lastIndexOf(".") + 1);
    }

    public String update(BattleScreen battleScreen) {return "End GamePhase";}
    public void render(OrthographicCamera camera, OrthographicCamera cameraTop, SpriteBatch spriteBatch, ImageManager imageManager, Mouse mouse, GameBoard gameBoard, ArrayList<BattlePlayer> battlePlayerList, BattlePlayer currenBattlePlayer) {}
}
