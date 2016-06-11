package com.mygdx.DidacticGame.Pantallas;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.mygdx.DidacticGame.Auxiliares.Pantalla;
import com.mygdx.DidacticGame.DidacticGame;

import java.util.ArrayList;

import static com.badlogic.gdx.utils.Align.center;
import static com.mygdx.DidacticGame.DidacticGame.BD;
import static com.mygdx.DidacticGame.DidacticGame.jugadores;
import static com.mygdx.DidacticGame.Auxiliares.Base_Datos.*;
import static com.mygdx.DidacticGame.DidacticGame.tarea;

public class Juego_QQSM extends Pantalla{

    DidacticGame juego;
    OrthographicCamera camara;
    SpriteBatch batch;
    Stage stage;
    InputAdapter click;

    Texture fondo;
    Rectangle boton_comodin50, boton_inicio;
    Rectangle boton_respuesta0, boton_respuesta1, boton_respuesta2, boton_respuesta3;
    Label pregunta_etiqueta, respuesta0_etiqueta, respuesta1_etiqueta, respuesta2_etiqueta, respuesta3_etiqueta;
    Label tiempo_etiqueta, aciertos_etiqueta, nombre_jugador_etiqueta, comodin50_etiqueta;
    ScrollPane nombre_jugador_scroll;
    ScrollPane pregunta_scroll, respuesta0_scroll, respuesta1_scroll, respuesta2_scroll, respuesta3_scroll;
    Pregunta pregunta_actual;

    Clasificacion clasificacion;

    ArrayList<Pregunta> preguntas;

    public Juego_QQSM(DidacticGame juego) {
        this.juego = juego;

        camara = new OrthographicCamera();
        camara.setToOrtho(false);
        batch = new SpriteBatch();
        stage = new Stage();

        pantalla_actual = "Juego_QQSM";

        clasificacion = new Clasificacion("QQSM", juego);

        preguntas = new ArrayList<>();
        BD.leer_preguntas(preguntas);
        if (preguntas.size() == 0)
            pregunta_actual = new Pregunta();
        else
            pregunta_actual = preguntas.get(0);

        texturas_cargar();
        botones_cargar();
        textos_cargar();
    }

    public void render (float delta) {
        Gdx.gl.glClearColor(jugadores.jugador_actual().color.r, jugadores.jugador_actual().color.g,
                jugadores.jugador_actual().color.b, jugadores.jugador_actual().color.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();

        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.draw(fondo, 0, 0, anchura_juego, altura_juego);
        if (Gdx.app.getType() == Application.ApplicationType.Desktop)
            batch.draw(boton_atras, 0, 0, anchura_juego, altura_juego);
        texturas_mostrar();
        batch.end();

        stage.act();
        stage.draw();
    }

    public void show () {
        sistema_botones(juego);
        stage.addActor(nombre_jugador_scroll);
        stage.addActor(pregunta_scroll);
        stage.addActor(respuesta0_scroll);
        stage.addActor(respuesta1_scroll);
        stage.addActor(respuesta2_scroll);
        stage.addActor(respuesta3_scroll);

        InputMultiplexer inputs = new InputMultiplexer();
        inputs.addProcessor(botones_genericos);
        inputs.addProcessor(click);
        inputs.addProcessor(stage);
        Gdx.input.setInputProcessor(inputs);
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);
    }

    public void resume() { pantalla_actual = "Juego_QQSM"; }

    public void dispose() { //es la ultima en ejecutarse, se encarga de liberar recursos y dejar la memoria limpia

        batch.dispose();
    }

    public void botones_cargar() {

        boton_inicio = new Rectangle(proporcion_x(0.6), 0, proporcion_x(0.15), proporcion_y(0.22));
        boton_comodin50 = new Rectangle(proporcion_x(0.5), 0, proporcion_x(0.5), proporcion_y(0.5));
        boton_respuesta0 = new Rectangle(0, proporcion_y(0.5), proporcion_x(0.5), proporcion_y(0.25));
        boton_respuesta1 = new Rectangle(proporcion_x(0.5), proporcion_y(0.5), proporcion_x(0.5), proporcion_y(0.25));
        boton_respuesta2 = new Rectangle(0, proporcion_y(0.75), proporcion_x(0.5), proporcion_y(0.25));
        boton_respuesta3 = new Rectangle(proporcion_x(0.5), proporcion_y(0.75), proporcion_x(0.5), proporcion_y(0.25));

        click = new InputAdapter() {
            public boolean touchUp (int x, int y, int pointer, int button) {
                if (boton_inicio.contains(x, y)) { elegir(4); } else
                if (boton_comodin50.contains(x, y)) { elegir(5); } else
                if (boton_respuesta0.contains(x, y)) { elegir(0); } else
                if (boton_respuesta1.contains(x, y)) { elegir(1); } else
                if (boton_respuesta2.contains(x, y)) { elegir(2); } else
                if (boton_respuesta3.contains(x, y)) { elegir(3); }

                return false;
            }
        };
    }

