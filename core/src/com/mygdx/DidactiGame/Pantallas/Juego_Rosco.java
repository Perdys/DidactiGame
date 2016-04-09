package com.mygdx.DidactiGame.Pantallas;

import com.badlogic.gdx.*;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.mygdx.DidactiGame.DidactiGame;
import com.mygdx.DidactiGame.Auxiliares.Pantalla;
import com.mygdx.DidactiGame.Pantallas.Menus.Menu_Juegos;

import java.util.*;

import static com.badlogic.gdx.utils.Align.topLeft;
import static com.mygdx.DidactiGame.DidactiGame.jugadores;

public class Juego_Rosco extends Pantalla {

    DidactiGame juego;
    OrthographicCamera camara;
    SpriteBatch batch;
    InputAdapter click;
    Stage stage;

    Texture fondo, boton_jugando;
    Rectangle boton_inicio, boton_acierto, boton_fallo, boton_pregunta, boton_tiempo;

    String[] descripcion_actual = {"", ""};
    ScrollPane descripcion_scroll, nombre_scroll;
    Label descripcion_cuadro, nombre_cuadro;

    ArrayList<ArrayList<String[]>> descripciones;

    public Juego_Rosco(ArrayList<ArrayList<String[]>> descripciones, DidactiGame juego) {
        this.juego = juego;
        this.descripciones = descripciones;

        camara = new OrthographicCamera();
        camara.setToOrtho(false);
        batch = new SpriteBatch();
        stage = new Stage();

        pantalla_actual = "Juego_Rosco";

        fondo = new Texture("data/menu_juegos/juego_rosco/fondo_rosco.png");
        boton_jugando = new Texture("data/menu_juegos/juego_rosco/boton_on.png");

        botones_cargar();
        textos_cargar();
    }

    public void render(float delta) {	/*ejecutarse todas las veces posible por segundo, ejecuten todas las acciones del juego
		dibujar los elementos por pantalla, procesar la entrada y salida, mover los personajes, detectar colisiones, etc. */
        Gdx.gl.glClearColor(jugadores.jugador_actual().color.r, jugadores.jugador_actual().color.g,
                            jugadores.jugador_actual().color.b, jugadores.jugador_actual().color.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();

        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.draw(fondo, 0, 0, anchura_juego, altura_juego);
        if (jugadores.jugador_actual().jugando)
            batch.draw(boton_jugando, 0, 0, anchura_juego, altura_juego);
        texturas_mostrar();
        batch.end();

        stage.act();
        stage.draw();
    }

    public void resume() { pantalla_actual = "Juego_Rosco"; }

    public void show() {
        sistema_botones(juego);
        stage.addActor(descripcion_scroll);
        stage.addActor(nombre_scroll);

        InputMultiplexer inputs = new InputMultiplexer();
        inputs.addProcessor(botones_genericos);
        inputs.addProcessor(click);
        inputs.addProcessor(stage);
        Gdx.input.setInputProcessor(inputs);
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);
    }

    public void dispose() { //es la ultima en ejecutarse, se encarga de liberar recursos y dejar la memoria limpia

        batch.dispose();
    }

