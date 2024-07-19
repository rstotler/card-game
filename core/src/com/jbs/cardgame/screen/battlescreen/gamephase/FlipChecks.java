package com.jbs.cardgame.screen.battlescreen.gamephase;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jbs.cardgame.Settings;
import com.jbs.cardgame.component.Mouse;
import com.jbs.cardgame.entity.Card;
import com.jbs.cardgame.entity.battleplayer.BattlePlayer;
import com.jbs.cardgame.entity.board.BoardSlot;
import com.jbs.cardgame.entity.board.GameBoard;
import com.jbs.cardgame.screen.ImageManager;
import com.jbs.cardgame.screen.battlescreen.BattleScreen;

public class FlipChecks extends GamePhase {
    public ArrayList<Card> flipCardList;
    public BattlePlayer attackingPlayer;

    public String currentState;
    public float currentStatePercent;

    public FlipChecks(BattlePlayer attackingPlayer) {
        super();

        nextGamePhase = new PlayCard();

        flipCardList = new ArrayList<>();
        this.attackingPlayer = attackingPlayer;

        currentState = "Flip Stage 1";
        currentStatePercent = 1.0f;
    }

    public static void initFlipSurroundingCards(BattleScreen battleScreen, BoardSlot centerBoardSlot) {
        Card centerCard = centerBoardSlot.card;

        for(int i = 0; i < 4; i++) {
            BoardSlot targetBoardSlot = centerBoardSlot.getAdjacentBoardSlot(battleScreen.gameBoard, i);
            if(targetBoardSlot != null && targetBoardSlot.card != null
            && centerCard.currentOwnerInBattle != targetBoardSlot.card.currentOwnerInBattle
            && centerCard.isStrongerThan(targetBoardSlot.card, i, centerBoardSlot, targetBoardSlot)) {
                if(battleScreen.gamePhase != null
                && battleScreen.gamePhase.toString().equals("FlipChecks")) {
                    ((FlipChecks) (battleScreen.gamePhase)).flipCardList.add(targetBoardSlot.card);
                }
            }
        }
    }
    
    public String update(BattleScreen battleScreen) {
        if(currentState.equals("Flip Stage 1")) {
            currentStatePercent -= .08;
            if(currentStatePercent <= 0) {
                currentStatePercent = 0.0f;
                currentState = "Flip Stage 2";
                
                for(Card flippingCard : flipCardList) {
                    flippingCard.currentOwnerInBattle = attackingPlayer;
                }
            }
        } else if(currentState.equals("Flip Stage 2")) {
            currentStatePercent += .08;
            if(currentStatePercent >= 1) {
                currentStatePercent = 1.0f;
                return "End GamePhase";
            }
        }

        return "";
    }

    public void render(OrthographicCamera camera, OrthographicCamera cameraTop, SpriteBatch spriteBatch, ImageManager imageManager, Mouse mouse, GameBoard gameBoard, ArrayList<BattlePlayer> battlePlayerList, BattlePlayer currentBattlePlayer) {
    }

    public void renderFlippingCard(SpriteBatch spriteBatch, BoardSlot targetBoardSlot) {
        float locationModX = (Card.WIDTH / 2.0f) * (1.0f - currentStatePercent);
        float locationModY = 10 * (1.0f - currentStatePercent);
        spriteBatch.draw(Card.frameBufferCard.getColorBufferTexture(), targetBoardSlot.card.currentLocation.x + locationModX, targetBoardSlot.card.currentLocation.y + locationModY, Settings.SCREEN_WIDTH * currentStatePercent, Settings.SCREEN_HEIGHT, 0, 0, 1, 1);
    }
}
