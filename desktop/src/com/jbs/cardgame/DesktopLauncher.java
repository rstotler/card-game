package com.jbs.cardgame;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT);
		config.useVsync(true);
		config.setResizable(false);
		config.setForegroundFPS(Settings.FPS);
		config.setTitle(Settings.TITLE);
		new Lwjgl3Application(new CardGame(), config);
	}
}
