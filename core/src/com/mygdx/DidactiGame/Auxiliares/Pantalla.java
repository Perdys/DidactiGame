package com.mygdx.DidactiGame.Auxiliares;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.DidactiGame.DidactiGame;

import static com.mygdx.DidactiGame.DidactiGame.BD;
import static com.mygdx.DidactiGame.DidactiGame.jugadores;

public class Pantalla implements Screen {

    //Medidas
    public float anchura_juego = Gdx.graphics.getWidth();
    public float altura_juego = Gdx.graphics.getHeight();
    public float lado = anchura_juego < altura_juego ? anchura_juego : altura_juego;

    //Textos
    public static FreeTypeFontGenerator generador_texto = new FreeTypeFontGenerator(Gdx.files.internal("fuentes/TitilliumWeb-SemiBold.ttf"));
    public static FreeTypeFontGenerator.FreeTypeFontParameter tamano_texto = new FreeTypeFontGenerator.FreeTypeFontParameter();

    //Manejo menus
    public String pantalla_actual = "Defecto";
    public static InputAdapter botones_genericos;
    public static boolean modo_escritorio = false;
    public Texture boton_atras = new Texture("data/texturas/boton_atras.png");

    public Pantalla() {
        if (Gdx.app.getType() == Application.ApplicationType.Desktop)
            modo_escritorio = true;
    }

