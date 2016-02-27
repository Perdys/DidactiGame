package com.mygdx.psplbr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.*;

public class Rosco extends Pantalla {

    PsPlbr juego;
    Stage stage;
    OrthographicCamera camara;
    SpriteBatch batch;

    Texture fondo;
    ImageButton boton_inicio_parada, boton_reinicio, boton_acierto, boton_fallo;
    Integer n_jugadores, jugador_actual = 0;
    BitmapFont descripcion;

    public class Jugador {
        String nombre;
        boolean jugando = false;
        Integer letra_actual = -1, n_aciertos = 0, n_fallos = 0;
        float contador = 0, tiempo = 150;
        int letras[];

        public Jugador(int[] letras) { this.letras = letras; }
    }

    ArrayList<Jugador> jugadores;
    ArrayList<String> clasificacion;
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
        clasificacion = new ArrayList<>(n_jugadores);

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
        batch.draw(fondo, 0, 0);
        texturas_mostrar();
        batch.end();

        stage.act();
        stage.draw();
    }

    public void resize(int width, int height) {} //sirve para recalcular el tama�o de los elementos cuando se modifica la pantalla

    public void pause() {}	/*, y resume() y pause(), que son funciones que se ejecutan en Android cuando salimos de la aplicaci�n o se interrumpe la ejecuci�n de la misma y volvemos a ella.*/

    public void resume() {}

    public void show() {
        stage.addActor(boton_inicio_parada);
        stage.addActor(boton_reinicio);
        stage.addActor(boton_acierto);
        stage.addActor(boton_fallo);
        setStageButton(juego);

        InputMultiplexer inputs = new InputMultiplexer();
        inputs.addProcessor(stage_botones);
        inputs.addProcessor(stage);
        Gdx.input.setInputProcessor(inputs);
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);
    }

    public void hide() { dispose(); }

    public void dispose() { //es la ultima en ejecutarse, se encarga de liberar recursos y dejar la memoria limpia

        Fichero fichero = new Fichero("data/ficheros/rosco.txt");
        fichero.fichero_escribir(descripciones);

        batch.dispose();
        stage.dispose();
    }

    public ImageButton setButton(final String imagen) {
        ImageButton.ImageButtonStyle estilo_boton = new ImageButton.ImageButtonStyle();
        Skin skin = new Skin();
        skin.add("boton", new Texture(imagen));
        estilo_boton.up = skin.getDrawable("boton");
        estilo_boton.unpressedOffsetY = -(lado / 6);
        estilo_boton.pressedOffsetY = -(lado / 6);

        ImageButton boton = new ImageButton(estilo_boton);
        boton.setSize(anchura_juego / 5, altura_juego / 5);

        boton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switch (imagen) {
                    case "data/rosco/boton_inicio_parada.jpg":
                        if (jugadores.get(jugador_actual).jugando) {
                            jugadores.get(jugador_actual).letra_actual = jugadores.get(jugador_actual).letra_actual == 25 ? 0 : jugadores.get(jugador_actual).letra_actual + 1;

                            jugador_siguiente();
                        } else {
                            jugadores.get(jugador_actual).jugando = true;
                            jugadores.get(jugador_actual).letra_actual = jugadores.get(jugador_actual).letra_actual == -1 ? 0 : jugadores.get(jugador_actual).letra_actual;

                            letra_siguiente();
                        }
                        break;
                    case "data/rosco/boton_reinicio.jpg": juego.setScreen(new Menu(juego));
                        break;
                    case "data/rosco/boton_acierto.jpg":
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

    public void letra_siguiente() {
        for (; jugadores.get(jugador_actual).letras[jugadores.get(jugador_actual).letra_actual] != 0; ++jugadores.get(jugador_actual).letra_actual)
            if (jugadores.get(jugador_actual).letra_actual == 25)
                jugadores.get(jugador_actual).letra_actual = -1;
    }

    public void jugador_siguiente() {
        jugadores.get(jugador_actual).jugando = false;

        if ((jugadores.get(jugador_actual).n_aciertos + jugadores.get(jugador_actual).n_fallos) >= 26) {
            clasificacion.add(jugadores.get(jugador_actual).n_aciertos - jugadores.get(jugador_actual).n_fallos, jugadores.get(jugador_actual).nombre);

            if (n_jugadores == 1)
                juego.setScreen(new Clasificacion(clasificacion, juego));

            jugadores.remove((int)jugador_actual);
            --n_jugadores;
        }

            if (jugador_actual + 1 == n_jugadores)
            jugador_actual = 0;
        else
            ++jugador_actual;
    }

    public void texturas_mostrar() {

        //Mostrar letras, aciertos y fallos
        for (int i = 0; i < 26; ++i) {
            if (jugadores.get(jugador_actual).letras[i] != 0 || jugadores.get(jugador_actual).letra_actual == i) {
                batch.draw(((Texture[]) (Menu.elementos_mostrar[0]))[i], 0, 0);
                if (jugadores.get(jugador_actual).letras[i] == 2)
                    batch.draw(((Texture[]) (Menu.elementos_mostrar[3]))[i], 0, 0);
                else if (jugadores.get(jugador_actual).letras[i] == 1)
                    batch.draw(((Texture[]) (Menu.elementos_mostrar[4]))[i], 0, 0);
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
        if (jugadores.get(jugador_actual).tiempo < 2) {
            jugadores.get(jugador_actual).n_fallos = 26 - jugadores.get(jugador_actual).n_aciertos;
            jugador_siguiente();
        }

        //Mostrar el tiempo y la puntuacion
        batch.draw(((Texture[][])(Menu.elementos_mostrar[1]))[0][jugadores.get(jugador_actual).n_aciertos%10], 0, 0);
        batch.draw(((Texture[][])(Menu.elementos_mostrar[1]))[1][jugadores.get(jugador_actual).n_aciertos/10], 0, 0);
        batch.draw(((Texture[][])(Menu.elementos_mostrar[2]))[0][(int)jugadores.get(jugador_actual).tiempo%10], 0, 0);
        batch.draw(((Texture[][])(Menu.elementos_mostrar[2]))[1][((int)jugadores.get(jugador_actual).tiempo%100)/10], 0, 0);
        batch.draw(((Texture[][])(Menu.elementos_mostrar[2]))[2][((int)jugadores.get(jugador_actual).tiempo%1000)/100], 0, 0);

        //Mostrar la descripcion de la letra actual
        if (descripciones.size() > jugadores.get(jugador_actual).letra_actual && jugadores.get(jugador_actual).letra_actual > -1)
            if (descripciones.get(jugadores.get(jugador_actual).letra_actual).size() > 0)
                descripcion.draw(batch, descripciones.get(jugadores.get(jugador_actual).letra_actual)
                                                     .get(MathUtils.random(descripciones.get(jugadores.get(jugador_actual).letra_actual).size())), 0, altura_juego);
    }

    public void texturas_cargar() {
        fondo = new Texture("data/rosco/fondo_rosco.jpg");
        boton_inicio_parada = setButton("data/rosco/boton_inicio_parada.jpg");
        boton_inicio_parada.setPosition(anchura_juego - boton_inicio_parada.getWidth() * 2, (float)(altura_juego * 0.33));
        boton_reinicio = setButton("data/rosco/boton_reinicio.jpg");
        boton_reinicio.setPosition(anchura_juego - boton_reinicio.getWidth(), (float)(altura_juego * 0.33));
        boton_acierto = setButton("data/rosco/boton_acierto.jpg");
        boton_acierto.setPosition(anchura_juego - boton_acierto.getWidth() * 2, (float)(altura_juego * 0.33) - boton_acierto.getHeight());
        boton_fallo = setButton("data/rosco/boton_fallo.jpg");
        boton_fallo.setPosition(anchura_juego - boton_fallo.getWidth(), (float)(altura_juego * 0.33) - boton_fallo.getHeight());
    }
}
