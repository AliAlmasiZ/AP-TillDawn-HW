package io.github.AliAlmasiZ.tillDawn.views.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.AliAlmasiZ.tillDawn.Main;
import io.github.AliAlmasiZ.tillDawn.controllers.LoginMenuController;
import io.github.AliAlmasiZ.tillDawn.models.GameAssetManager;
import io.github.AliAlmasiZ.tillDawn.models.Result;
import io.github.AliAlmasiZ.tillDawn.views.LoginMenuView;

public class LoginMenuScreen implements Screen {
    private final Main main;
    private final LoginMenuView view;
    private final LoginMenuController controller;

    public LoginMenuScreen(Main main) {
        this.controller = new LoginMenuController();
        this.view = new LoginMenuView(GameAssetManager.getGameAssetManager().pixthulhuuiSkin);
        this.main = main;

        view.loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Result result = controller.login(view.usernameField.getText(), view.passwordField.getText());
                if(result.isSuccessful()) {
                    main.setScreen(new MainMenuScreen(main));
                    view.dispose();
                    LoginMenuScreen.this.dispose();
                    return;
                }
                view.setStatusMessage(result);

            }
        });

        view.signupBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setScreen(new SignUpMenuScreen(main));
            }
        });

        view.forgetPassBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                view.showForgetPassMenu(true);
            }
        });

        view.submitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                view.showForgetPassMenu(false);
                view.setStatusMessage(controller.forgetPass(view.username2Field.getText(), view.securityQuestionField.getText()));
            }
        });


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        view.render(v);
    }

    @Override
    public void resize(int i, int i1) {
        view.resize(i, i1);
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

    }
}
