package io.github.AliAlmasiZ.tillDawn.views.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.AliAlmasiZ.tillDawn.Main;
import io.github.AliAlmasiZ.tillDawn.controllers.SignupMenuController;
import io.github.AliAlmasiZ.tillDawn.models.GameAssetManager;
import io.github.AliAlmasiZ.tillDawn.models.Result;
import io.github.AliAlmasiZ.tillDawn.views.SignupMenuView;

public class SignUpMenuScreen implements Screen {
    private final Main main;
    private final SignupMenuView view;
    private final SignupMenuController controller;

    public SignUpMenuScreen(Main main) {
        this.main = main;
        this.controller = new SignupMenuController();
        this.view = new SignupMenuView(GameAssetManager.getGameAssetManager().pixthulhuuiSkin);

        view.loginMenuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setScreen(new LoginMenuScreen(main));
            }
        });

        view.signupButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Result result = controller.signup(
                    view.usernameField.getText(),
                    view.passwordField.getText(),
                    view.securityAnswerField.getText()
                );


                if(result.isSuccessful())
                    main.setScreen(new MainMenuScreen(main));
                view.setStatusMessage(result);
            }
        });

        view.guestButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.guest();
                main.setScreen(new MainMenuScreen(main));
            }
        });


    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        view.render(delta);
    }

    @Override
    public void show() {

    }


    @Override
    public void resize(int width, int height) {
        view.resize(width, height);
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
