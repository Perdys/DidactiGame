package com.mygdx.DidactiGame.Pantallas.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.mygdx.DidactiGame.DidactiGame;
import com.mygdx.DidactiGame.Auxiliares.Pantalla;

import static com.mygdx.DidactiGame.DidactiGame.jugadores;

public class Menu_Jugadores extends Pantalla {

    DidactiGame juego;
    OrthographicCamera camara;
    SpriteBatch batch;
    Stage stage;
    InputAdapter click;

    public static SelectBox<String> jugador_selector;
    Pixmap color_selector, temp;
    Texture color_selector_texture, botones_jugadores;
    boolean mostrando_color_selector = false;
    Color color_fondo = Color.WHITE;
    Label jugador_edad;

    public Menu_Jugadores(DidactiGame juego) {
        this.juego = juego;

        camara = new OrthographicCamera();
        camara.setToOrtho(false);
        batch = new SpriteBatch();
        stage = new Stage();

        pantalla_actual = "Menu_Jugadores";

        texturas_cargar();
    }

    public void render (float delta) {
        Gdx.gl.glClearColor(color_fondo.r, color_fondo.g, color_fondo.b, color_fondo.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();

        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        if (mostrando_color_selector)
            batch.draw(color_selector_texture, 0, 0);
        batch.draw(botones_jugadores, anchura_juego - proporcion_y(0.5), proporcion_y(0.1), proporcion_y(0.4), proporcion_y(0.4));
        jugador_edad.draw(batch, 1);
        batch.end();

        stage.act();
        stage.draw();
    }

    public void show () {
        jugador_selector.setItems(new Array<>(jugadores.nombres().split("\n")));
        jugador_selector.pack();
        jugador_selector.setWidth(proporcion_x(0.4));

        sistema_botones(juego);
        stage.addActor(jugador_selector);

        InputMultiplexer inputs = new InputMultiplexer();
        inputs.addProcessor(stage);
        inputs.addProcessor(botones_genericos);
        inputs.addProcessor(click);
        Gdx.input.setInputProcessor(inputs);
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);
    }

    public void resume() { pantalla_actual = "Menu_Jugadores"; }

    public void dispose() { //es la ultima en ejecutarse, se encarga de liberar recursos y dejar la memoria limpia

        batch.dispose();
    }

    public void texturas_cargar() {
        botones_jugadores = new Texture("data/texturas/botones_jugador.png");

        jugador_edad = new Label("Edad: 99 años", texto_estilo(0.055));
        jugador_edad.setPosition(proporcion_x(0.1), proporcion_y(0.1));

        //Selector del jugador del que se quiere ver el perfil
        jugador_selector = new SelectBox<>(selector_estilo());
        jugador_selector.setPosition(proporcion_x(0.1), proporcion_y(0.75));
        jugador_selector.setMaxListCount(3);
        jugador_selector.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!jugadores.vacio()) {
                    jugador_edad.setText("Edad: " + jugadores.jugador(jugador_selector.getSelected()).edad + " años");
                    color_fondo = jugadores.jugador(jugador_selector.getSelected()).color;
                }
            }
        });

        //Selector del color personalizado del jugador actual
        color_selector = new Pixmap(Gdx.files.internal("data/texturas/color_selector/selector.png"));
        temp = new Pixmap((int)anchura_juego, (int)altura_juego, Pixmap.Format.RGBA8888);
        temp.drawPixmap(color_selector, 0, 0, color_selector.getWidth(), color_selector.getHeight(), (int)proporcion_x(0.5) - (int)proporcion_y(0.25), (int)proporcion_y(0.25), (int)proporcion_y(0.5), (int)proporcion_y(0.5));
        color_selector_texture = new Texture(temp);
        final Circle selector_externo = new Circle(proporcion_x(0.5), proporcion_y(0.5), proporcion_y(0.49) / 2);
        final Rectangle selector_color = new Rectangle(anchura_juego - proporcion_y(0.5), proporcion_y(0.7), proporcion_y(0.2), proporcion_y(0.2));
        final Rectangle selector_edad = new Rectangle(anchura_juego - proporcion_y(0.3), proporcion_y(0.7), proporcion_y(0.2), proporcion_y(0.2));
        final Rectangle anadir_jugador = new Rectangle(anchura_juego - proporcion_y(0.5), proporcion_y(0.5), proporcion_y(0.2), proporcion_y(0.2));
        final Rectangle eliminar_jugador = new Rectangle(anchura_juego - proporcion_y(0.3), proporcion_y(0.5), proporcion_y(0.2), proporcion_y(0.2));

        click = new InputAdapter() {
            public boolean touchUp (int x, int y, int pointer, int button) {
                if (button == 1)
                    return false;

                if (mostrando_color_selector && selector_externo.contains(x, y) /*&& !selector_interno.contains(x, y)*/) {
                    color_fondo = new Color(temp.getPixel(x, y));
                    jugadores.jugador(jugador_selector.getSelected()).color(color_fondo);
                    mostrando_color_selector = false;
                }
                else
                if (selector_color.contains(x, y))
                    mostrando_color_selector = true;
                else
                if (selector_edad.contains(x, y))
                //Selector de la edad
                Gdx.input.getTextInput (new Input.TextInputListener() {
                    public void input(String edad) {
                        if (edad.isEmpty())
                            edad = "Edad: 99 años";
                        jugadores.jugador(jugador_selector.getSelected()).edad(edad);
                        jugador_edad.setText("Edad: " + edad + " años");
                    }

                    public void canceled() {}

                }, "Introduzca la edad", "", "99");
                else
                if (eliminar_jugador.contains(x, y)) {
                    jugadores.eliminar(jugadores.jugador(jugador_selector.getSelected()));
                    jugador_selector.setItems(new Array<>(jugadores.nombres().split("\n")));
                    jugador_selector.pack();
                    jugador_selector.setWidth(proporcion_x(0.4));
                    stage.addActor(jugador_selector);
                }
                else
                if (anadir_jugador.contains(x, y))
                    Gdx.input.getTextInput (new Input.TextInputListener() {
                        public void input(String nombre) {
                            if (nombre.isEmpty())
                                nombre = "Jugador";
                            jugadores.anadir(nombre);
                            jugador_selector.setItems(new Array<>(jugadores.nombres().split("\n")));
                            jugador_selector.setSelected(nombre);
                            jugador_selector.pack();
                            jugador_selector.setWidth(proporcion_x(0.4));
                            stage.addActor(jugador_selector);
                            jugador_edad.setText("Edad: " + jugadores.jugador(nombre).edad + " años");
                        }

                        public void canceled() {}

                    }, "Introduzca el jugador", "", "Jugador");

                return false;
            }
        };
    }
}