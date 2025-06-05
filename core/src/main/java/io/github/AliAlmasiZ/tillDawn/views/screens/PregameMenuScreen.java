package io.github.AliAlmasiZ.tillDawn.views.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import io.github.AliAlmasiZ.tillDawn.Main;
import io.github.AliAlmasiZ.tillDawn.models.GameAssetManager;
import io.github.AliAlmasiZ.tillDawn.views.GameView;
import io.github.AliAlmasiZ.tillDawn.views.PregameMenuView;

public class PregameMenuScreen extends ScreenAdapter {
    private final Main main;
    private final PregameMenuView view;

    public PregameMenuScreen(Main main) {
        this.main = main;
        this.view = new PregameMenuView(GameAssetManager.getGameAssetManager().pixthulhuuiSkin);
    }
}
