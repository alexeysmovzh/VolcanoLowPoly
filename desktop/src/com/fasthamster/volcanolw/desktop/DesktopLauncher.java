package com.fasthamster.volcanolw.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.fasthamster.volcanolw.VolcanoLowPoly;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = 720;
        config.width = 1024;
        config.samples = 3;
		new LwjglApplication(new VolcanoLowPoly(), config);
	}
}
