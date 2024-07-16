package com.jbs.cardgame.entity.battleplayer;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
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

    public void updateHandLocations() {
        for(int i = 0; i < hand.size(); i++) {
            int handWidth = (Card.WIDTH * 2) + (HAND_OVERLAP_WIDTH * (hand.size() - 1));

            Card targetCard = hand.get(i);
            targetCard.handLocation.x = (Gdx.graphics.getWidth() / 2) - (handWidth / 2) + (HAND_OVERLAP_WIDTH * i);
            targetCard.handLocation.y = HAND_Y_OFFSET;
        }
    }

    public static Point getPlayerScreenLocation(int battlePlayerIndex, int battlePlayerListSize) {
        int locationX = 0;
        int locationY = 0;

        if(battlePlayerIndex == 0) {
            locationX = (Gdx.graphics.getWidth() / 2) - Card.WIDTH;
            locationY = -(Card.HEIGHT * 2);
        } else {
            if(battlePlayerListSize == 2) {
                locationX = (Gdx.graphics.getWidth() / 2) - Card.WIDTH;
                locationY = Gdx.graphics.getHeight();
            } else {
                if((battlePlayerIndex - 1) % 2 == 0) {
                    locationX = Gdx.graphics.getWidth();
                    locationY = (Gdx.graphics.getHeight() / 2) - Card.HEIGHT;
                } else {
                    locationX = -(Card.WIDTH * 2);
                    locationY = (Gdx.graphics.getHeight() / 2) - Card.HEIGHT;
                }
            }
        }
        
        return new Point(locationX, locationY);
    }
}
