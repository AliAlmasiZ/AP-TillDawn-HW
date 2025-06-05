package io.github.AliAlmasiZ.tillDawn;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.AliAlmasiZ.tillDawn.controllers.MainMenuController;
import io.github.AliAlmasiZ.tillDawn.models.DataBase.AppData;
import io.github.AliAlmasiZ.tillDawn.models.GameAssetManager;
import io.github.AliAlmasiZ.tillDawn.models.User;
import io.github.AliAlmasiZ.tillDawn.views.MainMenuView;
import io.github.AliAlmasiZ.tillDawn.views.screens.GameScreen;
import io.github.AliAlmasiZ.tillDawn.views.screens.MainMenuScreen;
import io.github.AliAlmasiZ.tillDawn.views.screens.SignUpMenuScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private static Main instance;

    public SpriteBatch batch;

    @Override
    public void create() {
        //TODO: for debug uncomment bellow code block :
        /*{
            AppData.getAppData().setActiveUser(new User("Ali", "pass", "meow"));
        }*/

        instance = this;
        batch = new SpriteBatch();
        setScreen(new SignUpMenuScreen(this));
    }

    @Override
    public void resize(int width, int height) {
        screen.resize(width,height);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        screen.dispose();
    }

    public static Main getInstance() {
        return instance;
    }

}