    public void botones_cargar() {

        boton_inicio = new Rectangle(proporcion_x(0.75), proporcion_y(0.65), proporcion_x(0.07), proporcion_y(0.1));
        boton_acierto = new Rectangle(proporcion_x(0.14), proporcion_y(0.60), proporcion_x(0.07), proporcion_y(0.15));
        boton_fallo = new Rectangle(proporcion_x(0.49), proporcion_y(0.60), proporcion_x(0.07), proporcion_y(0.15));
        boton_pregunta = new Rectangle(proporcion_x(0.31), proporcion_y(0.60), proporcion_x(0.07), proporcion_y(0.15));
        boton_tiempo = new Rectangle(proporcion_x(0.66), proporcion_y(0.51), proporcion_x(0.14), proporcion_y(0.1));

        click = new InputAdapter() {
            public boolean touchUp (int x, int y, int pointer, int button) {
                if (boton_acierto.contains(x, y)) {
                    if (!jugadores.vacio())
                        if (jugadores.jugador_actual().jugando) {
                            jugadores.jugador_actual().letras[jugadores.jugador_actual().letra_actual] = 1;
                            ++jugadores.jugador_actual().n_aciertos_rosco;

                            if ((jugadores.jugador_actual().n_aciertos_rosco + jugadores.jugador_actual().n_fallos_rosco) < 26)
                                letra_siguiente();
                            else
                                jugador_siguiente();
                        }
                } else
                if (boton_fallo.contains(x, y)) {
                    if (!jugadores.vacio())
                        if (jugadores.jugador_actual().jugando) {
                            jugadores.jugador_actual().letras[jugadores.jugador_actual().letra_actual] = 2;
                            ++jugadores.jugador_actual().n_fallos_rosco;

                            jugador_siguiente();
                        }
                } else
                if (boton_inicio.contains(x, y)) {
                    if (!jugadores.vacio())
                        if (jugadores.jugador_actual().jugando) {
                            jugadores.jugador_actual().letra_actual = jugadores.jugador_actual().letra_actual == 25 ? 0 : jugadores.jugador_actual().letra_actual + 1;

                            jugador_siguiente();
                        } else {
                            jugadores.jugador_actual().jugando = true;
                            jugadores.jugador_actual().letra_actual = jugadores.jugador_actual().letra_actual == -1 ? 0 : jugadores.jugador_actual().letra_actual;

                            letra_siguiente();
                        }
                } else
                if (boton_pregunta.contains(x, y)) {
                    if (!jugadores.vacio())
                        if (jugadores.jugador_actual().jugando) {
                            String aux = descripcion_actual[0];
                            descripcion_actual[0] = descripcion_actual[1];
                            descripcion_actual[1] = aux;
                        }
                } else
                if (boton_tiempo.contains(x, y)) {
                    if (!jugadores.vacio())
                        Gdx.input.getTextInput (new Input.TextInputListener() {

                            public void input(String tiempo) {
                                if (tiempo.isEmpty())
                                    tiempo = "150";
                                jugadores.jugador_actual().tiempo_rosco = Integer.parseInt(tiempo);
                            }

                            public void canceled() {}

                        }, "Introduzca el tiempo_rosco inicial", "", "150''");
                }

                return false;
            }
        };
    }

    public void letra_siguiente() {
        for (; jugadores.jugador_actual().letras[jugadores.jugador_actual().letra_actual] != 0; ++jugadores.jugador_actual().letra_actual)
            if (jugadores.jugador_actual().letra_actual == 25)
                jugadores.jugador_actual().letra_actual = -1;

        if (descripciones.size() != 0)
            if (descripciones.get(jugadores.jugador_actual().letra_actual).size() != 0) {
                int descripcion_aleatoria = MathUtils.random(descripciones.get(jugadores.jugador_actual().letra_actual).size() - 1);
                descripcion_actual = new String[]{descripciones.get(jugadores.jugador_actual().letra_actual).get(descripcion_aleatoria)[1],
                                                  descripciones.get(jugadores.jugador_actual().letra_actual).get(descripcion_aleatoria)[0]};
            }
            else
                descripcion_actual = new String[]{"INTRODUZCA UNA DESCRIPCION PARA ESTA LETRA", "NINGUNA"};
    }

    public void jugador_siguiente() {

        if (!jugadores.siguiente())
            juego.setScreen(new Clasificacion(juego));
    }

