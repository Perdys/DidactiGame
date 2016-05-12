package com.mygdx.DidacticGame.Auxiliares;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;

import static com.mygdx.DidacticGame.DidacticGame.BD;
import static com.mygdx.DidacticGame.DidacticGame.jugadores;

public class Jugadores {

    ArrayList<Jugador> coleccion = new ArrayList<>();
    private int jugador_actual = -1;

    public Jugadores() {

        BD.leer_jugadores(coleccion);
        if (!vacio())
            jugador_actual = 0;
    }

    public Jugador jugador(int numero) {
        return coleccion.get(numero);
    }

    public Jugador jugador_actual() {
        if (jugador_actual == -1)
            return null;
        else
            return coleccion.get(jugador_actual);
    }

    public boolean activados() {
        return (jugador_actual != -1);
    }

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
        BD.eliminar_jugador(jugador.nombre);

        desmarcar(jugador.nombre);
        coleccion.remove(coleccion.indexOf(jugador));
    }

    public void anadir(String nombre) {
        coleccion.add(0, new Jugador(nombre, 99, Color.WHITE));

        if (jugador_actual == -1)
            jugador_actual = 0;
    }

    public void marcar (String nombre) {
        jugadores.jugador(nombre).seleccionado = true;
        if (jugador_actual == -1)
            jugador_actual = coleccion.indexOf(jugadores.jugador(nombre));
    }

    //Se encarga de deseleccionar un jugador para la siguiente partida
    public void desmarcar (String nombre) {
        jugadores.jugador(nombre).seleccionado = false;

        for (int i = jugador_actual; i < coleccion.size(); ++i) {
            if (i + 1 == coleccion.size()) //Caso dar la vuelta hasta el primer jugador
                i = -2;
            else
            if (coleccion.get(i + 1).seleccionado) { //Caso siguiente jugador
                jugador_actual = i + 1;
                i = coleccion.size(); //Salir
            }
            else
            if (i == jugador_actual - 1) { //Caso ningun jugador restante despues de dar la vuelta
                jugador_actual = -1;
                i = coleccion.size(); //Salir
            }
        }
    }

    //Se encarga de encontrar al siguiente jugador que este seleccionado
    public boolean siguiente() {
        jugadores.jugador_actual().jugando = false;

        for (int i = jugador_actual; i < coleccion.size(); ++i) {
            if (i + 1 >= coleccion.size()) //Caso dar la vuelta hasta el primer jugador
                i = -2;
            else
            if (coleccion.get(i + 1).seleccionado) { //Caso siguiente jugador
                jugador_actual = i + 1;
                return true;
            }
            else
            if (i == jugador_actual - 1) //Caso ningun jugador restante despues de dar la vuelta
                return false;
        }

        return true;
    }

    public String nombres() {
        String nombres = BD.leer_nombres_jugadores();

        if (nombres.isEmpty())
            return "Añadir jugador";
        else
            return nombres;
    }
}
