package com.mygdx.psplbr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.OutputStream;
import java.util.ArrayList;

public class Fichero {

    FileHandle fichero;
    BufferedReader lector;

    public Fichero (String direccion) {

        fichero = Gdx.files.internal(direccion);

        try {
            lector = new BufferedReader(new FileReader(direccion));
        } catch (Exception ex) { Gdx.app.log("creacion buffer lectura", ex.toString()); }
    }

    public void fichero_escribir (ArrayList<ArrayList<String>> descripciones_entrada) {
        OutputStream salida;

        try {
            salida = fichero.write(false);
            for (int i = 0; !descripciones_entrada.isEmpty(); ++i) {
                for (int j = 0; descripciones_entrada.get(i).size() > 0; ++j)
                    try {
                        salida.write(descripciones_entrada.get(i).get(j).getBytes());
                    } catch (Exception ex) { Gdx.app.log("escritura_escritura_ArrayList", ex.toString()); }
                descripciones_entrada.remove(i);
            }
            salida.close();
        } catch (Exception ex) { Gdx.app.log("escritura_copia_ArrayList", ex.toString()); }
    }

    public void fichero_leer (ArrayList<ArrayList<String>> descripciones) {
        String[] letras = {"aA","bB","cC","dD","eE","fF","gG","hH","iI","jJ","kK","lL","mM",
                            "nN","oO","pP","qQ","rR","sS","tT","uU","vV","wW","xX","yY","zZ"};
        try {
            String linea = lector.readLine(); //leo la primera linea del fichero
            while (linea != null) {
                int i = 0;
                while (!letras[i].contains(Character.toString(linea.charAt(0))) || i > 25)
                    //mientras que la primera letra de la linea no coincide con alguna del vector de letras
                    //o pueda existir la primera letra de la linea
                    ++i;
                if (!descripciones.get(i).contains(linea)) //si la nueva linea no esta añadida todavia se añade
                    descripciones.get(i).add(linea);
                linea = lector.readLine(); //se lee la siguiente linea del fichero
            }
        } catch (Exception ex) { Gdx.app.log("lectura", ex.toString()); }
    }
/*
    public void fichero_escribir (Integer letra, String descripcion) {
        OutputStream salida;

        descripciones.add(letra, descripcion);

        try {
            salida = fichero.write(false);
            salida.write((letra.toString() + "-" + descripcion).getBytes());
            salida.close();
        } catch (Exception ex) { Gdx.app.log("escritura", ex.toString()); }
    }

    public void fichero_escribir (String descripcion) {
        OutputStream salida;

        descripciones.add(0, descripcion);

        try {
            salida = fichero.write(false);
            salida.write(descripcion.getBytes());
            salida.close();
        } catch (Exception ex) { Gdx.app.log("escritura sin letra", ex.toString()); }
    }*/
}
