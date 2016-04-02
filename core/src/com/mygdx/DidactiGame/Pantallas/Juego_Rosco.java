package com.mygdx.DidactiGame.Pantallas;

import com.badlogic.gdx.*;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.mygdx.DidactiGame.DidactiGame;
import com.mygdx.DidactiGame.Herramientas.Pantalla;

import java.awt.*;
import java.util.*;

public class Juego_Rosco extends Pantalla {

    DidactiGame juego;
    OrthographicCamera camara;
    SpriteBatch batch;
    InputAdapter click;
    Texture fondo, boton_jugando;
    Rectangle boton_inicio, boton_acierto, boton_fallo, boton_pregunta, boton_nombre, boton_tiempo;
    Integer n_jugadores, jugador_actual = 0;

    BitmapFont texto_fuente;
    String[] descripcion_actual = {"", ""};
    GlyphLayout texto_estilo;

    public class Jugador {
        String nombre = "Nombre";
        boolean jugando = false;
        Integer letra_actual = -1, n_aciertos = 0, n_fallos = 0;
        float contador = 0;
        int letras[], tiempo = 150;
        Color fondo_color = Color.BLUE;

        public Jugador(int[] letras) { this.letras = letras; }
    }

    ArrayList<Jugador> jugadores;
    Clasificacion clasificacion;
    ArrayList<ArrayList<String[]>> descripciones;

    public Juego_Rosco(ArrayList<String> nombres_jugadores, ArrayList<ArrayList<String[]>> descripciones, DidactiGame juego) {
        this.juego = juego;
        this.n_jugadores = nombres_jugadores.size();
        this.descripciones = descripciones;

        camara = new OrthographicCamera();
        camara.setToOrtho(false);
        batch = new SpriteBatch();

        pantalla_actual = "Juego_Rosco";

        clasificacion = new Clasificacion(juego);
        jugadores = new ArrayList<>(n_jugadores);

        for(int i = 0; i < n_jugadores; ++i) {
            Jugador jug = new Jugador(new int[26]);
            if (i < n_jugadores)
                jug.nombre = nombres_jugadores.get(i);
            jugadores.add(i, jug);
        }

        if (n_jugadores == 0) {
            jugadores.add(0, new Jugador(new int[26]));
            n_jugadores = 1;
        }

        tamano_texto.size = (int)proporcion_y(0.06);
        texto_estilo = new GlyphLayout();
        texto_fuente = generador_texto.generateFont(tamano_texto);

        fondo = new Texture("data/menu_juegos/juego_rosco/fondo_rosco.png");
        boton_jugando = new Texture("data/menu_juegos/juego_rosco/boton_on.png");

        botones();
    }

    public void render(float delta) {	/*ejecutarse todas las veces posible por segundo, ejecuten todas las acciones del juego
		dibujar los elementos por pantalla, procesar la entrada y salida, mover los personajes, detectar colisiones, etc. */
        Gdx.gl.glClearColor(jugadores.get(jugador_actual).fondo_color.r, jugadores.get(jugador_actual).fondo_color.g,
                            jugadores.get(jugador_actual).fondo_color.b, jugadores.get(jugador_actual).fondo_color.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();

        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.draw(fondo, 0, 0, anchura_juego, altura_juego);
        if (jugadores.get(jugador_actual).jugando)
            batch.draw(boton_jugando, 0, 0, anchura_juego, altura_juego);
        texturas_mostrar();
        batch.end();
    }

    public void resume() { pantalla_actual = "Juego_Rosco"; }

    public void show() {
        sistema_botones(juego);

        InputMultiplexer inputs = new InputMultiplexer();
        inputs.addProcessor(botones_genericos);
        inputs.addProcessor(click);
        Gdx.input.setInputProcessor(inputs);
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);
    }

    public void dispose() { //es la ultima en ejecutarse, se encarga de liberar recursos y dejar la memoria limpia

        batch.dispose();
    }

