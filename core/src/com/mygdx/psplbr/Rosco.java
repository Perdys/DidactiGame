package com.mygdx.psplbr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.*;

public class Rosco extends Pantalla {

    PsPlbr juego;
    Stage stage;
    OrthographicCamera camara;
    SpriteBatch batch;

    Texture fondo;
    ImageButton boton_inicio_parada, boton_reinicio, boton_acierto, boton_fallo;
    TextButton boton_nombre;
    Integer n_jugadores, jugador_actual = 0;
    BitmapFont descripcion;
    String descripcion_actual = "";

    public class Jugador {
        String nombre = "Introduzca su nombre";
        boolean jugando = false;
        Integer letra_actual = -1, n_aciertos = 0, n_fallos = 0;
        float contador = 0, tiempo = 20;
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

        jugadores = new ArrayList<>(n_jugadores);
        clasificacion = new Clasificacion(juego);

        for(int i = 0; i < n_jugadores; ++i)
            jugadores.add(i, new Jugador(new int[26]));

        descripcion = new BitmapFont();
        descripcion.setColor(Color.RED);

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
        stage.addActor(boton_inicio_parada);
        stage.addActor(boton_reinicio);
        stage.addActor(boton_acierto);
        stage.addActor(boton_fallo);
        sistema_botones_crear(juego);

        InputMultiplexer inputs = new InputMultiplexer();
        inputs.addProcessor(stage_botones);
        inputs.addProcessor(stage);
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

    public ImageButton imagen_texto_boton_crear(final String imagen) {
        ImageButton.ImageButtonStyle estilo_boton = new ImageButton.ImageButtonStyle();
        Skin skin = new Skin();
        skin.add("boton", new Texture(imagen));
        estilo_boton.up = skin.getDrawable("boton");
        estilo_boton.unpressedOffsetY = -(lado / 6);
        estilo_boton.pressedOffsetY = -(lado / 6);

        ImageButton boton = new ImageButton(estilo_boton);
        boton.setSize(anchura_juego / 7, altura_juego / 7);

        boton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switch (imagen) {
                    case "data/rosco/boton_inicio_parada.jpg":
                        if (!jugadores.isEmpty())
                            if (jugadores.get(jugador_actual).jugando) {
                                jugadores.get(jugador_actual).letra_actual = jugadores.get(jugador_actual).letra_actual == 25 ? 0 : jugadores.get(jugador_actual).letra_actual + 1;

                                jugador_siguiente();
                            } else {
                                jugadores.get(jugador_actual).jugando = true;
                                jugadores.get(jugador_actual).letra_actual = jugadores.get(jugador_actual).letra_actual == -1 ? 0 : jugadores.get(jugador_actual).letra_actual;

                                letra_siguiente();
                            }
                        break;
                    case "data/rosco/boton_reinicio.jpg": juego.setScreen(PsPlbr.menu);
                        break;
                    case "data/rosco/boton_acierto.jpg":
                        if (!jugadores.isEmpty())
                            if (jugadores.get(jugador_actual).jugando) {
                                jugadores.get(jugador_actual).letras[jugadores.get(jugador_actual).letra_actual] = 1;
                                ++jugadores.get(jugador_actual).n_aciertos;

                                if ((jugadores.get(jugador_actual).n_aciertos + jugadores.get(jugador_actual).n_fallos) < 26)
                                    letra_siguiente();
                                else
                                    jugador_siguiente();
                            }
                        break;
                    case "data/rosco/boton_fallo.jpg":
                        if (!jugadores.isEmpty())
                            if (jugadores.get(jugador_actual).jugando) {
                                jugadores.get(jugador_actual).letras[jugadores.get(jugador_actual).letra_actual] = 2;
                                ++jugadores.get(jugador_actual).n_fallos;

                                jugador_siguiente();
                            }
                        break;
                    default: juego.setScreen(new Menu(juego));
                        break;
                }
            }
        });
        return boton;
    }

    public TextButton texto_boton_crear() {
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = descripcion;
        textButtonStyle.fontColor = Color.ORANGE;

        TextButton boton = new TextButton(jugadores.get(jugador_actual).nombre, textButtonStyle);

        boton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                nombre_anadir();
            }
        });
        return boton;
    }

    public void nombre_anadir() {

        Gdx.input.getTextInput (new Input.TextInputListener() {

            public void input(String descripcion) {
                jugadores.get(jugador_actual).nombre = descripcion;
                boton_nombre.setText(descripcion);
            }

            public void canceled() {}
        }, "Introduzca el nombre del jugador", "", "N_A");
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
                    descripcion.draw(batch, descripcion_actual, (float) (anchura_juego * 0.33), altura_juego / 2);
            }
        }
    }


    public void texturas_cargar() {
        fondo = new Texture("data/rosco/fondo_rosco.jpg");
        boton_nombre = texto_boton_crear();
        boton_nombre.setPosition((float)(anchura_juego * 0.33), (float)(altura_juego * 0.25));
        boton_inicio_parada = imagen_texto_boton_crear("data/rosco/boton_inicio_parada.jpg");
        boton_inicio_parada.setPosition(anchura_juego - boton_inicio_parada.getWidth() * 3, (float)(altura_juego * 0.40));
        boton_reinicio = imagen_texto_boton_crear("data/rosco/boton_reinicio.jpg");
        boton_reinicio.setPosition(anchura_juego - boton_reinicio.getWidth() * 2, (float)(altura_juego * 0.40));
        boton_acierto = imagen_texto_boton_crear("data/rosco/boton_acierto.jpg");
        boton_acierto.setPosition(anchura_juego - boton_acierto.getWidth() * 3, (float)(altura_juego * 0.40) - boton_acierto.getHeight());
        boton_fallo = imagen_texto_boton_crear("data/rosco/boton_fallo.jpg");
        boton_fallo.setPosition(anchura_juego - boton_fallo.getWidth() * 2, (float)(altura_juego * 0.40) - boton_fallo.getHeight());
    }
}
