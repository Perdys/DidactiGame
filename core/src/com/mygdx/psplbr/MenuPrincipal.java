package com.mygdx.psplbr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;

public class MenuPrincipal extends Pantalla {

    PsPlbr juego;
    Stage stage;
    OrthographicCamera camara;
    SpriteBatch batch;

    ImageTextButton boton_jugar, boton_records, boton_opciones;
    Texto titulo;

    public MenuPrincipal(PsPlbr juego) {
        this.juego = juego;

        camara = new OrthographicCamera();
        camara.setToOrtho(false);
        stage = new Stage();
        batch = new SpriteBatch();

        pantalla_actual = 0;

        titulo = new Texto(1, "tilt it!");
        titulo.setPosition(anchura_juego / 2, (float)(altura_juego * 0.66) + BMF_titulo.getBounds(titulo.texto).height / 2, 0, BitmapFont.HAlignment.CENTER);
        boton_jugar = setButton("data/boton_jugar.png", "Jugar", juego);
        boton_jugar.setPosition(anchura_juego / 4 - boton_jugar.getWidth() / 2, (float)(altura_juego * 0.33) - boton_jugar.getHeight() / 2);
        boton_records = setButton("data/boton_records.png", "Records", juego);
        boton_records.setPosition(anchura_juego / 2 - boton_records.getWidth() / 2, (float)(altura_juego * 0.33) - boton_records.getHeight() / 2);
        boton_opciones = setButton("data/boton_opciones.png", "Opciones", juego);
        boton_opciones.setPosition((float)(anchura_juego * 0.75) - boton_opciones.getWidth() / 2, (float)(altura_juego * 0.33) - boton_opciones.getHeight() / 2);
    }

    public void render(float delta) {	/*ejecutarse todas las veces posible por segundo, ejecuten todas las acciones del juego
		dibujar los elementos por pantalla, procesar la entrada y salida, mover los personajes, detectar colisiones, etc. */
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();

        batch.setProjectionMatrix(camara.combined);

        stage.act();
        stage.draw();
    }

    public void resize(int width, int height) {} //sirve para recalcular el tama�o de los elementos cuando se modifica la pantalla

    public void pause() {}	/*, y resume() y pause(), que son funciones que se ejecutan en Android cuando salimos de la aplicaci�n o se interrumpe la ejecuci�n de la misma y volvemos a ella.*/

    public void resume() {}

    public void show() {
        stage.addActor(titulo);
        stage.addActor(boton_jugar);
        stage.addActor(boton_records);
        stage.addActor(boton_opciones);
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
