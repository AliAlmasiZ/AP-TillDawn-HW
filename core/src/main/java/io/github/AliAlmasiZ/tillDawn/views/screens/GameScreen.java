package io.github.AliAlmasiZ.tillDawn.views.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
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
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.AliAlmasiZ.tillDawn.Main;
import io.github.AliAlmasiZ.tillDawn.controllers.utils.ControlsManager;
import io.github.AliAlmasiZ.tillDawn.models.Entities.Bullet;
import io.github.AliAlmasiZ.tillDawn.models.Entities.Enemy;
import io.github.AliAlmasiZ.tillDawn.models.Entities.Tree;
import io.github.AliAlmasiZ.tillDawn.models.Entities.XPOrb;
import io.github.AliAlmasiZ.tillDawn.models.GameAssetManager;
import io.github.AliAlmasiZ.tillDawn.models.Player;
import io.github.AliAlmasiZ.tillDawn.models.enums.AbilityType;
import io.github.AliAlmasiZ.tillDawn.models.enums.EnemyType;
import io.github.AliAlmasiZ.tillDawn.models.enums.GameAction;
import io.github.AliAlmasiZ.tillDawn.models.enums.WeaponType;

import java.util.HashSet;


public class GameScreen extends ScreenAdapter {
    private final Main main;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;

    private Player player;

    private Texture treeTexture;
    private Animation<TextureRegion> tentacleMonsterAnim;
    private Animation<TextureRegion> eyebatAnim;
    private Animation<TextureRegion> elderBossAnim;



//    private Texture playerTexture;
//    private Animation<TextureRegion> enemyAnim;
    private Texture playerBulletTexture;
    private Texture enemyBulletTexture;
    private Texture xpOrbTexture;
    private Texture backgroundTexture;

    Array<Tree> trees;
    private Array<Enemy> enemies;
    private Array<Bullet> playerBullets;
    private Array<Bullet> enemyBullets;
    private Array<XPOrb> xpOrbs;

    private HashSet<Long> populatedTreeCells;
    private static final int TREE_CELL_SIZE = 1600;

    // Spawning timers
//    private long lastEnemySpawnTime;
    private long lastTentacleSpawnTime;
    private float tentacleMonsterSpawnRate = 3000;
    private static final float TENTACLE_MONSTER_SPAWN_DELAY = 2000;

    private long lastEyebatSpawnTime;
    private float eyebatSpawnRate = 10000;
    private boolean canSpawnEyebats = false;

    private boolean elderBossHasSpawned = false;


//    private float enemySpawnInterval = 2000;

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
    private Label weaponLabel;
    private Label ammoLabel;
    private Label pauseMessageLabel;
    private Label gameOverMessageLabel;
    private Label finalScoreLabel;
    private Label restartMessageLabel;
    private Table messagesTable;
    private Label autoAimStatusLabel;


    private boolean autoAimEnabled = false;

    private Pixmap cursorPixmap;
    private Cursor customCursor;


    private static final float GAME_WORLD_WIDTH = 1920;
    private static final float GAME_WORLD_HEIGHT = 1080;


    public GameScreen(Main main) {
        this.main = main;
        batch = main.batch;
    }

