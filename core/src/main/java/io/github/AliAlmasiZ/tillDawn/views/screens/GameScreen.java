package io.github.AliAlmasiZ.tillDawn.views.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.AliAlmasiZ.tillDawn.Main;
import io.github.AliAlmasiZ.tillDawn.controllers.utils.ControlsManager;
import io.github.AliAlmasiZ.tillDawn.models.DataBase.AppData;
import io.github.AliAlmasiZ.tillDawn.models.Entities.Bullet;
import io.github.AliAlmasiZ.tillDawn.models.Entities.Enemy;
import io.github.AliAlmasiZ.tillDawn.models.Entities.XPOrb;
import io.github.AliAlmasiZ.tillDawn.models.GameAssetManager;
import io.github.AliAlmasiZ.tillDawn.models.Player;
import io.github.AliAlmasiZ.tillDawn.models.enums.GameAction;


import java.util.Map;

public class GameScreen extends ScreenAdapter {
    private final Main main;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;

    private Player player;
    private Texture playerTexture;
    private Animation<TextureRegion> enemyAnim;
    private Texture bulletTexture;
    private Texture xpOrbTexture;
    private Texture backgroundTexture;

    private Array<Enemy> enemies;
    private Array<Bullet> bullets;
    private Array<XPOrb> xpOrbs;

    private long lastEnemySpawnTime;
    private float enemySpawnInterval = 2000;

    private BitmapFont font;
    private ShapeRenderer shapeRenderer;

    private boolean isPaused = false;
    private boolean gameOver = false;
    private int score = 0;
    private float gameTimer = 0;
    private final float MAX_GAME_TIME = 20 * 60;

    private Music gameMusic;
    private Sound shootSound;
    private Sound enemyHitSound;
    private Sound playerHitSound;
    private Sound xpPickupSound;
    private Sound levelUpSound;

    // UI Stage and elements
    private Stage uiStage;
    private Table uiTableRoot;
    private Label scoreLabel;
    private Label timeLabel;
    private Label healthLabel;
    private Label levelLabel;
    private Label xpLabel;
    private Label pauseMessageLabel;
    private Label gameOverMessageLabel;
    private Label finalScoreLabel;
    private Label restartMessageLabel;
    private Table messagesTable; // Table for pause/game over messages

    private static final float GAME_WORLD_WIDTH = 1920;
    private static final float GAME_WORLD_HEIGHT = 1080;


    public GameScreen(Main main) {
        this.main = main;
        batch = main.batch;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();

//        viewport = new FitViewport(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT, camera);
        viewport = new ScreenViewport(camera);
        camera.setToOrtho(false, GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT);


        playerTexture = new Texture(GameAssetManager.getGameAssetManager().getCharacter1Idle0());
        enemyAnim = GameAssetManager.getGameAssetManager().brainMonsterAnim;
        bulletTexture = GameAssetManager.getGameAssetManager().bulletTex;
        xpOrbTexture = GameAssetManager.getGameAssetManager().xpOrbTex;
        backgroundTexture = GameAssetManager.getGameAssetManager().backgroundTileTex;

//        enemyTexture = new Texture(GameAssetManager.getGameAssetManager().getCharacter1Idle0())

        //TODO : Temp for test
        player = new Player();
//        player = AppData.getAppData().activeUser.getPlayer();
        player.getPosition().set(GAME_WORLD_WIDTH / 2f - playerTexture.getWidth() / 2f,
            GAME_WORLD_HEIGHT / 2f - playerTexture.getHeight() / 2f);

        if (player != null) {
            float pWidth = player.getWidth();
            float pHeight = player.getHeight();
            player.position.set(
                GAME_WORLD_WIDTH / 2f - pWidth / 2f,
                GAME_WORLD_HEIGHT / 2f - pHeight / 2f
            );
            player.update(0);
        }

        enemies = new Array<>();
        bullets = new Array<>();
        xpOrbs = new Array<>();
        shapeRenderer = new ShapeRenderer();



        FreeTypeFontGenerator generator = null;
        try {

            generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/Font/ChevyRay - Express.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = 24;
            parameter.color = Color.WHITE;
            font = generator.generateFont(parameter);
//            font = new BitmapFont(Gdx.files.internal("Fonts/Font/ChevyRay - Express.ttf"));
//            font.setColor(Color.WHITE);

        } catch (Exception e) {
            Gdx.app.error("GameScreen", "Could not load bitmap font 'fonts/yourfont.fnt'. Make sure the .fnt and .png files are in assets/fonts/", e);
            font = new BitmapFont();
            font.setColor(Color.RED);
        } finally {
            if(generator != null)
                generator.dispose();
        }

        // --- UI STAGE SETUP ---
        uiStage = new Stage(new ScreenViewport(), batch);
//        Gdx.input.setInputProcessor(uiStage);

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        scoreLabel = new Label("Score: 0", labelStyle);
        timeLabel = new Label("Time: 00:00", labelStyle);
        healthLabel = new Label("Health: 100/100", labelStyle);
        levelLabel = new Label("Level: 1", labelStyle);
        xpLabel = new Label("XP: 0/100", labelStyle);

        pauseMessageLabel = new Label("PAUSED (Press " + ControlsManager.getKeyNameForAction(GameAction.PAUSE) + " or ESC to resume)", labelStyle);
        gameOverMessageLabel = new Label("GAME OVER", labelStyle);
        finalScoreLabel = new Label("Final Score: 0", labelStyle);
        restartMessageLabel = new Label("Press R to Restart", labelStyle);

        uiTableRoot = new Table();
        uiTableRoot.setFillParent(true);
        uiStage.addActor(uiTableRoot);

        Table statsTable = new Table();
        statsTable.top().left().pad(10);
        statsTable.add(scoreLabel).left().row();
        statsTable.add(timeLabel).left().row();
        statsTable.add(healthLabel).left().row();
        statsTable.add(levelLabel).left().row();
        statsTable.add(xpLabel).left().row();

        messagesTable = new Table();
        messagesTable.center();
        messagesTable.add(pauseMessageLabel).center().row();
        messagesTable.add(gameOverMessageLabel).center().padTop(10).row();
        messagesTable.add(finalScoreLabel).center().padTop(5).row();
        messagesTable.add(restartMessageLabel).center().padTop(5).row();
        messagesTable.setVisible(false);

        uiTableRoot.add(statsTable).expandX().top().left(); // Stats table at top-left
        uiTableRoot.row(); // New row
        uiTableRoot.add(messagesTable).expand().fill().center(); // Messages table takes remaining space, centered


        lastEnemySpawnTime = TimeUtils.millis();

    }

