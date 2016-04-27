package com.mygdx.DidactiGame.Pantallas.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.DidactiGame.DidactiGame;
import com.mygdx.DidactiGame.Auxiliares.Pantalla;

import java.util.ArrayList;

import static com.mygdx.DidactiGame.DidactiGame.jugadores;

public class Menu_Ranking extends Pantalla{

    DidactiGame juego;
    OrthographicCamera camara;
    SpriteBatch batch;
    Stage stage;

    SelectBox<String> juego_selector;
    Label puntuaciones;
    String rosco_ranking = "", qqsm_ranking = "";

    public Menu_Ranking(DidactiGame juego) {
        this.juego = juego;

        camara = new OrthographicCamera();
        camara.setToOrtho(false);
        batch = new SpriteBatch();
        stage = new Stage();

        pantalla_actual = "Menu_Ranking";

        texturas_cargar();
        //TODO hacer rankings segun juegos con selector del juego
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
        crear_ranking();

        sistema_botones(juego);
        stage.addActor(juego_selector);

        InputMultiplexer inputs = new InputMultiplexer();
        inputs.addProcessor(botones_genericos);
        inputs.addProcessor(stage);
        Gdx.input.setInputProcessor(inputs);
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);
    }

    public void resume() { pantalla_actual = "Menu_Ranking"; }

    public void dispose() { //es la ultima en ejecutarse, se encarga de liberar recursos y dejar la memoria limpia

        batch.dispose();
    }

    public void texturas_cargar() {

        puntuaciones = new Label("", texto_panel_scroll_estilo());
        puntuaciones.setPosition(proporcion_x(0.2), proporcion_y(0.4));

        //Selector del juego para ver leer_puntuaciones
        juego_selector = new SelectBox<>(selector_estilo());
        juego_selector.setPosition(proporcion_x(0.2), proporcion_y(0.2));
        juego_selector.setSize(proporcion_x(0.2), proporcion_y(0.2));
        juego_selector.setMaxListCount(3);
        juego_selector.setItems("Rosco", "QQSM");
        juego_selector.pack();
        juego_selector.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if ("Rosco".compareTo(juego_selector.getSelected()) == 0)
                    puntuaciones.setText(rosco_ranking);
                else
                    puntuaciones.setText(qqsm_ranking);
            }
        });
    }

    public void crear_ranking() {
        ArrayList<String> puntuaciones_temp_rosco = new ArrayList<>(10);
        puntuaciones_temp_rosco.ensureCapacity(10);
        puntuaciones_temp_rosco.add(0, "0 26 0");
        ArrayList<String> puntuaciones_temp_qqsm = new ArrayList<>(10);
        puntuaciones_temp_qqsm.ensureCapacity(10);
        puntuaciones_temp_qqsm.add(0, "0 26 0");

        for (int i = 0; i < jugadores.numeral(); ++i) {
            if (i > 10) {
                String[] puntuaciones_jugador_rosco = jugadores.jugador(i).puntuaciones("Rosco").split("\n");
                String[] puntuaciones_jugador_qqsm = jugadores.jugador(i).puntuaciones("QQSM").split("\n");
                //TODO meter en temp las leer_puntuaciones ordenadas por leer_puntuaciones
            }

        }
    }
}
