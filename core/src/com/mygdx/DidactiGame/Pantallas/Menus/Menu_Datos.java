package com.mygdx.DidactiGame.Pantallas.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.mygdx.DidactiGame.DidactiGame;
import com.mygdx.DidactiGame.Auxiliares.Pantalla;

import javax.swing.*;

import java.awt.*;

import static com.badlogic.gdx.utils.Align.topLeft;
import static com.mygdx.DidactiGame.DidactiGame.BD;
import static com.mygdx.DidactiGame.DidactiGame.jugadores;
import static com.mygdx.DidactiGame.Pantallas.Menus.Menu_Juegos.*;

public class Menu_Datos extends Pantalla{

    DidactiGame juego;
    OrthographicCamera camara;
    SpriteBatch batch;
    Stage stage;
    InputAdapter click;

    TextArea descripcion_editor, respuesta0_editor, respuesta1_editor, respuesta2_editor, respuesta3_editor;
    Label nombres_etiqueta, jugadores_etiqueta, rosco_etiqueta, qqsm_etiqueta;
    ScrollPane nombres_scroll;
    Texture rosco_botones, qqsm_botones;
    SelectBox palabra_selector, letra_selector, pregunta_selector, respuesta_correcta_selector;

    public class Editado {
        boolean descripcion_editado = false, respuesta0_editado = false,
        respuesta1_editado = false, respuesta2_editado = false, respuesta3_editado = false;
    }
    Editado editados;

    Palabra nueva_palabra;
    Pregunta nueva_pregunta;

    public Menu_Datos(DidactiGame juego) {
        this.juego = juego;

        camara = new OrthographicCamera();
        camara.setToOrtho(false);
        batch = new SpriteBatch();
        stage = new Stage();

        editados = new Editado();
        nueva_palabra = new Palabra();
        nueva_pregunta = new Pregunta();
        nueva_pregunta.respuestas = new String[4];
        pantalla_actual = "Menu_Datos";

        texturas_cargar();
        botones_cargar();
    }

    public void render (float delta) {
        Gdx.gl.glClearColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, Color.WHITE.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();

        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.draw(rosco_botones, anchura_juego - proporcion_x(0.2), rosco_etiqueta.getY(), proporcion_x(0.1), proporcion_y(0.062));
        batch.draw(qqsm_botones, anchura_juego - proporcion_x(0.2), qqsm_etiqueta.getY(), proporcion_x(0.1), proporcion_y(0.062));
        jugadores_etiqueta.draw(batch, 1);
        rosco_etiqueta.draw(batch, 1);
        qqsm_etiqueta.draw(batch, 1);
        batch.end();

        stage.act();
        stage.draw();
    }

