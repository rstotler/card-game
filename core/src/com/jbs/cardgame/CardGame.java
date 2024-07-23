package com.jbs.cardgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.jbs.cardgame.screen.Screen;
import com.jbs.cardgame.screen.gamescreen.GameScreen;
import com.jbs.cardgame.screen.mainmenuscreen.MainMenuScreen;

public class CardGame extends ApplicationAdapter {
	Screen screen;
	
	@Override
	public void create () {
		// screen = new BattleScreen();
		// screen = new GameScreen();
		screen = new MainMenuScreen();
	}

	@Override
	public void render () {
		screen.handleInput();
		
		String updateString = screen.update();
		if(updateString.equals("Click New Game")) {
			screen.dispose();
			screen = new GameScreen();
		}

		else if(updateString.equals("End Battle Screen")) {
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