    public void textos_cargar() {
        descripcion_cuadro = new Label("", estilo_etiqueta((int)proporcion_y(0.06)));
        descripcion_cuadro.setWidth(proporcion_x(0.33));
        descripcion_cuadro.setWrap(true);
        descripcion_cuadro.setAlignment(topLeft);
        descripcion_scroll = new ScrollPane(descripcion_cuadro);
        descripcion_scroll.setBounds(proporcion_x(0.2), proporcion_y(0.505), proporcion_x(0.33), proporcion_y(0.19));
        descripcion_scroll.layout();
        descripcion_scroll.setTouchable(Touchable.enabled);

        nombre_cuadro = new Label("", estilo_etiqueta((int)proporcion_y(0.09)));
        nombre_cuadro.setWidth(proporcion_x(0.14));
        nombre_cuadro.setWrap(true);
        nombre_cuadro.setAlignment(topLeft);
        nombre_scroll = new ScrollPane(nombre_cuadro);
        nombre_scroll.setBounds(proporcion_x(0.66), proporcion_y(0.63), proporcion_x(0.14), proporcion_y(0.07));
        nombre_scroll.layout();
        nombre_scroll.setTouchable(Touchable.enabled);
    }

    public void texturas_mostrar() {

        if (!jugadores.vacio()) {

            //Mostrar letras, aciertos y fallos
            for (int i = 0; i < 26; ++i) {
                if (jugadores.jugador_actual().letras[i] != 0 || jugadores.jugador_actual().letra_actual == i) {
                    batch.draw(Menu_Juegos.elementos_mostrar.letras[i], 0, 0, anchura_juego, altura_juego);
                    if (jugadores.jugador_actual().letras[i] == 2)
                        batch.draw(Menu_Juegos.elementos_mostrar.rojos[i], 0, 0, anchura_juego, altura_juego);
                    else if (jugadores.jugador_actual().letras[i] == 1)
                        batch.draw(Menu_Juegos.elementos_mostrar.verdes[i], 0, 0, anchura_juego, altura_juego);
                }
            }

            //Obtener tiempo_rosco restante
            jugadores.jugador_actual().contador_rosco += Gdx.graphics.getDeltaTime();
            if (jugadores.jugador_actual().contador_rosco >= 1.0f) {
                jugadores.jugador_actual().contador_rosco = 0;
                if (jugadores.jugador_actual().jugando)
                    jugadores.jugador_actual().tiempo_rosco--;
            }

            //Comprobar si se ha acabado el tiempo_rosco
            if (jugadores.jugador_actual().tiempo_rosco < 1) {
                jugadores.jugador_actual().n_fallos_rosco = 26 - jugadores.jugador_actual().n_aciertos_rosco;
                jugador_siguiente();
            }

            if (!jugadores.vacio()) {
                //Mostrar el tiempo_rosco y la puntuacion
                batch.draw(Menu_Juegos.elementos_mostrar.puntuacion[0][jugadores.jugador_actual().n_aciertos_rosco % 10], 0, 0, anchura_juego, altura_juego);
                batch.draw(Menu_Juegos.elementos_mostrar.puntuacion[1][jugadores.jugador_actual().n_aciertos_rosco / 10], 0, 0, anchura_juego, altura_juego);
                batch.draw(Menu_Juegos.elementos_mostrar.tiempo[0][jugadores.jugador_actual().tiempo_rosco % 10], 0, 0, anchura_juego, altura_juego);
                batch.draw(Menu_Juegos.elementos_mostrar.tiempo[1][(jugadores.jugador_actual().tiempo_rosco % 100) / 10], 0, 0, anchura_juego, altura_juego);
                batch.draw(Menu_Juegos.elementos_mostrar.tiempo[2][(jugadores.jugador_actual().tiempo_rosco % 1000) / 100], 0, 0, anchura_juego, altura_juego);

                //Mostrar el texto de la letra actual
                if (jugadores.jugador_actual().letra_actual > -1) { descripcion_cuadro.setText(descripcion_actual[0]); }

                //Mostrar el jugador del jugador actual
                if (!jugadores.vacio()) { nombre_cuadro.setText(jugadores.jugador_actual().nombre); }
            }
        }
    }
}