    public void show () {
        nombres_etiqueta.setText(jugadores.nombres());

        palabra_selector.setItems(new Array<>(BD.leer_rosco("palabra").split("\n")));
        palabra_selector.pack();
        letra_selector.setItems(new Array<>(BD.leer_rosco("posicion_letra").split("\n")));
        letra_selector.pack();
        descripcion_editor.setText(BD.leer_descripcion((String)palabra_selector.getSelected()));

        pregunta_selector.setItems(new Array<>(BD.leer_qqsm("pregunta").split("\n")));
        pregunta_selector.pack();
        pregunta_selector.setWidth(proporcion_x(0.25));
        String[] respuestas = BD.leer_respuestas((String)pregunta_selector.getSelected()).split("\n");
        respuesta_correcta_selector.setItems(new Array<>(respuestas));
        for (int i = 0; i < respuestas.length; ++i)
            if (BD.leer_respuesta_correcta((String)pregunta_selector.getSelected()).contains(respuestas[i]))
                respuesta_correcta_selector.setSelectedIndex(i);
        respuesta_correcta_selector.pack();
        respuesta0_editor.setText(BD.leer_respuesta0((String)pregunta_selector.getSelected()));
        respuesta1_editor.setText(BD.leer_respuesta1((String)pregunta_selector.getSelected()));
        respuesta2_editor.setText(BD.leer_respuesta2((String)pregunta_selector.getSelected()));
        respuesta3_editor.setText(BD.leer_respuesta3((String)pregunta_selector.getSelected()));

        sistema_botones(juego);
        stage.addActor(nombres_scroll);
        stage.addActor(descripcion_editor);
        stage.addActor(respuesta0_editor);
        stage.addActor(respuesta1_editor);
        stage.addActor(respuesta2_editor);
        stage.addActor(respuesta3_editor);
        stage.addActor(palabra_selector);
        stage.addActor(letra_selector);
        stage.addActor(pregunta_selector);

        InputMultiplexer inputs = new InputMultiplexer();
        inputs.addProcessor(botones_genericos);
        inputs.addProcessor(stage);
        inputs.addProcessor(click);
        Gdx.input.setInputProcessor(inputs);
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);
    }

    public void hide() {
        if (editados.descripcion_editado) {
            int posicion_letra = ((String)palabra_selector.getSelected()).indexOf((char)letra_selector.getSelected());
            BD.escribir_descripcion((String)palabra_selector.getSelected(), posicion_letra, descripcion_editor.getText());
            editados.descripcion_editado = false;
        }
        if (editados.respuesta0_editado) {
            if (((String)respuesta_correcta_selector.getSelected()).contains(respuesta0_editor.getText()))
                BD.escribir_acierto((String)pregunta_selector.getSelected(), respuesta0_editor.getText());

            BD.escribir_respuesta0((String)pregunta_selector.getSelected(), respuesta0_editor.getText());

            editados.respuesta0_editado = false;
        }
        if (editados.respuesta1_editado) {
            if (((String)respuesta_correcta_selector.getSelected()).contains(respuesta1_editor.getText()))
                BD.escribir_acierto((String)pregunta_selector.getSelected(), respuesta1_editor.getText());

            BD.escribir_respuesta1((String)pregunta_selector.getSelected(), respuesta1_editor.getText());

            editados.respuesta1_editado = false;
        }
        if (editados.respuesta2_editado) {
            if (((String)respuesta_correcta_selector.getSelected()).contains(respuesta2_editor.getText()))
                BD.escribir_acierto((String)pregunta_selector.getSelected(), respuesta2_editor.getText());

            BD.escribir_respuesta2((String)pregunta_selector.getSelected(), respuesta2_editor.getText());

            editados.respuesta2_editado = false;
        }
        if (editados.respuesta3_editado) {
            if (((String)respuesta_correcta_selector.getSelected()).contains(respuesta3_editor.getText()))
                BD.escribir_acierto((String)pregunta_selector.getSelected(), respuesta3_editor.getText());

            BD.escribir_respuesta3((String)pregunta_selector.getSelected(), respuesta3_editor.getText());

            editados.respuesta3_editado = false;
        }
    }

    public void resume() { pantalla_actual = "Menu_Datos"; }

    public void dispose() { //es la ultima en ejecutarse, se encarga de liberar recursos y dejar la memoria limpia

        batch.dispose();
    }

    public void texturas_cargar() {
        rosco_botones = new Texture("data/texturas/botones_datos.png");
        qqsm_botones = new Texture("data/texturas/botones_datos.png");

        //Rosco

        rosco_etiqueta = new Label("Rosco", texto_estilo(0.055));
        rosco_etiqueta.setPosition(proporcion_x(0.41), proporcion_y(0.9));

        palabra_selector = new SelectBox<>(selector_estilo(0.05));
        palabra_selector.setPosition(proporcion_x(0.4), proporcion_y(0.7));
        palabra_selector.setMaxListCount(3);
        palabra_selector.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //TODO
            }
        });

        letra_selector = new SelectBox<>(selector_estilo(0.05));
        letra_selector.setPosition(proporcion_x(0.7), proporcion_y(0.7));
        letra_selector.setMaxListCount(3);
        letra_selector.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //TODO
            }
        });

        descripcion_editor = new TextArea("", editor_estilo(0.02));
        descripcion_editor.setPosition(proporcion_x(0.4), proporcion_y(0.55));
        descripcion_editor.setSize(proporcion_x(0.5), proporcion_y(0.1));
        descripcion_editor.addListener(new InputListener() {
            public boolean keyUp (InputEvent event, int keycode) {
                editados.descripcion_editado = true;
                return false;
            }
        });

        //QQSM

        qqsm_etiqueta = new Label("QQSM", texto_estilo(0.055));
        qqsm_etiqueta.setPosition(proporcion_x(0.41), proporcion_y(0.45));

        pregunta_selector = new SelectBox<>(selector_estilo(0.05));
        pregunta_selector.setPosition(proporcion_x(0.4), proporcion_y(0.3));
        pregunta_selector.setMaxListCount(3);
        pregunta_selector.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //TODO
            }
        });

        respuesta_correcta_selector = new SelectBox<>(selector_estilo(0.05));
        respuesta_correcta_selector.setPosition(proporcion_x(0.7), proporcion_y(0.3));
        respuesta_correcta_selector.setMaxListCount(3);
        respuesta_correcta_selector.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //TODO
            }
        });

        respuesta0_editor = new TextArea("", editor_estilo(0.02));
        respuesta0_editor.setPosition(proporcion_x(0.4), proporcion_y(0.2));
        respuesta0_editor.setSize(proporcion_x(0.2), proporcion_y(0.1));
        respuesta0_editor.addListener(new InputListener() {
            public boolean keyUp (InputEvent event, int keycode) {
                editados.respuesta0_editado = true;
                return false;
            }
        });

        respuesta1_editor = new TextArea("", editor_estilo(0.02));
        respuesta1_editor.setPosition(proporcion_x(0.7), proporcion_y(0.2));
        respuesta1_editor.setSize(proporcion_x(0.2), proporcion_y(0.1));
        respuesta1_editor.addListener(new InputListener() {
            public boolean keyUp (InputEvent event, int keycode) {
                editados.respuesta1_editado = true;
                return false;
            }
        });

        respuesta2_editor = new TextArea("", editor_estilo(0.02));
        respuesta2_editor.setPosition(proporcion_x(0.4), proporcion_y(0.1));
        respuesta2_editor.setSize(proporcion_x(0.2), proporcion_y(0.1));
        respuesta2_editor.addListener(new InputListener() {
            public boolean keyUp (InputEvent event, int keycode) {
                editados.respuesta2_editado = true;
                return false;
            }
        });

        respuesta3_editor = new TextArea("", editor_estilo(0.02));
        respuesta3_editor.setPosition(proporcion_x(0.7), proporcion_y(0.1));
        respuesta3_editor.setSize(proporcion_x(0.2), proporcion_y(0.1));
        respuesta3_editor.addListener(new InputListener() {
            public boolean keyUp (InputEvent event, int keycode) {
                editados.respuesta3_editado = true;
                return false;
            }
        });

        //Jugadores

        jugadores_etiqueta = new Label("Jugadores", texto_estilo(0.055));
        jugadores_etiqueta.setPosition(proporcion_x(0.11), proporcion_y(0.9));

        nombres_etiqueta = new Label(jugadores.nombres(), texto_panel_scroll_estilo());
        nombres_etiqueta.setWidth(proporcion_x(0.2));
        nombres_etiqueta.setWrap(true);
        nombres_etiqueta.setAlignment(topLeft);
        nombres_scroll = new ScrollPane(nombres_etiqueta);
        nombres_scroll.setBounds(proporcion_x(0.1), proporcion_y(0.1), proporcion_x(0.2), proporcion_y(0.8));
        nombres_scroll.layout();
        nombres_scroll.setTouchable(Touchable.enabled);
    }

    public void botones_cargar() {

        final Rectangle anadir_rosco = new Rectangle(anchura_juego - proporcion_x(0.1777), proporcion_y(0.038), proporcion_x(0.0333), proporcion_y(0.062));
        final Rectangle eliminar_rosco = new Rectangle(anchura_juego - proporcion_x(0.2), proporcion_y(0.038), proporcion_x(0.0333), proporcion_y(0.062));
        final Rectangle anadir_fichero_rosco = new Rectangle(anchura_juego - proporcion_x(0.1444), proporcion_y(0.038), proporcion_x(0.0333), proporcion_y(0.062));

        final Rectangle anadir_qqsm = new Rectangle(anchura_juego - proporcion_x(0.1777), proporcion_y(0.488), proporcion_x(0.0333), proporcion_y(0.062));
        final Rectangle eliminar_qqsm = new Rectangle(anchura_juego - proporcion_x(0.2), proporcion_y(0.488), proporcion_x(0.0333), proporcion_y(0.062));
        final Rectangle anadir_fichero_qqsm = new Rectangle(anchura_juego - proporcion_x(0.1444), proporcion_y(0.488), proporcion_x(0.0333), proporcion_y(0.062));

        click = new InputAdapter() {
            public boolean touchUp(int x, int y, int pointer, int button) {
                if (anadir_rosco.contains(x, y)) {
                    Gdx.input.getTextInput(new Input.TextInputListener() {
                        public void input(final String palabra) {
                            if (!palabra.isEmpty()) {
                                nueva_palabra.palabra = palabra;

                                Gdx.input.getTextInput(new Input.TextInputListener() {
                                    public void input(String descripcion) {
                                        if (!descripcion.isEmpty()) {
                                            nueva_palabra.descripcion = descripcion;
                                            BD.escribir_descripcion(nueva_palabra);
                                        }
                                    }

                                    public void canceled() {
                                        nueva_palabra = new Palabra();
                                    }

                                }, "Introduzca la descripcion de la palabra", "", "descripcion");
                            }
                        }

                        public void canceled() {
                        }

                    }, "Introduzca la nueva palabra", "", "palabra");
                } else
                if (eliminar_rosco.contains(x, y)) {
                    BD.eliminar_descripcion((String)palabra_selector.getSelected());
                } else
                if (anadir_fichero_rosco.contains(x, y)) {
                    JFileChooser fichero = new JFileChooser();
                    fichero.showOpenDialog(null);
                    if (fichero.getSelectedFile() != null)
                        BD.escribir_descripciones(fichero.getSelectedFile());
                } else
                if (anadir_qqsm.contains(x, y)) {
                    Gdx.input.getTextInput(new Input.TextInputListener() {
                        public void input(final String pregunta) {
                            if (!pregunta.isEmpty()) {
                                nueva_pregunta.pregunta = pregunta;

                                Gdx.input.getTextInput(new Input.TextInputListener() {
                                    public void input(String respuesta0) {
                                        if (!respuesta0.isEmpty()) {
                                            nueva_pregunta.respuestas[0] = respuesta0;

                                            Gdx.input.getTextInput(new Input.TextInputListener() {
                                                public void input(String respuesta1) {
                                                    if (!respuesta1.isEmpty()) {
                                                        nueva_pregunta.respuestas[1] = respuesta1;

                                                        Gdx.input.getTextInput(new Input.TextInputListener() {
                                                            public void input(String respuesta2) {
                                                                if (!respuesta2.isEmpty()) {
                                                                    nueva_pregunta.respuestas[2] = respuesta2;

                                                                    Gdx.input.getTextInput(new Input.TextInputListener() {
                                                                        public void input(String respuesta3) {
                                                                            if (!respuesta3.isEmpty()) {
                                                                                nueva_pregunta.respuestas[3] = respuesta3;
                                                                                BD.escribir_pregunta(nueva_pregunta);
                                                                            }
                                                                        }

                                                                        public void canceled() {
                                                                            nueva_pregunta = new Pregunta();
                                                                            nueva_pregunta.respuestas = new String[4];
                                                                        }

                                                                    }, "Introduzca la cuarta respuesta", "", "cuarta respuesta");
                                                                }
                                                            }

                                                            public void canceled() {
                                                                nueva_pregunta = new Pregunta();
                                                                nueva_pregunta.respuestas = new String[4];
                                                            }

                                                        }, "Introduzca la tercera respuesta", "", "tercera respuesta");
                                                    }
                                                }

                                                public void canceled() {
                                                    nueva_pregunta = new Pregunta();
                                                    nueva_pregunta.respuestas = new String[4];
                                                }

                                            }, "Introduzca la segunda respuesta", "", "segunda respuesta");
                                        }
                                    }

                                    public void canceled() {
                                        nueva_pregunta = new Pregunta();
                                        nueva_pregunta.respuestas = new String[4];
                                    }

                                }, "Introduzca la primera respuesta", "", "primera respuesta");
                            }
                        }

                        public void canceled() {
                        }

                    }, "Introduzca la nueva pregunta", "", "pregunta");
                } else
                if (eliminar_qqsm.contains(x, y)) {
                    BD.eliminar_descripcion((String)palabra_selector.getSelected());
                } else
                if (anadir_fichero_qqsm.contains(x, y)) {
                    JFileChooser fichero = new JFileChooser();
                    fichero.showOpenDialog(null);
                    if (fichero.getSelectedFile() != null)
                        BD.escribir_preguntas(fichero.getSelectedFile());
                }

                    return false;
            }
        };
    }
}
