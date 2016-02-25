package com.mygdx.psplbr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.FileHandler;

public class Fichero {

    FileHandle fichero;
    BufferedReader lector;
    ArrayList<String> descripciones;

    public Fichero (String direccion) {
        descripciones = new ArrayList<>();
        fichero = Gdx.files.internal(direccion);

        try {
            lector = new BufferedReader(new FileReader(direccion));
        } catch (Exception ex) {}
    }

    public void escribir (Integer letra, String descripcion) {
        OutputStream salida = null;

        try {
            salida = fichero.write(false);
            salida.write((letra.toString() + "-" + descripcion).getBytes());
            salida.close();
        } catch (Exception ex) {}
    }

    public void leer () {
        try {
            String linea = lector.readLine();
            while (linea != null) {
                descripciones.add(linea);
                linea = lector.readLine();
            }
        } catch (Exception ex) {}
    }
}
