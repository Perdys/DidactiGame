package com.mygdx.psplbr;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.*;

public class Rosco extends Pantalla {

    PsPlbr juego;
    Stage stage;
    OrthographicCamera camara;
    SpriteBatch batch;
    InputAdapter click;

    Texture fondo, jugando;
    Rectangle boton_inicio, boton_acierto, boton_fallo;
    TextButton boton_nombre;
    Integer n_jugadores, jugador_actual = 0;
    BitmapFont descripcion;
    String descripcion_actual = "";

    public class Jugador {
        String nombre = "Introduzca su nombre y tiempo";
        boolean jugando = false;
        Integer letra_actual = -1, n_aciertos = 0, n_fallos = 0;
        float contador = 0, tiempo = 150;
        int letras[];

        public Jugador(int[] letras) { this.letras = letras; }
    }

    ArrayList<Jugador> jugadores;
    Clasificacion clasificacion;
    ArrayList<ArrayList<String>> descripciones;

    public Rosco(Integer n_jugadores, ArrayList<ArrayList<String>> descripciones, PsPlbr juego) {
        this.juego = juego;
        this.n_jugadores = n_jugadores;
        this.descripciones = descripciones;

        camara = new OrthographicCamera();
        camara.setToOrtho(false);
        stage = new Stage();
        batch = new SpriteBatch();

        pantalla_actual = 2;

        clasificacion = new Clasificacion(juego);
        jugadores = new ArrayList<>(n_jugadores);

        for(int i = 0; i < n_jugadores; ++i)
            jugadores.add(i, new Jugador(new int[26]));

        descripcion = new BitmapFont();
        descripcion.setColor(Color.BLACK);

        botones_crear();
        texturas_cargar();
    }

    public void render(float delta) {	/*ejecutarse todas las veces posible por segundo, ejecuten todas las acciones del juego
		dibujar los elementos por pantalla, procesar la entrada y salida, mover los personajes, detectar colisiones, etc. */
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();

        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.draw(fondo, 0, 0, anchura_juego, altura_juego);
        if (jugadores.get(jugador_actual).jugando)
            batch.draw(jugando, 0, 0, anchura_juego, altura_juego);
        texturas_mostrar();
        batch.end();

        stage.act();
        stage.draw();
    }

    public void resize(int width, int height) {} //sirve para recalcular el tama�o de los elementos cuando se modifica la pantalla

    public void pause() {}	/*, y resume() y pause(), que son funciones que se ejecutan en Android cuando salimos de la aplicaci�n o se interrumpe la ejecuci�n de la misma y volvemos a ella.*/

    public void resume() { pantalla_actual = 2; }

