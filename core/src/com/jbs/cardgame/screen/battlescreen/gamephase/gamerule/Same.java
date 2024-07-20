package com.jbs.cardgame.screen.battlescreen.gamephase.gamerule;

import java.util.ArrayList;

import com.jbs.cardgame.entity.Card;
import com.jbs.cardgame.entity.board.BoardSlot;
import com.jbs.cardgame.entity.board.GameBoard;
import com.jbs.cardgame.screen.battlescreen.gamephase.FlipChecks;

public class Same extends GameRule {
    public void checkSurroundingCards(FlipChecks flipCheckGamePhase, GameBoard gameBoard, BoardSlot centerBoardSlot) {
        ArrayList<Card> sameCardList = new ArrayList<>();
        
        for(int i = 0; i < 4; i++) {
            BoardSlot adjacentBoardSlot = centerBoardSlot.getAdjacentBoardSlot(gameBoard, i);
            if(adjacentBoardSlot != null && adjacentBoardSlot.card != null) {
                int attackPower = centerBoardSlot.card.getPowerRating(i, centerBoardSlot);
                int defensePower = adjacentBoardSlot.card.getPowerRating(Card.getOppositeDirectionIndex(i), adjacentBoardSlot);

                if(adjacentBoardSlot.card.currentOwnerInBattle != centerBoardSlot.card.currentOwnerInBattle
                && !flipCheckGamePhase.flipCardList.contains(adjacentBoardSlot.card)
                && attackPower == defensePower) {
                    sameCardList.add(adjacentBoardSlot.card);
                }
            }
        }

        if(sameCardList.size() > 1) {
            flipCheckGamePhase.activatedRuleList.add(toString());
            for(Card sameCard : sameCardList) {
                if(sameCard.currentOwnerInBattle == flipCheckGamePhase.attackingPlayer) {
                    sameCard.isCurrentOwner = true;
                } else {
                    sameCard.isCurrentOwner = false;
                }
                sameCard.gameRuleFlip = true;
                flipCheckGamePhase.flipCardList.add(sameCard);
            }
        }
    }
}
