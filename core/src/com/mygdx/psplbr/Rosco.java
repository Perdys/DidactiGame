package com.mygdx.psplbr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

public class Rosco extends Pantalla {

    PsPlbr juego;
    Stage stage;
    OrthographicCamera camara;
    SpriteBatch batch;

    Texture fondo;
    ImageButton boton_inicio_parada, boton_reinicio, boton_acierto, boton_fallo;


    public class Jugador {
        boolean jugando = false;
        Integer letra_actual = -1, n_aciertos = 0, n_fallos = 0;
        float contador = 0, tiempo = 150;
        int letras[];
    }

    ArrayList<Jugador> jugadores;

    public Rosco(PsPlbr juego) {
        this.juego = juego;

        camara = new OrthographicCamera();
        camara.setToOrtho(false);
        stage = new Stage();
        batch = new SpriteBatch();

        jugadores = new ArrayList<>(n_jugadores);

        for(int i = 0; i < n_jugadores; ++i) {
            Jugador jugador = new Jugador();
            jugador.letras = new int[26];
            jugadores.add(i, jugador);
        }

        pantalla_actual = 2;

        fondo = new Texture("data/rosco/fondo_rosco.jpg");
        boton_inicio_parada = setButton("data/rosco/boton_inicio_parada.jpg");
        boton_inicio_parada.setPosition((float)(anchura_juego * 0.75) - boton_inicio_parada.getWidth(), (float)(altura_juego * 0.33));
        boton_reinicio = setButton("data/rosco/boton_reinicio.jpg");
        boton_reinicio.setPosition((float)(anchura_juego * 0.75), (float)(altura_juego * 0.33));
        boton_acierto = setButton("data/rosco/boton_acierto.jpg");
        boton_acierto.setPosition((float)(anchura_juego * 0.75) - boton_acierto.getWidth(), (float)(altura_juego * 0.33) - boton_acierto.getHeight());
        boton_fallo = setButton("data/rosco/boton_fallo.jpg");
        boton_fallo.setPosition((float)(anchura_juego * 0.75), (float)(altura_juego * 0.33) - boton_fallo.getHeight());
    }

    public void render(float delta) {	/*ejecutarse todas las veces posible por segundo, ejecuten todas las acciones del juego
		dibujar los elementos por pantalla, procesar la entrada y salida, mover los personajes, detectar colisiones, etc. */
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();

        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.draw(fondo, 0, 0);
        mostrar_elementos();
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

        boton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switch (imagen) {
                    case "data/rosco/boton_inicio_parada.jpg":
                        if (jugadores.get(jugador_actual).jugando) {
                            jugadores.get(jugador_actual).jugando = false;
                            siguiente_jugador();
                        } else {
                            jugadores.get(jugador_actual).jugando = true;
                            ++jugadores.get(jugador_actual).letra_actual;
                        }
                        break;
                    case "data/rosco/boton_reinicio.jpg": juego.setScreen(new Menu(juego));
                        break;
                    case "data/rosco/boton_acierto.jpg":
                        if (jugadores.get(jugador_actual).jugando) {
                            jugadores.get(jugador_actual).letras[jugadores.get(jugador_actual).letra_actual] = 1;
                            ++jugadores.get(jugador_actual).letra_actual;
                            ++jugadores.get(jugador_actual).n_aciertos;
                        }
                        break;
                    case "data/rosco/boton_fallo.jpg":
                        if (jugadores.get(jugador_actual).jugando) {
                            jugadores.get(jugador_actual).letras[jugadores.get(jugador_actual).letra_actual] = 2;
                            ++jugadores.get(jugador_actual).letra_actual;
                            ++jugadores.get(jugador_actual).n_fallos;
                            jugadores.get(jugador_actual).jugando = false;
                            siguiente_jugador();
                        }
                        break;
                    default: break;
                }
            }
        });
        return boton;
    }

    public void siguiente_jugador() {
        if (jugador_actual + 1 == n_jugadores)
            jugador_actual = 0;
        else
            ++jugador_actual;
    }

    public void mostrar_elementos() {

        for (int i = 0; i < 26; ++i) {
            if (jugadores.get(jugador_actual).letras[i] != 0 || jugadores.get(jugador_actual).letra_actual == i)
                batch.draw(((Texture[])(Menu.elementos_mostrar[0]))[i], 0, 0);
            if (jugadores.get(jugador_actual).letras[i] == 2)
                batch.draw(((Texture[])(Menu.elementos_mostrar[3]))[i], 0, 0);
            else if (jugadores.get(jugador_actual).letras[i] == 1)
                batch.draw(((Texture[])(Menu.elementos_mostrar[4]))[i], 0, 0);
        }

        jugadores.get(jugador_actual).contador += Gdx.graphics.getDeltaTime();
        if(jugadores.get(jugador_actual).contador >= 1.0f) {
            jugadores.get(jugador_actual).contador = 0;
            jugadores.get(jugador_actual).tiempo--;
        }

        batch.draw(((Texture[][])(Menu.elementos_mostrar[1]))[0][jugadores.get(jugador_actual).n_aciertos%10], 0, 0);
        batch.draw(((Texture[][])(Menu.elementos_mostrar[1]))[1][jugadores.get(jugador_actual).n_aciertos/10], 0, 0);
        batch.draw(((Texture[][])(Menu.elementos_mostrar[2]))[0][(int)jugadores.get(jugador_actual).tiempo%10], 0, 0);
        batch.draw(((Texture[][])(Menu.elementos_mostrar[2]))[1][((int)jugadores.get(jugador_actual).tiempo%100)/10], 0, 0);
        batch.draw(((Texture[][])(Menu.elementos_mostrar[2]))[2][((int)jugadores.get(jugador_actual).tiempo%1000)/100], 0, 0);
    }
}
