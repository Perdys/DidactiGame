package com.mygdx.DidactiGame.Auxiliares;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.DidactiGame.Pantallas.Menus.Menu_Juegos.*;

import static com.mygdx.DidactiGame.Auxiliares.Jugador.*;
import static com.mygdx.DidactiGame.Pantallas.Menus.Menu_Ranking.*;

import java.io.File;
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

            jugadores_bd = DriverManager.getConnection("jdbc:sqlite:data/jugadores.db");
            jugadores_sentencia = jugadores_bd.createStatement();
            jugadores_sentencia.setQueryTimeout(30);
            jugadores_sentencia.executeUpdate("create table if not exists jugadores (nombre varchar primary key, edad int, color varchar)");
            jugadores_sentencia.executeUpdate("create table if not exists puntuaciones " +
                    "(jugador varchar, juego varchar, aciertos int, fallos int, tiempo int, fecha date, foreign key (jugador) references jugadores(nombre))");

            rosco_bd = DriverManager.getConnection("jdbc:sqlite:data/rosco.db");
            rosco_sentencia = rosco_bd.createStatement();
            rosco_sentencia.setQueryTimeout(30);
            rosco_sentencia.executeUpdate("create table if not exists rosco (palabra varchar primary key, posicion_letra int, descripcion text)");

            qqsm_bd = DriverManager.getConnection("jdbc:sqlite:data/qqsm.db");
            qqsm_sentencia = qqsm_bd.createStatement();
            qqsm_sentencia.setQueryTimeout(30);
            qqsm_sentencia.executeUpdate("create table if not exists qqsm (pregunta text primary key, correcta varchar, respuesta0 varchar, respuesta1 varchar, respuesta2 varchar, respuesta3 varchar)");

        } catch (SQLException sql){ Gdx.app.log("sql_crear_sentencia", sql.toString()); }
    }

    public void anadir_palabra(Palabra palabra) {
        try {
            rosco_sentencia.executeUpdate("insert into rosco (palabra, posicion_letra, descripcion) values('" + palabra.palabra + "', '" + palabra.posicion_letra + "', '" + palabra.descripcion + "')");
        } catch (SQLException sql){ Gdx.app.log("sql_anadir_palabra", sql.toString()); }
    }

    public void actualizar_palabra(String palabra, int posicion_letra, String descripcion) {
        try {
            rosco_sentencia.executeUpdate("update rosco set descripcion = '" + descripcion + "' where palabra = '"+ palabra + "'");
            rosco_sentencia.executeUpdate("update rosco set posicion_letra = '" + posicion_letra + "' where palabra = '"+ palabra + "'");
        } catch (SQLException sql){ Gdx.app.log("sql_actualizar_palabra", sql.toString()); }
    }

    public void actualizar_correcta(String pregunta, String correcta) {
        try {
            qqsm_sentencia.executeUpdate("update qqsm set correcta = '" + correcta + "' where pregunta = '"+ pregunta + "'");
        } catch (SQLException sql){ Gdx.app.log("sql_actualizar_correcta", sql.toString()); }
    }

    public void actualizar_respuesta0(String pregunta, String respuesta0) {
        try {
            qqsm_sentencia.executeUpdate("update qqsm set respuesta0 = '" + respuesta0 + "' where pregunta = '"+ pregunta + "'");
        } catch (SQLException sql){ Gdx.app.log("sql_actualizar_respuesta0", sql.toString()); }
    }

    public void actualizar_respuesta1(String pregunta, String respuesta1) {
        try {
            qqsm_sentencia.executeUpdate("update qqsm set respuesta1 = '" + respuesta1 + "' where pregunta = '"+ pregunta + "'");
        } catch (SQLException sql){ Gdx.app.log("sql_actualizar_respuesta1", sql.toString()); }
    }

    public void actualizar_respuesta2(String pregunta, String respuesta2) {
        try {
            qqsm_sentencia.executeUpdate("update qqsm set respuesta2 = '" + respuesta2 + "' where pregunta = '"+ pregunta + "'");
        } catch (SQLException sql){ Gdx.app.log("sql_actualizar_respuesta2", sql.toString()); }
    }

    public void actualizar_respuesta3(String pregunta, String respuesta3) {
        try {
            qqsm_sentencia.executeUpdate("update qqsm set respuesta3 = '" + respuesta3 + "' where pregunta = '"+ pregunta + "'");
        } catch (SQLException sql){ Gdx.app.log("sql_actualizar_respuesta3", sql.toString()); }
    }

    public String leer_respuesta0(String pregunta) {
        String respuesta = "";
        try {
            ResultSet rs = qqsm_sentencia.executeQuery("select respuesta0 from qqsm where pregunta = '" + pregunta + "'");
            if(rs.next())
                respuesta = rs.getString(1);
            rs.close();
        } catch (SQLException sql){ Gdx.app.log("sql_leer_respuesta0", sql.toString()); }
        return respuesta;
    }

    public String leer_respuesta1(String pregunta) {
        String respuesta = "";
        try {
            ResultSet rs = qqsm_sentencia.executeQuery("select respuesta1 from qqsm where pregunta = '" + pregunta + "'");
            if(rs.next())
                respuesta = rs.getString(1);
            rs.close();
        } catch (SQLException sql){ Gdx.app.log("sql_leer_respuesta1", sql.toString()); }
        return respuesta;
    }

    public String leer_respuesta2(String pregunta) {
        String respuesta = "";
        try {
            ResultSet rs = qqsm_sentencia.executeQuery("select respuesta2 from qqsm where pregunta = '" + pregunta + "'");
            if(rs.next())
                respuesta = rs.getString(1);
            rs.close();
        } catch (SQLException sql){ Gdx.app.log("sql_leer_respuesta2", sql.toString()); }
        return respuesta;
    }

    public String leer_respuesta3(String pregunta) {
        String respuesta = "";
        try {
            ResultSet rs = qqsm_sentencia.executeQuery("select respuesta3 from qqsm where pregunta = '" + pregunta + "'");
            if(rs.next())
                respuesta = rs.getString(1);
            rs.close();
        } catch (SQLException sql){ Gdx.app.log("sql_leer_respuesta3", sql.toString()); }
        return respuesta;
    }

    public void leer_palabras(ArrayList<ArrayList<Palabra>> descripciones) {

        try {
            String[] letras = {"aA", "bB", "cC", "dD", "eE", "fF", "gG", "hH", "iI", "jJ", "kK", "lL", "mM",
                    "nN", "oO", "pP", "qQ", "rR", "sS", "tT", "uU", "vV", "wW", "xX", "yY", "zZ"};

            ResultSet rs = rosco_sentencia.executeQuery("select * from rosco");

            while (rs.next()) {
                int j = 0;
                Palabra palabra = new Palabra(rs.getString("palabra"), rs.getInt("posicion_letra"), rs.getString("descripcion"));
                while (!letras[j].contains(Character.toString(palabra.palabra.charAt(palabra.posicion_letra))) && j < 25)
                    //mientras que la primera letra de la linea no coincide con alguna del vector de letras
                    //o pueda existir la primera letra de la linea
                    ++j;

                if (palabra.posicion_letra != 0)
                    palabra.descripcion = "Contiene la " + palabra.palabra.charAt(palabra.posicion_letra) + ": \n" + palabra.descripcion;
                else
                    palabra.descripcion = "Empieza por " + palabra.palabra.charAt(palabra.posicion_letra) + ": \n" + palabra.descripcion;
                descripciones.get(j).add(palabra);
            }
            rs.close();
        } catch (SQLException sql){ Gdx.app.log("sql_leer_palabras", sql.toString()); }
    }

    public String leer_columna_rosco(String columna) {
        String contenido = "";
        try {
            ResultSet rs = rosco_sentencia.executeQuery("select " + columna + " from rosco");

            while (rs.next())
                contenido += rs.getString(1) + "\n";

            rs.close();
        } catch (SQLException sql){ Gdx.app.log("sql_leer_columna_rosco", sql.toString()); }
        return contenido;
    }

    public String leer_columna_qqsm(String columna) {
        String contenido = "";
        try {
            ResultSet rs = qqsm_sentencia.executeQuery("select " + columna + " from qqsm");

            while (rs.next())
                contenido += rs.getString(1) + "\n";

            rs.close();
        } catch (SQLException sql){ Gdx.app.log("sql_leer_columna_qqsm", sql.toString()); }
        return contenido;
    }

    public String leer_respuestas(String pregunta) {
        String contenido = "";
        try {
            ResultSet rs = qqsm_sentencia.executeQuery("select correcta from qqsm where pregunta = '" + pregunta + "'");
            if(rs.next())
                contenido += rs.getString(1) + "\n";
            rs = qqsm_sentencia.executeQuery("select respuesta0 from qqsm where pregunta = '" + pregunta + "'");
            if(rs.next() && !contenido.contains(rs.getString(1)))
                contenido += rs.getString(1) + "\n";
            rs = qqsm_sentencia.executeQuery("select respuesta1 from qqsm where pregunta = '" + pregunta + "'");
            if(rs.next() && !contenido.contains(rs.getString(1)))
                contenido += rs.getString(1) + "\n";
            rs = qqsm_sentencia.executeQuery("select respuesta2 from qqsm where pregunta = '" + pregunta + "'");
            if(rs.next() && !contenido.contains(rs.getString(1)))
                contenido += rs.getString(1) + "\n";
            rs = qqsm_sentencia.executeQuery("select respuesta3 from qqsm where pregunta = '" + pregunta + "'");
            if(rs.next() && !contenido.contains(rs.getString(1)))
                contenido += rs.getString(1);

            rs.close();
        } catch (SQLException sql){ Gdx.app.log("sql_leer_respuestas", sql.toString()); }
        return contenido;
    }

    public String leer_respuesta_correcta(String pregunta) {
        String contenido = "EMPTY";
        try {
            ResultSet rs = qqsm_sentencia.executeQuery("select correcta from qqsm where pregunta = '" + pregunta + "'");
            if(rs.next())
                contenido = rs.getString(1);
            rs.close();
        } catch (SQLException sql){ Gdx.app.log("sql_leer_respuesta_correcta", sql.toString()); }
        return contenido;
    }

    public void actualizar_descripcion(String palabra, String descripcion) {
        try {
            rosco_sentencia.executeUpdate("update rosco set descripcion = '" + descripcion + "' where palabra = '"+ palabra + "'");
        } catch (SQLException sql){ Gdx.app.log("sql_actualizar_descripcion", sql.toString()); }
    }

    public void actualizar_posicion_letra(String palabra, int posicion_letra) {
        try {
            rosco_sentencia.executeUpdate("update rosco set posicion_letra = '" + posicion_letra + "' where palabra = '"+ palabra + "'");
        } catch (SQLException sql){ Gdx.app.log("sql_actualizar_posicion_letra", sql.toString()); }
    }

    public Palabra leer_palabra(String palabra) {
        Palabra palabra_return = new Palabra();
        try {
            ResultSet rs = rosco_sentencia.executeQuery("select descripcion from rosco where palabra = '" + palabra + "'");
            if(rs.next())
                palabra_return.descripcion = rs.getString(1);
            rs = rosco_sentencia.executeQuery("select posicion_letra from rosco where palabra = '" + palabra + "'");
            if(rs.next())
                palabra_return.posicion_letra = rs.getInt(1);

            palabra_return.palabra = palabra;
            rs.close();
        } catch (SQLException sql){ Gdx.app.log("sql_leer_palabra", sql.toString()); }
        return palabra_return;
    }

    public void anadir_puntuacion(String jugador, int aciertos, int fallos, int tiempo, String fecha, String juego_tipo) {
        try {
            jugadores_sentencia.executeUpdate("insert or ignore into puntuaciones (jugador, juego, aciertos, fallos, tiempo, fecha) values('" + jugador + "', '" + juego_tipo + "', '" + aciertos + "', '" + fallos + "', '" + tiempo + "', '" + fecha + "')");
        } catch (SQLException sql){ Gdx.app.log("sql_anadir_puntuacion", sql.toString()); }
    }

    public void anadir_pregunta(Pregunta pregunta) {
        try {
            qqsm_sentencia.executeUpdate("insert or ignore into qqsm (pregunta, correcta, respuesta0, respuesta1, respuesta2, respuesta3) values('" + pregunta.pregunta + "', '" + pregunta.correcta + "', '" +
                    pregunta.respuestas[0] + "', '" + pregunta.respuestas[1] + "', '" + pregunta.respuestas[2] + "', '" + pregunta.respuestas[3] + "')");
        } catch (SQLException sql){ Gdx.app.log("sql_anadir_pregunta", sql.toString()); }
    }

    public String leer_puntuaciones(String jugador, String juego_tipo) {
        String puntuacion = "";
        try {
            ResultSet rs;
            if (juego_tipo.compareTo("Rosco") == 0)
                rs = jugadores_sentencia.executeQuery("select aciertos, fallos, tiempo, fecha from puntuaciones where jugador = '" + jugador + "' and juego = '" + juego_tipo + "'");
            else
                rs = jugadores_sentencia.executeQuery("select aciertos, fallos, tiempo, fecha from puntuaciones where jugador = '" + jugador + "' and juego = '" + juego_tipo + "'");

            while (rs.next())
                puntuacion += "  " + puntuacion_formato(new String[] {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)}) + "\n";

            rs.close();
        } catch (SQLException sql){ Gdx.app.log("sql_leer_puntuaciones", sql.toString()); }
        return puntuacion;
    }

    public ArrayList<Puntuacion> leer_puntuaciones(String juego_tipo) {
        ArrayList<Puntuacion> puntuaciones = new ArrayList<>();
        try {
            ResultSet rs;
            if (juego_tipo.compareTo("Rosco") == 0)
                rs = jugadores_sentencia.executeQuery("select * from puntuaciones where juego = '" + juego_tipo + "' order by aciertos desc, fallos asc, tiempo desc limit 10");
            else
                rs = jugadores_sentencia.executeQuery("select * from puntuaciones where juego = '" + juego_tipo + "' order by aciertos desc, fallos asc, tiempo asc limit 10");

            while (rs.next()) {
                Puntuacion puntuacion = new Puntuacion();
                puntuacion.jugador = rs.getString(1);
                puntuacion.juego_tipo = rs.getString(2);
                puntuacion.aciertos = rs.getInt(3);
                puntuacion.fallos = rs.getInt(4);
                puntuacion.tiempo = rs.getInt(5);
                puntuacion.fecha = rs.getString(6);
                puntuacion.puntuacion_mostrar = "  " + puntuacion_formato(new String[]{rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)}) + "\n";
                puntuaciones.add(puntuacion);
            }

            rs.close();
        } catch (SQLException sql){ Gdx.app.log("sql_leer_puntuaciones", sql.toString()); }
        return puntuaciones;
    }

    public void anadir_jugador(String jugador, String edad, String color) {
        try {
            jugadores_sentencia.executeUpdate("insert or ignore into jugadores (nombre, edad, color) values('" + jugador + "', '" + edad + "', '" + color + "')");
        } catch (SQLException sql){ Gdx.app.log("sql_anadir_jugador", sql.toString()); }
    }

    public void actualizar_edad(String jugador, String edad) {
        try {
            jugadores_sentencia.executeUpdate("update jugadores set edad = '" + edad + "' where nombre = '"+ jugador + "'");
        } catch (SQLException sql){ Gdx.app.log("sql_actualizar_edad", sql.toString()); }
    }

    public void actualizar_color(String jugador, String color) {
        try {
            jugadores_sentencia.executeUpdate("update jugadores set color = '" + color + "' where nombre = '"+ jugador + "'");
        } catch (SQLException sql){ Gdx.app.log("sql_actualizar_color", sql.toString()); }
    }

    public void leer_jugadores(ArrayList<Jugador> coleccion) {
        try {
            jugadores_bd = DriverManager.getConnection("jdbc:sqlite:data/jugadores.db");
            jugadores_sentencia = jugadores_bd.createStatement();
            ResultSet rs = jugadores_sentencia.executeQuery("select nombre, edad, color from jugadores");

            while (rs.next())
                coleccion.add(new Jugador(rs.getString(1), rs.getString(2), rs.getString(3)));

            rs.close();
        } catch (SQLException sql){ Gdx.app.log("sql_leer_jugadores", sql.toString()); }
    }

    public void eliminar_jugador(String jugador) {
        try {
            jugadores_sentencia.executeQuery("delete from jugadores where nombre = '" + jugador + "'");
            jugadores_sentencia.executeQuery("delete from puntuaciones where jugador = '" + jugador + "'");
        } catch (SQLException sql){ Gdx.app.log("sql_eliminar_jugador", sql.toString()); }
    }

    public void eliminar_palabra(String palabra) {
        try {
            rosco_sentencia.executeQuery("delete from rosco where palabra = '" + palabra + "'");
        } catch (SQLException sql){ Gdx.app.log("sql_eliminar_palabra", sql.toString()); }
    }

    public void eliminar_pregunta(String pregunta) {
        try {
            qqsm_sentencia.executeQuery("delete from qqsm where pregunta = '" + pregunta + "'");
        } catch (SQLException sql){ Gdx.app.log("sql_eliminar_pregunta", sql.toString()); }
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

    public void leer_preguntas(ArrayList<Pregunta> preguntas) {
        try {
            ResultSet rs = qqsm_sentencia.executeQuery("select * from qqsm");

            for (int i = 0; rs.next(); ++i) {
                preguntas.add(i, new Pregunta());
                preguntas.get(i).respuestas = new String[4];
                preguntas.get(i).pregunta = rs.getString(1);

                preguntas.get(i).correcta = rs.getString(2);
                for (int j = 3; j < 7; ++j)
                    preguntas.get(i).respuestas[j - 3] = rs.getString(j);
            }

            rs.close();
        } catch (SQLException sql){ Gdx.app.log("sql_leer_preguntas", sql.toString()); }
    }

    public void anadir_fichero_palabras(File direccion) {
        Palabra palabra = new Palabra();
        FileHandle fichero = new FileHandle(direccion.getPath());

        String[] lineas = fichero.readString().split("\n");
        String[] linea;
        for (int i = 0; i < lineas.length; ++i) {
            linea = lineas[i].split("/");
            palabra.palabra = linea[0];
            palabra.posicion_letra = Integer.valueOf(linea[1].trim()) - 1;
            palabra.descripcion = linea[2];
            anadir_palabra(palabra);
        }
    }

    public void anadir_fichero_preguntas(File direccion) {
        Pregunta pregunta = new Pregunta();
        pregunta.respuestas = new String[4];
        FileHandle fichero = new FileHandle(direccion.getPath());

        String[] lineas = fichero.readString().split("\n");
        String[] linea;
        for (int i = 0; i < lineas.length; ++i) {
            linea = lineas[i].split("/");
            pregunta.pregunta = linea[0];
            pregunta.correcta = linea[1];
            pregunta.respuestas[0] = linea[2];
            pregunta.respuestas[1] = linea[3];
            pregunta.respuestas[2] = linea[4];
            pregunta.respuestas[3] = linea[5];
            anadir_pregunta(pregunta);
        }
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

    public void escribir_descripciones(ArrayList<ArrayList<Palabra>> descripciones_entrada) {

        try {
            rosco_sentencia.execute("drop table if exists rosco");
            rosco_sentencia.executeUpdate("create table if not exists rosco (palabra text primary key, descripcion text)");
            while (!descripciones_entrada.isEmpty()) {
                while (!descripciones_entrada.get(0).isEmpty()) {
                    rosco_sentencia.executeUpdate("insert into rosco values('" + descripciones_entrada.get(0).get(0).palabra + "', '" + descripciones_entrada.get(0).get(0).descripcion + "')");
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

    public String leer_descripcion(String palabra) {
        String descripcion = "";
        try {
            ResultSet rs = rosco_sentencia.executeQuery("select descripcion from rosco where palabra = '" + palabra + "'");
            if(rs.next())
                descripcion = rs.getString(1);
            rs.close();
        } catch (SQLException sql){ Gdx.app.log("sql_leer_descripcion", sql.toString()); }
        return descripcion;
    }
}
