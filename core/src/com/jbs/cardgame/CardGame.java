package com.jbs.cardgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.jbs.cardgame.screen.Screen;
import com.jbs.cardgame.screen.battlescreen.BattleScreen;
import com.jbs.cardgame.screen.gamescreen.GameScreen;

public class CardGame extends ApplicationAdapter {
	Screen screen;
	
	@Override
	public void create () {
		screen = new BattleScreen();
	}

	@Override
	public void render () {
		screen.handleInput();
		
		String updateString = screen.update();
		if(updateString.equals("End Battle Screen")) {
			screen.dispose();
			screen = new GameScreen();
		}

		screen.render();
	}
	
	@Override
	public void dispose () {
		screen.dispose();
	}
}
