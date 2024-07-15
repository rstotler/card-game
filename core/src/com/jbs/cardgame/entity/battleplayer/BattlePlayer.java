package com.jbs.cardgame.entity.battleplayer;

import java.util.ArrayList;

import com.jbs.cardgame.entity.Card;

public class BattlePlayer {
    boolean isPlayer;

    public ArrayList<Card> hand;

    public BattlePlayer(boolean isPlayer) {
        this.isPlayer = isPlayer;
        
        hand = new ArrayList<>();
    }
}
