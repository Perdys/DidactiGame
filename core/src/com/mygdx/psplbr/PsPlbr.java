package com.mygdx.psplbr;

import com.badlogic.gdx.Game;

public class PsPlbr extends Game {

	static Menu menu;

	public void create() { menu = new Menu(this); setScreen(menu); }

	public void render() { super.render(); }

	public void resize(int width, int height) {
		super.resize(width, height);
	}

	public void pause() {
		super.pause();
	}

	public void resume() { super.resume(); }

	public void dispose() {
		super.dispose();
	}
}
