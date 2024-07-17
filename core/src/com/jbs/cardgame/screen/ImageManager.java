package com.jbs.cardgame.screen;

import com.badlogic.gdx.graphics.Texture;

public class ImageManager {
    public Texture cardBackTexture;

    public ImageManager() {
        cardBackTexture = new Texture("images/cards/Back.png");
    }

    public void dispose() {
        cardBackTexture.dispose();
    }
}