    public void sistema_botones (final DidactiGame juego) {

        botones_genericos = new InputAdapter() {

            public boolean keyUp(int keycode) {
                switch (keycode) {
                    case Input.Keys.BACK:
                        acciones(pantalla_actual);
                        break;
                    case Input.Keys.HOME:
                        BD.liberar();
                        juego.dispose();
                        Gdx.app.exit();
                        break;
                    default:
                        break;
                }
                return false;
            }

            public boolean touchUp (int x, int y, int pointer, int button) {
                Circle atras = new Circle(0, altura_juego, proporcion_x(0.1));

                if (button == 1 || (atras.contains(x, y) && modo_escritorio)) {
                    acciones(pantalla_actual);
                    return true;
                }
                return false;
            }

            public void acciones (String pantalla_actual) {
                switch (pantalla_actual) {
                    case "Defecto":case "Menu_Inicial":
                        BD.liberar();
                        juego.dispose();
                        Gdx.app.exit();
                        break;
                    case "Menu_Jugadores":case "Menu_Juegos":case "Menu_Datos":case "Menu_Ranking":case "Menu_Puntuaciones":case "Clasificacion":
                        juego.setScreen(DidactiGame.menu_inicial);
                        break;
                    case "Juego_Rosco":case "Juego_QQSM":
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

    public TextField.TextFieldStyle editor_estilo(double tamano) {
        tamano_texto.size = (int) proporcion_y(tamano);
        TextField.TextFieldStyle estilo = editor_estilo();
        estilo.font = generador_texto.generateFont(tamano_texto);

        return estilo;
    }

    public TextField.TextFieldStyle editor_estilo() {

        tamano_texto.size = (int)proporcion_y(0.05);
        TextField.TextFieldStyle estilo_texto = new TextField.TextFieldStyle(
                generador_texto.generateFont(tamano_texto), Color.BLACK,
                new TextureRegionDrawable(new TextureRegion(new Texture("data/texturas/editor/cursor.png"))),
                new TextureRegionDrawable(new TextureRegion(new Texture("data/texturas/editor/seleccion.png"))),
                new TextureRegionDrawable(new TextureRegion(new Texture("data/texturas/editor/fondo.png"))));

        estilo_texto.cursor.setMinWidth(proporcion_x(0.003));
        estilo_texto.background.setLeftWidth(proporcion_x(0.02));
        estilo_texto.background.setRightWidth(proporcion_x(0.02));
        estilo_texto.background.setTopHeight(proporcion_y(0.02));

        return estilo_texto;
    }

    public SelectBox.SelectBoxStyle selector_estilo (double tamano) {
        tamano_texto.size = (int)proporcion_y(tamano);
        BitmapFont texto = generador_texto.generateFont(tamano_texto);
        SelectBox.SelectBoxStyle estilo = selector_estilo();
        estilo.background.setMinHeight(proporcion_y(tamano));
        estilo.listStyle.background.setMinHeight(proporcion_y(tamano));
        estilo.font = texto;
        estilo.listStyle.font = texto;
        return estilo;
    }

    public SelectBox.SelectBoxStyle selector_estilo() {
        tamano_texto.size = (int)proporcion_y(0.15);
        BitmapFont texto = generador_texto.generateFont(tamano_texto);
        SelectBox.SelectBoxStyle estilo_selector = new SelectBox.SelectBoxStyle();

        estilo_selector.font = texto;
        estilo_selector.fontColor = Color.BLACK;
        estilo_selector.background = new TextureRegionDrawable(new TextureRegion(new Texture("data/texturas/jugador_selector/fondo.png")));
        estilo_selector.background.setLeftWidth(proporcion_x(0.02));
        estilo_selector.background.setRightWidth(proporcion_x(0.02));
        estilo_selector.background.setBottomHeight(proporcion_y(0.02));
        estilo_selector.background.setTopHeight(proporcion_y(0.02));
        estilo_selector.background.setMinHeight(proporcion_y(0.15));
        estilo_selector.scrollStyle = new ScrollPane.ScrollPaneStyle();
        estilo_selector.listStyle = new List.ListStyle();
        estilo_selector.listStyle.font = texto;
        estilo_selector.listStyle.fontColorSelected = Color.BLACK;
        estilo_selector.listStyle.fontColorUnselected = Color.DARK_GRAY;
        estilo_selector.listStyle.selection = new TextureRegionDrawable(new TextureRegion(new Texture("data/texturas/jugador_selector/seleccion.png")));
        estilo_selector.listStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture("data/texturas/jugador_selector/desplegable.png")));
        estilo_selector.listStyle.background.setLeftWidth(proporcion_x(0.02));
        estilo_selector.listStyle.background.setRightWidth(proporcion_x(0.02));
        estilo_selector.listStyle.background.setTopHeight(proporcion_y(0.02));
        estilo_selector.listStyle.background.setBottomHeight(proporcion_y(0.02));
        estilo_selector.listStyle.background.setMinHeight(proporcion_y(0.15));

        return estilo_selector;
    }

    public Label.LabelStyle texto_estilo(double tamano) {
        tamano_texto.size = (int)proporcion_y(tamano);
        BitmapFont texto_fuente = generador_texto.generateFont(tamano_texto);

        Label.LabelStyle estilo_etiqueta = new Label.LabelStyle();
        estilo_etiqueta.font = texto_fuente;
        estilo_etiqueta.fontColor = Color.BLACK;

        return estilo_etiqueta;
    }

    public Label.LabelStyle texto_panel_scroll_estilo() {
        tamano_texto.size = (int)proporcion_y(0.04);
        BitmapFont texto_fuente = generador_texto.generateFont(tamano_texto);

        Label.LabelStyle estilo_etiqueta = new Label.LabelStyle();
        estilo_etiqueta.font = texto_fuente;
        estilo_etiqueta.fontColor = Color.BLACK;

        estilo_etiqueta.background = new TextureRegionDrawable(new TextureRegion(new Texture("data/texturas/texto/fondo.png")));
        estilo_etiqueta.background.setLeftWidth(proporcion_x(0.02));
        estilo_etiqueta.background.setRightWidth(proporcion_x(0.01));
        estilo_etiqueta.background.setTopHeight(proporcion_y(0.03));
        estilo_etiqueta.background.setBottomHeight(proporcion_y(0.02));

        return estilo_etiqueta;
    }

    public CheckBox.CheckBoxStyle checkbox_estilo() {
        tamano_texto.size = (int)proporcion_y(0.06);
        CheckBox.CheckBoxStyle estilo_checkbox = new CheckBox.CheckBoxStyle(
                new TextureRegionDrawable(new TextureRegion(new Texture("data/texturas/checkbox/off.png"))),
                new TextureRegionDrawable(new TextureRegion(new Texture("data/texturas/checkbox/on.png"))),
                generador_texto.generateFont(tamano_texto), Color.BLACK);
        estilo_checkbox.checkboxOn.setMinWidth((int) proporcion_y(0.06));
        estilo_checkbox.checkboxOn.setMinHeight((int) proporcion_y(0.06));
        estilo_checkbox.checkboxOff.setMinWidth((int) proporcion_y(0.06));
        estilo_checkbox.checkboxOff.setMinHeight((int) proporcion_y(0.06));

        return estilo_checkbox;
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