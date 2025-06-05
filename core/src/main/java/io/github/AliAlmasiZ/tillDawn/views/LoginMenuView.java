package io.github.AliAlmasiZ.tillDawn.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class LoginMenuView {
    private Stage stage;
    private Table table;
    public TextField usernameField, passwordField;
    public TextButton loginButton, backButton;

    public LoginMenuView(Skin skin) {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        float height = stage.getViewport().getWorldHeight();
        float width = stage.getViewport().getWorldWidth();

        table = new Table();
        table.setFillParent(true);
        table.center();
        table.pad(40);

        stage.addActor(table);

        //Title
        Label title = new Label("Login", skin);
        title.setFontScale(1.5f);
        table.add(title).colspan(2).padBottom(20);
        table.row();

        //Username
        Label usernameLabel = new Label("Username:", skin);
        usernameField = new TextField("", skin);
        usernameField.setMessageText("Enter username");
        table.add(usernameLabel).pad(10).right();
        table.add(usernameField).pad(10).width(300);
        table.row();

        //Pass
        Label passwordLabel = new Label("Password:", skin);
        passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        passwordField.setMessageText("Enter password");
        table.add(passwordLabel).pad(10).right();
        table.add(passwordField).pad(10).width(300);
        table.row();

        //Buttons
        loginButton = new TextButton("Login", skin);
        backButton = new TextButton("Back", skin);

        Table buttonTable = new Table();
        buttonTable.defaults().pad(10).height(75).width(400);
        buttonTable.add(loginButton);
        buttonTable.add(backButton);
        table.add(buttonTable).colspan(2).padTop(20).center();

    }


    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }
}
