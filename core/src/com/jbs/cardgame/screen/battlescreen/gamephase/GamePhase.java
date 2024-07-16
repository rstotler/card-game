package com.jbs.cardgame.screen.battlescreen.gamephase;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.jbs.cardgame.component.Mouse;
import com.jbs.cardgame.entity.battleplayer.BattlePlayer;
import com.jbs.cardgame.entity.board.GameBoard;

public class GamePhase {
    public void update(BattlePlayer currentBattlePlayer) {}
    public void render(OrthographicCamera camera, OrthographicCamera cameraTop, ShapeRenderer shapeRenderer, Mouse mouse, GameBoard gameBoard, ArrayList<BattlePlayer> battlePlayerList) {}
}