    public void show() {
        stage.addActor(boton_nombre);
        sistema_botones_crear(juego);

        InputMultiplexer inputs = new InputMultiplexer();
        inputs.addProcessor(stage_botones);
        inputs.addProcessor(stage);
        inputs.addProcessor(click);
        Gdx.input.setInputProcessor(inputs);
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);
    }

    public void hide() {}

    public void dispose() { //es la ultima en ejecutarse, se encarga de liberar recursos y dejar la memoria limpia

        Fichero fichero = new Fichero("data/ficheros/rosco.txt");
        fichero.fichero_escribir(descripciones);

        batch.dispose();
        stage.dispose();
    }

    public void botones_crear() {

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = descripcion;
        textButtonStyle.fontColor = Color.BLACK;

        boton_nombre = new TextButton(jugadores.get(jugador_actual).nombre, textButtonStyle);
        boton_nombre.setPosition((float)(anchura_juego * 0.66), (float)(altura_juego * 0.65));

        boton_nombre.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                tiempo_anadir();
                nombre_anadir();
            }
        });

        boton_inicio = new Rectangle((float) (anchura_juego * 0.75), (float) (altura_juego * 0.65), (float) (anchura_juego * 0.07), (float) (altura_juego * 0.08));
        boton_acierto = new Rectangle((float) (anchura_juego * 0.23), (float) (altura_juego * 0.60), (float) (anchura_juego * 0.07), (float) (altura_juego * 0.15));
        boton_fallo = new Rectangle((float) (anchura_juego * 0.42), (float) (altura_juego * 0.60), (float) (anchura_juego * 0.07), (float) (altura_juego * 0.15));

        click = new InputAdapter() {
            public boolean touchUp (int x, int y, int pointer, int button) {

                if (boton_inicio.contains(x, y))
                    if (!jugadores.isEmpty())
                        if (jugadores.get(jugador_actual).jugando) {
                            jugadores.get(jugador_actual).letra_actual = jugadores.get(jugador_actual).letra_actual == 25 ? 0 : jugadores.get(jugador_actual).letra_actual + 1;

                            jugador_siguiente();
                        } else {
                            jugadores.get(jugador_actual).jugando = true;
                            jugadores.get(jugador_actual).letra_actual = jugadores.get(jugador_actual).letra_actual == -1 ? 0 : jugadores.get(jugador_actual).letra_actual;

                            letra_siguiente();
                        }
                if (boton_acierto.contains(x, y))
                    if (!jugadores.isEmpty())
                        if (jugadores.get(jugador_actual).jugando) {
                            jugadores.get(jugador_actual).letras[jugadores.get(jugador_actual).letra_actual] = 1;
                            ++jugadores.get(jugador_actual).n_aciertos;

                            if ((jugadores.get(jugador_actual).n_aciertos + jugadores.get(jugador_actual).n_fallos) < 26)
                                letra_siguiente();
                            else
                                jugador_siguiente();
                        }
                if (boton_fallo.contains(x, y))
                    if (!jugadores.isEmpty())
                        if (jugadores.get(jugador_actual).jugando) {
                            jugadores.get(jugador_actual).letras[jugadores.get(jugador_actual).letra_actual] = 2;
                            ++jugadores.get(jugador_actual).n_fallos;

                            jugador_siguiente();
                        }
                return true; // return true to indicate the event was handled
            }
        };
    }

    public void nombre_anadir() {

        Gdx.input.getTextInput (new Input.TextInputListener() {

            public void input(String descripcion) {
                if (descripcion.isEmpty())
                    descripcion = "Jugador_" + (jugador_actual + 1);
                jugadores.get(jugador_actual).nombre = descripcion;
                boton_nombre.setText(descripcion);
            }

            public void canceled() {
                jugadores.get(jugador_actual).nombre = "Jugador_" + (jugador_actual + 1);
                boton_nombre.setText("Jugador_" + (jugador_actual + 1));
            }

        }, "Introduzca el nombre del jugador", "", "Jugador_" + (jugador_actual + 1));
    }

    public void tiempo_anadir() {

        Gdx.input.getTextInput (new Input.TextInputListener() {

            public void input(String tiempo) {
                if (tiempo.isEmpty())
                    tiempo = "150";
                jugadores.get(jugador_actual).tiempo = Integer.parseInt(tiempo);
            }

            public void canceled() {}

        }, "Introduzca el tiempo inicial", "", "150''");
    }

    public void letra_siguiente() {
        for (; jugadores.get(jugador_actual).letras[jugadores.get(jugador_actual).letra_actual] != 0; ++jugadores.get(jugador_actual).letra_actual)
            if (jugadores.get(jugador_actual).letra_actual == 25)
                jugadores.get(jugador_actual).letra_actual = -1;

        if (descripciones.size() != 0)
            if (descripciones.get(jugadores.get(jugador_actual).letra_actual).size() != 0)
                descripcion_actual = descripciones.get(jugadores.get(jugador_actual).letra_actual)
                                                  .get(MathUtils.random(descripciones.get(jugadores.get(jugador_actual).letra_actual).size()-1));
            else
                descripcion_actual = "INTRODUZCA UNA DESCRIPCION PARA ESTA LETRA";
    }

    public void jugador_siguiente() {
        jugadores.get(jugador_actual).jugando = false;

        if ((jugadores.get(jugador_actual).n_aciertos + jugadores.get(jugador_actual).n_fallos) >= 26) {
            clasificacion.anadir(jugadores.get(jugador_actual).n_aciertos, jugadores.get(jugador_actual).nombre);

            if (n_jugadores == 1)
                juego.setScreen(clasificacion);

            jugadores.remove((int)jugador_actual);
            --n_jugadores;
        }

        if (jugador_actual + 1 >= n_jugadores)
            jugador_actual = 0;
        else
            ++jugador_actual;

        if (!jugadores.isEmpty())
            boton_nombre.setText(jugadores.get(jugador_actual).nombre);
    }

    public void texturas_mostrar() {

        if (!jugadores.isEmpty()) {

            //Mostrar letras, aciertos y fallos
            for (int i = 0; i < 26; ++i) {
                if (jugadores.get(jugador_actual).letras[i] != 0 || jugadores.get(jugador_actual).letra_actual == i) {
                    batch.draw(Menu.elementos_mostrar.letras[i], 0, 0, anchura_juego, altura_juego);
                    if (jugadores.get(jugador_actual).letras[i] == 2)
                        batch.draw(Menu.elementos_mostrar.rojos[i], 0, 0, anchura_juego, altura_juego);
                    else if (jugadores.get(jugador_actual).letras[i] == 1)
                        batch.draw(Menu.elementos_mostrar.verdes[i], 0, 0, anchura_juego, altura_juego);
                }
            }

            //Obtener tiempo restante
            jugadores.get(jugador_actual).contador += Gdx.graphics.getDeltaTime();
            if (jugadores.get(jugador_actual).contador >= 1.0f) {
                jugadores.get(jugador_actual).contador = 0;
                if (jugadores.get(jugador_actual).jugando)
                    jugadores.get(jugador_actual).tiempo--;
            }

            //Comprobar si se ha acabado el tiempo
            if (jugadores.get(jugador_actual).tiempo < 1) {
                jugadores.get(jugador_actual).n_fallos = 26 - jugadores.get(jugador_actual).n_aciertos;
                jugador_siguiente();
            }

            if (!jugadores.isEmpty()) {
                //Mostrar el tiempo y la puntuacion
                batch.draw(Menu.elementos_mostrar.puntuacion[0][jugadores.get(jugador_actual).n_aciertos % 10], 0, 0, anchura_juego, altura_juego);
                batch.draw(Menu.elementos_mostrar.puntuacion[1][jugadores.get(jugador_actual).n_aciertos / 10], 0, 0, anchura_juego, altura_juego);
                batch.draw(Menu.elementos_mostrar.tiempo[0][(int) jugadores.get(jugador_actual).tiempo % 10], 0, 0, anchura_juego, altura_juego);
                batch.draw(Menu.elementos_mostrar.tiempo[1][((int) jugadores.get(jugador_actual).tiempo % 100) / 10], 0, 0, anchura_juego, altura_juego);
                batch.draw(Menu.elementos_mostrar.tiempo[2][((int) jugadores.get(jugador_actual).tiempo % 1000) / 100], 0, 0, anchura_juego, altura_juego);

                //Mostrar la descripcion de la letra actual
                if (jugadores.get(jugador_actual).letra_actual > -1)
                    descripcion.draw(batch, descripcion_actual, (float)(anchura_juego * 0.2), (float)(altura_juego * 0.68));
            }
        }
    }

    public void texturas_cargar() {
        fondo = new Texture("data/rosco/fondo_rosco.jpg");
        jugando = new Texture("data/rosco/boton_on.png");
    }
}
