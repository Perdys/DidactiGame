package com.mygdx.DidactiGame.Auxiliares;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;

import static com.mygdx.DidactiGame.DidactiGame.jugadores;

public class Jugadores {

    ArrayList<Jugador> coleccion = new ArrayList<>();
    FileHandle directorio = Gdx.files.absolute(Gdx.files.getLocalStoragePath() + "data/ficheros/jugadores/");
    private int jugador_actual = 0;

    public Jugadores() {
        Fichero fichero;

        for(int i = 0; i < directorio.list().length; ++i) {
            fichero = new Fichero("data/ficheros/jugadores/" + directorio.list()[i].name());
            Jugador jug = new Jugador(fichero);
            coleccion.add(i, jug);
        }

        if (directorio.list().length == 0) {
            coleccion.add(0, new Jugador());
        }
    }

    public Jugador jugador(int numero) { return coleccion.get(numero); }

    public Jugador jugador_actual() { return coleccion.get(jugador_actual); }

    public Jugador jugador(String nombre) {
        for (int i = 0; i < coleccion.size(); ++i) {
            if (coleccion.get(i).nombre.compareTo(nombre) == 0)
                return coleccion.get(i);
        }
        return null;
    }

    public boolean vacio() { return coleccion.isEmpty(); }

    public int numeral() { return coleccion.size(); }

    public void eliminar(Jugador jugador) {
        Fichero fichero = new Fichero("data/ficheros/jugadores/" + jugador.nombre);
        fichero.fichero.delete();

        coleccion.remove(coleccion.indexOf(jugador));
    }

    public void anadir(String nombre) {
        coleccion.add(new Jugador(new Fichero("data/ficheros/jugadores/" + nombre)));
    }

    public boolean siguiente() {

        jugadores.jugador_actual().jugando = false;

        if ((jugadores.jugador_actual().n_aciertos_rosco + jugadores.jugador_actual().n_fallos_rosco) >= 26) {
            jugadores.jugador_actual().seleccionado = false;

            if (coleccion.size() <= 1)
                return false;
        }

        for (int i = jugador_actual + 1; i < coleccion.size(); ++i) {
            if (coleccion.get(i).seleccionado) {
                ++jugador_actual;
                return true;
            }
            if (i >= coleccion.size())
                i = 0;
            if (i == jugador_actual)
                return false;
        }

        return true;
    }

    public String nombres() {
        String nombres = "";

        for(int i = 0; i < directorio.list().length; ++i)
            nombres += directorio.list()[i].name() + "\n";

        return nombres;
    }
}
