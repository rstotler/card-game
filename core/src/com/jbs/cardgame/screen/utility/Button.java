package com.jbs.cardgame.screen.utility;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jbs.cardgame.Settings;

public class Button {
    public static GlyphLayout fontLayout = new GlyphLayout();

    public Rect rect;
    public Point displayMod;

    public String label;
    public BitmapFont font;
    public Rect textSize;
    
    public Button(String label, BitmapFont font) {
        Point location = new Point(Settings.SCREEN_WIDTH / 2, Settings.SCREEN_HEIGHT / 2);
        
        displayMod = new Point(0, 0);
        textSize = new Rect(0, 0);

        if(!label.isEmpty() && font != null) {
            fontLayout.setText(font, label);
            textSize.width = (int) fontLayout.width;
            textSize.height = (int) fontLayout.height;
            displayMod.x = (int) -(textSize.width / 2);
            displayMod.y = (int) -(textSize.height / 2);
        }
        
        rect = new Rect(location, textSize.width, textSize.height);
        this.label = label;
        this.font = font;
        
    }

    public void render(SpriteBatch spriteBatch, BitmapFont renderFont, boolean hoverCheck, float alphaPercent) {
        BitmapFont targetFont = renderFont;
        if(targetFont == null) {
            targetFont = font;
        }

        if(targetFont != null) {
            spriteBatch.begin();
    
            if(hoverCheck) {
                targetFont.setColor(Color.WHITE);
            } else {
                targetFont.setColor(Color.DARK_GRAY);
            }
            Rect displayRect = getDisplayRect();

            Color currentFontColor = targetFont.getColor();
            targetFont.setColor(currentFontColor.r, currentFontColor.g, currentFontColor.b, alphaPercent);
            targetFont.draw(spriteBatch, label, displayRect.location.x, displayRect.location.y + textSize.height);
            
            targetFont.setColor(currentFontColor.r, currentFontColor.g, currentFontColor.b, 1.0f);
            spriteBatch.end();
        }
    }

    public Rect getDisplayRect() {
        Point location = new Point(rect.location.x + displayMod.x, rect.location.y + displayMod.y);
        return new Rect(location, rect.width, rect.height);
    }

    public String toString() {
        return getClass().toString().substring(getClass().toString().lastIndexOf(".") + 1);
    }
}
