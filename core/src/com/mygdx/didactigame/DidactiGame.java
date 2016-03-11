package com.mygdx.DidactiGame;

import com.badlogic.gdx.Game;
import com.mygdx.DidactiGame.Pantallas.Menu_Juegos;

public class DidactiGame extends Game {

	public static Menu_Juegos menu_juegos;

	public void create() { menu_juegos = new Menu_Juegos(this); setScreen(menu_juegos); }

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
