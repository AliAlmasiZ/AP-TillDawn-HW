package io.github.AliAlmasiZ.tillDawn.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.AliAlmasiZ.tillDawn.models.DataBase.AppData;

public class ProfileMenuView {


    private Stage stage;
    private Table table;

    public final TextButton changeUsernameBtn, changePasswordBtn, deleteAccountBtn, changeAvatarBtn;
//    public final Image avatarImage;
    public final TextField messageField;


    public ProfileMenuView(Skin skin) {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        float width = stage.getViewport().getScreenWidth();
        float height = stage.getViewport().getScreenHeight();

        table = new Table();
        table.setFillParent(true);
        table.center();
        table.pad(40);

        stage.addActor(table);

        //Title
        Label title = new Label("Profile Menu", skin);
        title.setFontScale(1.5f);
        table.add(title).colspan(2).padBottom(20);
        table.row();

        //Avatar
//        Texture avatarTexture = new Texture(Gdx.files.internal("avatars/default_avatar.png"));
//        avatarImage = new Image(new TextureRegionDrawable(avatarTexture));
//        avatarImage.setSize(128, 128);
//        table.add(avatarImage).padRight(30).size(128, 128);
        Table details = new Table();
        Label nameLabel = new Label("Username: " + AppData.getAppData().getActivePlayer().getUsername(), skin);
        Label scoreLabel = new Label("Score: " + AppData.getAppData().getActivePlayer().getScore(), skin);
        details.add(nameLabel).left().padBottom(10);
        details.row();
        details.add(scoreLabel).left().padBottom(20);
        details.row();

        changeUsernameBtn = new TextButton("Change Username", skin);
        changePasswordBtn = new TextButton("Change Password", skin);
        deleteAccountBtn = new TextButton("Delete Account", skin);
        changeAvatarBtn = new TextButton("Change Avatar", skin);

        // Add buttons to details
        details.add(changeUsernameBtn).fillX().pad(5);
        details.row();
        details.add(changePasswordBtn).fillX().pad(5);
        details.row();
        details.add(changeAvatarBtn).fillX().pad(5);
        details.row();
        details.add(deleteAccountBtn).fillX().pad(5);

        table.add(details);
        table.row();

        messageField = new TextField("", skin);
        messageField.setDisabled(true);
        messageField.setMessageText("");
        table.add(messageField).colspan(2).fillX().padTop(20).height(30);


    }


    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

}
