package com.mygdx.DidactiGame.Pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.DidactiGame.Auxiliares.Jugador;
import com.mygdx.DidactiGame.Auxiliares.Jugadores;
import com.mygdx.DidactiGame.DidactiGame;
import com.mygdx.DidactiGame.Auxiliares.Pantalla;

import java.util.ArrayList;

import static com.mygdx.DidactiGame.DidactiGame.jugadores;

public class Clasificacion extends Pantalla {

    DidactiGame juego;
    Stage stage;
    OrthographicCamera camara;
    SpriteBatch batch;

    BitmapFont puntuaciones_tabla;
    String puntuaciones = "NADA";

    public Clasificacion (DidactiGame juego) {
        this.juego = juego;

        camara = new OrthographicCamera();
        camara.setToOrtho(false);
        stage = new Stage();
        batch = new SpriteBatch();

        pantalla_actual = "Clasificacion";

        puntuaciones_tabla = new BitmapFont();
        puntuaciones_tabla.setColor(Color.RED);
        //TODO mejorar esta mierda con varios ScrollPane
        puntuaciones = "- TABLA PUNTUACIONES -\n\n\n-PUNTUACION-  -JUGADOR-\n\n";
        for (int i = 0; i < jugadores.numeral(); ++i)
            puntuaciones = puntuaciones.concat("             " + jugadores.jugador(i).n_aciertos_rosco +
                    "                     " + jugadores.jugador(i).nombre + "\n");

        jugadores = new Jugadores();
    }

    public void render(float delta) {	/*ejecutarse todas las veces posible por segundo, ejecuten todas las acciones del juego
		dibujar los elementos por pantalla, procesar la entrada y salida, mover los personajes, detectar colisiones, etc. */
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();

        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        puntuaciones_tabla.draw(batch, puntuaciones, proporcion_x(0.33), proporcion_y(0.5));
        batch.end();

        stage.act();
        stage.draw();
    }

    public void resume() { pantalla_actual = "Clasificacion"; }
}
