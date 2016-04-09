package com.mygdx.DidactiGame.Pantallas.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.mygdx.DidactiGame.DidactiGame;
import com.mygdx.DidactiGame.Auxiliares.Pantalla;

import java.awt.*;

public class Menu_Puntuaciones extends Pantalla{

    DidactiGame juego;
    OrthographicCamera camara;
    SpriteBatch batch;
    Stage stage = new Stage();

    public static SelectBox<String> juego_selector;

    public Menu_Puntuaciones(DidactiGame juego) {
        this.juego = juego;

        camara = new OrthographicCamera();
        camara.setToOrtho(false);
        batch = new SpriteBatch();

        pantalla_actual = "Menu_Puntuaciones";

        texturas_cargar();
        //TODO añadir selectbox de rosco o QQSM
        //TODO añadir scrollpane de las puntuaciones
    }

    public void render (float delta) {
        Gdx.gl.glClearColor(Color.MAGENTA.getRed(), Color.MAGENTA.getGreen(), Color.MAGENTA.getBlue(), Color.MAGENTA.getAlpha());
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
        stage.addActor(juego_selector);

        InputMultiplexer inputs = new InputMultiplexer();
        inputs.addProcessor(botones_genericos);
        Gdx.input.setInputProcessor(inputs);
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);
    }

    public void resume() { pantalla_actual = "Menu_Puntuaciones"; }

    public void dispose() { //es la ultima en ejecutarse, se encarga de liberar recursos y dejar la memoria limpia

        batch.dispose();
    }

    public void texturas_cargar() {

        //Selector del juego para ver puntuaciones
        juego_selector = new SelectBox<>(estilo_selector());
        juego_selector.setPosition(proporcion_x(0.2), proporcion_y(0.2));
        juego_selector.setSize(proporcion_x(0.2), proporcion_y(0.2));
        juego_selector.setMaxListCount(3);
        juego_selector.setItems("Rosco", "QQSM");
        juego_selector.pack();
    }
}
