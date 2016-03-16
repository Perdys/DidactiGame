package com.mygdx.DidactiGame.Pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.DidactiGame.DidactiGame;
import com.mygdx.DidactiGame.Herramientas.Pantalla;

public class Menu_Datos extends Pantalla{

    DidactiGame juego;
    OrthographicCamera camara;
    SpriteBatch batch;
    Stage stage;

    TextArea jugadores_editor, letras_rosco_editor;

    public Menu_Datos(DidactiGame juego) {
        this.juego = juego;

        camara = new OrthographicCamera();
        camara.setToOrtho(false);
        batch = new SpriteBatch();
        stage = new Stage();

        pantalla_actual = "Menu_Datos";

        jugadores_editor();
        letras_rosco_editor();
    }

    public void render (float delta) {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();

        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        jugadores_editor.draw(batch, 1);
        letras_rosco_editor.draw(batch, 1);
        texturas_mostrar();
        batch.end();
    }

    public void show () {
        sistema_botones(juego);
        stage.addActor(jugadores_editor);
        stage.addActor(letras_rosco_editor);

        InputMultiplexer inputs = new InputMultiplexer();
        inputs.addProcessor(botones_genericos);
        inputs.addProcessor(stage);
        Gdx.input.setInputProcessor(inputs);
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);
    }

    public void hide() {
        jugadores_fichero.escribir(jugadores_editor.getText());
    }

    public void resume() { pantalla_actual = "Menu_Datos"; }

    public void dispose() { //es la ultima en ejecutarse, se encarga de liberar recursos y dejar la memoria limpia

        batch.dispose();
    }

    public void jugadores_editor () {

        tamano_texto.size = (int)proporcion_y(0.06);
        TextField.TextFieldStyle estilo_texto = new TextField.TextFieldStyle(
                generador_texto.generateFont(tamano_texto), Color.GREEN,
                new TextureRegionDrawable(new TextureRegion(new Texture("data/menu_datos/editor/cursor_editor.png"))),
                new TextureRegionDrawable(new TextureRegion(new Texture("data/menu_datos/editor/seleccion_editor.png"))),
                new TextureRegionDrawable(new TextureRegion(new Texture("data/menu_datos/editor/fondo_editor.png"))));
        estilo_texto.cursor.setMinWidth(proporcion_x(0.003));
        estilo_texto.background.setLeftWidth(proporcion_x(0.01));
        estilo_texto.background.setRightWidth(proporcion_x(0.01));
        estilo_texto.background.setTopHeight(proporcion_y(0.01));
        estilo_texto.background.setBottomHeight(proporcion_y(0.01));

        jugadores_editor = new TextArea(jugadores_fichero.contenido, estilo_texto);
        jugadores_editor.setPosition(proporcion_x(0.1), proporcion_y(0.1));
        jugadores_editor.setSize(proporcion_x(0.2), proporcion_y(0.8));
        jugadores_editor.addListener(new InputListener() {
            public boolean keyUp (InputEvent event, int keycode) {
                return false;
            }
        });
    }

    public void letras_rosco_editor () {

        tamano_texto.size = (int)proporcion_y(0.06);
        TextField.TextFieldStyle estilo_texto = new TextField.TextFieldStyle(
                generador_texto.generateFont(tamano_texto), Color.GREEN,
                new TextureRegionDrawable(new TextureRegion(new Texture("data/menu_datos/editor/cursor_editor.png"))),
                new TextureRegionDrawable(new TextureRegion(new Texture("data/menu_datos/editor/seleccion_editor.png"))),
                new TextureRegionDrawable(new TextureRegion(new Texture("data/menu_datos/editor/fondo_editor.png"))));
        estilo_texto.cursor.setMinWidth(proporcion_x(0.003));
        estilo_texto.background.setLeftWidth(proporcion_x(0.01));
        estilo_texto.background.setRightWidth(proporcion_x(0.01));
        estilo_texto.background.setTopHeight(proporcion_y(0.01));
        estilo_texto.background.setBottomHeight(proporcion_y(0.01));

        letras_rosco_editor = new TextArea(letras_rosco_fichero.contenido, estilo_texto);
        letras_rosco_editor.setPosition(proporcion_x(0.4), proporcion_y(0.55));
        letras_rosco_editor.setSize(proporcion_x(0.5), proporcion_y(0.35));
        letras_rosco_editor.addListener(new InputListener() {
            public boolean keyUp (InputEvent event, int keycode) {
                return false;
            }
        });
    }

    public void texturas_mostrar() {

        batch.draw(new Texture("data/menu_datos/rosco.png"), letras_rosco_editor.getX(), letras_rosco_editor.getY() + letras_rosco_editor.getHeight(), proporcion_x(0.07), proporcion_y(0.07));
        batch.draw(new Texture("data/menu_datos/jugadores.png"), jugadores_editor.getX(), jugadores_editor.getY() + jugadores_editor.getHeight(), proporcion_x(0.05), proporcion_y(0.07));
    }
}