    @Override
    public void show() {
        ControlsManager.refreshControls();

//        viewport = new FitViewport(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT, camera);
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
        camera.setToOrtho(false, GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT);
        Gdx.input.setCursorCatched(false);



        treeTexture = GameAssetManager.getGameAssetManager().treeTex;
        tentacleMonsterAnim = GameAssetManager.getGameAssetManager().brainMonsterAnim;
        eyebatAnim = GameAssetManager.getGameAssetManager().eyebatMonsterAnim;
        elderBossAnim = GameAssetManager.getGameAssetManager().elderBossAnim;
        playerBulletTexture = GameAssetManager.getGameAssetManager().bulletTex;
        enemyBulletTexture = GameAssetManager.getGameAssetManager().bulletTex;
        xpOrbTexture = GameAssetManager.getGameAssetManager().xpOrbTex;
        backgroundTexture = GameAssetManager.getGameAssetManager().backgroundTileTex;

//        enemyTexture = new Texture(GameAssetManager.getGameAssetManager().getCharacter1Idle0())

        //TODO : Temp for test
        player = new Player();
//        player = AppData.getAppData().activeUser.getPlayer();

        if (player != null) {
            float pWidth = player.getWidth();
            float pHeight = player.getHeight();
            player.position.set(
                GAME_WORLD_WIDTH / 2f - pWidth / 2f,
                GAME_WORLD_HEIGHT / 2f - pHeight / 2f
            );
            player.update(0);
        }
        trees = new Array<>();
        enemies = new Array<>();
        playerBullets = new Array<>();
        enemyBullets = new Array<>();
        xpOrbs = new Array<>();
        shapeRenderer = new ShapeRenderer();

        populatedTreeCells = new HashSet<>();
        spawnTreesAroundPlayer();


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
        Gdx.input.setInputProcessor(uiStage);

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        scoreLabel = new Label("Score: 0", labelStyle);
        timeLabel = new Label("Time: 00:00", labelStyle);
        healthLabel = new Label("Health: 100/100", labelStyle);
        levelLabel = new Label("Level: 1", labelStyle);
        xpLabel = new Label("XP: 0/100", labelStyle);
        weaponLabel = new Label("Weapon: REVOLVER", labelStyle);
        ammoLabel = new Label("Ammo: 6/6", labelStyle);
        autoAimStatusLabel = new Label("Auto-Aim: OFF", labelStyle);

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
        statsTable.add(weaponLabel).left().padTop(5).row();
        statsTable.add(ammoLabel).left().row();
        statsTable.add(autoAimStatusLabel).left().padTop(5).row();


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


        try {
            cursorPixmap = new Pixmap(Gdx.files.internal("Images/Sprite/T_CursorSprite.png"));
            int xHotSpot = cursorPixmap.getWidth() / 2;
            int yHotSpot = cursorPixmap.getHeight() / 2;

            if (cursorPixmap.getWidth() > 0 && cursorPixmap.getHeight() > 0) {
                customCursor = Gdx.graphics.newCursor(cursorPixmap, xHotSpot, yHotSpot);
                Gdx.graphics.setCursor(customCursor);
            } else {
                Gdx.app.error("GameScreen", "Cursor pixmap is empty or could not be loaded.");
            }
        } catch (Exception e) {
            Gdx.app.error("GameScreen", "Could not load custom cursor: cursors/custom_cursor.png", e);
        }

        if (cursorPixmap != null) {
            cursorPixmap.dispose();
            cursorPixmap = null;
        }

        lastTentacleSpawnTime = TimeUtils.millis() - (long)tentacleMonsterSpawnRate + (long)TENTACLE_MONSTER_SPAWN_DELAY; // Start spawning soon after delay
        lastEyebatSpawnTime = TimeUtils.millis();
        canSpawnEyebats = false;
        elderBossHasSpawned = false;
    }

    private void spawnTreesAroundPlayer() {
        if (player == null || treeTexture == null) return;

        // Determine the player's current grid cell coordinates
        int cellX = (int) (player.position.x / TREE_CELL_SIZE);
        int cellY = (int) (player.position.y / TREE_CELL_SIZE);

        // Check a 3x3 grid of cells around the player
        for (int y = cellY - 1; y <= cellY + 1; y++) {
            for (int x = cellX - 1; x <= cellX + 1; x++) {
                // Create a unique ID for the cell
                long cellId = (long) x << 32 | (y & 0xffffffffL);

                // If this cell has not been populated yet, spawn trees in it
                if (!populatedTreeCells.contains(cellId)) {
                    // Spawn a random number of trees in this cell
                    int treesToSpawn = MathUtils.random(5, 15); // e.g., 5 to 15 trees per cell
                    for (int i = 0; i < treesToSpawn; i++) {
                        float treeX = (x * TREE_CELL_SIZE) + MathUtils.random(0, TREE_CELL_SIZE - treeTexture.getWidth());
                        float treeY = (y * TREE_CELL_SIZE) + MathUtils.random(0, TREE_CELL_SIZE - treeTexture.getHeight());
                        trees.add(new Tree(treeTexture, new Vector2(treeX, treeY)));
                    }
                    // Mark this cell as populated
                    populatedTreeCells.add(cellId);
                    Gdx.app.log("Tree Spawning", "Populated tree cell: (" + x + ", " + y + ")");
                }
            }
        }
    }

