package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import Screens.PlayScreen;

public class MyGdxGame extends Game {
	public static final int V_WIDTH = 640;
	public static final int V_HEIGHT = 640;
	public static AssetManager manager;

	public SpriteBatch batch;
	
	@Override
	public void create () {

		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load("audio/music/TheSunRises.mp3", Music.class);
		manager.finishLoading();
		setScreen(new PlayScreen(this));

	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		manager.dispose();
	}


}
