package com.mygdx.DidactiGame;

import com.badlogic.gdx.Game;
import com.mygdx.DidactiGame.Pantallas.*;

public class DidactiGame extends Game {

	public static Menu_Inicial menu_inicial;
	public static Menu_Juegos menu_juegos;
	public static Menu_Opciones menu_opciones;
	public static Menu_Personalizar menu_personalizar;
	public static Menu_Datos menu_datos;
	public static Menu_Nuevo menu_nuevo;

	public void create() {
		menu_inicial = new Menu_Inicial(this);
        menu_juegos = new Menu_Juegos(this);
        menu_opciones = new Menu_Opciones(this);
		menu_personalizar = new Menu_Personalizar(this);
		menu_datos = new Menu_Datos(this);
		menu_nuevo = new Menu_Nuevo(this);

		setScreen(menu_inicial);
	}
}
