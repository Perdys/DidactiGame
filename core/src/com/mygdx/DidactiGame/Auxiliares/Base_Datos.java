package com.mygdx.DidactiGame.Auxiliares;

import com.badlogic.gdx.Gdx;

import java.sql.*;
import java.util.ArrayList;

public class Base_Datos {

    Connection jugadores_bd = null, rosco_bd = null, qqsm_bd = null;
    Statement jugadores_sentencia, rosco_sentencia, qqsm_sentencia;

    public Base_Datos () {

        try {
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException clas) { Gdx.app.log("sql_clase", clas.toString());}

            jugadores_bd = DriverManager.getConnection("jdbc:sqlite:jugadores.db");
            jugadores_sentencia = jugadores_bd.createStatement();
            jugadores_sentencia.setQueryTimeout(30);  // set timeout to 30 sec.
            jugadores_sentencia.executeUpdate("create table if not exists jugadores (nombre varchar(20) primary key, edad varchar(3), color varchar(16))");
            jugadores_sentencia.executeUpdate("create table if not exists puntuaciones (jugador varchar(20), juego varchar(10), puntuacion varchar(70), foreign key (jugador) references jugadores(nombre))");

            rosco_bd = DriverManager.getConnection("jdbc:sqlite:rosco.db");
            rosco_sentencia = rosco_bd.createStatement();
            rosco_sentencia.setQueryTimeout(30);  // set timeout to 30 sec.
            rosco_sentencia.executeUpdate("create table if not exists rosco (palabra varchar(20) primary key, descripcion text)");

            qqsm_bd = DriverManager.getConnection("jdbc:sqlite:qqsm.db");
            qqsm_sentencia = qqsm_bd.createStatement();
            qqsm_sentencia.setQueryTimeout(30);  // set timeout to 30 sec.
            qqsm_sentencia.executeUpdate("create table if not exists qqsm (id text, puntuacion text)");

        } catch (SQLException sql){ Gdx.app.log("sql_crear_sentencia", sql.toString()); }
    }

    public void escribir_descripciones(ArrayList<ArrayList<String[]>> descripciones_entrada) {

        try {
            rosco_sentencia.execute("drop table if exists rosco");
            rosco_sentencia.executeUpdate("create table if not exists rosco (palabra text primary key, descripcion text)");
            while (!descripciones_entrada.isEmpty()) {
                while (!descripciones_entrada.get(0).isEmpty()) {
                    rosco_sentencia.executeUpdate("insert into rosco values('" + descripciones_entrada.get(0).get(0)[0] + "', '" + descripciones_entrada.get(0).get(0)[1] + "')");
                    descripciones_entrada.get(0).remove(0);
                }
                descripciones_entrada.remove(0);
            }
        } catch (SQLException sql){ Gdx.app.log("sql_escribir_descripciones", sql.toString()); }
    }

    public void escribir_descripciones(String contenido) {
        try {
            rosco_sentencia.execute("drop table if exists rosco");
            rosco_sentencia.executeUpdate("create table if not exists rosco (palabra text primary key, descripcion text)");

            String linea, palabra, descripcion;
            for (int i = 0; i < contenido.split("\n").length; ++i) {
                linea = contenido.split("\n")[i];
                palabra = linea.split(" ")[0];
                descripcion = linea.substring(palabra.length() + 1);

                rosco_sentencia.executeUpdate("insert into rosco values('" + palabra + "', '" + descripcion + "')");
            }
        }catch (SQLException sql){ Gdx.app.log("sql_escribir_editor", sql.toString()); }
    }

    public void leer_descripciones(ArrayList<ArrayList<String[]>> descripciones) {

        try {
            String[] letras = {"aA", "bB", "cC", "dD", "eE", "fF", "gG", "hH", "iI", "jJ", "kK", "lL", "mM",
                    "nN", "oO", "pP", "qQ", "rR", "sS", "tT", "uU", "vV", "wW", "xX", "yY", "zZ"};

            ResultSet rs = rosco_sentencia.executeQuery("select * from rosco");

            while (rs.next()) {
                int j = 0;
                while (!letras[j].contains(Character.toString(rs.getString("palabra").charAt(0))) && j < 25)
                    //mientras que la primera letra de la linea no coincide con alguna del vector de letras
                    //o pueda existir la primera letra de la linea
                    ++j;

                descripciones.get(j).add(new String[]{rs.getString("palabra"), rs.getString("descripcion")});
            }
            rs.close();
        } catch (SQLException sql){ Gdx.app.log("sql_leer_descripciones", sql.toString()); }
    }

    public String leer_letras() {
        String letras = "";
        try {
            ResultSet rs = rosco_sentencia.executeQuery("select * from rosco");

            while (rs.next())
                letras += rs.getString(1) + " " + rs.getString(2) + "\n";

            rs.close();
        } catch (SQLException sql){ Gdx.app.log("sql_leer_letras", sql.toString()); }
        return letras;
    }

    public void escribir_puntuacion (String jugador, String puntuacion, String juego_tipo) {
        try {
            jugadores_sentencia.executeUpdate("insert into puntuaciones (jugador, juego, puntuacion) values('" + jugador + "', '" + juego_tipo + "', '" + puntuacion + "')");
        } catch (SQLException sql){ Gdx.app.log("sql_escribir_puntuacion", sql.toString()); }
    }

    public String puntuaciones (String jugador, String juego_tipo) {
        String puntuacion = "";
        try {
            ResultSet rs = jugadores_sentencia.executeQuery("select puntuacion from puntuaciones where jugador = '" + jugador + "' and juego = '" + juego_tipo + "'");

            while (rs.next())
                puntuacion += "  " + rs.getString(1) + "\n";

            rs.close();
        } catch (SQLException sql){ Gdx.app.log("sql_leer_puntuaciones", sql.toString()); }
        return puntuacion;
    }

    public void escribir_jugador (String jugador, String edad, String color) {
        try {
            jugadores_sentencia.executeUpdate("insert or ignore into jugadores (nombre, edad, color) values('" + jugador + "', '" + edad + "', '" + color + "')");
        } catch (SQLException sql){ Gdx.app.log("sql_escribir_jugador", sql.toString()); }
    }

    public void escribir_edad (String jugador, String edad) {
        try {
            jugadores_sentencia.executeUpdate("update jugadores set edad = '" + edad + "' where nombre = '"+ jugador + "'");
        } catch (SQLException sql){ Gdx.app.log("sql_escribir_edad", sql.toString()); }
    }

    public void escribir_color (String jugador, String color) {
        try {
            jugadores_sentencia.executeUpdate("update jugadores set color = '" + color + "' where nombre = '"+ jugador + "'");
        } catch (SQLException sql){ Gdx.app.log("sql_escribir_color", sql.toString()); }
    }

    public void leer_jugadores(ArrayList<Jugador> coleccion) {
        try {
            ResultSet rs = jugadores_sentencia.executeQuery("select nombre, edad, color from jugadores");

            while (rs.next())
                coleccion.add(new Jugador(rs.getString(1), rs.getString(2), rs.getString(3)));

            rs.close();
        } catch (SQLException sql){ Gdx.app.log("sql_leer_jugadores", sql.toString()); }
    }

    public void eliminar_jugador(String jugador) {
        try {
            jugadores_sentencia.executeQuery("delete from jugadores where nombre = '" + jugador + "'");
        } catch (SQLException sql){ Gdx.app.log("sql_eliminar_jugador", sql.toString()); }
    }

    public String leer_nombres_jugadores() {
        String nombres = "";
        try {
            ResultSet rs = jugadores_sentencia.executeQuery("select nombre from jugadores");

            while (rs.next())
                nombres += rs.getString(1) + "\n";

            rs.close();
        } catch (SQLException sql){ Gdx.app.log("sql_leer_nombres_jugadores", sql.toString()); }
        return nombres;
    }

    public void liberar() {
        try {
            jugadores_sentencia.close();
            rosco_sentencia.close();
            qqsm_sentencia.close();
            qqsm_bd.close();
            rosco_bd.close();
            jugadores_bd.close();
        } catch (SQLException sql){ Gdx.app.log("sql_liberar", sql.toString()); }
    }
}
