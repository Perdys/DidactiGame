package com.mygdx.DidactiGame.Pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.DidactiGame.DidactiGame;
import com.mygdx.DidactiGame.Herramientas.Pantalla;

import java.util.ArrayList;

public class Menu_Juegos extends Pantalla {
    DidactiGame juego;
    Stage stage;
    OrthographicCamera camara;
    SpriteBatch batch;

    Juego_Rosco rosco;
    Texture fondo;
    ImageButton boton_jugar, boton_mas, boton_menos, boton_anadir;

    public static class Elementos_Mostrar {
        public Texture[] letras, rojos, verdes;
        public Texture[][] tiempo, puntuacion, jugadores;
    }
    public Integer n_jugadores = 1;

    ArrayList<ArrayList<String[]>> descripciones;
    static Elementos_Mostrar elementos_mostrar;

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
    }

    public void render(float delta) {	/*ejecutarse todas las veces posible por segundo, ejecuten todas las acciones del juego
		dibujar los elementos por pantalla, procesar la entrada y salida, mover los personajes, detectar colisiones, etc. */
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();

        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.draw(fondo, 0, 0, anchura_juego, altura_juego);
        batch.draw(elementos_mostrar.jugadores[0][n_jugadores%10], 0, 0, anchura_juego, altura_juego);
        batch.draw(elementos_mostrar.jugadores[1][n_jugadores/10], 0, 0, anchura_juego, altura_juego);
        batch.end();

        stage.act();
        stage.draw();
    }

    public void resume() { pantalla_actual = "Menu_Juegos"; }

    public void show() {
        stage.addActor(boton_jugar);
        stage.addActor(boton_mas);
        stage.addActor(boton_menos);
        stage.addActor(boton_anadir);
        sistema_botones(juego);

        InputMultiplexer inputs = new InputMultiplexer();
        inputs.addProcessor(botones_genericos);
        inputs.addProcessor(stage);
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

    public ImageButton imagen_texto_boton (final String imagen) {
        ImageButton.ImageButtonStyle estilo_boton = new ImageButton.ImageButtonStyle();
        Skin skin = new Skin();
        skin.add("boton", new Texture(imagen));
        estilo_boton.up = skin.getDrawable("boton");
        estilo_boton.unpressedOffsetY = -(lado / 6);
        estilo_boton.pressedOffsetY = -(lado / 6);

        ImageButton boton = new ImageButton(estilo_boton);

        boton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switch (imagen) {
                    case "data/menu_juegos/boton_jugar.jpg":
                        rosco = new Juego_Rosco(n_jugadores, descripciones, juego);
                        juego.setScreen(rosco);
                        break;
                    case "data/menu_juegos/boton_mas.jpg": ++n_jugadores;
                        break;
                    case "data/menu_juegos/boton_menos.jpg": if (n_jugadores > 1) --n_jugadores;
                        break;
                    case "data/menu_juegos/boton_anadir.jpg": descripcion_anadir();
                        break;
                    default: break;
                }
            }
        });
        return boton;
    }

    public void descripcion_anadir() {

        Gdx.input.getTextInput (new Input.TextInputListener() {

            public void input(String letra_nueva) {
                String[] letras = {"aA", "bB", "cC", "dD", "eE", "fF", "gG", "hH", "iI", "jJ", "kK", "lL", "mM",
                                   "nN", "oO", "pP", "qQ", "rR", "sS", "tT", "uU", "vV", "wW", "xX", "yY", "zZ"};

                int i = 0;
                while (!letras[i].contains(Character.toString(letra_nueva.charAt(0))) || i > 25)
                    //mientras que la primera letra de la linea no coincide con alguna del vector de letras
                    //o pueda existir la primera letra de la linea
                    ++i;

                String palabra = letra_nueva.substring(0, letra_nueva.indexOf(" "));
                String descripcion = letra_nueva.substring(letra_nueva.indexOf(" ") + 1);
                if (!descripciones.get(i).contains(new String[]{palabra, descripcion}))
                    descripciones.get(i).add(new String[]{palabra, descripcion});
            }

            public void canceled() {}

        }, "Introduzca la descripcion de la letra", "", "'palabra' Empieza por '?':...");
    }

    public void texturas_cargar() {
        fondo = new Texture("data/menu_juegos/fondo_juegos.jpg");
        boton_jugar = imagen_texto_boton("data/menu_juegos/boton_jugar.jpg");
        boton_jugar.setPosition(proporcion_x(0.75) - boton_jugar.getWidth() / 2, proporcion_y(0.33) - boton_jugar.getHeight() / 2);
        boton_mas = imagen_texto_boton("data/menu_juegos/boton_mas.jpg");
        boton_mas.setPosition(proporcion_x(0.33) - boton_mas.getWidth() / 2, proporcion_y(0.33));
        boton_menos = imagen_texto_boton("data/menu_juegos/boton_menos.jpg");
        boton_menos.setPosition(proporcion_x(0.33) - boton_menos.getWidth() / 2, proporcion_y(0.33) - boton_menos.getHeight());
        boton_anadir = imagen_texto_boton("data/menu_juegos/boton_anadir.jpg");
        boton_anadir.setPosition(proporcion_x(0.75) - boton_menos.getWidth() / 2, proporcion_y(0.75) - boton_anadir.getHeight());

        elementos_mostrar.jugadores = new Texture[2][10];

        for (int i = 0; i < 2; ++i)
            for (int j = 0; j < 10; ++j)
                elementos_mostrar.jugadores[i][j] = new Texture("data/menu_juegos/jugadores/j" + i + "_" + j + ".png");

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
