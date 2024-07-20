package com.jbs.cardgame.screen.battlescreen.gamephase;

import java.util.*;

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
import com.jbs.cardgame.screen.battlescreen.gamephase.gamerule.GameRule;

/* 
 * Card Flip Rules:
 * Same - Flips cards when 2 sides are the same.
 * Plus - Flips cards when 2 sides equal the same sum.
 * Minus - Flips cards when 2 sides equal the same difference.
 * Multiply - Flips cards when 2 sides equal the same product.
 * Divide - Flips cards when 2 sides equal the same quotient.
 * Combo - Cascade flips all adjacent cards AFTER one of the above rules are activated.
 */

public class FlipChecks extends GamePhase {
    public ArrayList<Card> flipCardList;
    public ArrayList<Card> comboFlipList;
    public BattlePlayer attackingPlayer;

    public ArrayList<String> activatedRuleList;

    public String currentState;
    public float currentStatePercent;

    public FlipChecks(BattlePlayer attackingPlayer) {
        super();

        nextGamePhase = new PlayCard();

        flipCardList = new ArrayList<>();
        comboFlipList = new ArrayList<>();
        this.attackingPlayer = attackingPlayer;

        activatedRuleList = new ArrayList<>();

        currentState = "";
        currentStatePercent = 1.0f;
    }

    public static void initFlipSurroundingCards(BattleScreen battleScreen, BoardSlot centerBoardSlot) {
        if(centerBoardSlot.card != null
        && battleScreen.gamePhase != null
        && battleScreen.gamePhase.toString().equals("FlipChecks")) {
            FlipChecks gamePhase = ((FlipChecks) (battleScreen.gamePhase));
            
            // Rule Checks //
            for(GameRule gameRule : battleScreen.gameRuleList) {
                gameRule.checkSurroundingCards(gamePhase, battleScreen.gameBoard, centerBoardSlot);
            }

            // Flip Checks //
            Card centerCard = centerBoardSlot.card;
            for(int i = 0; i < 4; i++) {
                BoardSlot adjacentBoardSlot = centerBoardSlot.getAdjacentBoardSlot(battleScreen.gameBoard, i);
                if(adjacentBoardSlot != null && adjacentBoardSlot.card != null
                && centerCard.currentOwnerInBattle != adjacentBoardSlot.card.currentOwnerInBattle
                && centerCard.isStrongerThan(adjacentBoardSlot.card, i, centerBoardSlot, adjacentBoardSlot)
                && !gamePhase.flipCardList.contains(adjacentBoardSlot.card)) {
                    gamePhase.flipCardList.add(adjacentBoardSlot.card);
                }
            }

            // Set Current State //
            if(gamePhase.flipCardList.size() == 0) {
                gamePhase.currentState = "Flip Cards Stage 2";
                gamePhase.currentStatePercent = 1.0f;
            } else if(gamePhase.activatedRuleList.size() > 0) {
                gamePhase.currentState = "Display Rule Stage 1";
                gamePhase.currentStatePercent = 0.0f;
            } else {
                gamePhase.currentState = "Flip Cards Stage 1";
                gamePhase.currentStatePercent = 1.0f;
            }
        }
    }
    
    public void initComboFlips(BattleScreen battleScreen) {
        ArrayList<Card> targetCardList = null;
        if(comboFlipList.size() > 0) {
            targetCardList = comboFlipList;
        } else {
            targetCardList = flipCardList;
        }
        
        // Remove Cards That Already Belonged To AttackingPlayer    //
        // And Remove Cards That Did Not Get Flipped Via A GameRule //
        if(comboFlipList.size() == 0) {
            ArrayList<Card> deleteCardList = new ArrayList<>();
            for(Card flipCard : flipCardList) {
                if(flipCard.isCurrentOwner
                || !flipCard.gameRuleFlip) {
                    deleteCardList.add(flipCard);
                }
            }
            for(Card deleteCard : deleteCardList) {
                flipCardList.remove(deleteCard);
            }
        }

        // Initiate New Card Flips //
        ArrayList<Card> newFlipList = new ArrayList<>();
        for(Card flipCard : targetCardList) {
            for(int i = 0; i < 4; i++) {
                if(flipCard.boardSlot != null) {
                    BoardSlot adjacentBoardSlot = flipCard.boardSlot.getAdjacentBoardSlot(battleScreen.gameBoard, i);
                    if(adjacentBoardSlot != null && adjacentBoardSlot.card != null
                    && adjacentBoardSlot.card.currentOwnerInBattle != attackingPlayer) {
                        int attackPower = flipCard.boardSlot.card.getPowerRating(i, flipCard.boardSlot);
                        int defensePower = adjacentBoardSlot.card.getPowerRating(Card.getOppositeDirectionIndex(i), adjacentBoardSlot);
                        if(attackPower > defensePower) {
                            newFlipList.add(adjacentBoardSlot.card);
                        }
                    }
                }
            }
        }
        targetCardList.clear();

        if(newFlipList.size() > 0) {
            comboFlipList.addAll(newFlipList);
            currentState = "Flip Cards Stage 1";
            currentStatePercent = 1.0f;
        }
    }

