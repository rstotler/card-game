package com.jbs.cardgame;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(1280, 768);
		config.useVsync(true);
		config.setResizable(false);
		config.setForegroundFPS(60);
		config.setTitle("Card Game");
		new Lwjgl3Application(new CardGame(), config);
	}
}
