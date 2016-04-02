package com.mygdx.DidactiGame.Pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.mygdx.DidactiGame.DidactiGame;
import com.mygdx.DidactiGame.Herramientas.Pantalla;


public class Menu_Opciones  extends Pantalla {

    DidactiGame juego;
    OrthographicCamera camara;
    SpriteBatch batch;
    Stage stage;

    public static SelectBox<String> selector;

    public Menu_Opciones(DidactiGame juego) {
        this.juego = juego;

        camara = new OrthographicCamera();
        camara.setToOrtho(false);
        batch = new SpriteBatch();
        stage = new Stage();

        pantalla_actual = "Menu_Opciones";

        texturas_cargar();
    }

    public void render (float delta) {
        Gdx.gl.glClearColor(Color.YELLOW.r, Color.YELLOW.g, Color.YELLOW.b, Color.YELLOW.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();

        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        selector.draw(batch, 1000);
        batch.end();

        stage.act();
        stage.draw();
    }

    public void show () {
        selector.setItems(new Array<>(nombres_jugadores_fichero.contenido.split("\n")));
        selector.pack();

        sistema_botones(juego);
        stage.addActor(selector);

        InputMultiplexer inputs = new InputMultiplexer();
        inputs.addProcessor(stage);
        inputs.addProcessor(botones_genericos);
        Gdx.input.setInputProcessor(inputs);
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);
    }

    public void resume() { pantalla_actual = "Menu_Opciones"; }

    public void dispose() { //es la ultima en ejecutarse, se encarga de liberar recursos y dejar la memoria limpia

        batch.dispose();
    }

    public void texturas_cargar() {
        BitmapFont texto = generador_texto.generateFont(tamano_texto);
        SelectBox.SelectBoxStyle estilo_selector = new SelectBox.SelectBoxStyle();
        estilo_selector.font = texto;
        estilo_selector.fontColor = Color.GREEN;
        estilo_selector.background = new TextureRegionDrawable(new TextureRegion(new Texture("data/menu_opciones/editor/rojo.png")));
        estilo_selector.scrollStyle = new ScrollPane.ScrollPaneStyle();
        estilo_selector.listStyle = new List.ListStyle();
        estilo_selector.listStyle.font = texto;
        estilo_selector.listStyle.fontColorSelected = Color.FOREST;
        estilo_selector.listStyle.fontColorUnselected = Color.OLIVE;
        estilo_selector.listStyle.selection = new TextureRegionDrawable(new TextureRegion(new Texture("data/menu_opciones/editor/seleccion.png")));
        estilo_selector.listStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture("data/menu_opciones/editor/azul.png")));
        selector = new SelectBox<>(estilo_selector);
        selector.setPosition(proporcion_x(0.2), proporcion_y(0.8));
        selector.setSize(proporcion_x(0.2), proporcion_y(0.2));
        selector.setMaxListCount(3);
    }
}