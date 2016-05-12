package com.mygdx.DidacticGame.Pantallas.Menus;

import com.badlogic.gdx.*;
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
import com.mygdx.DidacticGame.DidacticGame;
import com.mygdx.DidacticGame.Auxiliares.Pantalla;

import javax.swing.*;

import static com.badlogic.gdx.utils.Align.topLeft;
import static com.mygdx.DidacticGame.DidacticGame.BD;
import static com.mygdx.DidacticGame.DidacticGame.jugadores;
import static com.mygdx.DidacticGame.Auxiliares.Base_Datos.Palabra;
import static com.mygdx.DidacticGame.Auxiliares.Base_Datos.Pregunta;
import static com.mygdx.DidacticGame.DidacticGame.tarea;

public class Menu_Datos extends Pantalla{

    DidacticGame juego;
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

    public Menu_Datos(DidacticGame juego) {
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
        if (Gdx.app.getType() == Application.ApplicationType.Desktop)
            batch.draw(boton_atras, 0, 0, anchura_juego, altura_juego);
        batch.draw((Texture)tarea.asset.get("data/texturas/botones_datos.png"), anchura_juego - proporcion_x(0.2), rosco_etiqueta.getY(), proporcion_x(0.1), proporcion_y(0.062));
        batch.draw((Texture)tarea.asset.get("data/texturas/botones_datos.png"), anchura_juego - proporcion_x(0.2), qqsm_etiqueta.getY(), proporcion_x(0.1), proporcion_y(0.062));
        jugadores_etiqueta.draw(batch, 1);
        rosco_etiqueta.draw(batch, 1);
        qqsm_etiqueta.draw(batch, 1);
        batch.end();

        stage.act();
        stage.draw();
    }

