package com.mygdx.DidactiGame.Pantallas.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.DidactiGame.DidactiGame;
import com.mygdx.DidactiGame.Auxiliares.Pantalla;
import com.mygdx.DidactiGame.Pantallas.Juego_Rosco;

import java.util.ArrayList;

import static com.mygdx.DidactiGame.DidactiGame.jugadores;

public class Menu_Juegos extends Pantalla {
    DidactiGame juego;
    Stage stage;
    OrthographicCamera camara;
    SpriteBatch batch;
    InputAdapter click;

    Juego_Rosco rosco;
    Texture fondo;
    Rectangle boton_jugar;

    public static class Elementos_Mostrar {
        public Texture[] letras, rojos, verdes;
        public Texture[][] tiempo, puntuacion;
    }

    ArrayList<ArrayList<String[]>> descripciones;
    public static Elementos_Mostrar elementos_mostrar;

    public Menu_Juegos(DidactiGame juego) {
        this.juego = juego;

        camara = new OrthographicCamera();
        camara.setToOrtho(false);
        stage = new Stage();
        batch = new SpriteBatch();

        pantalla_actual = "Menu_Juegos";

        elementos_mostrar = new Elementos_Mostrar();

        descripciones = new ArrayList<>(26);
        for (int i = 0; i < 26; ++i) descripciones.add(i, new ArrayList<String[]>(1));
        letras_rosco_fichero.leer(descripciones);

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
        batch.draw(fondo, 0, 0, anchura_juego, altura_juego);
        batch.end();

        stage.act();
        stage.draw();
    }

    public void resume() { pantalla_actual = "Menu_Juegos"; }

    public void show() {
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

        letras_rosco_fichero.escribir(descripciones);
        rosco.dispose();
        generador_texto.dispose();
        batch.dispose();
        stage.dispose();
    }

    public void botones_cargar() {

        boton_jugar = new Rectangle(proporcion_x(0.5), proporcion_y(0), proporcion_x(0.5), proporcion_y(1));
        //TODO añadir boton jugar QQSM
        click = new InputAdapter() {
            public boolean touchUp (int x, int y, int pointer, int button) {
                if (boton_jugar.contains(x, y)) {
                        rosco = new Juego_Rosco(descripciones, juego);
                        juego.setScreen(rosco);
                }
                return false;
            }
        };
    }

    public void texturas_cargar() {
        fondo = new Texture("data/menu_juegos/fondo_juegos.jpg");

        tamano_texto.size = (int)proporcion_y(0.06);
        CheckBox.CheckBoxStyle estilo_checkbox = new CheckBox.CheckBoxStyle(
                new TextureRegionDrawable(new TextureRegion(new Texture("data/menu_juegos/checkOFF.png"))),
                new TextureRegionDrawable(new TextureRegion(new Texture("data/menu_juegos/checkON.png"))),
                generador_texto.generateFont(tamano_texto), Color.GREEN);
        estilo_checkbox.checkboxOn.setMinWidth((int) proporcion_y(0.06));
        estilo_checkbox.checkboxOn.setMinHeight((int) proporcion_y(0.06));
        estilo_checkbox.checkboxOff.setMinWidth((int) proporcion_y(0.06));
        estilo_checkbox.checkboxOff.setMinHeight((int) proporcion_y(0.06));

        Table panel_checkboxs = new Table();
        for (int i = 0; i < jugadores.numeral(); ++i) {
            CheckBox cb = new CheckBox(jugadores.jugador(i).nombre, estilo_checkbox);
            cb.setChecked(jugadores.jugador(i).seleccionado);
            panel_checkboxs.add(cb).left();
            panel_checkboxs.row();
        }
        ScrollPane panel_scroll = new ScrollPane(panel_checkboxs);
        Table table = new Table();
        table.setSize(proporcion_x(0.2), proporcion_y(0.8));
        table.setPosition(proporcion_x(0.1), proporcion_y(0.5) - table.getHeight() / 2);
        table.add(panel_scroll);
        stage.addActor(table);

        panel_checkboxs.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                jugadores.jugador(((CheckBox)(actor)).getText().toString()).seleccionado = ((CheckBox)(actor)).isChecked();
            }
        });

        elementos_mostrar.letras = new Texture[26];
        elementos_mostrar.rojos = new Texture[26];
        elementos_mostrar.verdes = new Texture[26];
        elementos_mostrar.puntuacion = new Texture[2][10];
        elementos_mostrar.tiempo = new Texture[3][10];

        for (int i = 0; i < 26; ++i){
            elementos_mostrar.letras[i] = new Texture("data/menu_juegos/juego_rosco/digitos/letras/l" + i + ".png");
            elementos_mostrar.rojos[i] = new Texture("data/menu_juegos/juego_rosco/rojo/r" + i + ".png");
            elementos_mostrar.verdes[i] = new Texture("data/menu_juegos/juego_rosco/verde/v" + i + ".png");
            if (i < 3)
                for (int j = 0; j < 10; ++j) {
                    if (i < 2)
                        elementos_mostrar.puntuacion[i][j] = new Texture("data/menu_juegos/juego_rosco/digitos/puntuacion/p" + i + "_" + j + ".png");
                    elementos_mostrar.tiempo[i][j] = new Texture("data/menu_juegos/juego_rosco/digitos/tiempo/t" + i + "_" + j +".png");
                }
        }
    }
}
