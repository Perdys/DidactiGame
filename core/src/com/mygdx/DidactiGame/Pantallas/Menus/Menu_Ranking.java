package com.mygdx.DidactiGame.Pantallas.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.DidactiGame.DidactiGame;
import com.mygdx.DidactiGame.Auxiliares.Pantalla;

public class Menu_Ranking extends Pantalla{

    DidactiGame juego;
    OrthographicCamera camara;
    SpriteBatch batch;

    public Menu_Ranking(DidactiGame juego) {
        this.juego = juego;

        camara = new OrthographicCamera();
        camara.setToOrtho(false);
        batch = new SpriteBatch();

        pantalla_actual = "Menu_Ranking";

        //TODO hacer rankings segun juegos con selector del juego
    }

    public void render (float delta) {
        Gdx.gl.glClearColor(Color.YELLOW.r, Color.YELLOW.g, Color.YELLOW.b, Color.YELLOW.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();

        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.end();
    }

    public void show () {
        sistema_botones(juego);

        InputMultiplexer inputs = new InputMultiplexer();
        inputs.addProcessor(botones_genericos);
        Gdx.input.setInputProcessor(inputs);
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);
    }

    public void resume() { pantalla_actual = "Menu_Ranking"; }

    public void dispose() { //es la ultima en ejecutarse, se encarga de liberar recursos y dejar la memoria limpia

        batch.dispose();
    }
}
