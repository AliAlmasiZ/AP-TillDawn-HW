package io.github.AliAlmasiZ.tillDawn.views.screens;

import com.badlogic.gdx.ScreenAdapter;
import io.github.AliAlmasiZ.tillDawn.Main;
import io.github.AliAlmasiZ.tillDawn.models.GameAssetManager;
import io.github.AliAlmasiZ.tillDawn.views.SettingMenuView;

public class SettingMenuScreen extends ScreenAdapter {
    private final Main main;
    private final SettingMenuView view;


    public SettingMenuScreen(Main main) {
        this.main = main;
        this.view = new SettingMenuView(GameAssetManager.getGameAssetManager().pixthulhuuiSkin);
    }
}
