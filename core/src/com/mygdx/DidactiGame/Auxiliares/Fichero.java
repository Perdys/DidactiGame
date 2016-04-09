package com.mygdx.DidactiGame.Auxiliares;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;

public class Fichero {

    FileHandle fichero;
    public String contenido;

    public Fichero() {}

    public Fichero (String direccion) {
        fichero = Gdx.files.absolute(Gdx.files.getLocalStoragePath() + direccion);
        if (fichero.exists())
            contenido = fichero.readString();
        else {
            fichero.writeString("99\n" + Color.WHITE.toIntBits(), true);
            contenido = fichero.readString();
        }
    }

    public void escribir(ArrayList<ArrayList<String[]>> descripciones_entrada) {

        contenido = "";

        if (!descripciones_entrada.isEmpty() && !descripciones_entrada.get(0).isEmpty()) {
            contenido += descripciones_entrada.get(0).get(0)[0] + " " + descripciones_entrada.get(0).get(0)[1] + "\n";
            descripciones_entrada.get(0).remove(0);
        }

        while (!descripciones_entrada.isEmpty()) {
            while (!descripciones_entrada.get(0).isEmpty()) {
                if (!contenido.contains(descripciones_entrada.get(0).get(0)[1]))
                    contenido += descripciones_entrada.get(0).get(0)[0] + " " + descripciones_entrada.get(0).get(0)[1] + "\n";
                descripciones_entrada.get(0).remove(0);
            }
            descripciones_entrada.remove(0);
        }

        fichero.writeString(contenido, false);
    }

    public void escribir (String contenido) { fichero.writeString(contenido, false); }

    public static void edad_escribir (String jugador, String edad) {
        FileHandle fichero = Gdx.files.absolute(Gdx.files.getLocalStoragePath() + "data/ficheros/jugadores/" + jugador);
        fichero.writeString(edad + "\n" + fichero.readString().split("\n")[1], false);
    }

    public static void color_escribir (String jugador, String color) {
        FileHandle fichero = Gdx.files.absolute(Gdx.files.getLocalStoragePath() + "data/ficheros/jugadores/" + jugador);
        fichero.writeString(fichero.readString().split("\n")[0] + "\n" + color, false);
    }

    public void leer(ArrayList<ArrayList<String[]>> descripciones) {

        String[] letras = {"aA","bB","cC","dD","eE","fF","gG","hH","iI","jJ","kK","lL","mM",
                           "nN","oO","pP","qQ","rR","sS","tT","uU","vV","wW","xX","yY","zZ"};
        String palabra, descripcion;

        contenido = fichero.readString();
        String[] lineas = contenido.split("\n"); //leo el fichero entero y lo almaceno en lineas
        if (lineas.length > 1)
            for (int i = 0, j = 0; i < lineas.length; ++i, j = 0) {
                while (!letras[j].contains(Character.toString(lineas[i].charAt(0))))
                    //mientras que la primera letra de la linea no coincide con alguna del vector de letras
                    //o pueda existir la primera letra de la linea
                    ++j;

                if (lineas[i].contains(" ")) {
                    palabra = lineas[i].substring(0, lineas[i].indexOf(" "));
                    descripcion = lineas[i].substring(lineas[i].indexOf(" ") + 1);
                    descripciones.get(j).add(new String[]{palabra, descripcion});
                }
            }
    }

    public String nombre_jugador() { return fichero.name(); }

    public String edad_jugador() {
        if (contenido.isEmpty())
            return "99";
        else
            return contenido.split("\n")[0];
    }

    public Color color_jugador() {
        if (contenido.isEmpty())
            return Color.WHITE;
        else
            return new Color(Integer.parseInt(contenido.split("\n")[1]));
    }
}