    public void botones () {

        boton_inicio = new Rectangle(proporcion_x(0.75), proporcion_y(0.65), proporcion_x(0.07), proporcion_y(0.1));
        boton_acierto = new Rectangle(proporcion_x(0.14), proporcion_y(0.60), proporcion_x(0.07), proporcion_y(0.15));
        boton_fallo = new Rectangle(proporcion_x(0.49), proporcion_y(0.60), proporcion_x(0.07), proporcion_y(0.15));
        boton_pregunta = new Rectangle(proporcion_x(0.31), proporcion_y(0.60), proporcion_x(0.07), proporcion_y(0.15));
        boton_nombre = new Rectangle(proporcion_x(0.65), proporcion_y(0.29), proporcion_x(0.17), proporcion_y(0.08));
        boton_tiempo = new Rectangle(proporcion_x(0.66), proporcion_y(0.51), proporcion_x(0.14), proporcion_y(0.1));

        click = new InputAdapter() {
            public boolean touchUp (int x, int y, int pointer, int button) {
                if (boton_acierto.contains(x, y)) {
                    if (!jugadores.isEmpty())
                        if (jugadores.get(jugador_actual).jugando) {
                            jugadores.get(jugador_actual).letras[jugadores.get(jugador_actual).letra_actual] = 1;
                            ++jugadores.get(jugador_actual).n_aciertos;

                            if ((jugadores.get(jugador_actual).n_aciertos + jugadores.get(jugador_actual).n_fallos) < 26)
                                letra_siguiente();
                            else
                                jugador_siguiente();
                        }
                } else
                if (boton_fallo.contains(x, y)) {
                    if (!jugadores.isEmpty())
                        if (jugadores.get(jugador_actual).jugando) {
                            jugadores.get(jugador_actual).letras[jugadores.get(jugador_actual).letra_actual] = 2;
                            ++jugadores.get(jugador_actual).n_fallos;

                            jugador_siguiente();
                        }
                } else
                if (boton_inicio.contains(x, y)) {
                    if (!jugadores.isEmpty())
                        if (jugadores.get(jugador_actual).jugando) {
                            jugadores.get(jugador_actual).letra_actual = jugadores.get(jugador_actual).letra_actual == 25 ? 0 : jugadores.get(jugador_actual).letra_actual + 1;

                            jugador_siguiente();
                        } else {
                            jugadores.get(jugador_actual).jugando = true;
                            jugadores.get(jugador_actual).letra_actual = jugadores.get(jugador_actual).letra_actual == -1 ? 0 : jugadores.get(jugador_actual).letra_actual;

                            letra_siguiente();
                        }
                } else
                if (boton_pregunta.contains(x, y)) {
                    if (!jugadores.isEmpty())
                        if (jugadores.get(jugador_actual).jugando) {
                            String aux = descripcion_actual[0];
                            descripcion_actual[0] = descripcion_actual[1];
                            descripcion_actual[1] = aux;
                        }
                } else
                if (boton_nombre.contains(x, y)) {
                    if (!jugadores.isEmpty())
                        Gdx.input.getTextInput (new Input.TextInputListener() {

                            public void input(String nombre_entrada) {
                                if (nombre_entrada.isEmpty())
                                    nombre_entrada = "Jugador_" + (jugador_actual + 1);
                                jugadores.get(jugador_actual).nombre = nombre_entrada;
                            }

                            public void canceled() {
                                jugadores.get(jugador_actual).nombre = "Jugador_" + (jugador_actual + 1);
                            }

                        }, "Introduzca el nombre del jugador", "", "Jugador_" + (jugador_actual + 1));
                } else
                if (boton_tiempo.contains(x, y)) {
                    if (!jugadores.isEmpty())
                        Gdx.input.getTextInput (new Input.TextInputListener() {

                            public void input(String tiempo) {
                                if (tiempo.isEmpty())
                                    tiempo = "150";
                                jugadores.get(jugador_actual).tiempo = Integer.parseInt(tiempo);
                            }

                            public void canceled() {}

                        }, "Introduzca el tiempo inicial", "", "150''");
                }

                return false;
            }
        };
    }

