package com.mygdx.DidactiGame.Pantallas.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.DidactiGame.DidactiGame;
import com.mygdx.DidactiGame.Auxiliares.Pantalla;
import com.mygdx.DidactiGame.Pantallas.Juego_QQSM;
import com.mygdx.DidactiGame.Pantallas.Juego_Rosco;

import java.util.ArrayList;

import static com.mygdx.DidactiGame.DidactiGame.BD;
import static com.mygdx.DidactiGame.DidactiGame.jugadores;

public class Menu_Juegos extends Pantalla {
    DidactiGame juego;
    Stage stage;
    OrthographicCamera camara;
    SpriteBatch batch;
    InputAdapter click;

    Juego_Rosco rosco;
    Juego_QQSM qqsm;
    Texture fondo, botones_juegos;
    Rectangle boton_jugar_rosco, boton_jugar_qqsm;

    Table panel_checkboxs;
    Table table;
    CheckBox.CheckBoxStyle estilo_checkbox = checkbox_estilo();

    public static class Texturas {
        public Texture[] letras, fallos, aciertos;
        public Texture[][] tiempo, puntuacion;
    }

    public static ArrayList<ArrayList<String[]>> letras_descripciones;
    public static Texturas texturas;

    public Menu_Juegos(DidactiGame juego) {
        this.juego = juego;

        camara = new OrthographicCamera();
        camara.setToOrtho(false);
        stage = new Stage();
        batch = new SpriteBatch();

        pantalla_actual = "Menu_Juegos";

        texturas = new Texturas();

        letras_descripciones = new ArrayList<>(26);
        for (int i = 0; i < 26; ++i) letras_descripciones.add(i, new ArrayList<String[]>(1));
        BD.leer_descripciones(letras_descripciones);

        texturas_cargar();
        botones_cargar();
    }

    public void render(float delta) {	/*ejecutarse todas las veces posible por segundo, ejecuten todas las acciones del juego
		dibujar los elementos por pantalla, procesar la entrada y salida, mover los personajes, detectar colisiones, etc. */
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();

        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.draw(botones_juegos, anchura_juego - proporcion_x(0.5) - proporcion_y(0.1), proporcion_y(0.1), proporcion_x(0.5), proporcion_y(0.25));
        batch.end();

        stage.act();
        stage.draw();
    }

    public void resume() { pantalla_actual = "Menu_Juegos"; }

    public void show() {
        actualizar_checkbox();
        sistema_botones(juego);

        InputMultiplexer inputs = new InputMultiplexer();
        inputs.addProcessor(botones_genericos);
        inputs.addProcessor(stage);
        inputs.addProcessor(click);
        Gdx.input.setInputProcessor(inputs);
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);
    }

    public void dispose() { //es la ultima en ejecutarse, se encarga de liberar recursos y dejar la memoria limpia

        BD.escribir_descripciones(letras_descripciones);
        rosco.dispose();
        generador_texto.dispose();
        batch.dispose();
        stage.dispose();
    }

    public void actualizar_checkbox() {
        panel_checkboxs.clearChildren();
        for (int i = 0; i < jugadores.numeral(); ++i) {
            CheckBox cb = new CheckBox(jugadores.jugador(i).nombre, estilo_checkbox);
            cb.setChecked(jugadores.jugador(i).seleccionado);
            panel_checkboxs.add(cb).left();
            panel_checkboxs.row();
        }
    }

    public void botones_cargar() {

        boton_jugar_rosco = new Rectangle(anchura_juego - proporcion_x(0.5) - proporcion_y(0.1), proporcion_y(0.65), proporcion_x(0.25), proporcion_y(0.25));
        boton_jugar_qqsm = new Rectangle(anchura_juego - proporcion_x(0.25) - proporcion_y(0.1), proporcion_y(0.65), proporcion_x(0.25), proporcion_y(0.25));

        click = new InputAdapter() {
            public boolean touchUp (int x, int y, int pointer, int button) {
                if (boton_jugar_rosco.contains(x, y)) {
                    rosco = new Juego_Rosco(letras_descripciones, juego);
                    if (jugadores.jugadores_activados())
                        juego.setScreen(rosco);
                } else
                if (boton_jugar_qqsm.contains(x, y)) {
                    qqsm = new Juego_QQSM(juego);
                    if (jugadores.jugadores_activados())
                        juego.setScreen(qqsm);
                }
                return false;
            }
        };
    }

    public void texturas_cargar() {
        fondo = new Texture("data/texturas/fondo/juegos.jpg");
        botones_juegos = new Texture("data/texturas/botones_juegos.png");

        panel_checkboxs = new Table();
        ScrollPane panel_scroll = new ScrollPane(panel_checkboxs);
        table = new Table();
        table.setSize(proporcion_x(0.2), proporcion_y(0.8));
        table.setPosition(proporcion_x(0.1), proporcion_y(0.5) - table.getHeight() / 2);
        table.add(panel_scroll);
        stage.addActor(table);

        panel_checkboxs.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (((CheckBox)(actor)).isChecked())
                    jugadores.marcar(((CheckBox)(actor)).getText().toString());
                else
                    jugadores.desmarcar(((CheckBox)(actor)).getText().toString());
            }
        });

        texturas.letras = new Texture[26];
        texturas.fallos = new Texture[26];
        texturas.aciertos = new Texture[26];
        texturas.puntuacion = new Texture[2][10];
        texturas.tiempo = new Texture[3][10];

        for (int i = 0; i < 26; ++i){
            texturas.letras[i] = new Texture("data/texturas/juego_rosco/digitos/letras/l" + i + ".png");
            texturas.fallos[i] = new Texture("data/texturas/juego_rosco/rojo/r" + i + ".png");
            texturas.aciertos[i] = new Texture("data/texturas/juego_rosco/verde/v" + i + ".png");
            if (i < 3)
                for (int j = 0; j < 10; ++j) {
                    if (i < 2)
                        texturas.puntuacion[i][j] = new Texture("data/texturas/juego_rosco/digitos/puntuacion/p" + i + "_" + j + ".png");
                    texturas.tiempo[i][j] = new Texture("data/texturas/juego_rosco/digitos/tiempo/t" + i + "_" + j +".png");
                }
        }
    }
}
