package com.mygdx.DidactiGame.Pantallas.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.mygdx.DidactiGame.DidactiGame;
import com.mygdx.DidactiGame.Auxiliares.Pantalla;

import static com.badlogic.gdx.utils.Align.topLeft;
import static com.mygdx.DidactiGame.DidactiGame.BD;
import static com.mygdx.DidactiGame.DidactiGame.jugadores;
import static com.mygdx.DidactiGame.Pantallas.Menus.Menu_Juegos.letras_descripciones;

public class Menu_Datos extends Pantalla{

    DidactiGame juego;
    OrthographicCamera camara;
    SpriteBatch batch;
    Stage stage;

    TextArea letras_rosco_editor;
    Label jugadores_etiqueta;
    ScrollPane jugadores_scroll;
    Texture rosco_icono, jugadores_icono;
    boolean editado = false;

    public Menu_Datos(DidactiGame juego) {
        this.juego = juego;

        camara = new OrthographicCamera();
        camara.setToOrtho(false);
        batch = new SpriteBatch();
        stage = new Stage();

        pantalla_actual = "Menu_Datos";

        texturas_cargar();
        jugadores_cargar();
        letras_rosco_editor();
    }

    public void render (float delta) {
        Gdx.gl.glClearColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, Color.WHITE.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();

        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        //TODO añadir editor de QQSM
        batch.draw(rosco_icono, letras_rosco_editor.getX(), letras_rosco_editor.getY() + letras_rosco_editor.getHeight(), proporcion_x(0.07), proporcion_y(0.07));
        batch.draw(jugadores_icono, jugadores_scroll.getX(), jugadores_scroll.getY() + jugadores_scroll.getHeight(), proporcion_x(0.05), proporcion_y(0.07));
        batch.end();

        stage.act();
        stage.draw();
    }

    public void show () {
        jugadores_etiqueta.setText(jugadores.nombres());

        sistema_botones(juego);
        stage.addActor(jugadores_scroll);
        stage.addActor(letras_rosco_editor);

        InputMultiplexer inputs = new InputMultiplexer();
        inputs.addProcessor(botones_genericos);
        inputs.addProcessor(stage);
        Gdx.input.setInputProcessor(inputs);
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);
    }

    public void hide() {
        if (editado) {
            BD.escribir_descripciones(letras_rosco_editor.getText());
            BD.leer_descripciones(letras_descripciones);
            editado = false;
        }
    }

    public void resume() { pantalla_actual = "Menu_Datos"; }

    public void dispose() { //es la ultima en ejecutarse, se encarga de liberar recursos y dejar la memoria limpia

        batch.dispose();
    }

    public void jugadores_cargar() {

        jugadores_etiqueta = new Label(jugadores.nombres(), texto_panel_scroll_estilo());
        jugadores_etiqueta.setWidth(proporcion_x(0.2));
        jugadores_etiqueta.setWrap(true);
        jugadores_etiqueta.setAlignment(topLeft);
        jugadores_scroll = new ScrollPane(jugadores_etiqueta);
        jugadores_scroll.setBounds(proporcion_x(0.1), proporcion_y(0.1), proporcion_x(0.2), proporcion_y(0.8));
        jugadores_scroll.layout();
        jugadores_scroll.setTouchable(Touchable.enabled);
    }

    public void letras_rosco_editor () {

        letras_rosco_editor = new TextArea(BD.leer_letras(), editor_estilo());
        letras_rosco_editor.setPosition(proporcion_x(0.4), proporcion_y(0.55));
        letras_rosco_editor.setSize(proporcion_x(0.5), proporcion_y(0.35));
        letras_rosco_editor.addListener(new InputListener() {
            public boolean keyUp (InputEvent event, int keycode) {
                editado = true;
                return false;
            }
        });
    }

    public void texturas_cargar() {
        rosco_icono = new Texture("data/texturas/rosco.png");
        jugadores_icono = new Texture("data/texturas/jugadores.png");
    }
}
