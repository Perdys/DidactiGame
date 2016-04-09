package com.mygdx.DidactiGame.Auxiliares;

import com.badlogic.gdx.graphics.Color;

public class Jugador {
    public String nombre = "Jugador";
    public String edad = "99";
    public Color color = Color.BLUE;

    public boolean seleccionado = true, jugando = false;
    public Integer letra_actual = -1, n_aciertos_rosco = 0, n_fallos_rosco = 0;
    public float contador_rosco = 0;
    public int letras[] = new int[26], tiempo_rosco = 150;

    public Jugador() {}

    public Jugador (Fichero fichero) {
        this.nombre = fichero.nombre_jugador();
        this.edad = fichero.edad_jugador();
        this.color = fichero.color_jugador();
    }
}