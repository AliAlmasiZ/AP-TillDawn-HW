package io.github.AliAlmasiZ.tillDawn.views.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.AliAlmasiZ.tillDawn.Main;
import io.github.AliAlmasiZ.tillDawn.models.GameAssetManager;
import io.github.AliAlmasiZ.tillDawn.views.MainMenuView;

public class MainMenuScreen  implements Screen {
    private final Main main;
    private final MainMenuView view;

    public MainMenuScreen(Main main) {
        this.main = main;
        this.view = new MainMenuView(GameAssetManager.getGameAssetManager().pixthulhuuiSkin);

        view.profileBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setScreen(new ProfileMenuScreen(main));
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        view.render(delta);
    }

    @Override
    public void resize(int i, int i1) {
        view.resize(i, i1);
    }

    @Override
    public void show() {

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