    private Enemy findNearestEnemy() {
        if (player == null || enemies.isEmpty()) {
            return null;
        }
        Enemy nearestEnemy = null;
        float minDistanceSq = Float.MAX_VALUE;

        Vector2 playerCenter = new Vector2(
            player.position.x + player.getWidth() / 2f,
            player.position.y + player.getHeight() / 2f
        );

        for (Enemy enemy : enemies) {
            if (enemy == null) continue;
            Vector2 enemyCenter = new Vector2(
                enemy.position.x + enemy.getWidth() / 2f,
                enemy.position.y + enemy.getHeight() / 2f
            );
            float distanceSq = playerCenter.dst2(enemyCenter);
            if (distanceSq < minDistanceSq) {
                minDistanceSq = distanceSq;
                nearestEnemy = enemy;
            }
        }
        return nearestEnemy;
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
        if(Gdx.input.isKeyJustPressed(ControlsManager.getKeyForAction(GameAction.TOGGLE_AUTO_AIM))) {
            autoAimEnabled = !autoAimEnabled;
            Gdx.app.log("GameScreen", "Auto-aim " + (autoAimEnabled ? "enabled" : "disabled"));
        }

        if (player != null && Gdx.input.isKeyJustPressed(ControlsManager.getKeyForAction(GameAction.RELOAD))) {
            player.startReload();
        }

        if (isPaused || gameOver || player == null) {
            if (player != null) player.isMoving = false;
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

//        Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
//        viewport.unproject(mousePos);
//
//        float playerCenterX = player.position.x + playerTexture.getWidth() / 2f;
//        float playerCenterY = player.position.y + playerTexture.getHeight() / 2f;
//
//        float angleRadians = MathUtils.atan2(mousePos.y - playerCenterY, mousePos.x - playerCenterX);
//        float angleDegrees = angleRadians * MathUtils.radiansToDegrees;
//
//        player.setAimAngle(angleDegrees);

        // Aiming Logic
        if (autoAimEnabled) {
            Enemy targetEnemy = findNearestEnemy();
            if (targetEnemy != null) {
                float pCenterX = player.position.x + player.getWidth() / 2f, pCenterY = player.position.y + player.getHeight() / 2f;
                float eCenterX = targetEnemy.position.x + targetEnemy.getWidth() / 2f, eCenterY = targetEnemy.position.y + targetEnemy.getHeight() / 2f;
                player.setAimAngle(MathUtils.atan2(eCenterY - pCenterY, eCenterX - pCenterX) * MathUtils.radiansToDegrees);
                Vector2 enemyScreenPos = viewport.project(new Vector2(eCenterX, eCenterY));
                Gdx.input.setCursorPosition((int) enemyScreenPos.x, Gdx.graphics.getHeight() - (int) enemyScreenPos.y);
            } else {
                manualAim();
            }
        } else {
            manualAim();
        }

//        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
//
//            if (TimeUtils.millis() - player.lastShotTime > player.shootCooldown || true) {
//                spawnBullet();
//                player.lastShotTime = TimeUtils.millis();
//            }
//        }

        if (Gdx.input.isButtonPressed(ControlsManager.getKeyForAction(GameAction.SHOOT))) { // isButtonPressed for continuous fire
            if (player.canShoot() && TimeUtils.millis() - player.lastShotTime > player.shootCooldown) {
                spawnPlayerBullet();
                player.lastShotTime = TimeUtils.millis();
                if (!player.isReloading) player.currentAmmo--; // Decrement ammo if not reloading
                if (player.currentAmmo == 0 && !player.isReloading) {
                    // TODO: auto-reload
                    // player.startReload();
                }
            }
        }


        //Cheats
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            player.health = player.maxHealth;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
            gameTimer += 60;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
            player.levelUp();
            AbilityType[] allAbilities = AbilityType.values();
            player.applyAbility(allAbilities[MathUtils.random(allAbilities.length - 1)]);
        }
    }

    private void manualAim() {
        Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(mousePos);
        float pCenterX = player.position.x + player.getWidth() / 2f;
        float pCenterY = player.position.y + player.getHeight() / 2f;
        player.setAimAngle(MathUtils.atan2(mousePos.y - pCenterY, mousePos.x - pCenterX) * MathUtils.radiansToDegrees);
    }

    private void spawnPlayerBullet() {
        if (player == null) return;
        Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(mousePos);

        float playerCenterX = player.position.x + player.getWidth() / 2f;
        float playerCenterY = player.position.y + player.getHeight() / 2f;

        float baseAngleRad = MathUtils.atan2(mousePos.y - playerCenterY, mousePos.x - playerCenterX);

        int effectiveDamage = player.getEffectiveDamage();

        for (int i = 0; i < player.projectilesPerShot; i++) {
            float currentAngleRad = baseAngleRad;
            if (player.currentWeapon == WeaponType.SHOTGUN && player.projectilesPerShot > 1) {
                float spreadRange = 30f;
                float randomSpread = MathUtils.random(-spreadRange / 2f, spreadRange / 2f);
                currentAngleRad = baseAngleRad + (randomSpread * MathUtils.degreesToRadians);
            }
            // For Dual SMGs, if they are meant to fire from slightly different positions or angles,
            // that logic would go here too, potentially alternating. For now, they fire like revolver.

            playerBullets.add(new Bullet(playerBulletTexture,
                new Vector2(playerCenterX, playerCenterY), currentAngleRad, effectiveDamage, true));
        }
    }


