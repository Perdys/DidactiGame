package com.mygdx.DidactiGame.Auxiliares;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.DidactiGame.DidactiGame;

import static com.mygdx.DidactiGame.DidactiGame.jugadores;

public class Pantalla implements Screen {

    //Medidas
    public float anchura_juego = Gdx.graphics.getWidth();
    public float altura_juego = Gdx.graphics.getHeight();
    public float lado = anchura_juego < altura_juego ? anchura_juego : altura_juego;

    //Textos
    public static FreeTypeFontGenerator generador_texto = new FreeTypeFontGenerator(Gdx.files.internal("fuentes/pixel.ttf"));
    public static FreeTypeFontGenerator.FreeTypeFontParameter tamano_texto = new FreeTypeFontGenerator.FreeTypeFontParameter();

    //Manejo menus
    public String pantalla_actual = "Defecto";
    public static InputAdapter botones_genericos;

    //Ficheros
    public Fichero letras_rosco_fichero = new Fichero("data/ficheros/descripciones_letras.txt");

    public Pantalla() {}

    public void sistema_botones (final DidactiGame juego) {
        botones_genericos = new InputAdapter() {

            public boolean keyUp(int keycode) {
                switch (keycode) {
                    case Input.Keys.BACK:
                        acciones(pantalla_actual);
                        break;
                    case Input.Keys.HOME:
                        juego.dispose();
                        Gdx.app.exit();
                        break;
                    default:
                        break;
                }
                return false;
            }

            public boolean touchUp (int x, int y, int pointer, int button) {
                if (button == 1) {
                    acciones(pantalla_actual);
                    return true;
                }
                return false;
            }

            public void acciones (String pantalla_actual) {
                switch (pantalla_actual) {
                    case "Defecto":case "Menu_Inicial":
                        juego.dispose();
                        Gdx.app.exit();
                        break;
                    case "Menu_Jugadores":case "Menu_Juegos":case "Menu_Datos":case "Menu_Ranking":case "Menu_Puntuaciones":case "Clasificacion":
                        juego.setScreen(DidactiGame.menu_inicial);
                        break;
                    case "Juego_Rosco":
                        jugadores = new Jugadores();
                        juego.setScreen(DidactiGame.menu_juegos);
                        break;
                    default:
                        juego.setScreen(DidactiGame.menu_inicial);
                        break;
                }
            }
        };
    }

    public TextField.TextFieldStyle estilo_editor(String tipo) {

        tamano_texto.size = (int)proporcion_y(0.06);
        TextField.TextFieldStyle estilo_texto = new TextField.TextFieldStyle(
                generador_texto.generateFont(tamano_texto), Color.GREEN,
                new TextureRegionDrawable(new TextureRegion(new Texture("data/menu_datos/editor/cursor_editor.png"))),
                new TextureRegionDrawable(new TextureRegion(new Texture("data/menu_datos/editor/seleccion_editor.png"))),
                new TextureRegionDrawable(new TextureRegion(new Texture("data/menu_datos/editor/fondo_editor_horizontal.png"))));

        estilo_texto.cursor.setMinWidth(proporcion_x(0.003));
        estilo_texto.background.setLeftWidth(proporcion_x(0.01));
        estilo_texto.background.setRightWidth(proporcion_x(0.01));
        estilo_texto.background.setTopHeight(proporcion_y(0.01));
        estilo_texto.background.setBottomHeight(proporcion_y(0.01));

        return estilo_texto;
    }

