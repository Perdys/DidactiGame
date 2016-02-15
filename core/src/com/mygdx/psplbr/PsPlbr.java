package com.mygdx.psplbr;

import com.badlogic.gdx.Game;

public class PsPlbr extends Game {

	public void create() { setScreen(new Menu(this)); }

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
