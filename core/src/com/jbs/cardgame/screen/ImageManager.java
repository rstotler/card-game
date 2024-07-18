package com.jbs.cardgame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ImageManager {
    public Texture cardBackTexture;
    public Texture cardBorderTexture;

    public ShaderProgram shaderProgramColorChannel;

    public ImageManager() {
        cardBackTexture = new Texture("images/cards/Back.png");
        cardBorderTexture = new Texture("images/cards/Border.png");

        String vertexShader = Gdx.files.internal("shaders/vertex.glsl").readString();
        String fragmentShader = Gdx.files.internal("shaders/color_channel.glsl").readString();
        shaderProgramColorChannel = new ShaderProgram(vertexShader, fragmentShader);
    }

    public void dispose() {
        cardBackTexture.dispose();
        cardBorderTexture.dispose();
    }
}
