package io.github.AliAlmasiZ.tillDawn.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.AliAlmasiZ.tillDawn.models.Result;


public class SignupMenuView {
    private final Stage stage;
    private final Table table;
    public final Label usernameLabel, passwordLabel, statusMessageLabel;
    public final TextField usernameField, passwordField, /*securityQuestionField,*/ securityAnswerField;
    public final TextButton signupButton, guestButton, loginMenuBtn;


    public SignupMenuView(Skin skin) {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        float width = stage.getViewport().getScreenWidth();
        float height = stage.getViewport().getScreenHeight();

        table = new Table();
        table.setFillParent(true);
        table.center();
        table.pad(40);

        stage.addActor(table);

        statusMessageLabel = new Label("", skin);
        statusMessageLabel.setVisible(false);
        table.add(statusMessageLabel).colspan(2).pad(20);
        table.row();

        //Title
        Label title = new Label(Text.CREATE_ACCOUNT.getText(), skin);
        title.setFontScale(1.5f);
        table.add(title).colspan(2).padBottom(20);
        table.row();

        //Username
        usernameLabel = new Label( Text.USERNAME.getText() + ":", skin);
        usernameField = new TextField("", skin);
        usernameField.setMessageText(Text.ENTER_USERNAME.getText());
        table.add(usernameLabel).pad(10).right();
        table.add(usernameField).pad(10).width(400);
        table.row();

        //Password
        passwordLabel = new Label(Text.PASSWORD.getText() + ":", skin);
        passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        passwordField.setMessageText(Text.ENTER_PASSWORD.getText());
        table.add(passwordLabel).pad(10).right();
        table.add(passwordField).pad(10).width(400);
        table.row();

//        //Security Questions
//        Label securityQuestionLabel = new Label("Security Question:", skin);
//        securityQuestionLabel.setFontScale(0.9f);
//        securityQuestionField = new TextField("", skin);
//        securityQuestionField.setMessageText("Enter Question");
//        table.add(securityQuestionLabel).pad(10).right();
//        table.add(securityQuestionField).pad(10).width(400);
//        table.row();

        //Security Answer
        Label securityAnswerLabel = new Label(Text.SECURITY_ANSWER.getText() + ":", skin);
        securityAnswerLabel.setFontScale(0.9f);
        securityAnswerField = new TextField("", skin);
        securityAnswerField.setMessageText(Text.ENTER_ANSWER.getText());
        table.add(securityAnswerLabel).pad(10).right();
        table.add(securityAnswerField).pad(10).width(400);
        table.row();

        //Buttons
        signupButton = new TextButton(Text.SIGNUP.getText(), skin);
        guestButton = new TextButton(Text.PLAY_AS_GUEST.getText(), skin);
        loginMenuBtn = new TextButton(Text.GOTO_LOGIN_MENU.getText(), skin);
        float labelScale = 0.9f;
        for (TextButton button : new TextButton[]{signupButton, guestButton, loginMenuBtn}) {
            button.getLabel().setFontScale(labelScale);
            button.pad(8, 12, 8, 12);
        }
        Table buttonTable = new Table();
        buttonTable.defaults().pad(10).height(75).width(500);
        buttonTable.add(signupButton);
        buttonTable.row();
        buttonTable.add(guestButton);
        buttonTable.row();
        buttonTable.add(loginMenuBtn);
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
        stage.draw();
    }

    public void setStatusMessage(Result result) {
        this.statusMessageLabel.setText(result.message());
        this.statusMessageLabel.setVisible(!result.isSuccessful());
    }
}