    public void elegir (int eleccion) {
        if (!jugadores.vacio())
                switch (eleccion) {
                    case 4:
                        if (jugadores.jugador_actual().jugando) {
                            jugadores.jugador_actual().jugando = false;
                            siguiente_jugador();
                        } else {
                            jugadores.jugador_actual().jugando = true;
                            siguiente_pregunta();
                        }
                        break;
                    case 5:
                        if (jugadores.jugador_actual().jugando && !jugadores.jugador_actual().comodin50_usado)
                            usar_comodin50();
                        break;
                    default:
                        if (jugadores.jugador_actual().jugando) {
                            if (pregunta_actual.correcta.compareTo(pregunta_actual.respuestas[eleccion]) == 0) {
                                //acierto
                                jugadores.jugador_actual().n_aciertos += 1;
                                aciertos_etiqueta.setText(Integer.toString(jugadores.jugador_actual().n_aciertos));
                                siguiente_pregunta();
                            } else {
                                //fallo
                                jugadores.jugador_actual().n_fallos += 1;
                                jugadores.jugador_actual().jugando = false;
                                siguiente_jugador();
                            }
                        } break;
                }
    }

    public void siguiente_pregunta() {
        if (preguntas.size() == 0)
            siguiente_jugador();
        else {
            int pregunta_aleatoria = MathUtils.random(preguntas.size() - 1);
            pregunta_actual = preguntas.get(pregunta_aleatoria);
            preguntas.remove(pregunta_actual);
        }
    }

    public void siguiente_jugador() {
        pregunta_etiqueta.setText("");
        respuesta0_etiqueta.setText("");
        respuesta1_etiqueta.setText("");
        respuesta2_etiqueta.setText("");
        respuesta3_etiqueta.setText("");
        aciertos_etiqueta.setText("0");
        comodin50_etiqueta.setText("50 : 50");
        tiempo_etiqueta.setText("GO");

        preguntas = new ArrayList<>();
        BD.leer_preguntas(preguntas);

        jugadores.jugador_actual().seleccionado = false;
        clasificacion.puntuacion_anadir(jugadores.jugador_actual().nombre, jugadores.jugador_actual().guardar_puntuacion("QQSM"));

        if (!jugadores.siguiente())
            juego.setScreen(clasificacion);

        //Mostrar el nombre del jugador actual
        if (!jugadores.vacio()) { nombre_jugador_etiqueta.setText(jugadores.jugador_actual().nombre); }
    }

