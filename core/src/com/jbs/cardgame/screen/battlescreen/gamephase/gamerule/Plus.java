package com.jbs.cardgame.screen.battlescreen.gamephase.gamerule;

import java.util.ArrayList;

import com.jbs.cardgame.entity.Card;
import com.jbs.cardgame.entity.board.BoardSlot;
import com.jbs.cardgame.entity.board.GameBoard;
import com.jbs.cardgame.screen.battlescreen.gamephase.FlipChecks;

public class Plus extends GameRule {
    public void checkSurroundingCards(FlipChecks flipCheckGamePhase, GameBoard gameBoard, BoardSlot centerBoardSlot) {
        ArrayList<ArrayList<Card>> plusCountCardList = new ArrayList<ArrayList<Card>>();
        int highestCountIndex = -1;

        for(int i = 0; i < 4; i++) {
            ArrayList<Card> countCardList = new ArrayList<Card>();
            plusCountCardList.add(countCardList);

            BoardSlot adjacentBoardSlot = centerBoardSlot.getAdjacentBoardSlot(gameBoard, i);
            if(adjacentBoardSlot != null && adjacentBoardSlot.card != null
            && !flipCheckGamePhase.flipCardList.contains(adjacentBoardSlot.card)) {
                int attackPower = centerBoardSlot.card.getPowerRating(i, centerBoardSlot);
                int defensePower = adjacentBoardSlot.card.getPowerRating(Card.getOppositeDirectionIndex(i), adjacentBoardSlot);
                int targetSum = attackPower + defensePower;

                for(int ii = 0; ii < 4; ii++) {
                    BoardSlot innerAdjacentBoardSlot = centerBoardSlot.getAdjacentBoardSlot(gameBoard, ii);
                    if(innerAdjacentBoardSlot != null && innerAdjacentBoardSlot.card != null
                    && !flipCheckGamePhase.flipCardList.contains(innerAdjacentBoardSlot.card)) {
                        int innerAttackPower = centerBoardSlot.card.getPowerRating(ii, centerBoardSlot);
                        int innerDefensePower = innerAdjacentBoardSlot.card.getPowerRating(Card.getOppositeDirectionIndex(ii), innerAdjacentBoardSlot);
                        int innerSum = innerAttackPower + innerDefensePower;

                        if(innerSum == targetSum) {
                            plusCountCardList.get(i).add(innerAdjacentBoardSlot.card);
                        }
                    }
                }
            }

            // Mob Owns A Card Check //
            for(Card plusCard : plusCountCardList.get(i)) {
                if(plusCard.currentOwnerInBattle != centerBoardSlot.card.currentOwnerInBattle) {
                    if(highestCountIndex == -1
                    || plusCountCardList.get(i).size() > plusCountCardList.get(highestCountIndex).size()) {
                        highestCountIndex = i;
                    }
                    break;
                }
            }
        }

        if(highestCountIndex != -1
        && plusCountCardList.get(highestCountIndex).size() > 1) {
            flipCheckGamePhase.activatedRuleList.add(toString());
            for(Card flipCard : plusCountCardList.get(highestCountIndex)) {
                if(flipCard.currentOwnerInBattle == flipCheckGamePhase.attackingPlayer) {
                    flipCard.isCurrentOwner = true;
                } else {
                    flipCard.isCurrentOwner = false;
                }
                flipCard.gameRuleFlip = true;
                flipCheckGamePhase.flipCardList.add(flipCard);
            }
        }
    }
}
