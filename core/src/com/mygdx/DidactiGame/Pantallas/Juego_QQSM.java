package com.mygdx.DidactiGame.Pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.mygdx.DidactiGame.Auxiliares.Pantalla;
import com.mygdx.DidactiGame.DidactiGame;

import java.util.ArrayList;

import static com.badlogic.gdx.utils.Align.top;
import static com.badlogic.gdx.utils.Align.topLeft;
import static com.mygdx.DidactiGame.DidactiGame.BD;
import static com.mygdx.DidactiGame.DidactiGame.jugadores;
import static com.mygdx.DidactiGame.Pantallas.Menus.Menu_Juegos.*;

public class Juego_QQSM extends Pantalla{

    DidactiGame juego;
    OrthographicCamera camara;
    SpriteBatch batch;
    Stage stage;
    InputAdapter click;

    Texture fondo, comodin50;
    Rectangle boton_comodin50, boton_inicio;
    Rectangle boton_respuesta0, boton_respuesta1, boton_respuesta2, boton_respuesta3;
    Label pregunta_etiqueta, respuesta0_etiqueta, respuesta1_etiqueta, respuesta2_etiqueta, respuesta3_etiqueta;
    Label tiempo_etiqueta, aciertos_etiqueta;
    ScrollPane pregunta_scroll, respuesta0_scroll, respuesta1_scroll, respuesta2_scroll, respuesta3_scroll;
    int pregunta_actual = 0;

    Clasificacion clasificacion;

    ArrayList<Pregunta> preguntas;

    public Juego_QQSM(DidactiGame juego) {
        this.juego = juego;

        camara = new OrthographicCamera();
        camara.setToOrtho(false);
        batch = new SpriteBatch();
        stage = new Stage();

        pantalla_actual = "Juego_QQSM";

        clasificacion = new Clasificacion(juego);

        preguntas = new ArrayList<>();
        BD.leer_preguntas(preguntas);

        texturas_cargar();
        botones_cargar();
        textos_cargar();
    }

    public void render (float delta) {
        Gdx.gl.glClearColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, Color.WHITE.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();

        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.draw(fondo, 0, 0, anchura_juego, altura_juego);
        texturas_mostrar();
        batch.end();

        stage.act();
        stage.draw();
    }