    private void handleInput(float delta) {
        if (gameOver) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                main.setScreen(new GameScreen(main));
                dispose();
                return;
            }
        }

        if(Gdx.input.isKeyJustPressed(ControlsManager.getKeyForAction(GameAction.PAUSE)) ||
            Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isPaused = !isPaused;
        }

        if(isPaused) {
            if(player != null) player.setMoving(false);
            return;
        }

        if(player == null)
            return;

        boolean movedX = false;
        boolean movedY = false;
        float moveSpeed = player.getSpeed() * delta;

        if (Gdx.input.isKeyPressed(ControlsManager.getKeyForAction(GameAction.MOVE_UP))) {
            player.position.y += moveSpeed;
            movedY = true;
        }
        if (Gdx.input.isKeyPressed(ControlsManager.getKeyForAction(GameAction.MOVE_DOWN))) {
            player.position.y -= moveSpeed;
            movedY = true;
        }
        if (Gdx.input.isKeyPressed(ControlsManager.getKeyForAction(GameAction.MOVE_LEFT))) {
            player.position.x -= moveSpeed;
            movedX = true;
        }
        if (Gdx.input.isKeyPressed(ControlsManager.getKeyForAction(GameAction.MOVE_RIGHT))) {
            player.position.x += moveSpeed;
            movedX = true;
        }

        player.isMoving = movedX || movedY;

        Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(mousePos);

        float playerCenterX = player.position.x + playerTexture.getWidth() / 2f;
        float playerCenterY = player.position.y + playerTexture.getHeight() / 2f;

        float angleRadians = MathUtils.atan2(mousePos.y - playerCenterY, mousePos.x - playerCenterX);
        float angleDegrees = angleRadians * MathUtils.radiansToDegrees;

        player.setAimAngle(angleDegrees);

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {

            if (TimeUtils.millis() - player.lastShotTime > player.shootCooldown || true) {
                spawnBullet();
                player.lastShotTime = TimeUtils.millis();
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            player.health = player.maxHealth;
        }
    }

    private void spawnBullet() {
        if (player == null) return;
        Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(mousePos);

        float playerCenterX = player.position.x + player.getWidth() / 2f;
        float playerCenterY = player.position.y + player.getHeight() / 2f;

        float angleRad = MathUtils.atan2(mousePos.y - playerCenterY, mousePos.x - playerCenterX);
        Bullet bullet = new Bullet(bulletTexture, new Vector2(playerCenterX, playerCenterY), angleRad);
        bullets.add(bullet);
    }

    private void spawnEnemy() {
        float spawnX = 0, spawnY = 0;
        int edge = MathUtils.random(3);
        float camX = camera.position.x;
        float camY = camera.position.y;
        float halfViewWidth = viewport.getWorldWidth() / 2f;
        float halfViewHeight = viewport.getWorldHeight() / 2f;
        float spawnOffset = 50f;

        switch (edge) {
            case 0: spawnX = MathUtils.random(camX - halfViewWidth, camX + halfViewWidth); spawnY = camY + halfViewHeight + spawnOffset; break;
            case 1: spawnX = MathUtils.random(camX - halfViewWidth, camX + halfViewWidth); spawnY = camY - halfViewHeight - spawnOffset; break;
            case 2: spawnX = camX - halfViewWidth - spawnOffset; spawnY = MathUtils.random(camY - halfViewHeight, camY + halfViewHeight); break;
            case 3: spawnX = camX + halfViewWidth + spawnOffset; spawnY = MathUtils.random(camY - halfViewHeight, camY + halfViewHeight); break;
        }

        Enemy enemy = new Enemy(enemyAnim, new Vector2(spawnX, spawnY));
        enemies.add(enemy);
        lastEnemySpawnTime = TimeUtils.millis();
        enemySpawnInterval *= 0.99f;
    }

    private void spawnXPOrb(Vector2 position) {
        XPOrb orb = new XPOrb(xpOrbTexture, new Vector2(position.x, position.y));
        xpOrbs.add(orb);
    }

    private void updateGameLogic(float delta) {
        if (isPaused || gameOver) return;


        gameTimer += delta;
        if(gameTimer > MAX_GAME_TIME) {
            gameOver = true;
            return;
        }
        if(player != null) player.update(delta);

        for(int i = bullets.size - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            bullet.update(delta);
            if(bullet.isOffScreen(camera, viewport)) {
                bullets.removeIndex(i);
            }
        }

        for (Enemy enemy : enemies) {
            if (player != null) {
                Vector2 playerCenter = new Vector2(
                    player.position.x + player.getWidth() / 2f,
                    player.position.y + player.getHeight() / 2f
                );
                enemy.update(delta, playerCenter);
            }
        }

        if (TimeUtils.millis() - lastEnemySpawnTime > enemySpawnInterval) {
            spawnEnemy();
        }

        for (int i = xpOrbs.size - 1; i >= 0; i--) {
            xpOrbs.get(i).update(delta);
        }
        if (player != null) checkCollisions();
    }

    private void checkCollisions() {
        if(player == null) return;
        Rectangle playerBounds = player.getBounds();

        for (int i = bullets.size - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            Rectangle bulletBounds = bullet.getBounds();
            for (int j = enemies.size - 1; j >= 0 ; j--) {
                Enemy enemy = enemies.get(j);
                if(bulletBounds.overlaps(enemy.getBounds())) {
                    bullets.removeIndex(i);
                    enemy.takeDamage(player.damage);
                    if(enemy.health <= 0) {
                        Vector2 enemyPosition = new Vector2(enemies.get(j).position);
                        enemies.removeIndex(j);
                        score += 10;
                        spawnXPOrb(enemyPosition);
                    }
                    break;
                }
            }
        }

        for(int i = enemies.size - 1; i >= 0; i--) {
            Enemy enemy = enemies.get(i);
            if(playerBounds.overlaps(enemy.getBounds())) {
                if(TimeUtils.millis() - player.lastHitTime > player.invincibilityDuration) {
                    player.takeDamage(enemy.damage);
                    if(player.health <= 0) {
                        gameOver = true;
                        return;
                    }
                }
            }
        }

        for (int i = xpOrbs.size - 1; i >= 0; i--) {
            XPOrb orb = xpOrbs.get(i);
            if(playerBounds.overlaps(orb.getBounds())) {
                player.gainXP(orb.xpValue);
                xpOrbs.removeIndex(i);
                if(player.xp >= player.xpToNextLevel) {
                    player.levelUp();
                }
            }
        }
    }


    private void updateUILabels() {
        if (player == null || scoreLabel == null) return; // Ensure UI elements are initialized

        scoreLabel.setText("Score: " + score);
        timeLabel.setText(String.format("Time: %02d:%02d", (int)(gameTimer / 60), (int)(gameTimer % 60)));
        healthLabel.setText("Health: " + player.health + "/" + player.maxHealth);
        levelLabel.setText("Level: " + player.level);
        xpLabel.setText("XP: " + player.xp + "/" + player.xpToNextLevel);

        if (isPaused && !gameOver) {
            messagesTable.setVisible(true);
            pauseMessageLabel.setVisible(true);
            gameOverMessageLabel.setVisible(false);
            finalScoreLabel.setVisible(false);
            restartMessageLabel.setVisible(false);
        } else if (gameOver) {
            messagesTable.setVisible(true);
            pauseMessageLabel.setVisible(false);
            String outcome = (gameTimer >= MAX_GAME_TIME) ? "YOU SURVIVED!" : "GAME OVER";
            gameOverMessageLabel.setText(outcome);
            gameOverMessageLabel.setVisible(true);
            finalScoreLabel.setText("Final Score: " + score);
            finalScoreLabel.setVisible(true);
            restartMessageLabel.setVisible(true);
        } else {
            messagesTable.setVisible(false);
        }
    }

    @Override
    public void render(float delta) {
        handleInput(delta);
        if(!isPaused && !gameOver) {
            updateGameLogic(delta);
        }

        if(player != null) {
            camera.position.set(
                player.position.x + player.getWidth() / 2f,
                player.position.y + player.getHeight() / 2f,
                0
            );
        }
        camera.update();

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        batch.begin();

        //Background drawing
        if(backgroundTexture != null) {
            float tileWidth = backgroundTexture.getWidth();
            float tileHeight = backgroundTexture.getHeight();

            if(tileWidth > 0 && tileHeight > 0) {
                float viewLeft = camera.position.x - viewport.getWorldWidth() / 2f;
                float viewBottom = camera.position.y - viewport.getWorldHeight() / 2f;
                float viewRight = camera.position.x + viewport.getWorldWidth() / 2f;
                float viewTop = camera.position.y + viewport.getWorldHeight() / 2f;

                float startX = MathUtils.floor(viewLeft / tileWidth) * tileWidth;
                float startY = MathUtils.floor(viewBottom / tileHeight) * tileHeight;

                for (float y = startY; y < viewTop; y += tileHeight) {
                    for (float x = startX; x < viewRight; x += tileWidth) {
                        batch.draw(backgroundTexture, x, y, tileWidth, tileHeight);
                    }
                }
            } else {
                batch.draw(backgroundTexture,
                    camera.position.x - viewport.getWorldWidth() / 2f,
                    camera.position.y - viewport.getWorldHeight() / 2f,
                    viewport.getWorldWidth(),
                    viewport.getWorldHeight());
            }
        }
        //--------------------------


        for (XPOrb orb : xpOrbs) orb.draw(batch);
        for (Enemy enemy : enemies) enemy.draw(batch);
        if(player != null) player.draw(batch);
        for(Bullet bullet : bullets) bullet.draw(batch);
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if (player != null) player.drawHealthBar(shapeRenderer);
        for (Enemy enemy : enemies) enemy.drawHealthBar(shapeRenderer);
        shapeRenderer.end();
//
//        batch.begin();
//        font.draw(batch, "Score: " + score, 20, viewport.getScreenHeight() - 20);
//        font.draw(batch, String.format("Time: %02d:%02d", (int)(gameTimer / 60), (int)(gameTimer % 60)), viewport.getScreenWidth() - 150, viewport.getScreenHeight() - 20);
//        if (player != null) {
//            font.draw(batch, "Level: " + player.level + " XP: " + player.xp + "/" + player.xpToNextLevel, 20, viewport.getScreenHeight() - 50);
//            font.draw(batch, "Health: " + player.health + "/" + player.maxHealth, 20, viewport.getScreenHeight() - 80);
//        }
//
//        if (isPaused && !gameOver) {
//            font.draw(batch, "PAUSED (Press " + ControlsManager.getKeyNameForAction(GameAction.PAUSE) + " or ESC to resume)", viewport.getScreenWidth() / 2f - 200, viewport.getScreenHeight() / 2f);
//        }
//        if (gameOver) {
//            String outcome = (gameTimer >= MAX_GAME_TIME) ? "YOU SURVIVED!" : "GAME OVER";
//            font.draw(batch, outcome, viewport.getScreenWidth() / 2f - 100, viewport.getScreenHeight() / 2f + 50);
//            font.draw(batch, "Final Score: " + score, viewport.getScreenWidth() / 2f - 100, viewport.getScreenHeight() / 2f);
//            font.draw(batch, "Press R to Restart", viewport.getScreenWidth() / 2f - 100, viewport.getScreenHeight() / 2f - 50);
//            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {//TODO
//                main.setScreen(new GameScreen(main));
//                dispose();
//            }
//        }
//        batch.end();

        // Render UI
        updateUILabels();
        uiStage.getViewport().apply();
        uiStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        uiStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        if (uiStage != null) {
            uiStage.getViewport().update(width, height, true);
        }
    }

    @Override
    public void resume() {
        ControlsManager.refreshControls();
    }

    @Override
    public void dispose() {
        enemies.clear();
        bullets.clear();
        xpOrbs.clear();
    }
}

