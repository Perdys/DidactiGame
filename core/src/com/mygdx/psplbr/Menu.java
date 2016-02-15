package com.mygdx.psplbr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import javafx.scene.layout.Background;

public class Menu extends Pantalla {

    PsPlbr juego;
    Stage stage;
    OrthographicCamera camara;
    SpriteBatch batch;

    ImageButton boton_jugar, boton_mas, boton_menos;
    public Texture fondo;

    public Menu(PsPlbr juego) {
        this.juego = juego;

        camara = new OrthographicCamera();
        camara.setToOrtho(false);
        stage = new Stage();
        batch = new SpriteBatch();

        pantalla_actual = 0;

        fondo = new Texture("data/menu/fondo_menu.jpg");
        boton_jugar = setButton("data/menu/boton_jugar.jpg", juego);
        boton_jugar.setPosition((float)(anchura_juego * 0.75) - boton_jugar.getWidth() / 2, (float)(altura_juego * 0.33) - boton_jugar.getHeight() / 2);
        boton_mas = setButton("data/menu/boton_mas.jpg", juego);
        boton_mas.setPosition((float)(anchura_juego * 0.33) - boton_mas.getWidth() / 2, (float)(altura_juego * 0.33) + boton_mas.getHeight());
        boton_menos = setButton("data/menu/boton_menos.jpg", juego);
        boton_menos.setPosition((float)(anchura_juego * 0.33) - boton_menos.getWidth() / 2, (float)(altura_juego * 0.33) - boton_menos.getHeight());
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
        stage.addActor(boton_jugar);
        stage.addActor(boton_mas);
        stage.addActor(boton_menos);
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