    public void show () {
        sistema_botones(juego);
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

        boton_inicio = new Rectangle(0, 0, proporcion_x(0.5), proporcion_y(0.5));
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
                        jugadores.jugador_actual().jugando = true;
                        break;
                    case 5:
                        if (jugadores.jugador_actual().jugando)
                            usar_comodin50();
                        break;
                    default:
                        if (jugadores.jugador_actual().jugando) {
                            if (preguntas.get(pregunta_actual).correcta.contains(preguntas.get(pregunta_actual).respuestas[eleccion])) {
                                //acierto
                                aciertos_etiqueta.setText(Integer.toString(Integer.valueOf(aciertos_etiqueta.getText().toString()) + 1));
                                ++pregunta_actual;
                            } else {
                                //fallo
                                jugadores.jugador_actual().jugando = false;
                                siguiente_jugador();
                            }
                        } break;
                }
    }

    public void siguiente_jugador() {}

    public void textos_cargar() {
        tiempo_etiqueta = new Label("", texto_estilo(0.55));
        tiempo_etiqueta.setWidth(proporcion_x(0.1));
        tiempo_etiqueta.setPosition(proporcion_x(0.5) - tiempo_etiqueta.getWidth() / 2, proporcion_y(0.8));
        tiempo_etiqueta.setWrap(true);
        tiempo_etiqueta.setAlignment(top);

        aciertos_etiqueta = new Label("0", texto_estilo(0.55));
        aciertos_etiqueta.setWidth(proporcion_x(0.1));
        aciertos_etiqueta.setPosition(proporcion_x(0.5) - aciertos_etiqueta.getWidth() / 2, proporcion_y(0.25) - aciertos_etiqueta.getHeight() / 2);
        aciertos_etiqueta.setWrap(true);
        aciertos_etiqueta.setAlignment(top);

        pregunta_etiqueta = new Label("", texto_estilo(0.55));
        pregunta_etiqueta.setWidth(proporcion_x(0.33));
        pregunta_etiqueta.setWrap(true);
        pregunta_etiqueta.setAlignment(topLeft);
        pregunta_scroll = new ScrollPane(pregunta_etiqueta);
        pregunta_scroll.setBounds(0, proporcion_y(0.5), proporcion_x(0.8), proporcion_y(0.25));
        pregunta_scroll.layout();
        pregunta_scroll.setTouchable(Touchable.enabled);

        respuesta0_etiqueta = new Label("", texto_estilo(0.55));
        respuesta0_etiqueta.setWidth(proporcion_x(0.33));
        respuesta0_etiqueta.setWrap(true);
        respuesta0_etiqueta.setAlignment(top);
        respuesta0_scroll = new ScrollPane(respuesta0_etiqueta);
        respuesta0_scroll.setBounds(0, proporcion_y(0.25), proporcion_x(0.5), proporcion_y(0.25));
        respuesta0_scroll.layout();
        respuesta0_scroll.setTouchable(Touchable.enabled);

        respuesta1_etiqueta = new Label("", texto_estilo(0.55));
        respuesta1_etiqueta.setWidth(proporcion_x(0.33));
        respuesta1_etiqueta.setWrap(true);
        respuesta1_etiqueta.setAlignment(top);
        respuesta1_scroll = new ScrollPane(respuesta1_etiqueta);
        respuesta1_scroll.setBounds(proporcion_x(0.5), proporcion_y(0.25), proporcion_x(0.5), proporcion_y(0.25));
        respuesta1_scroll.layout();
        respuesta1_scroll.setTouchable(Touchable.enabled);

        respuesta2_etiqueta = new Label("", texto_estilo(0.55));
        respuesta2_etiqueta.setWidth(proporcion_x(0.33));
        respuesta2_etiqueta.setWrap(true);
        respuesta2_etiqueta.setAlignment(top);
        respuesta2_scroll = new ScrollPane(respuesta2_etiqueta);
        respuesta2_scroll.setBounds(0, 0, proporcion_x(0.5), proporcion_y(0.25));
        respuesta2_scroll.layout();
        respuesta2_scroll.setTouchable(Touchable.enabled);

        respuesta3_etiqueta = new Label("", texto_estilo(0.055));
        respuesta3_etiqueta.setWidth(proporcion_x(0.33));
        respuesta3_etiqueta.setWrap(true);
        respuesta3_etiqueta.setAlignment(top);
        respuesta3_scroll = new ScrollPane(respuesta3_etiqueta);
        respuesta3_scroll.setBounds(proporcion_x(0.5), 0, proporcion_x(0.5), proporcion_y(0.25));
        respuesta3_scroll.layout();
        respuesta3_scroll.setTouchable(Touchable.enabled);
    }

    public void texturas_mostrar() {
        if (!jugadores.vacio()) {

            //Obtener tiempo incremental
            jugadores.jugador_actual().contador_qqsm += Gdx.graphics.getDeltaTime();
            if (jugadores.jugador_actual().contador_qqsm >= 1.0f) {
                jugadores.jugador_actual().contador_qqsm = 0;
                if (jugadores.jugador_actual().jugando)
                    jugadores.jugador_actual().tiempo_qqsm++;
            }

            if (!jugadores.jugador_actual().comodin50_usado)
                batch.draw(comodin50, 0, 0);

            tiempo_etiqueta.draw(batch, 1);
            aciertos_etiqueta.draw(batch, 1);
        }
    }

    public void texturas_cargar() {
        fondo = new Texture("data/texturas/fondo/qqsm.png");
        comodin50 = new Texture("data/texturas/juego_qqsm/comodin50.png");
    }

    public void usar_comodin50() {}
}
