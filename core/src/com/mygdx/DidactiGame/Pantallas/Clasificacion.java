package com.mygdx.DidactiGame.Pantallas;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.mygdx.DidactiGame.Auxiliares.Jugadores;
import com.mygdx.DidactiGame.DidactiGame;
import com.mygdx.DidactiGame.Auxiliares.Pantalla;

import static com.badlogic.gdx.utils.Align.topLeft;
import static com.mygdx.DidactiGame.DidactiGame.jugadores;

public class Clasificacion extends Pantalla {

    DidactiGame juego;
    Stage stage;
    OrthographicCamera camara;
    SpriteBatch batch;

    String puntuaciones = "", nombre_juego;
    Label puntuaciones_etiqueta, juego_etiqueta;
    ScrollPane puntuaciones_scroll;
    Texture particulas_texto;

    public Clasificacion (String nombre_juego, DidactiGame juego) {
        this.juego = juego;
        this.nombre_juego = nombre_juego;

        camara = new OrthographicCamera();
        camara.setToOrtho(false);
        stage = new Stage();
        batch = new SpriteBatch();

        pantalla_actual = "Clasificacion";

        texturas_cargar();
    }

    public void render(float delta) {	/*ejecutarse todas las veces posible por segundo, ejecuten todas las acciones del juego
		dibujar los elementos por pantalla, procesar la entrada y salida, mover los personajes, detectar colisiones, etc. */
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();

        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        if (Gdx.app.getType() == Application.ApplicationType.Desktop)
            batch.draw(boton_atras, 0, 0, anchura_juego, altura_juego);
        batch.draw(particulas_texto, proporcion_x(0.325), proporcion_y(0.77) - particulas_texto.getHeight() * proporcion_x(0.35) / particulas_texto.getWidth(),
                proporcion_x(0.35), particulas_texto.getHeight() * proporcion_x(0.35) / particulas_texto.getWidth());
        juego_etiqueta.draw(batch, 1);
        batch.end();

        stage.act();
        stage.draw();
    }

    public void show () {
        puntuaciones_etiqueta.setText(puntuaciones);

        sistema_botones(juego);
        stage.addActor(puntuaciones_scroll);

        InputMultiplexer inputs = new InputMultiplexer();
        inputs.addProcessor(botones_genericos);
        inputs.addProcessor(stage);
        Gdx.input.setInputProcessor(inputs);
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);
    }

    public void resume() { pantalla_actual = "Clasificacion"; }

    public void hide() { jugadores = new Jugadores(); }

    public void puntuacion_anadir(String nombre, String puntuacion) {
        puntuaciones = puntuaciones.concat("\n" + nombre + ": \n  " + puntuacion);
    }

    public void texturas_cargar() {
        particulas_texto = new Texture("data/texturas/texto/particulas.png");

        puntuaciones_etiqueta = new Label("", texto_panel_scroll_estilo());
        puntuaciones_etiqueta.setWidth(proporcion_x(0.2));
        puntuaciones_etiqueta.setWrap(true);
        puntuaciones_etiqueta.setAlignment(topLeft);
        puntuaciones_scroll = new ScrollPane(puntuaciones_etiqueta);
        puntuaciones_scroll.setBounds(proporcion_x(0.325), proporcion_y(0.1), proporcion_x(0.35), proporcion_y(0.65));
        puntuaciones_scroll.layout();
        puntuaciones_scroll.setTouchable(Touchable.enabled);

        juego_etiqueta = new Label(nombre_juego, texto_estilo(0.1));
        juego_etiqueta.setWidth(proporcion_x(0.35));
        juego_etiqueta.setWrap(true);
        juego_etiqueta.setAlignment(topLeft);
        juego_etiqueta.setPosition(proporcion_x(0.4), proporcion_y(0.8));
    }
}
