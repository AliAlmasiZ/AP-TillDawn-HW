package io.github.AliAlmasiZ.tillDawn.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.AliAlmasiZ.tillDawn.models.Result;


public class LoginMenuView {
    private Stage stage;
    private Label statusMessageLabel;
    private Table mainTable;
    private Table forgetPassTable;
    public TextField usernameField, username2Field, passwordField, securityQuestionField;
    public TextButton loginButton, signupBtn, forgetPassBtn, submitBtn;

    public LoginMenuView(Skin skin) {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        float height = stage.getViewport().getWorldHeight();
        float width = stage.getViewport().getWorldWidth();

        initiateMainTable(skin);
        initiateForgetPassTable(skin);

    }

    private void initiateMainTable(Skin skin) {
        mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.center();
        mainTable.pad(40);

        stage.addActor(mainTable);

        statusMessageLabel = new Label("", skin);
        statusMessageLabel.setVisible(false);
        mainTable.add(statusMessageLabel).colspan(2);
        mainTable.row();

        //Title
        Label title = new Label(Text.LOGIN.getText(), skin);
        title.setFontScale(1.5f);
        mainTable.add(title).colspan(2).padBottom(20);
        mainTable.row();

        //Username
        Label usernameLabel = new Label(Text.USERNAME.getText() + ":", skin);
        usernameField = new TextField("", skin);
        usernameField.setMessageText(Text.ENTER_USERNAME.getText());
        mainTable.add(usernameLabel).pad(10).right();
        mainTable.add(usernameField).pad(10).width(300);
        mainTable.row();

        //Pass
        Label passwordLabel = new Label(Text.PASSWORD.getText() + ":", skin);
        passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        passwordField.setMessageText(Text.ENTER_PASSWORD.getText());
        mainTable.add(passwordLabel).pad(10).right();
        mainTable.add(passwordField).pad(10).width(300);
        mainTable.row();

        //Buttons
        loginButton = new TextButton(Text.LOGIN.getText(), skin);
        signupBtn = new TextButton(Text.SIGNUP.getText(), skin);
        forgetPassBtn = new TextButton(Text.FORGET_PASSWORD.getText(), skin);

//        forgetPassBtn.getLabel().setFontScale(0.75f);


        Table buttonTable = new Table();
        buttonTable.defaults().pad(10).height(75).width(300);
        buttonTable.add(loginButton);
        buttonTable.add(signupBtn);
        buttonTable.row();
        buttonTable.add(forgetPassBtn).colspan(2).width(600);
        mainTable.add(buttonTable).colspan(2).padTop(20).center();
    }

    private void initiateForgetPassTable(Skin skin) {
        forgetPassTable = new Table();
        forgetPassTable.setFillParent(true);
        forgetPassTable.setVisible(false);
        forgetPassTable.center();
        forgetPassTable.pad(40);

        username2Field = new TextField("", skin);
        username2Field.setMessageText(Text.USERNAME.getText());

        securityQuestionField = new TextField("", skin);
        securityQuestionField.setMessageText(Text.ENTER_YOUR_SECURITY_ANSWER.getText());

        submitBtn = new TextButton(Text.SUBMIT.getText(), skin);

        forgetPassTable.defaults().pad(40).width(500);
        forgetPassTable.add(username2Field).row();
        forgetPassTable.add(securityQuestionField);
        forgetPassTable.row();
        forgetPassTable.add(submitBtn);

        stage.addActor(forgetPassTable);
    }


    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void showForgetPassMenu(boolean b) {
        mainTable.setVisible(!b);
        forgetPassTable.setVisible(b);
    }

    public void setStatusMessage(Result result) {
        if(result == null) {
            this.statusMessageLabel.setVisible(false);
            this.statusMessageLabel.setText("");
            return;
        }
        this.statusMessageLabel.setText(result.message());
        this.statusMessageLabel.setVisible(true);
//        this.statusMessageLabel.setVisible(!result.isSuccessful());
    }

    public void dispose() {
        stage.dispose();
    }
}
