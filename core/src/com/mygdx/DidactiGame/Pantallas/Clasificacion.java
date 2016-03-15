package com.mygdx.DidactiGame.Pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.DidactiGame.DidactiGame;
import com.mygdx.DidactiGame.Herramientas.Pantalla;

import java.util.ArrayList;

public class Clasificacion extends Pantalla {

    DidactiGame juego;
    Stage stage;
    OrthographicCamera camara;
    SpriteBatch batch;

    BitmapFont puntuaciones_tabla;
    String puntuaciones = "NADA";

    public class Jugador {
        int puntuacion = 0;
        String nombre = "Jugador X";

        public Jugador (int p, String n) { this.puntuacion = p; this.nombre = n; }
    }

    ArrayList<Jugador> jugadores;

    public Clasificacion (DidactiGame juego) {
        this.juego = juego;

        camara = new OrthographicCamera();
        camara.setToOrtho(false);
        stage = new Stage();
        batch = new SpriteBatch();

        pantalla_actual = "Clasificacion";

        puntuaciones_tabla = new BitmapFont();
        puntuaciones_tabla.setColor(Color.RED);
        jugadores = new ArrayList<>(1);
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

    public void anadir (int puntuacion, String nombre) {
        int j = 0;
        for (int i = 0; i < jugadores.size(); ++i) {
            if (jugadores.get(i).puntuacion < puntuacion)
                i = jugadores.size();
            ++j;
        }
        jugadores.add(j, new Jugador(puntuacion, nombre));

        puntuaciones = "- TABLA PUNTUACIONES -\n\n\n-PUNTUACION-  -JUGADOR-\n\n";
        for (int i = 0; i < jugadores.size(); ++i)
            puntuaciones = puntuaciones.concat("             " + jugadores.get(i).puntuacion +
                    "                     " + jugadores.get(i).nombre + "\n");
    }
}