    public void letra_siguiente() {
        for (; jugadores.get(jugador_actual).letras[jugadores.get(jugador_actual).letra_actual] != 0; ++jugadores.get(jugador_actual).letra_actual)
            if (jugadores.get(jugador_actual).letra_actual == 25)
                jugadores.get(jugador_actual).letra_actual = -1;

        if (descripciones.size() != 0)
            if (descripciones.get(jugadores.get(jugador_actual).letra_actual).size() != 0) {
                int descripcion_aleatoria = MathUtils.random(descripciones.get(jugadores.get(jugador_actual).letra_actual).size() - 1);
                descripcion_actual = new String[]{descripciones.get(jugadores.get(jugador_actual).letra_actual).get(descripcion_aleatoria)[1],
                                                  descripciones.get(jugadores.get(jugador_actual).letra_actual).get(descripcion_aleatoria)[0]};
            }
            else
                descripcion_actual = new String[]{"INTRODUZCA UNA DESCRIPCION PARA ESTA LETRA", "NINGUNA"};
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
    }

    public void texturas_mostrar() {

        if (!jugadores.isEmpty()) {

            //Mostrar letras, aciertos y fallos
            for (int i = 0; i < 26; ++i) {
                if (jugadores.get(jugador_actual).letras[i] != 0 || jugadores.get(jugador_actual).letra_actual == i) {
                    batch.draw(Menu_Juegos.elementos_mostrar.letras[i], 0, 0, anchura_juego, altura_juego);
                    if (jugadores.get(jugador_actual).letras[i] == 2)
                        batch.draw(Menu_Juegos.elementos_mostrar.rojos[i], 0, 0, anchura_juego, altura_juego);
                    else if (jugadores.get(jugador_actual).letras[i] == 1)
                        batch.draw(Menu_Juegos.elementos_mostrar.verdes[i], 0, 0, anchura_juego, altura_juego);
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
                batch.draw(Menu_Juegos.elementos_mostrar.puntuacion[0][jugadores.get(jugador_actual).n_aciertos % 10], 0, 0, anchura_juego, altura_juego);
                batch.draw(Menu_Juegos.elementos_mostrar.puntuacion[1][jugadores.get(jugador_actual).n_aciertos / 10], 0, 0, anchura_juego, altura_juego);
                batch.draw(Menu_Juegos.elementos_mostrar.tiempo[0][jugadores.get(jugador_actual).tiempo % 10], 0, 0, anchura_juego, altura_juego);
                batch.draw(Menu_Juegos.elementos_mostrar.tiempo[1][(jugadores.get(jugador_actual).tiempo % 100) / 10], 0, 0, anchura_juego, altura_juego);
                batch.draw(Menu_Juegos.elementos_mostrar.tiempo[2][(jugadores.get(jugador_actual).tiempo % 1000) / 100], 0, 0, anchura_juego, altura_juego);

                //Mostrar la texto de la letra actual
                if (jugadores.get(jugador_actual).letra_actual > -1) {
                    texto_estilo.setText(texto_fuente, descripcion_actual[0], Color.BLACK, proporcion_x(0.33), 8, true);
                    texto_fuente.draw(batch, texto_estilo, proporcion_x(0.2), proporcion_y(0.69));
                    /*Label etiketa = new Label();
                    Table tabla = new Table();
                    ScrollPane panel_scroll = new ScrollPane(batch);*/
                }

                //Mostrar el nombre del jugador actual
                if (!jugadores.isEmpty()) {
                    texto_estilo.setText(texto_fuente, jugadores.get(jugador_actual).nombre, Color.BLACK, proporcion_x(0.14), 8, true);
                    texto_fuente.draw(batch, texto_estilo, proporcion_x(0.66), proporcion_y(0.68));
                }
            }
        }
    }
}