    public void spawnEnemyBullet(Vector2 startPos, float angleRad, int damage) {
        enemyBullets.add(new Bullet(enemyBulletTexture, startPos, angleRad, damage, false));
    }

    private void manageEnemySpawning() {
        // Tentacle Monster Spawning (HP: 25, Spawn Rate: every 3s spawns i/30 monsters)
        if (TimeUtils.millis() - lastTentacleSpawnTime > tentacleMonsterSpawnRate) {
            for(int i = 0; i < gameTimer / 30; i++)
                spawnNewEnemy(EnemyType.TENTACLE_MONSTER);
            lastTentacleSpawnTime = TimeUtils.millis();
        }

        // Eyebat Spawning Logic (HP: 50, Spawn after T/4, then every 10s spawns (4i-T+30) / 30)
        if (!canSpawnEyebats && gameTimer >= MAX_GAME_TIME / 4.0f) {
            canSpawnEyebats = true;
            Gdx.app.log("GameScreen", "Eyebat spawning enabled.");
            lastEyebatSpawnTime = TimeUtils.millis();
        }
        if (canSpawnEyebats && TimeUtils.millis() - lastEyebatSpawnTime > eyebatSpawnRate) {
            for(int i = 0; i < MathUtils.floor((4 * gameTimer - MAX_GAME_TIME + 30) / 30); i++)
                spawnNewEnemy(EnemyType.EYEBAT);
            lastEyebatSpawnTime = TimeUtils.millis();
        }

        // Elder Boss Spawning (HP: 400, Spawns after T/2)
        if (!elderBossHasSpawned && gameTimer >= MAX_GAME_TIME / 2.0f) {
            spawnNewEnemy(EnemyType.ELDER_BOSS);
            elderBossHasSpawned = true;
            Gdx.app.log("GameScreen", "ELDER BOSS HAS SPAWNED!");
        }
    }

    private void spawnNewEnemy(EnemyType type) {
        float spawnX = 0, spawnY = 0;
        int edge = MathUtils.random(3);
        float camX = camera.position.x;
        float camY = camera.position.y;
        float halfViewWidth = viewport.getWorldWidth() / 2f;
        float halfViewHeight = viewport.getWorldHeight() / 2f;
        float spawnOffset = 50f;



        switch (edge) {
            case 0:
                spawnX = MathUtils.random(camX - halfViewWidth, camX + halfViewWidth);
                spawnY = camY + halfViewHeight + spawnOffset;
                break;
            case 1: spawnX = MathUtils.random(camX - halfViewWidth, camX + halfViewWidth);
                spawnY = camY - halfViewHeight - spawnOffset;
                break;
            case 2: spawnX = camX - halfViewWidth - spawnOffset;
                spawnY = MathUtils.random(camY - halfViewHeight, camY + halfViewHeight);
                break;
            case 3: spawnX = camX + halfViewWidth + spawnOffset;
                spawnY = MathUtils.random(camY - halfViewHeight, camY + halfViewHeight);
                break;
        }

        Animation<TextureRegion> animationToUse = GameAssetManager.getGameAssetManager().brainMonsterAnim;

        if(type == EnemyType.EYEBAT) {
            animationToUse = GameAssetManager.getGameAssetManager().eyebatMonsterAnim;
        } else if (type == EnemyType.ELDER_BOSS) {
            animationToUse = GameAssetManager.getGameAssetManager().elderBossAnim;
        } else if (type == EnemyType.TENTACLE_MONSTER) {
            animationToUse = GameAssetManager.getGameAssetManager().brainMonsterAnim;
        }

        Enemy enemy = new Enemy(type, animationToUse, new Vector2(spawnX, spawnY));
        enemies.add(enemy);

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

        spawnTreesAroundPlayer();

        if(player != null) player.update(delta);

        for(int i = playerBullets.size - 1; i >= 0; i--) {
            Bullet bullet = playerBullets.get(i);
            bullet.update(delta);
            if(bullet.isOffScreen(camera, viewport)) {
                playerBullets.removeIndex(i);
            }
        }

        for(int i = enemyBullets.size - 1; i >= 0; i--) {
            Bullet bullet = enemyBullets.get(i);
            bullet.update(delta);
            if(bullet.isOffScreen(camera, viewport)) {
                enemyBullets.removeIndex(i);
            }
        }



        for (Enemy enemy : enemies) {
            if (player != null) {
                Vector2 playerCenter = new Vector2(
                    player.position.x + player.getWidth() / 2f,
                    player.position.y + player.getHeight() / 2f
                );
                enemy.update(delta, playerCenter, this);
            }
        }
        manageEnemySpawning();


        for (int i = xpOrbs.size - 1; i >= 0; i--) {
            xpOrbs.get(i).update(delta);
        }
        if (player != null) checkCollisions();
    }