    public String update(BattleScreen battleScreen) {
        if(currentState.equals("Display Rule Stage 1")) {
            currentStatePercent += .055;
            if(currentStatePercent >= 1) {
                currentState = "Display Rule Stage 2";
                currentStatePercent = 0.0f;
            }
        }

        else if(currentState.equals("Display Rule Stage 2")) {
            currentStatePercent += .05;
            if(currentStatePercent >= 1) {
                currentState = "Display Rule Stage 3";
                currentStatePercent = 0.0f;
            }
        }

        else if(currentState.equals("Display Rule Stage 3")) {
            currentStatePercent += .055;
            if(currentStatePercent >= 1) {
                currentState = "Flip Cards Stage 1";
                currentStatePercent = 1.0f;
            }
        }
        
        else if(currentState.equals("Flip Cards Stage 1")) {
            currentStatePercent -= .08;
            if(currentStatePercent <= 0) {
                currentStatePercent = 0.0f;
                currentState = "Flip Cards Stage 2";
                
                for(Card flippingCard : flipCardList) {
                    if(flippingCard.currentOwnerInBattle != attackingPlayer) {
                        flippingCard.currentOwnerInBattle = attackingPlayer;
                    }
                }
                for(Card flippingCard : comboFlipList) {
                    if(flippingCard.currentOwnerInBattle != attackingPlayer) {
                        flippingCard.currentOwnerInBattle = attackingPlayer;
                    }
                }
            }
        }
        
        else if(currentState.equals("Flip Cards Stage 2")) {
            currentStatePercent += .08;
            if(currentStatePercent >= 1) {
                currentStatePercent = 1.0f;

                for(Card flippingCard : flipCardList) {
                    if(flippingCard.originalOwnerInBattle != attackingPlayer) {
                        flippingCard.originalOwnerInBattle = attackingPlayer;
                    }
                }
                for(Card flippingCard : comboFlipList) {
                    if(flippingCard.originalOwnerInBattle != attackingPlayer) {
                        flippingCard.originalOwnerInBattle = attackingPlayer;
                    }
                }

                boolean comboCheck = false;
                for(GameRule gameRule : battleScreen.gameRuleList) {
                    if(gameRule.toString().equals("Combo")) {
                        comboCheck = true;
                        break;
                    }
                }

                if((flipCardList.size() > 0
                && activatedRuleList.size() > 0  // If Cards Were Flipped Using A GameRule (Same, Plus, Etc..)
                && comboCheck)                   // AND If Combo GameRule Is Active
                || comboFlipList.size() > 0) {
                    initComboFlips(battleScreen);
                    if(comboFlipList.size() == 0) {
                        return "End GamePhase";
                    }
                } else {
                    return "End GamePhase";
                }
            }
        }

        return "";
    }

    public void render(OrthographicCamera camera, OrthographicCamera cameraTop, SpriteBatch spriteBatch, ImageManager imageManager, Mouse mouse, GameBoard gameBoard, ArrayList<BattlePlayer> battlePlayerList, BattlePlayer currentBattlePlayer) {
        if(currentState.contains("Display Rule")) {
            spriteBatch.setProjectionMatrix(cameraTop.combined);
            spriteBatch.begin();

            String displayRuleString = "";
            for(int i = 0; i < activatedRuleList.size(); i++) {
                String activeRuleString = activatedRuleList.get(i);
                if(i == 0) {
                    displayRuleString = displayRuleString + activeRuleString;
                } else {
                    displayRuleString = displayRuleString + " + " + activeRuleString;
                }
            }
            GameRule.fontLayout.setText(GameRule.font, displayRuleString);
            
            float displayRuleCenterX = (Settings.SCREEN_WIDTH / 2) - (GameRule.fontLayout.width / 2); // Default - Center
            float displayRuleX = displayRuleCenterX;
            if(currentState.equals("Display Rule Stage 1")) {
                displayRuleX = Settings.SCREEN_WIDTH - ((Settings.SCREEN_WIDTH - displayRuleCenterX) * currentStatePercent);
            } else if(currentState.equals("Display Rule Stage 2")) {
                displayRuleX = displayRuleCenterX;
            } else if(currentState.equals("Display Rule Stage 3")) {
                displayRuleX = displayRuleCenterX - ((displayRuleCenterX + GameRule.fontLayout.width) * currentStatePercent);
            }

            float displayRuleY = ((Settings.SCREEN_HEIGHT + (GameRule.fontLayout.height / 2)) / 2) + 7;
            GameRule.font.draw(spriteBatch, displayRuleString, displayRuleX, displayRuleY);
            
            spriteBatch.end();
        }
    }

    public void renderFlippingCard(SpriteBatch spriteBatch, BoardSlot targetBoardSlot) {
        float locationModX = 0.0f;
        float locationModY = 0.0f;
        float widthPercent = 1.0f;
        if(currentState.contains("Flip Cards")) {
            locationModX = (Card.WIDTH / 2.0f) * (1.0f - currentStatePercent);
            locationModY = 10 * (1.0f - currentStatePercent);
            widthPercent = currentStatePercent;
        }
        
        spriteBatch.draw(Card.frameBufferCard.getColorBufferTexture(), targetBoardSlot.card.currentLocation.x + locationModX, targetBoardSlot.card.currentLocation.y + locationModY, Settings.SCREEN_WIDTH * widthPercent, Settings.SCREEN_HEIGHT, 0, 0, 1, 1);
    }
}
