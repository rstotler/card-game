package com.jbs.cardgame.screen.battlescreen.gamephase;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jbs.cardgame.component.Mouse;
import com.jbs.cardgame.entity.Card;
import com.jbs.cardgame.entity.battleplayer.BattlePlayer;
import com.jbs.cardgame.entity.board.GameBoard;
import com.jbs.cardgame.screen.ImageManager;
import com.jbs.cardgame.screen.battlescreen.BattleScreen;
import com.jbs.cardgame.screen.utility.Point;

public class PlayCard extends GamePhase {
    public PlayCard() {
        super();
    }
    
    public String update(BattleScreen battleScreen) {

        // Update Mob Turn //
        if(battleScreen.currentTurnBattlePlayer != battleScreen.battlePlayerList.get(0)) {
            Point targetSlot = null;
            Card selectedHandCard = null;
            boolean breakCheck = false;
            for(int y = 0; y < battleScreen.gameBoard.boardSlot[0].length; y++) {
                for(int x = 0; x < battleScreen.gameBoard.boardSlot.length; x++) {
                    if(battleScreen.gameBoard.boardSlot[x][y].isPlayable
                    && battleScreen.gameBoard.boardSlot[x][y].card == null) {
                        targetSlot = new Point(x, y);
                        breakCheck = true;
                        break;
                    }
                }
                if(breakCheck) {
                    break;
                }
            }
            if(battleScreen.currentTurnBattlePlayer.hand.size() > 0) {
                selectedHandCard = battleScreen.currentTurnBattlePlayer.hand.get(0);
            }

            if(targetSlot != null
            && battleScreen.currentTurnBattlePlayer.placeCardOnGameBoard(battleScreen.gamePhase, battleScreen.gameBoard, selectedHandCard, targetSlot, battleScreen.currentTurnBattlePlayer, false)) {
                battleScreen.currentTurnBattlePlayer.removeCardFromHand(selectedHandCard);

                battleScreen.gamePhase = new FlipChecks(battleScreen.currentTurnBattlePlayer);
                FlipChecks.initFlipSurroundingCards(battleScreen, battleScreen.gameBoard.boardSlot[targetSlot.x][targetSlot.y]);
            }
        }

        return "";
    }

    public void render(OrthographicCamera camera, OrthographicCamera cameraTop, SpriteBatch spriteBatch, ImageManager imageManager, Mouse mouse, GameBoard gameBoard, ArrayList<BattlePlayer> battlePlayerList, BattlePlayer currentBattlePlayer) {
    }
}
