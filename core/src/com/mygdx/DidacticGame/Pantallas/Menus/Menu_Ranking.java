package com.mygdx.DidacticGame.Pantallas.Menus;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.DidacticGame.DidacticGame;
import com.mygdx.DidacticGame.Auxiliares.Pantalla;

import java.util.ArrayList;

import static com.mygdx.DidacticGame.DidacticGame.BD;
import static com.mygdx.DidacticGame.DidacticGame.tarea;

public class Menu_Ranking extends Pantalla{

    DidacticGame juego;
    OrthographicCamera camara;
    SpriteBatch batch;
    Stage stage;

    SelectBox<String> juego_selector;
    Label puntuaciones;
    String rosco_ranking = "", qqsm_ranking = "";
    ScrollPane puntuaciones_scroll;
    Texture particulas_texto;

    public static class Puntuacion {
        public String jugador, juego_tipo;
        public int aciertos = 0, fallos = 0, tiempo = 0;
        public String fecha = "";
        public String puntuacion_mostrar;
    }
    ArrayList<Puntuacion> puntuaciones_rosco, puntuaciones_qqsm;

    public Menu_Ranking(DidacticGame juego) {
        this.juego = juego;

        camara = new OrthographicCamera();
        camara.setToOrtho(false);
        batch = new SpriteBatch();
        stage = new Stage();

        pantalla_actual = "Menu_Ranking";

        puntuaciones_rosco = new ArrayList<>();
        puntuaciones_qqsm = new ArrayList<>();

        texturas_cargar();
        crear_ranking();
    }

    public void render (float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();

        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        if (Gdx.app.getType() == Application.ApplicationType.Desktop)
            batch.draw(boton_atras, 0, 0, anchura_juego, altura_juego);
        batch.draw(particulas_texto, proporcion_x(0.325), proporcion_y(0.74) - particulas_texto.getHeight() * proporcion_x(0.35) / particulas_texto.getWidth(),
                proporcion_x(0.35), particulas_texto.getHeight() * proporcion_x(0.35) / particulas_texto.getWidth());
        batch.end();

        stage.act();
        stage.draw();
    }

    public void show () {
        crear_ranking();
        if ("Rosco".compareTo(juego_selector.getSelected()) == 0)
            puntuaciones.setText(rosco_ranking);
        else
            puntuaciones.setText(qqsm_ranking);

        sistema_botones(juego);
        stage.addActor(juego_selector);
        stage.addActor(puntuaciones_scroll);

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
        particulas_texto = tarea.asset.get("data/texturas/texto/particulas.png");

        puntuaciones = new Label("", texto_panel_scroll_estilo());
        puntuaciones.setPosition(proporcion_x(0.2), proporcion_y(0.4));
        puntuaciones.setSize(proporcion_x(0.7), proporcion_y(0.5));
        puntuaciones_scroll = new ScrollPane(puntuaciones);
        puntuaciones_scroll.setBounds(proporcion_x(0.325), proporcion_y(0.05), proporcion_x(0.35), proporcion_y(0.60));
        puntuaciones_scroll.layout();
        puntuaciones_scroll.setTouchable(Touchable.enabled);

        //Selector del juego para ver leer_puntuaciones
        juego_selector = new SelectBox<>(selector_estilo());
        juego_selector.setPosition(proporcion_x(0.35), proporcion_y(0.75));
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
        rosco_ranking = "";
        qqsm_ranking = "";
        ArrayList<Puntuacion> puntuaciones_rosco = BD.leer_puntuaciones("Rosco");
        ArrayList<Puntuacion> puntuaciones_qqsm = BD.leer_puntuaciones("QQSM");
        for (int i = 0; i < puntuaciones_rosco.size(); ++i)
            rosco_ranking += (i + 1) + " posición -> " + puntuaciones_rosco.get(i).jugador + ":\n" + puntuaciones_rosco.get(i).puntuacion_mostrar;

        for (int i = 0; i < puntuaciones_qqsm.size(); ++i)
            qqsm_ranking += (i + 1) + " posición -> " + puntuaciones_qqsm.get(i).jugador + ":\n" + puntuaciones_qqsm.get(i).puntuacion_mostrar;
    }
}
