package com.mygdx.DidactiGame.Herramientas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.DidactiGame.DidactiGame;

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
    public Fichero nombres_jugadores_fichero = new Fichero("data/ficheros/nombres_jugadores.txt");
    public Fichero opciones_jugadores_fichero = new Fichero("data/ficheros/opciones_jugadores.txt");

    public Pantalla() {}

    public void sistema_botones (final DidactiGame juego) {
        botones_genericos = new InputAdapter() {

            public boolean keyUp(int keycode) {
                Gdx.app.log("click", Integer.toString(keycode));
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
                    case "Menu_Opciones":case "Menu_Juegos":case "Menu_Datos":case "Menu_Nuevo":case "Menu_Personalizar":case "Clasificacion":
                        juego.setScreen(DidactiGame.menu_inicial);
                        break;
                    case "Juego_Rosco":
                        juego.setScreen(DidactiGame.menu_juegos);
                        break;
                    default:
                        juego.setScreen(DidactiGame.menu_inicial);
                        break;
                }
            }
        };
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
                    case "data/boton_inicio_parada.png": //reiniciar tiempo o pararlo;
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