package com.mygdx.psplbr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

public class Rosco extends Pantalla {

    PsPlbr juego;
    Stage stage;
    OrthographicCamera camara;
    SpriteBatch batch;

    ImageButton boton_inicio_parada, boton_reinicio, boton_acierto, boton_fallo;
    public Texture fondo;

    public Rosco(PsPlbr juego) {
        this.juego = juego;

        camara = new OrthographicCamera();
        camara.setToOrtho(false);
        stage = new Stage();
        batch = new SpriteBatch();

        pantalla_actual = 2;

        fondo = new Texture("data/rosco/fondo_rosco.jpg");
        boton_inicio_parada = setButton("data/rosco/boton_inicio_parada.jpg", juego);
        boton_inicio_parada.setPosition((float)(anchura_juego * 0.75) - boton_inicio_parada.getWidth(), (float)(altura_juego * 0.33));
        boton_reinicio = setButton("data/rosco/boton_reinicio.jpg", juego);
        boton_reinicio.setPosition((float)(anchura_juego * 0.75), (float)(altura_juego * 0.33));
        boton_acierto = setButton("data/rosco/boton_acierto.jpg", juego);
        boton_acierto.setPosition((float)(anchura_juego * 0.75) - boton_acierto.getWidth(), (float)(altura_juego * 0.33) - boton_acierto.getHeight());
        boton_fallo = setButton("data/rosco/boton_fallo.jpg", juego);
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
}
