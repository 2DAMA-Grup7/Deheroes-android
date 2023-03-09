package org.grup7.deheroes.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;

import org.grup7.deheroes.Vars;
import org.grup7.deheroes.utils.Assets;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginScreen implements Screen {
    private final Game game;
    private final Stage stage;
    private final Music music;
    private final Table setLoginTable;
    protected Skin skin = new Skin(Gdx.files.internal(Assets.Skin.uiSkin));


    public LoginScreen(Game game) {
        this.game = game;
        music = Gdx.audio.newMusic(Gdx.files.internal(Assets.Music.menu));
        music.setLooping(true);
        music.play();
        stage = new Stage();

        setLoginTable = LoginTable();

        stage.addActor(setLoginTable);

        setLoginTable.setVisible(true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
        if(auth){
            stage.dispose();
            game.setScreen(new MainMenu(game));
        }

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        music.stop();
    }

    private Table LoginTable() {
        TextField inputUser = new TextField("User", skin);
        TextField inputPasswd = new TextField("Password", skin);
        TextButton enterButton = new TextButton("Login", skin);


        Window window = new Window("Login", skin);


        window.add(inputUser).center();
        window.row();
        window.add(inputPasswd).center();
        window.row();
        window.add(enterButton).center();
        Table table = new Table();
        table.setFillParent(true);
        table.add(window);
        enterButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                JSONObject data = new JSONObject();
                try {
                    data.put("username" , inputUser.getText());
                    data.put("password" , inputPasswd.getText());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                connect(data);
            }
        });
        return table;
    }
    boolean auth=false;

    private void connect(JSONObject data){
        Net.HttpRequest httpPOST = new Net.HttpRequest(Net.HttpMethods.POST);
        httpPOST.setUrl(Vars.LoginURL);

        try {
            httpPOST.setContent("username=" + data.getString("username") + "&password=" + data.getString("password"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Gdx.net.sendHttpRequest(httpPOST, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                Gdx.app.log("MSG", httpResponse.getResultAsString());
                auth=true;
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("LOGIN", "was NOT successful!");
            }

            @Override
            public void cancelled() {
                Gdx.app.log("LOGIN", "was cancelled!");
            }
        });
        //TODO get JSON CONFIG TODO POST SCORE!!
    }
}