    public void textos_cargar() {
        nombre_jugador_etiqueta = new Label(jugadores.jugador_actual().nombre, texto_estilo(0.15));
        nombre_jugador_etiqueta.setWrap(true);
        nombre_jugador_etiqueta.setAlignment(center);
        nombre_jugador_scroll = new ScrollPane(nombre_jugador_etiqueta);
        nombre_jugador_scroll.setBounds(0, proporcion_y(0.75), proporcion_x(0.5), proporcion_y(0.25));
        nombre_jugador_scroll.layout();
        nombre_jugador_scroll.setTouchable(Touchable.enabled);

        tiempo_etiqueta = new Label("GO", texto_estilo(0.15));
        tiempo_etiqueta.setWidth(proporcion_x(0.3));
        tiempo_etiqueta.setPosition(proporcion_x(0.525), proporcion_y(0.76));
        tiempo_etiqueta.setWrap(true);
        tiempo_etiqueta.setAlignment(center);

        comodin50_etiqueta = new Label("50 : 50", texto_estilo(0.08));
        comodin50_etiqueta.setWidth(proporcion_x(0.2));
        comodin50_etiqueta.setPosition(proporcion_x(0.775), proporcion_y(0.82));
        comodin50_etiqueta.setWrap(true);
        comodin50_etiqueta.setAlignment(center);

        aciertos_etiqueta = new Label("0", texto_estilo(0.15));
        aciertos_etiqueta.setWidth(proporcion_x(0.1));
        aciertos_etiqueta.setPosition(proporcion_x(0.5) - aciertos_etiqueta.getWidth() / 2, proporcion_y(0.25) - aciertos_etiqueta.getHeight() / 2);
        aciertos_etiqueta.setWrap(true);
        aciertos_etiqueta.setAlignment(center);

        pregunta_etiqueta = new Label("", texto_estilo(0.08));
        pregunta_etiqueta.setWrap(true);
        pregunta_etiqueta.setAlignment(center);
        pregunta_scroll = new ScrollPane(pregunta_etiqueta);
        pregunta_scroll.setBounds(proporcion_x(0.1), proporcion_y(0.525), proporcion_x(0.8), proporcion_y(0.225));
        pregunta_scroll.layout();
        pregunta_scroll.setTouchable(Touchable.enabled);

        respuesta0_etiqueta = new Label("", texto_estilo(0.12));
        respuesta0_etiqueta.setWidth(proporcion_x(0.33));
        respuesta0_etiqueta.setWrap(true);
        respuesta0_etiqueta.setAlignment(center);
        respuesta0_scroll = new ScrollPane(respuesta0_etiqueta);
        respuesta0_scroll.setBounds(proporcion_x(0.05), proporcion_y(0.3), proporcion_x(0.4), proporcion_y(0.2));
        respuesta0_scroll.layout();
        respuesta0_scroll.setTouchable(Touchable.enabled);

        respuesta1_etiqueta = new Label("", texto_estilo(0.12));
        respuesta1_etiqueta.setWrap(true);
        respuesta1_etiqueta.setAlignment(center);
        respuesta1_scroll = new ScrollPane(respuesta1_etiqueta);
        respuesta1_scroll.setBounds(proporcion_x(0.55), proporcion_y(0.3), proporcion_x(0.4), proporcion_y(0.2));
        respuesta1_scroll.layout();
        respuesta1_scroll.setTouchable(Touchable.enabled);

        respuesta2_etiqueta = new Label("", texto_estilo(0.12));
        respuesta2_etiqueta.setWrap(true);
        respuesta2_etiqueta.setAlignment(center);
        respuesta2_scroll = new ScrollPane(respuesta2_etiqueta);
        respuesta2_scroll.setBounds(proporcion_x(0.05), proporcion_y(0.05), proporcion_x(0.4), proporcion_y(0.2));
        respuesta2_scroll.layout();
        respuesta2_scroll.setTouchable(Touchable.enabled);

        respuesta3_etiqueta = new Label("", texto_estilo(0.12));
        respuesta3_etiqueta.setWrap(true);
        respuesta3_etiqueta.setAlignment(center);
        respuesta3_scroll = new ScrollPane(respuesta3_etiqueta);
        respuesta3_scroll.setBounds(proporcion_x(0.55), proporcion_y(0.05), proporcion_x(0.4), proporcion_y(0.2));
        respuesta3_scroll.layout();
        respuesta3_scroll.setTouchable(Touchable.enabled);
    }

    public void texturas_mostrar() {
        if (!jugadores.vacio() && jugadores.jugador_actual().jugando) {

            //Obtener tiempo incremental
            jugadores.jugador_actual().contador += Gdx.graphics.getDeltaTime();
            if (jugadores.jugador_actual().contador >= 1.0f) {
                jugadores.jugador_actual().contador = 0;
                if (jugadores.jugador_actual().jugando)
                    jugadores.jugador_actual().tiempo_qqsm++;
            }

            pregunta_etiqueta.setText(pregunta_actual.pregunta);
            respuesta0_etiqueta.setText(pregunta_actual.respuestas[0]);
            respuesta1_etiqueta.setText(pregunta_actual.respuestas[1]);
            respuesta2_etiqueta.setText(pregunta_actual.respuestas[2]);
            respuesta3_etiqueta.setText(pregunta_actual.respuestas[3]);

            tiempo_etiqueta.setText(Integer.toString(jugadores.jugador_actual().tiempo_qqsm));
            tiempo_etiqueta.draw(batch, 1);
            aciertos_etiqueta.draw(batch, 1);
            comodin50_etiqueta.draw(batch, 1);
        }
        else
            if (!jugadores.vacio()) {
                tiempo_etiqueta.draw(batch, 1);
                aciertos_etiqueta.draw(batch, 1);
            }
    }

    public void texturas_cargar() {
        fondo = tarea.asset.get("data/texturas/fondo/qqsm.png");
    }

    public void usar_comodin50() {
        comodin50_etiqueta.setText("");
        jugadores.jugador_actual().comodin50_usado = true;
        int respuesta_aleatoria1 = MathUtils.random(3), respuesta_aleatoria2 = MathUtils.random(3);

        while (pregunta_actual.correcta.compareTo(pregunta_actual.respuestas[respuesta_aleatoria1]) == 0)
            respuesta_aleatoria1 = MathUtils.random(3);
        pregunta_actual.respuestas[respuesta_aleatoria1] = "";

        while (pregunta_actual.correcta.compareTo(pregunta_actual.respuestas[respuesta_aleatoria2]) == 0 || respuesta_aleatoria1 == respuesta_aleatoria2)
            respuesta_aleatoria2 = MathUtils.random(3);
        pregunta_actual.respuestas[respuesta_aleatoria2] = "";
    }
}
