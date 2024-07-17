package com.jbs.cardgame.entity.battleplayer;

import java.util.ArrayList;

import com.jbs.cardgame.Settings;
import com.jbs.cardgame.entity.Card;
import com.jbs.cardgame.screen.Point;

public class BattlePlayer {
    public static final int HAND_OVERLAP_WIDTH = 50;
    public static final int HAND_Y_OFFSET = -90;

    public boolean isPlayer;

    public ArrayList<Card> deck; // Index 0 - Top Of Deck
    public ArrayList<Card> hand;

    public BattlePlayer(boolean isPlayer) {
        this.isPlayer = isPlayer;
        
        deck = loadDebugDeck();
        hand = new ArrayList<>();
    }

    public ArrayList<Card> loadDebugDeck() {
        ArrayList<Card> debugDeck = new ArrayList<>();
        for(int i = 0; i < 20; i++) {
            debugDeck.add(new Card());
        }

        return debugDeck;
    }

    public void drawCardToHand() {
        if(deck.size() > 0) {
            hand.add(deck.get(0));
            deck.remove(0);

            updateHandLocations();
        }
    }

    public void removeCardFromHand(Card targetCard) {
        for(int i = 0; i < hand.size(); i++) {
            if(hand.get(i) == targetCard) {
                hand.remove(i);
                break;
            }
        }
    }

    public void updateHandLocations() {
        for(int i = 0; i < hand.size(); i++) {
            int handWidth = (Card.WIDTH * 2) + (HAND_OVERLAP_WIDTH * (hand.size() - 1));

            Card targetCard = hand.get(i);
            targetCard.currentLocation.x = (Settings.SCREEN_WIDTH / 2) - (handWidth / 2) + (HAND_OVERLAP_WIDTH * i);
            targetCard.targetLocation.x = targetCard.currentLocation.x;
            targetCard.currentLocation.y = HAND_Y_OFFSET;
            targetCard.targetLocation.y = targetCard.currentLocation.y;
        }
    }

    public static Point getPlayerScreenLocation(int battlePlayerIndex, int battlePlayerListSize) {
        int locationX = 0;
        int locationY = 0;

        if(battlePlayerIndex == 0) {
            locationX = (Settings.SCREEN_WIDTH / 2) - Card.WIDTH;
            locationY = -(Card.HEIGHT * 2);
        } else {
            if(battlePlayerListSize == 2) {
                locationX = (Settings.SCREEN_WIDTH / 2) - Card.WIDTH;
                locationY = Settings.SCREEN_HEIGHT;
            } else {
                if((battlePlayerIndex - 1) % 2 == 0) {
                    locationX = Settings.SCREEN_WIDTH;
                    locationY = (Settings.SCREEN_HEIGHT / 2) - Card.HEIGHT;
                } else {
                    locationX = -(Card.WIDTH * 2);
                    locationY = (Settings.SCREEN_HEIGHT / 2) - Card.HEIGHT;
                }
            }
        }
        
        return new Point(locationX, locationY);
    }
}
