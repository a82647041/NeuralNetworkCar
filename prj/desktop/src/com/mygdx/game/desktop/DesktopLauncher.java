package com.mygdx.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Test;
import com.mygdx.game.Util.MyUtil;

public class DesktopLauncher {

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width= MyUtil.WINDOW_WIDTH;
		config.height=MyUtil.WINDOW_HEIGHT;
		config.title="机器学习自动驾驶";

		config.width=1280;
		config.height=720;
		config.x = 100;
		config.y = 50;

		new LwjglApplication(new MyGdxGame(), config);
	}
}
