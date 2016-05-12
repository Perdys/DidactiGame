package com.mygdx.DidacticGame.Pantallas.Menus;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.DidacticGame.DidacticGame;
import com.mygdx.DidacticGame.Auxiliares.Pantalla;

import static com.mygdx.DidacticGame.DidacticGame.*;

public class Menu_Inicial extends Pantalla{

    DidacticGame juego;
    OrthographicCamera camara;
    SpriteBatch batch;
    InputAdapter click;

    Circle boton_juegos;
    Rectangle boton_jugadores, boton_datos, boton_puntuaciones, boton_ranking;
    Texture fondo;

    public Menu_Inicial (final DidacticGame juego) {
        this.juego = juego;

        camara = new OrthographicCamera();
        camara.setToOrtho(false);
        batch = new SpriteBatch();

        pantalla_actual = "Menu_Inicial";

        fondo =  new Texture("data/texturas/fondo/inicial.jpg");

        botones_cargar();
    }

    public void render (float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();

        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.draw(fondo, 0, 0, anchura_juego, altura_juego);
        if (Gdx.app.getType() == Application.ApplicationType.Desktop)
            batch.draw(boton_atras, 0, 0, anchura_juego, altura_juego);
        batch.end();
    }

    public void show () {
        sistema_botones(juego);

        InputMultiplexer inputs = new InputMultiplexer();
        inputs.addProcessor(botones_genericos);
        inputs.addProcessor(click);
        Gdx.input.setInputProcessor(inputs);
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);
    }

    public void resume() { pantalla_actual = "Menu_Inicial"; }

    public void botones_cargar() {
        boton_juegos = new Circle(proporcion_x(0.5), proporcion_y(0.5), proporcion_x(0.13));
        boton_datos = new Rectangle(proporcion_x(0.23), 0, proporcion_x(0.27), proporcion_y(0.5));
        boton_jugadores = new Rectangle(proporcion_x(0.5), 0, proporcion_x(0.27), proporcion_y(0.5));
        boton_puntuaciones = new Rectangle(proporcion_x(0.23), proporcion_y(0.5), proporcion_x(0.27), proporcion_y(0.5));
        boton_ranking = new Rectangle(proporcion_x(0.5), proporcion_y(0.5), proporcion_x(0.27), proporcion_y(0.5));

        click = new InputAdapter() {
            public boolean touchUp(int x, int y, int pointer, int button) {
                if (boton_juegos.contains(x, y)) {
                    if (menu_juegos == null)
                        menu_juegos = new Menu_Juegos(juego);
                    juego.setScreen(menu_juegos);
                } else if (boton_jugadores.contains(x, y)) {
                    if (menu_jugadores == null)
                        menu_jugadores = new Menu_Jugadores(juego);
                    juego.setScreen(DidacticGame.menu_jugadores);
                } else if (boton_datos.contains(x, y)) {
                    if (menu_datos == null)
                        menu_datos = new Menu_Datos(juego);
                    juego.setScreen(DidacticGame.menu_datos);
                } else if (boton_puntuaciones.contains(x, y)) {
                    if (menu_puntuaciones== null)
                        menu_puntuaciones = new Menu_Puntuaciones(juego);
                    juego.setScreen(DidacticGame.menu_puntuaciones);
                } else if (boton_ranking.contains(x, y)) {
                    if (menu_ranking == null)
                        menu_ranking = new Menu_Ranking(juego);
                    juego.setScreen(DidacticGame.menu_ranking);
                }

                return false;
            }
        };
    }

    public void dispose() { //es la ultima en ejecutarse, se encarga de liberar recursos y dejar la memoria limpia

        batch.dispose();
    }
}
