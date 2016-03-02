package com.mygdx.psplbr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.util.ArrayList;

public class Fichero {

    FileHandle fichero;

    public Fichero (String direccion) { fichero = Gdx.files.local(direccion); }

    public void fichero_escribir (ArrayList<ArrayList<String>> descripciones_entrada) {

        while (!descripciones_entrada.isEmpty()) {
            while (!descripciones_entrada.get(0).isEmpty()) {
                Gdx.app.log("fichero_escribir", descripciones_entrada.get(0).get(0));
                fichero.writeString(descripciones_entrada.get(0).get(0), true);
                descripciones_entrada.get(0).remove(0);
            }
            descripciones_entrada.remove(0);
        }
    }

    public void fichero_leer (ArrayList<ArrayList<String>> descripciones) {

        String[] letras = {"aA","bB","cC","dD","eE","fF","gG","hH","iI","jJ","kK","lL","mM",
                           "nN","oO","pP","qQ","rR","sS","tT","uU","vV","wW","xX","yY","zZ"};

        String[] lineas = fichero.readString().split("\n"); //leo el fichero entero y lo almaceno en lineas
        for (int i = 0, j = 0; i < lineas.length; ++i, j = 0) {
            while (!letras[j].contains(Character.toString(lineas[i].charAt(0))) || j > 25)
                //mientras que la primera letra de la linea no coincide con alguna del vector de letras
                //o pueda existir la primera letra de la linea
                ++j;

            descripciones.add(new ArrayList<String>());
            descripciones.get(j).add(lineas[i]);
        }
    }
}