package com.mygdx.DidactiGame.Pantallas.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.mygdx.DidactiGame.DidactiGame;
import com.mygdx.DidactiGame.Auxiliares.Pantalla;

import static com.badlogic.gdx.utils.Align.topLeft;
import static com.mygdx.DidactiGame.DidactiGame.jugadores;

public class Menu_Puntuaciones extends Pantalla{

    DidactiGame juego;
    OrthographicCamera camara;
    SpriteBatch batch;
    Stage stage;

    Color color_fondo = Color.WHITE;
    Texture particulas_texto;

    public SelectBox<String> jugador_selector;
    Label puntuaciones_rosco, puntuaciones_qqsm;
    ScrollPane puntuaciones_scroll_rosco, puntuaciones_scroll_qqsm;

    public Menu_Puntuaciones(DidactiGame juego) {
        this.juego = juego;

        camara = new OrthographicCamera();
        camara.setToOrtho(false);
        batch = new SpriteBatch();
        stage = new Stage();

        pantalla_actual = "Menu_Puntuaciones";

        texturas_cargar();
    }

    public void render (float delta) {
        Gdx.gl.glClearColor(color_fondo.r, color_fondo.g, color_fondo.b, color_fondo.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();

        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.draw(particulas_texto, proporcion_x(0.1), proporcion_y(0.6), proporcion_x(0.35), particulas_texto.getHeight() * proporcion_x(0.35) / particulas_texto.getWidth());
        batch.draw(particulas_texto, proporcion_x(0.55), proporcion_y(0.6), proporcion_x(0.35), particulas_texto.getHeight() * proporcion_x(0.35) / particulas_texto.getWidth());
        batch.end();

        stage.act();
        stage.draw();
    }

    public void show () {
        jugador_selector.setItems(new Array<>(jugadores.nombres().split("\n")));
        jugador_selector.pack();
        if (!jugadores.vacio()) {
            puntuaciones_rosco.setText(jugadores.jugador(jugador_selector.getSelected()).puntuaciones("Rosco"));
            puntuaciones_qqsm.setText(jugadores.jugador(jugador_selector.getSelected()).puntuaciones("QQSM"));
            color_fondo = jugadores.jugador(jugador_selector.getSelected()).color;
        }

        sistema_botones(juego);
        stage.addActor(jugador_selector);
        stage.addActor(puntuaciones_scroll_rosco);
        stage.addActor(puntuaciones_scroll_qqsm);

        InputMultiplexer inputs = new InputMultiplexer();
        inputs.addProcessor(botones_genericos);
        inputs.addProcessor(stage);
        Gdx.input.setInputProcessor(inputs);
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);
    }

    public void resume() { pantalla_actual = "Menu_Puntuaciones"; }

    public void dispose() { //es la ultima en ejecutarse, se encarga de liberar recursos y dejar la memoria limpia

        batch.dispose();
    }

    public void texturas_cargar() {
        particulas_texto = new Texture("data/texturas/texto/particulas.png");

        puntuaciones_rosco = new Label("", texto_panel_scroll_estilo());
        puntuaciones_rosco.setWidth(proporcion_x(0.2));
        puntuaciones_rosco.setWrap(true);
        puntuaciones_rosco.setAlignment(topLeft);
        puntuaciones_scroll_rosco = new ScrollPane(puntuaciones_rosco);
        puntuaciones_scroll_rosco.setBounds(proporcion_x(0.1), proporcion_y(0.1), proporcion_x(0.35), proporcion_y(0.5));
        puntuaciones_scroll_rosco.layout();
        puntuaciones_scroll_rosco.setTouchable(Touchable.enabled);

        puntuaciones_qqsm = new Label("", texto_panel_scroll_estilo());
        puntuaciones_qqsm.setWidth(proporcion_x(0.2));
        puntuaciones_qqsm.setWrap(true);
        puntuaciones_qqsm.setAlignment(topLeft);
        puntuaciones_scroll_qqsm = new ScrollPane(puntuaciones_qqsm);
        puntuaciones_scroll_qqsm.setBounds(proporcion_x(0.55), proporcion_y(0.1), proporcion_x(0.35), proporcion_y(0.5));
        puntuaciones_scroll_qqsm.layout();
        puntuaciones_scroll_qqsm.setTouchable(Touchable.enabled);

        //Selector del jugador del que se quiere ver las puntuaciones
        jugador_selector = new SelectBox<>(selector_estilo());
        jugador_selector.setPosition(proporcion_x(0.1), proporcion_y(0.75));
        jugador_selector.setMaxListCount(3);
        jugador_selector.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!jugadores.vacio()) {
                    puntuaciones_rosco.setText(jugadores.jugador(jugador_selector.getSelected()).puntuaciones("Rosco"));
                    puntuaciones_qqsm.setText(jugadores.jugador(jugador_selector.getSelected()).puntuaciones("QQSM"));
                    color_fondo = jugadores.jugador(jugador_selector.getSelected()).color;
                }
            }
        });
    }
}
