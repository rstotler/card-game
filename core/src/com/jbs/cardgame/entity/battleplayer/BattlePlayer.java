package com.jbs.cardgame.entity.battleplayer;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.jbs.cardgame.entity.Card;

public class BattlePlayer {
    public static int HAND_OVERLAP_WIDTH = 50;

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

    public void drawCard() {
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
            targetCard.handLocation.y = -90;
        }
    }
}