    public SelectBox.SelectBoxStyle estilo_selector() {

        BitmapFont texto = generador_texto.generateFont(tamano_texto);
        SelectBox.SelectBoxStyle estilo_selector = new SelectBox.SelectBoxStyle();

        estilo_selector.font = texto;
        estilo_selector.fontColor = Color.GREEN;
        estilo_selector.background = new TextureRegionDrawable(new TextureRegion(new Texture("data/menu_jugadores/editor/rojo.png")));
        estilo_selector.scrollStyle = new ScrollPane.ScrollPaneStyle();
        estilo_selector.listStyle = new List.ListStyle();
        estilo_selector.listStyle.font = texto;
        estilo_selector.listStyle.fontColorSelected = Color.FOREST;
        estilo_selector.listStyle.fontColorUnselected = Color.OLIVE;
        estilo_selector.listStyle.selection = new TextureRegionDrawable(new TextureRegion(new Texture("data/menu_jugadores/editor/seleccion.png")));
        estilo_selector.listStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture("data/menu_jugadores/editor/azul.png")));

        return estilo_selector;
    }

    public Label.LabelStyle estilo_etiqueta(int tamano) {
        tamano_texto.size = tamano;
        BitmapFont texto_fuente = generador_texto.generateFont(tamano_texto);

        Label.LabelStyle estilo_etiqueta = new Label.LabelStyle();
        estilo_etiqueta.font = texto_fuente;
        estilo_etiqueta.fontColor = Color.BLACK;

        return estilo_etiqueta;
    }

    public Label.LabelStyle estilo_etiqueta(int tamano, String fondo) {
        tamano_texto.size = tamano;
        BitmapFont texto_fuente = generador_texto.generateFont(tamano_texto);

        Label.LabelStyle estilo_etiqueta = new Label.LabelStyle();
        estilo_etiqueta.font = texto_fuente;
        estilo_etiqueta.fontColor = Color.GREEN;

        estilo_etiqueta.background = new TextureRegionDrawable(new TextureRegion(new Texture("data/menu_datos/editor/" + fondo + ".png")));
        estilo_etiqueta.background.setLeftWidth(proporcion_x(0.01));
        estilo_etiqueta.background.setRightWidth(proporcion_x(0.01));
        estilo_etiqueta.background.setTopHeight(proporcion_y(0.01));
        estilo_etiqueta.background.setBottomHeight(proporcion_y(0.01));

        return estilo_etiqueta;
    }

    public float proporcion_x (double valor) { return (float) (anchura_juego * valor); }

    public float proporcion_y (double valor) { return (float) (altura_juego * valor); }

    public static Pixmap rectangulo_redondeado(float ancho, float alto, int radio, Color color) {

        Pixmap pixmap = new Pixmap((int)ancho, (int)alto, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);

        pixmap.fillRectangle(0, radio, pixmap.getWidth(), pixmap.getHeight() - 2 * radio);
        pixmap.fillRectangle(radio, 0, pixmap.getWidth() - 2 * radio, pixmap.getHeight());

        // Bottom-left circle
        pixmap.fillCircle(radio, radio, radio);
        // Top-left circle
        pixmap.fillCircle(radio, pixmap.getHeight() - radio, radio);
        // Bottom-right circle
        pixmap.fillCircle(pixmap.getWidth() - radio, radio, radio);
        // Top-right circle
        pixmap.fillCircle(pixmap.getWidth() - radio, pixmap.getHeight() - radio, radio);

        return pixmap;
    }

    public void render(float delta) {}

    public void resize(int width, int height) { anchura_juego = width; altura_juego = height; }

    public void pause() {}

    public void resume() {}

    public void show() {}

    public void hide() {}

    public void dispose () {}
}
        /*

    public static TextureRegion[] getSprites(String filepath, int col, int fil) {
        Texture walkSheet = new Texture(Gdx.files.internal(filepath));
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / col, walkSheet.getHeight() / fil);
        TextureRegion[] walkFrames = new TextureRegion[col * fil];
        int index = 0;
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < fil; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        return walkFrames;
    }

        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/pixel.ttf"));
        BMF_titulo = generator.generateFont((int) anchura_juego / 4);
        BMF_boton = generator.generateFont((int) anchura_juego / 12);
        BMF_texto = generator.generateFont((int) anchura_juego / 13);
        BMF_titulo.setColor(Color.BLACK);
        BMF_boton.setColor(Color.BLACK);
        BMF_texto.setColor(Color.BLACK);*/
/*
    public ImageTextButton imagen_texto_boton(final String imagen, String texto, final DidactiGame juego) {
        ImageTextButton.ImageTextButtonStyle estilo_boton = new ImageTextButton.ImageTextButtonStyle();
        Skin skin = new Skin();
        skin.add("boton", new Texture(imagen));
        estilo_boton.up = skin.getDrawable("boton");
        //estilo_boton.font = BMF_boton;
        estilo_boton.fontColor = Color.BLACK;
        estilo_boton.unpressedOffsetY = -(lado / 6);
        estilo_boton.pressedOffsetY = -(lado / 6);

        ImageTextButton boton = new ImageTextButton(texto, estilo_boton);
        //boton.setSize(lado / 4, lado / 4);

        boton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switch (imagen) {
                    case "data/boton_inicio_parada.png": //reiniciar tiempo_rosco o pararlo;
                        break;
                    case "data/boton_reinicio.png": juego.setScreen(new Juego_Rosco(juego));
                        break;
                    default: break;
                }
            }
        });
        return boton;
    }
*/
    /*public class Texto extends Actor {

        public String texto;
        public float x, y, espacio;
        public BitmapFont.HAlignment alineacion;
        public int tipo;

        public Texto(int tipo, String texto, float x, float y, float espacio, BitmapFont.HAlignment alineacion) {
            this.texto = texto;
            this.x = x;
            this.y = y;
            this.espacio = espacio;
            this.alineacion = alineacion;
            this.tipo = tipo;
        }

        public Texto(int tipo, String texto) {
            this.texto = texto;
            this.tipo = tipo;
        }

        public void setPosition(float x, float y, float espacio, BitmapFont.HAlignment alineacion) {
            this.x = x;
            this.y = y;
            this.espacio = espacio;
            this.alineacion = alineacion;
        }

        @Override
        public void draw(SpriteBatch batch, float alpha) {
            switch (tipo) {
                case 1: BMF_titulo.drawMultiLine(batch, texto, x, y, espacio, alineacion);
                    break;
                case 2: BMF_texto.drawMultiLine(batch, texto, x, y, espacio, alineacion);
                    break;
                default: break;
            }
        }
    }*/