package io.github.AliAlmasiZ.tillDawn.views.screens;

import com.badlogic.gdx.ScreenAdapter;
import io.github.AliAlmasiZ.tillDawn.Main;
import io.github.AliAlmasiZ.tillDawn.models.GameAssetManager;
import io.github.AliAlmasiZ.tillDawn.views.GameView;
import io.github.AliAlmasiZ.tillDawn.views.ScoreBoardMenuView;

public class ScoreboardScreen extends ScreenAdapter {
    private final Main main;
    private final ScoreBoardMenuView view;

    public ScoreboardScreen(Main main) {
        this.main = main;
        this.view = new ScoreBoardMenuView(GameAssetManager.getGameAssetManager().pixthulhuuiSkin);

    }
}
