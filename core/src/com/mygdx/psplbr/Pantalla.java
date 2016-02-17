package com.mygdx.psplbr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Pantalla implements Screen {

    public static float anchura_juego = Gdx.graphics.getWidth();
    public static float altura_juego = Gdx.graphics.getHeight();
    public static float proporcion_ancho_juego = anchura_juego / 1280;
    public static float proporcion_alto_juego = altura_juego / 720;
    public static float lado = anchura_juego < altura_juego ? anchura_juego : altura_juego;
    public Integer pantalla_actual = 0, n_jugadores = 1, jugador_actual = 0;
    public static Stage stage_botones;
    //public FreeTypeFontGenerator generator;
    //public BitmapFont BMF_titulo, BMF_boton, BMF_texto;

    public Pantalla() {}

    public void setStageButton(final PsPlbr juego) {
        stage_botones = new Stage();
        stage_botones.addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.BACK:
                        switch (pantalla_actual) {
                            case 0:
                                Gdx.app.exit();
                                break;
                            default:
                                juego.setScreen(new Menu(juego));
                                break;
                        }
                        break;
                    case Input.Keys.HOME:
                        Gdx.app.exit();
                    default:
                        break;
                }
                return false;
            }
        });
    }

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

    public void render(float delta) {}

    public void resize(int width, int height) {}

    public void pause() {}

    public void resume() {}

    public void show() {}

    public void hide() { dispose(); }

    public void dispose () {}
}
        /*generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/pixel.ttf"));
        BMF_titulo = generator.generateFont((int) anchura_juego / 4);
        BMF_boton = generator.generateFont((int) anchura_juego / 12);
        BMF_texto = generator.generateFont((int) anchura_juego / 13);
        BMF_titulo.setColor(Color.BLACK);
        BMF_boton.setColor(Color.BLACK);
        BMF_texto.setColor(Color.BLACK);*/
/*
    public ImageTextButton setButton(final String imagen, String texto, final PsPlbr juego) {
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
                    case "data/boton_reinicio.png": juego.setScreen(new Rosco(juego));
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