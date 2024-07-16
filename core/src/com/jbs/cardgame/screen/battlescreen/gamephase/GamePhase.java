package com.jbs.cardgame.screen.battlescreen.gamephase;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.jbs.cardgame.component.Mouse;
import com.jbs.cardgame.entity.battleplayer.BattlePlayer;
import com.jbs.cardgame.entity.board.GameBoard;

public class GamePhase {
    public String update(ArrayList<BattlePlayer> battlePlayerList, BattlePlayer currentBattlePlayer) {return "End GamePhase";}
    public void render(OrthographicCamera camera, OrthographicCamera cameraTop, ShapeRenderer shapeRenderer, Mouse mouse, GameBoard gameBoard, ArrayList<BattlePlayer> battlePlayerList, BattlePlayer currenBattlePlayer) {}
}