    private void checkCollisions() {
        if(player == null) return;
        Rectangle playerBounds = player.getBounds();

        for (int i = playerBullets.size - 1; i >= 0; i--) {
            Bullet bullet = playerBullets.get(i);
            if(bullet == null || !bullet.isPlayerBullet) continue;
            Rectangle bulletBounds = bullet.getBounds();
            for (int j = enemies.size - 1; j >= 0 ; j--) {
                Enemy enemy = enemies.get(j);
                if(bulletBounds.overlaps(enemy.getBounds())) {
                    playerBullets.removeIndex(i);
                    enemy.takeDamage(bullet.damage);
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

        for(int i = enemyBullets.size - 1; i >= 0; i--) {
            Bullet bullet = enemyBullets.get(i);
            if(bullet == null || bullet.isPlayerBullet) return;
            if (playerBounds.overlaps(bullet.getBounds())) {
                if (TimeUtils.millis() - player.lastHitTime > player.invincibilityDuration) {
                    player.takeDamage(bullet.damage);
                    enemyBullets.removeIndex(i);
                    if (player.health <= 0) { gameOver = true; return; }
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

        for (Tree tree : trees) {
            if(playerBounds.overlaps(tree.getBounds())) {
                if(TimeUtils.millis() - player.lastHitTime > player.invincibilityDuration) {
                    player.takeDamage(Tree.TREE_DAMAGE);
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
                    AbilityType[] allAbilities = AbilityType.values();
                    player.applyAbility(allAbilities[MathUtils.random(allAbilities.length - 1)]);
                }
            }
        }
    }


    private void updateUILabels() {
        if (player == null || scoreLabel == null) return; // Ensure UI elements are initialized

        scoreLabel.setText("Score: " + score);
        timeLabel.setText(String.format("Remaining Time: %02d:%02d", (int)((MAX_GAME_TIME - gameTimer) / 60), (int)((MAX_GAME_TIME - gameTimer) % 60)));
        healthLabel.setText("Health: " + player.health + "/" + player.maxHealth);
        levelLabel.setText("Level: " + player.level);
        xpLabel.setText("XP: " + player.xp + "/" + player.xpToNextLevel);
        weaponLabel.setText("Weapon: " + player.currentWeapon.name());
        String ammoText = player.isReloading ? "Reloading..." : player.currentAmmo + "/" + player.currentMaxAmmo;
        ammoLabel.setText("Ammo: " + ammoText);
        autoAimStatusLabel.setText("Auto-Aim: " + (autoAimEnabled ? "ON" : "OFF"));


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

        for (Tree tree : trees) tree.draw(batch);
        for (XPOrb orb : xpOrbs) orb.draw(batch);
        for (Enemy enemy : enemies) enemy.draw(batch);
        if(player != null) player.draw(batch);
        for(Bullet bullet : playerBullets) bullet.draw(batch);
        for (Bullet bullet : enemyBullets) bullet.draw(batch);
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if (player != null) player.drawHealthBar(shapeRenderer);
        for (Enemy enemy : enemies) enemy.drawHealthBar(shapeRenderer);
        shapeRenderer.end();

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
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        if (customCursor != null) {
            customCursor.dispose(); // Dispose the custom cursor object
            customCursor = null;
        }

        enemies.clear();
        playerBullets.clear();
        xpOrbs.clear();
    }

    @Override
    public void hide() {
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        if (customCursor != null) {
            customCursor.dispose(); // Dispose the custom cursor object
            customCursor = null;
        }
        Gdx.input.setInputProcessor(null);
    }
}

