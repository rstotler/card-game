package com.jbs.cardgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.jbs.cardgame.screen.Screen;
import com.jbs.cardgame.screen.battlescreen.BattleScreen;

public class CardGame extends ApplicationAdapter {
	Screen screen;
	
	@Override
	public void create () {
		screen = new BattleScreen();
	}

	@Override
	public void render () {
		screen.handleInput();
		screen.update();
		screen.render();
	}
	
	@Override
	public void dispose () {
		screen.dispose();
	}
}
