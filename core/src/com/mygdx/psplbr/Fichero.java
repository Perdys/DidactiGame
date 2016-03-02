package com.mygdx.psplbr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.util.ArrayList;

public class Fichero {

    FileHandle fichero;

    public Fichero (String direccion) { fichero = Gdx.files.local(direccion); }

    public void fichero_escribir (ArrayList<ArrayList<String>> descripciones_entrada) {

        for (int i = 0; !descripciones_entrada.isEmpty(); ++i) {
            for (int j = 0; !descripciones_entrada.get(i).isEmpty(); ++j) {
                fichero.writeString(descripciones_entrada.get(i).get(j), true);
                descripciones_entrada.get(i).remove(j);
            }
            descripciones_entrada.remove(i);
        }
    }

    public void fichero_leer (ArrayList<ArrayList<String>> descripciones) {

        String[] letras = {"aA","bB","cC","dD","eE","fF","gG","hH","iI","jJ","kK","lL","mM",
                            "nN","oO","pP","qQ","rR","sS","tT","uU","vV","wW","xX","yY","zZ"};

        String[] lineas = fichero.readString().split("\n"); //leo el fichero entero y lo almaceno en lineas
        for (int i = 0; i < lineas.length; ++i) {
            int j = 0;
            while (!letras[j].contains(Character.toString(lineas[i].charAt(0))) || j > 25)
                //mientras que la primera letra de la linea no coincide con alguna del vector de letras
                //o pueda existir la primera letra de la linea
                ++j;

            descripciones.add(new ArrayList<String>());
            descripciones.get(j).add(lineas[i]);
        }
    }
}
