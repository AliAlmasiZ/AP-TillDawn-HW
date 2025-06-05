package io.github.AliAlmasiZ.tillDawn.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.CharArray;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.AliAlmasiZ.tillDawn.models.DataBase.AppData;
import io.github.AliAlmasiZ.tillDawn.models.GameAssetManager;
import io.github.AliAlmasiZ.tillDawn.models.User;

public class ProfileMenuView {
    private final OrthographicCamera camera;
    private final SpriteBatch batch;
    private final ShapeRenderer shapes;
    private BitmapFont titleFont;
    private BitmapFont detailFont;
    private final User user;

    private final float avatarSize = 120;
    private final float avatarRadius = avatarSize / 2;
    private final Vector2 avatarPosition = new Vector2();


    private Stage stage;
    private Table buttonTable, dataTable;

    public final TextButton changeUsernameBtn, changePasswordBtn, deleteAccountBtn, changeAvatarBtn, backBtn;
//    public final Image avatarImage;
    public final Label messageLabel;


    public ProfileMenuView(Skin skin) {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();
        shapes = new ShapeRenderer();


        /*FreeTypeFontGenerator titleGenerator = new FreeTypeFontGenerator(Gdx.files.internal(
            GameAssetManager.getGameAssetManager().CHEVY_RAY_LANTERN
        ));
        FreeTypeFontGenerator detailGenerator = new FreeTypeFontGenerator(Gdx.files.internal(
            GameAssetManager.getGameAssetManager().CHEVY_RAY_EXPRESS
        ));

        FreeTypeFontGenerator.FreeTypeFontParameter titleParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        FreeTypeFontGenerator.FreeTypeFontParameter detailParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        titleParameter.size = 20;
        detailParameter.size = 12;

        titleFont = titleGenerator.generateFont(titleParameter);
        detailFont = detailGenerator.generateFont(detailParameter);*/


        user = AppData.getAppData().getActiveUser();


        float width = stage.getViewport().getScreenWidth();
        float height = stage.getViewport().getScreenHeight();

        buttonTable = new Table();
        buttonTable.setFillParent(true);
        buttonTable.center();
        buttonTable.pad(40);

        stage.addActor(buttonTable);

        //Title
        Label title = new Label("Profile Menu", skin);
        title.setFontScale(1.5f);
        buttonTable.add(title).colspan(2).padBottom(20);
        buttonTable.row();

        //Avatar
//        Texture avatarTexture = new Texture(Gdx.files.internal("avatars/default_avatar.png"));
//        avatarImage = new Image(new TextureRegionDrawable(avatarTexture));
//        avatarImage.setSize(128, 128);
//        table.add(avatarImage).padRight(30).size(128, 128);




        dataTable = new Table();
        Label nameLabel = new Label("Username: " + AppData.getAppData().getActiveUser().getUsername(), skin);
        Label scoreLabel = new Label("Score: " + AppData.getAppData().getActiveUser().getScore(), skin);
        dataTable.add(nameLabel).left().padBottom(10);
//        details.row();
        dataTable.add(scoreLabel).left().padBottom(20);
        dataTable.row();

        changeUsernameBtn = new TextButton("Change Username", skin);
        changePasswordBtn = new TextButton("Change Password", skin);
        deleteAccountBtn = new TextButton("Delete Account", skin);
        changeAvatarBtn = new TextButton("Change Avatar", skin);
        backBtn = new TextButton(Text.GO_BACK.getText(), skin);

        // Add buttons to details
        dataTable.add(changeUsernameBtn).fillX().pad(5);
        dataTable.row();
        dataTable.add(changePasswordBtn).fillX().pad(5);
        dataTable.row();
        dataTable.add(changeAvatarBtn).fillX().pad(5);
        dataTable.row();
        dataTable.add(deleteAccountBtn).fillX().pad(5);

        buttonTable.add(dataTable);
        buttonTable.row();

        messageLabel = new Label("", skin);
        messageLabel.setVisible(false);
        messageLabel.setText("");
        buttonTable.add(messageLabel).colspan(2).fillX().padTop(20).height(30);


    }


    public void render(float delta) {

//        stage.act(delta);
//        stage.draw();


        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        avatarPosition.set(
            camera.viewportWidth * 0.5f - avatarRadius,
            camera.viewportHeight * 0.7f - avatarRadius
        );

        batch.setProjectionMatrix(camera.combined);
        shapes.setProjectionMatrix(camera.combined);

        //TODO : background

        drawAvatar();


    }


    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }




    private void drawAvatar() {
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(0.2f, 0.2f, 0.25f, 1);
        shapes.circle(
            avatarPosition.x + avatarRadius,
            avatarPosition.y + avatarRadius,
            avatarRadius
        );
        shapes.end();

        Gdx.gl.glEnable(GL20.GL_STENCIL_TEST);
        Gdx.gl.glClear(GL20.GL_STENCIL_BUFFER_BIT);
        Gdx.gl.glStencilFunc(GL20.GL_ALWAYS, 1, 0xFF);
        Gdx.gl.glStencilOp(GL20.GL_KEEP, GL20.GL_KEEP, GL20.GL_REPLACE);
        Gdx.gl.glStencilMask(0xFF);

        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(Color.WHITE);
        shapes.circle(
            avatarPosition.x + avatarRadius,
            avatarPosition.y + avatarRadius,
            avatarRadius
        );
        shapes.end();

        Gdx.gl.glStencilFunc(GL20.GL_EQUAL, 1, 0xFF);
        Gdx.gl.glStencilMask(0x00);

        batch.begin();
        batch.draw(user.getAvatar(),
            avatarPosition.x, avatarPosition.y,
            avatarSize, avatarSize
            );
        batch.end();

        Gdx.gl.glDisable(GL20.GL_STENCIL_TEST);

        shapes.begin(ShapeRenderer.ShapeType.Line);
        shapes.setColor(0.4f, 0.6f, 1f, 1);
        shapes.circle(
            avatarPosition.x + avatarRadius,
            avatarPosition.y + avatarRadius,
            avatarRadius
        );
        shapes.end();
    }

}
