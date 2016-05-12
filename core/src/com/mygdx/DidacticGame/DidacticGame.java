package com.mygdx.DidacticGame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.DidacticGame.Auxiliares.Base_Datos;
import com.mygdx.DidacticGame.Auxiliares.Jugadores;
import com.mygdx.DidacticGame.Pantallas.Menus.*;

public class DidacticGame extends Game {

	public static Menu_Inicial menu_inicial;
	public static Menu_Juegos menu_juegos;
	public static Menu_Jugadores menu_jugadores;
	public static Menu_Puntuaciones menu_puntuaciones;
	public static Menu_Datos menu_datos;
	public static Menu_Ranking menu_ranking;

	public static Jugadores jugadores;
	public static Base_Datos BD;
	public static TareaTexturas tarea;

	public void create() {

        BD = new Base_Datos();
        jugadores = new Jugadores();

		menu_inicial = new Menu_Inicial(this);
		setScreen(menu_inicial);

		tarea = new TareaTexturas();
		tarea.cargar_pantalla();
		tarea.cargar_datos();
		tarea.cargar_puntuaciones();
		tarea.cargar_ranking();
		tarea.cargar_jugadores();
		tarea.cargar_juegos();
		tarea.asset.finishLoading();
	}

	public class TareaTexturas {
		public AssetManager asset;

		public TareaTexturas() { asset = new AssetManager(); }

		public void cargar_datos(){
			asset.load("data/texturas/botones_datos.png", Texture.class);
		}
		public void cargar_juegos(){
			asset.load("data/texturas/texto/particulas.png", Texture.class);
			asset.load("data/texturas/botones_juegos.png", Texture.class);
			asset.load("data/texturas/fondo/qqsm.png", Texture.class);
			asset.load("data/texturas/fondo/rosco.png", Texture.class);
			asset.load("data/texturas/juego_rosco/boton_on.png", Texture.class);

			for (int i = 0; i < 26; ++i){
				asset.load("data/texturas/juego_rosco/digitos/letras/l" + i + ".png", Texture.class);
				asset.load("data/texturas/juego_rosco/rojo/r" + i + ".png", Texture.class);
				asset.load("data/texturas/juego_rosco/verde/v" + i + ".png", Texture.class);
				if (i < 3)
					for (int j = 0; j < 10; ++j) {
						if (i < 2)
							asset.load("data/texturas/juego_rosco/digitos/puntuacion/p" + i + "_" + j + ".png", Texture.class);
						asset.load("data/texturas/juego_rosco/digitos/tiempo/t" + i + "_" + j +".png", Texture.class);
					}
			}

			for (int i = 0; i < 4; ++i) {
				asset.load("data/texturas/juego_qqsm/rojo/r" + i + ".png", Texture.class);
				asset.load("data/texturas/juego_qqsm/verde/v" + i + ".png", Texture.class);
			}
		}
		public void cargar_jugadores(){
			asset.load("data/texturas/botones_jugador.png", Texture.class);
		}
		public void cargar_puntuaciones(){
			asset.load("data/texturas/texto/particulas.png", Texture.class);
		}
		public void cargar_ranking(){
			asset.load("data/texturas/texto/particulas.png", Texture.class);
		}
		public void cargar_pantalla() {
			asset.load("data/texturas/checkbox/off.png", Texture.class);
			asset.load("data/texturas/checkbox/on.png", Texture.class);
			asset.load("data/texturas/texto/fondo.png", Texture.class);
			asset.load("data/texturas/selector/seleccion.png", Texture.class);
			asset.load("data/texturas/selector/desplegable.png", Texture.class);
			asset.load("data/texturas/selector/fondo.png", Texture.class);
			asset.load("data/texturas/selector/desplegablegris.png", Texture.class);
			asset.load("data/texturas/editor/cursor.png", Texture.class);
			asset.load("data/texturas/editor/seleccion.png", Texture.class);
			asset.load("data/texturas/editor/fondo.png", Texture.class);
			asset.load("data/texturas/editor/cursor.png", Texture.class);
			asset.load("data/texturas/editor/seleccion.png", Texture.class);
			asset.load("data/texturas/editor/fondo.png", Texture.class);
		}
	}
}
