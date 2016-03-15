package com.mygdx.DidactiGame.Pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.DidactiGame.DidactiGame;
import com.mygdx.DidactiGame.Herramientas.Pantalla;

public class Menu_Inicial extends Pantalla{

    DidactiGame juego;
    OrthographicCamera camara;
    SpriteBatch batch;
    InputAdapter click;

    Texture fondo;
    Circle boton_juegos;
    Rectangle boton_opciones, boton_datos, boton_personalizar, boton_nuevo;

    public Menu_Inicial (DidactiGame juego) {
        this.juego = juego;

        camara = new OrthographicCamera();
        camara.setToOrtho(false);
        batch = new SpriteBatch();

        pantalla_actual = "Menu_Inicial";

        fondo = new Texture("data/menu_inicial/fondo_inicial.jpg");

        botones_cargar();
    }

    public void render (float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();

        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.draw(fondo, 0, 0, anchura_juego, altura_juego);
        batch.end();

    }

    public void show () {
        sistema_botones(juego);

        InputMultiplexer inputs = new InputMultiplexer();
        inputs.addProcessor(botones_genericos);
        inputs.addProcessor(click);
        Gdx.input.setInputProcessor(inputs);
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);
    }

    public void resume() { pantalla_actual = "Menu_Inicial"; }

    public void botones_cargar() {

        boton_juegos = new Circle(proporcion_x(0.5), proporcion_y(0.5), proporcion_x(0.13));
        boton_datos = new Rectangle(proporcion_x(0.23), 0, proporcion_x(0.27), proporcion_y(0.5));
        boton_opciones = new Rectangle(proporcion_x(0.5), 0, proporcion_x(0.27), proporcion_y(0.5));
        boton_personalizar = new Rectangle(proporcion_x(0.23), proporcion_y(0.5), proporcion_x(0.27), proporcion_y(0.5));
        boton_nuevo = new Rectangle(proporcion_x(0.5), proporcion_y(0.5), proporcion_x(0.27), proporcion_y(0.5));

        click = new InputAdapter() {
            public boolean touchUp(int x, int y, int pointer, int button) {
                if (boton_juegos.contains(x, y)) { juego.setScreen(DidactiGame.menu_juegos); }
                else
                if (boton_opciones.contains(x, y)) { juego.setScreen(DidactiGame.menu_opciones); }
                else
                if (boton_datos.contains(x, y)) { juego.setScreen(DidactiGame.menu_datos); }
                else
                if (boton_personalizar.contains(x, y)) { juego.setScreen(DidactiGame.menu_personalizar); }
                else
                if (boton_nuevo.contains(x, y)) { juego.setScreen(DidactiGame.menu_nuevo); }

                return false;
            }
        };
    }

    public void dispose() { //es la ultima en ejecutarse, se encarga de liberar recursos y dejar la memoria limpia

        batch.dispose();
    }
}
