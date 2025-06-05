package io.github.AliAlmasiZ.tillDawn.views.screens;

import com.badlogic.gdx.ScreenAdapter;
import io.github.AliAlmasiZ.tillDawn.Main;
import io.github.AliAlmasiZ.tillDawn.models.GameAssetManager;
import io.github.AliAlmasiZ.tillDawn.views.HintMenuView;

public class HintMenuScreen extends ScreenAdapter {
    private final Main main;
    private final HintMenuView view;

    public HintMenuScreen(Main main) {
        this.main = main;
        this.view = new HintMenuView(GameAssetManager.getGameAssetManager().pixthulhuuiSkin);

    }
}
