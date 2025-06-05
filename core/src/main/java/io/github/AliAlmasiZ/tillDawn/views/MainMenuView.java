package io.github.AliAlmasiZ.tillDawn.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
public class MainMenuView {
    public Stage stage;
    public TextButton settingsBtn, profileBtn, pregameBtn, scoreboardBtn, hintBtn, loadGameBtn, logoutBtn;
    public Label mainMenuLabel;
    public Image avatarImage;
    public Label usernameLabel, scoreLabel;

    public MainMenuView(Skin skin) {
        // Stage and input setup
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Main table layout
        Table table = new Table();
        table.setFillParent(true);
        table.center().pad(40);
        stage.addActor(table);

        // Title label
        mainMenuLabel = new Label("Main Menu", skin);
        mainMenuLabel.setFontScale(1.5f);
        table.add(mainMenuLabel).colspan(2).padBottom(20);
        table.row();

        // User avatar and info
//        Texture avatarTexture = new Texture(Gdx.files.internal("avatars/default_avatar.png"));
//        avatarImage = new Image(new TextureRegionDrawable(avatarTexture));
//        avatarImage.setSize(64, 64);
//        table.add(avatarImage).padRight(20).size(64, 64);

//        Table infoTable = new Table();
//        usernameLabel = new Label("Username: Player1", skin);
//        scoreLabel = new Label("Score: 1234", skin);
//        infoTable.add(usernameLabel).left().padBottom(5);
//        infoTable.row();
//        infoTable.add(scoreLabel).left();
//
//        table.add(infoTable).left();
//        table.row().padTop(30);

        // Continue saved game button
        loadGameBtn = new TextButton("Continue Saved Game", skin);
        table.add(loadGameBtn).colspan(2).fillX().pad(5);
        table.row();

        // Menu buttons
        settingsBtn = new TextButton("Settings", skin);
        profileBtn = new TextButton("Profile", skin);
        pregameBtn = new TextButton("Pre-Game", skin);
        scoreboardBtn = new TextButton("Scoreboard", skin);
        hintBtn = new TextButton("Hint Menu", skin);
        logoutBtn = new TextButton("Logout", skin);

        table.add(settingsBtn).fillX().pad(5);
        table.add(profileBtn).fillX().pad(5);
        table.row();
        table.add(pregameBtn).fillX().pad(5);
        table.add(scoreboardBtn).fillX().pad(5);
        table.row();
        table.add(hintBtn).fillX().pad(5);
        table.add(logoutBtn).fillX().pad(5);
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
    }

}