    public void show () {
        nombres_etiqueta.setText(jugadores.nombres());

        actualizar_palabras();
        actualizar_preguntas();

        sistema_botones(juego);
        stage.addActor(respuesta_correcta_selector);
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
            int posicion_letra = ((String)palabra_selector.getSelected()).indexOf((String)letra_selector.getSelected());
            BD.actualizar_palabra((String)palabra_selector.getSelected(), posicion_letra, descripcion_editor.getText());
            editados.descripcion_editado = false;
        }
        if (editados.respuesta0_editado) {
            if (((String)respuesta_correcta_selector.getSelected()).compareTo(respuesta0_editor.getText()) == 0)
                BD.actualizar_correcta((String)pregunta_selector.getSelected(), respuesta0_editor.getText());

            BD.actualizar_respuesta0((String)pregunta_selector.getSelected(), respuesta0_editor.getText());

            editados.respuesta0_editado = false;
        }
        if (editados.respuesta1_editado) {
            if (((String)respuesta_correcta_selector.getSelected()).compareTo(respuesta1_editor.getText()) == 0)
                BD.actualizar_correcta((String)pregunta_selector.getSelected(), respuesta1_editor.getText());

            BD.actualizar_respuesta1((String)pregunta_selector.getSelected(), respuesta1_editor.getText());

            editados.respuesta1_editado = false;
        }
        if (editados.respuesta2_editado) {
            if (((String)respuesta_correcta_selector.getSelected()).compareTo(respuesta2_editor.getText()) == 0)
                BD.actualizar_correcta((String)pregunta_selector.getSelected(), respuesta2_editor.getText());

            BD.actualizar_respuesta2((String)pregunta_selector.getSelected(), respuesta2_editor.getText());

            editados.respuesta2_editado = false;
        }
        if (editados.respuesta3_editado) {
            if (((String)respuesta_correcta_selector.getSelected()).compareTo(respuesta3_editor.getText()) == 0)
                BD.actualizar_correcta((String)pregunta_selector.getSelected(), respuesta3_editor.getText());

            BD.actualizar_respuesta3((String)pregunta_selector.getSelected(), respuesta3_editor.getText());

            editados.respuesta3_editado = false;
        }
    }

    public void resume() { pantalla_actual = "Menu_Datos"; }

    public void dispose() { //es la ultima en ejecutarse, se encarga de liberar recursos y dejar la memoria limpia

        batch.dispose();
    }

    public void texturas_cargar() {

        //Rosco

        rosco_etiqueta = new Label("Rosco", texto_estilo(0.07));
        rosco_etiqueta.setPosition(proporcion_x(0.41), proporcion_y(0.85));

        palabra_selector = new SelectBox<>(selector_estilo(0.05));
        palabra_selector.setPosition(proporcion_x(0.4), proporcion_y(0.75));
        palabra_selector.setMaxListCount(3);
        palabra_selector.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Palabra palabra = BD.leer_palabra((String)((SelectBox)actor).getSelected());
                letra_selector.setItems(palabra.palabra.split(""));
                letra_selector.setSelectedIndex(palabra.posicion_letra);
                descripcion_editor.setText(palabra.descripcion);
            }
        });

        letra_selector = new SelectBox<>(selector_estilo(0.05));
        letra_selector.setPosition(proporcion_x(0.7), proporcion_y(0.75));
        letra_selector.setMaxListCount(3);
        letra_selector.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                BD.actualizar_posicion_letra(((String)palabra_selector.getSelected()), letra_selector.getSelectedIndex());
            }
        });

        descripcion_editor = new TextArea("", editor_estilo(0.03));
        descripcion_editor.setPosition(proporcion_x(0.4), proporcion_y(0.55));
        descripcion_editor.setSize(proporcion_x(0.5), proporcion_y(0.2));
        descripcion_editor.addListener(new InputListener() {
            public boolean keyUp (InputEvent event, int keycode) {
                if (66 == keycode)
                    BD.actualizar_descripcion(((String)palabra_selector.getSelected()), descripcion_editor.getText());
                else
                    editados.descripcion_editado = true;

                return false;
            }
        });

        //QQSM

        qqsm_etiqueta = new Label("QQSM", texto_estilo(0.07));
        qqsm_etiqueta.setPosition(proporcion_x(0.41), proporcion_y(0.4));

        pregunta_selector = new SelectBox<>(selector_estilo(0.05));
        pregunta_selector.setPosition(proporcion_x(0.4), proporcion_y(0.3));
        pregunta_selector.setMaxListCount(3);
        pregunta_selector.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                String[] respuestas = BD.leer_respuestas((String)pregunta_selector.getSelected()).split("\n");
                respuesta_correcta_selector.setItems(respuestas);
                for (int i = 0; i < respuestas.length; ++i)
                    if (BD.leer_respuesta_correcta((String)pregunta_selector.getSelected()).compareTo(respuestas[i]) == 0) {
                        respuesta_correcta_selector.setSelectedIndex(i);
                    }
                respuesta_correcta_selector.pack();
                respuesta0_editor.setText(BD.leer_respuesta0((String)pregunta_selector.getSelected()));
                respuesta1_editor.setText(BD.leer_respuesta1((String)pregunta_selector.getSelected()));
                respuesta2_editor.setText(BD.leer_respuesta2((String)pregunta_selector.getSelected()));
                respuesta3_editor.setText(BD.leer_respuesta3((String)pregunta_selector.getSelected()));
            }
        });

        respuesta_correcta_selector = new SelectBox<>(selector_estilo(0.05));
        respuesta_correcta_selector.setPosition(proporcion_x(0.7), proporcion_y(0.3));
        respuesta_correcta_selector.setMaxListCount(3);
        respuesta_correcta_selector.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                BD.actualizar_correcta((String)pregunta_selector.getSelected(), (String)respuesta_correcta_selector.getSelected());
            }
        });

        respuesta0_editor = new TextArea("", editor_estilo(0.03));
        respuesta0_editor.setPosition(proporcion_x(0.4), proporcion_y(0.2));
        respuesta0_editor.setSize(proporcion_x(0.18), proporcion_y(0.1));
        respuesta0_editor.addListener(new InputListener() {
            public boolean keyUp (InputEvent event, int keycode) {
                if (66 == keycode)
                    BD.actualizar_respuesta0(((String)pregunta_selector.getSelected()), respuesta0_editor.getText());
                else
                    editados.respuesta0_editado = true;
                return false;
            }
        });

        respuesta1_editor = new TextArea("", editor_estilo(0.03));
        respuesta1_editor.setPosition(proporcion_x(0.7), proporcion_y(0.2));
        respuesta1_editor.setSize(proporcion_x(0.18), proporcion_y(0.1));
        respuesta1_editor.addListener(new InputListener() {
            public boolean keyUp (InputEvent event, int keycode) {
                if (66 == keycode)
                    BD.actualizar_respuesta1(((String)pregunta_selector.getSelected()), respuesta1_editor.getText());
                else
                    editados.respuesta1_editado = true;
                return false;
            }
        });

        respuesta2_editor = new TextArea("", editor_estilo(0.03));
        respuesta2_editor.setPosition(proporcion_x(0.4), proporcion_y(0.1));
        respuesta2_editor.setSize(proporcion_x(0.18), proporcion_y(0.1));
        respuesta2_editor.addListener(new InputListener() {
            public boolean keyUp (InputEvent event, int keycode) {
                if (66 == keycode)
                    BD.actualizar_respuesta2(((String)pregunta_selector.getSelected()), respuesta2_editor.getText());
                else
                    editados.respuesta2_editado = true;
                return false;
            }
        });

        respuesta3_editor = new TextArea("", editor_estilo(0.03));
        respuesta3_editor.setPosition(proporcion_x(0.7), proporcion_y(0.1));
        respuesta3_editor.setSize(proporcion_x(0.18), proporcion_y(0.1));
        respuesta3_editor.addListener(new InputListener() {
            public boolean keyUp (InputEvent event, int keycode) {
                if (66 == keycode)
                    BD.actualizar_respuesta3(((String)pregunta_selector.getSelected()), respuesta3_editor.getText());
                else
                    editados.respuesta3_editado = true;
                return false;
            }
        });

        //Jugadores

        jugadores_etiqueta = new Label("Jugadores", texto_estilo(0.07));
        jugadores_etiqueta.setPosition(proporcion_x(0.11), proporcion_y(0.85));

        nombres_etiqueta = new Label(jugadores.nombres(), texto_panel_scroll_estilo());
        nombres_etiqueta.setWidth(proporcion_x(0.2));
        nombres_etiqueta.setWrap(true);
        nombres_etiqueta.setAlignment(topLeft);
        nombres_scroll = new ScrollPane(nombres_etiqueta);
        nombres_scroll.setBounds(proporcion_x(0.1), proporcion_y(0.05), proporcion_x(0.2), proporcion_y(0.8));
        nombres_scroll.layout();
        nombres_scroll.setTouchable(Touchable.enabled);
    }

    public void botones_cargar() {

        final Rectangle anadir_rosco = new Rectangle(anchura_juego - proporcion_x(0.1777), proporcion_y(0.1), proporcion_x(0.0333), proporcion_y(0.062));
        final Rectangle eliminar_rosco = new Rectangle(anchura_juego - proporcion_x(0.2), proporcion_y(0.1), proporcion_x(0.0333), proporcion_y(0.062));
        final Rectangle anadir_fichero_rosco = new Rectangle(anchura_juego - proporcion_x(0.1444), proporcion_y(0.1), proporcion_x(0.0333), proporcion_y(0.062));

        final Rectangle anadir_qqsm = new Rectangle(anchura_juego - proporcion_x(0.1777), proporcion_y(0.55), proporcion_x(0.0333), proporcion_y(0.062));
        final Rectangle eliminar_qqsm = new Rectangle(anchura_juego - proporcion_x(0.2), proporcion_y(0.55), proporcion_x(0.0333), proporcion_y(0.062));
        final Rectangle anadir_fichero_qqsm = new Rectangle(anchura_juego - proporcion_x(0.1444), proporcion_y(0.55), proporcion_x(0.0333), proporcion_y(0.062));

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
                                            BD.anadir_palabra(nueva_palabra);
                                            actualizar_palabras();
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
                    BD.eliminar_palabra((String)palabra_selector.getSelected());
                    actualizar_palabras();
                } else
                if (anadir_fichero_rosco.contains(x, y)) {
                    JFileChooser fichero = new JFileChooser();
                    fichero.showOpenDialog(null);
                    if (fichero.getSelectedFile() != null) {
                        BD.anadir_fichero_palabras(fichero.getSelectedFile());
                        actualizar_palabras();
                    }
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
                                            nueva_pregunta.correcta = respuesta0;

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
                                                                                BD.anadir_pregunta(nueva_pregunta);
                                                                                actualizar_preguntas();
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

                                }, "Introduzca la respuesta correcta", "", "respuesta correcta");
                            }
                        }

                        public void canceled() {
                        }

                    }, "Introduzca la nueva pregunta", "", "pregunta");
                } else
                if (eliminar_qqsm.contains(x, y)) {
                    BD.eliminar_pregunta((String)pregunta_selector.getSelected());
                    actualizar_preguntas();
                } else
                if (anadir_fichero_qqsm.contains(x, y)) {
                    JFileChooser fichero = new JFileChooser();
                    fichero.showOpenDialog(null);
                    if (fichero.getSelectedFile() != null) {
                        BD.anadir_fichero_preguntas(fichero.getSelectedFile());
                        actualizar_preguntas();
                    }
                }

                return false;
            }
        };
    }

    public void actualizar_palabras() {
        palabra_selector.setItems(BD.leer_columna_rosco("palabra").split("\n"));
        palabra_selector.pack();
        Palabra palabra = BD.leer_palabra((String)palabra_selector.getSelected());
        letra_selector.setItems(palabra.palabra.split(""));
        letra_selector.setSelectedIndex(palabra.posicion_letra);
        letra_selector.pack();
        descripcion_editor.setText(palabra.descripcion);
    }

    public void actualizar_preguntas() {
        pregunta_selector.setItems(BD.leer_columna_qqsm("pregunta").split("\n"));
        pregunta_selector.pack();
        pregunta_selector.setWidth(proporcion_x(0.25));
        String[] respuestas = BD.leer_respuestas((String)pregunta_selector.getSelected()).split("\n");
        respuesta_correcta_selector.setItems(respuestas);
        for (int i = 0; i < respuestas.length; ++i)
            if (BD.leer_respuesta_correcta((String) pregunta_selector.getSelected()).compareTo(respuestas[i]) == 0)
                respuesta_correcta_selector.setSelectedIndex(i);

        respuesta_correcta_selector.pack();
        respuesta0_editor.setText(BD.leer_respuesta0((String)pregunta_selector.getSelected()));
        respuesta1_editor.setText(BD.leer_respuesta1((String)pregunta_selector.getSelected()));
        respuesta2_editor.setText(BD.leer_respuesta2((String)pregunta_selector.getSelected()));
        respuesta3_editor.setText(BD.leer_respuesta3((String)pregunta_selector.getSelected()));
    }
}
