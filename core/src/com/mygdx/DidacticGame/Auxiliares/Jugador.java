package com.mygdx.DidacticGame.Auxiliares;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Date;

import static com.mygdx.DidacticGame.DidacticGame.BD;

public class Jugador {

    public String nombre = "Jugador";
    public int edad = 99;
    public Color color = Color.BLUE;

    public boolean seleccionado = true, jugando = false;

    public Integer letra_actual = -1;
    public int letras_visitadas[] = new int[26];

    public Integer n_aciertos = 0, n_fallos = 0;
    public int tiempo_qqsm = 0, tiempo_rosco = 150;
    public float contador = 0;
    public boolean comodin50_usado = false;

    public Jugador (String nombre, int edad, Color color) {
        this.nombre = nombre;
        this.edad = edad;
        this.color = color;
        BD.anadir_jugador(nombre, edad, color.toString());
    }

    public Jugador (String nombre, int edad, String color) {
        this.nombre = nombre;
        this.edad = edad;
        this.color = Color.valueOf(color);
    }

    public String guardar_puntuacion(String juego_tipo) {
        String[] campos = new Date(TimeUtils.millis()).toString().split(" ");
        String fecha = " " + campos[2];
        switch (campos[1]) {
            case "Jan": fecha = fecha.concat("/01/"); break;
            case "Feb": fecha = fecha.concat("/02/"); break;
            case "Mar": fecha = fecha.concat("/03/"); break;
            case "Apr": fecha = fecha.concat("/04/"); break;
            case "May": fecha = fecha.concat("/05/"); break;
            case "Jun": fecha = fecha.concat("/06/"); break;
            case "Jul": fecha = fecha.concat("/07/"); break;
            case "Ago": fecha = fecha.concat("/08/"); break;
            case "Sep": fecha = fecha.concat("/09/"); break;
            case "Oct": fecha = fecha.concat("/10/"); break;
            case "Nov": fecha = fecha.concat("/11/"); break;
            case "Dec": fecha = fecha.concat("/12/"); break;
            default: fecha = fecha.concat("/00/"); break;
        }
        fecha = fecha.concat(campos[5]);

        String puntuacion = "";

        if (juego_tipo.compareTo("QQSM") == 0) {
            puntuacion = "\nQ " + n_aciertos + " " + n_fallos + " " + tiempo_qqsm + fecha;
            BD.anadir_puntuacion(nombre, n_aciertos, n_fallos, tiempo_qqsm, fecha, "QQSM");
        }
        else
        if (juego_tipo.compareTo("Rosco") == 0) {
            puntuacion = "\nR " + n_aciertos + " " + n_fallos + " " + tiempo_rosco + fecha;
            BD.anadir_puntuacion(nombre, n_aciertos, n_fallos, tiempo_rosco, fecha, "Rosco");
        }

        return puntuacion_formato(new String[] {puntuacion.split(" ")[1], puntuacion.split(" ")[2], puntuacion.split(" ")[3], puntuacion.split(" ")[4]});
    }

    public static String puntuacion_formato(String[] registros) {
        if (registros[0].length() == 1) registros[0] = "".concat(registros[0] + "  ");
        if (registros[1].length() == 1) registros[1] = " ".concat(registros[1] + " ");
        if (registros[2].length() == 1) registros[2] = "   ".concat(registros[2] + "  ");
        else if (registros[2].length() == 2) registros[2] = " ".concat(registros[2] + " ");

        return (registros[0] + "        " + registros[1] + "        " + registros[2] + "  " + registros[3]);
    }

    public String puntuaciones(String juego_tipo) { return BD.leer_puntuaciones(nombre, juego_tipo); }

    public void edad (int edad) {
        this.edad = edad;
        BD.actualizar_edad(nombre, edad);
    }

    public void color (Color color) {
        this.color = color;
        BD.actualizar_color(nombre, color.toString());
    }
}