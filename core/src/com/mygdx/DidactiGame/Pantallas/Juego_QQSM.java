package com.mygdx.DidactiGame.Pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.DidactiGame.Auxiliares.Pantalla;
import com.mygdx.DidactiGame.DidactiGame;

public class Juego_QQSM extends Pantalla{

    DidactiGame juego;
    OrthographicCamera camara;
    SpriteBatch batch;
    Stage stage;

    public Juego_QQSM(DidactiGame juego) {
        this.juego = juego;

        camara = new OrthographicCamera();
        camara.setToOrtho(false);
        batch = new SpriteBatch();
        stage = new Stage();

        pantalla_actual = "Juego_QQSM";

        texturas_cargar();
    }

    public void render (float delta) {
        Gdx.gl.glClearColor(Color.YELLOW.r, Color.YELLOW.g, Color.YELLOW.b, Color.YELLOW.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();

        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.end();

        stage.act();
        stage.draw();
    }

    public void show () {
        sistema_botones(juego);

        InputMultiplexer inputs = new InputMultiplexer();
        inputs.addProcessor(botones_genericos);
        inputs.addProcessor(stage);
        Gdx.input.setInputProcessor(inputs);
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);
    }

    public void resume() { pantalla_actual = "Juego_QQSM"; }

    public void dispose() { //es la ultima en ejecutarse, se encarga de liberar recursos y dejar la memoria limpia

        batch.dispose();
    }

    public void texturas_cargar() {
    }
}
