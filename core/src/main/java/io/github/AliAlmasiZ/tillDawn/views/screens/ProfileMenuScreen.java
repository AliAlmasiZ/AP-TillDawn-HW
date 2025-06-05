package io.github.AliAlmasiZ.tillDawn.views.screens;

import com.badlogic.gdx.Screen;
import io.github.AliAlmasiZ.tillDawn.Main;
import io.github.AliAlmasiZ.tillDawn.models.GameAssetManager;
import io.github.AliAlmasiZ.tillDawn.views.ProfileMenuView;

public class ProfileMenuScreen implements Screen {
    private final Main main;
    private final ProfileMenuView view = new ProfileMenuView(GameAssetManager.getGameAssetManager().pixthulhuuiSkin);


    public ProfileMenuScreen(Main main) {
        this.main = main;

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        view.render(delta);
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
