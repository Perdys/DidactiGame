package com.mygdx.psplbr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ArraySelection;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import javafx.util.Pair;

import java.util.Map;

public class Menu extends Pantalla{
    PsPlbr juego;
    Stage stage;
    OrthographicCamera camara;
    SpriteBatch batch;

    Texture fondo;
    ImageButton boton_jugar, boton_mas, boton_menos;

    public Object[] partidas_jugadores, elementos_mostrar;
    public Texture[] letras, rojos, verdes;
    public Texture[][] tiempo, puntuacion;

    public Menu(PsPlbr juego) {
        this.juego = juego;

        camara = new OrthographicCamera();
        camara.setToOrtho(false);
        stage = new Stage();
        batch = new SpriteBatch();

        pantalla_actual = 1;

        fondo = new Texture("data/menu/fondo_menu.jpg");
        boton_jugar = setButton("data/menu/boton_jugar.jpg");
        boton_jugar.setPosition((float)(anchura_juego * 0.75) - boton_jugar.getWidth() / 2, (float)(altura_juego * 0.33) - boton_jugar.getHeight() / 2);
        boton_mas = setButton("data/menu/boton_mas.jpg");
        boton_mas.setPosition((float)(anchura_juego * 0.33) - boton_mas.getWidth() / 2, (float)(altura_juego * 0.33));
        boton_menos = setButton("data/menu/boton_menos.jpg");
        boton_menos.setPosition((float)(anchura_juego * 0.33) - boton_menos.getWidth() / 2, (float)(altura_juego * 0.33) - boton_menos.getHeight());
    }

    public void render(float delta) {	/*ejecutarse todas las veces posible por segundo, ejecuten todas las acciones del juego
		dibujar los elementos por pantalla, procesar la entrada y salida, mover los personajes, detectar colisiones, etc. */
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();

        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.draw(fondo, 0, 0);
        batch.end();

        stage.act();
        stage.draw();
    }

    public void resize(int width, int height) {} //sirve para recalcular el tama�o de los elementos cuando se modifica la pantalla

    public void pause() {}	/*, y resume() y pause(), que son funciones que se ejecutan en Android cuando salimos de la aplicaci�n o se interrumpe la ejecuci�n de la misma y volvemos a ella.*/

    public void resume() {}

    public void show() {
        stage.addActor(boton_jugar);
        stage.addActor(boton_mas);
        stage.addActor(boton_menos);
        setStageButton(juego);

        InputMultiplexer inputs = new InputMultiplexer();
        inputs.addProcessor(stage_botones);
        inputs.addProcessor(stage);
        Gdx.input.setInputProcessor(inputs);
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);
    }

    public void hide() { dispose(); }

    public void dispose() { //es la ultima en ejecutarse, se encarga de liberar recursos y dejar la memoria limpia
        batch.dispose();
        stage.dispose();
    }

    public ImageButton setButton(final String imagen) {
        ImageButton.ImageButtonStyle estilo_boton = new ImageButton.ImageButtonStyle();
        Skin skin = new Skin();
        skin.add("boton", new Texture(imagen));
        estilo_boton.up = skin.getDrawable("boton");
        estilo_boton.unpressedOffsetY = -(lado / 6);
        estilo_boton.pressedOffsetY = -(lado / 6);

        ImageButton boton = new ImageButton(estilo_boton);
        //boton.setSize(lado / 4, lado / 4);

        boton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switch (imagen) {
                    case "data/menu/boton_jugar.jpg": jugar();
                        break;
                    case "data/menu/boton_mas.jpg": ++n_jugadores;
                        break;
                    case "data/menu/boton_menos.jpg": if (n_jugadores > 2) --n_jugadores;
                        break;
                    default: break;
                }
            }
        });
        return boton;
    }

    public void jugar (){
        partidas_jugadores = new Rosco[n_jugadores];
        letras = new Texture[26];
        rojos = new Texture[26];
        verdes = new Texture[26];
        puntuacion = new Texture[2][10];
        tiempo = new Texture[3][10];
        elementos_mostrar = new Object[]{ letras, puntuacion, tiempo, rojos, verdes};

        for (int i = 0; i < 26; ++i){
            letras[i] = new Texture("data/rosco/digitos/letras/l" + i + ".png");
            rojos[i] = new Texture("data/rosco/rojo/r" + i + ".png");
            verdes[i] = new Texture("data/rosco/verde/v" + i + ".png");
            if (i < 3)
                for (int j = 0; j < 10; ++j) {
                    if (i < 2)
                        puntuacion[i][j] = new Texture("data/rosco/digitos/puntuacion/p" + i + "_" + j + ".png");
                    tiempo[i][j] = new Texture("data/rosco/digitos/tiempo/t" + i + "_" + j +".png");
                }
        }

        for(int i = 0; i < n_jugadores; ++i)
            partidas_jugadores[i] = new Rosco(partidas_jugadores, elementos_mostrar, juego);

        juego.setScreen((Rosco)partidas_jugadores[jugador_actual]);
    }
}
