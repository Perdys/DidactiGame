package com.mygdx.psplbr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.util.ArrayList;

public class Fichero {

    FileHandle fichero;

    public Fichero (String direccion) { fichero = Gdx.files.local(direccion); }

    public void fichero_escribir (ArrayList<ArrayList<String[]>> descripciones_entrada) {

        Gdx.app.log("linea", "yesssssssssssssssssssssssss");
        if (!descripciones_entrada.isEmpty() && !descripciones_entrada.get(0).isEmpty()) {
            fichero.writeString(descripciones_entrada.get(0).get(0)[0] + " " + descripciones_entrada.get(0).get(0)[1] + "\n", false);
            descripciones_entrada.get(0).remove(0);
        }

        while (!descripciones_entrada.isEmpty()) {
            while (!descripciones_entrada.get(0).isEmpty()) {
                fichero.writeString(descripciones_entrada.get(0).get(0)[0] + " " + descripciones_entrada.get(0).get(0)[1] + "\n", true);
                descripciones_entrada.get(0).remove(0);
            }
            descripciones_entrada.remove(0);
        }
    }

    public void fichero_leer (ArrayList<ArrayList<String[]>> descripciones) {

        String[] letras = {"aA","bB","cC","dD","eE","fF","gG","hH","iI","jJ","kK","lL","mM",
                           "nN","oO","pP","qQ","rR","sS","tT","uU","vV","wW","xX","yY","zZ"};
        String palabra, descripcion;

        String[] lineas = fichero.readString().split("\n"); //leo el fichero entero y lo almaceno en lineas
        if (lineas.length > 1)
            for (int i = 0, j = 0; i < lineas.length; ++i, j = 0) {
                while (!letras[j].contains(Character.toString(lineas[i].charAt(0))))
                    //mientras que la primera letra de la linea no coincide con alguna del vector de letras
                    //o pueda existir la primera letra de la linea
                    ++j;

                Gdx.app.log("linea", lineas[i]);
                if (lineas[i].contains(" ")) {
                    palabra = lineas[i].substring(0, lineas[i].indexOf(" "));
                    descripcion = lineas[i].substring(lineas[i].indexOf(" "));
                    descripciones.get(j).add(new String[]{palabra, descripcion});
                }
            }
    }
}