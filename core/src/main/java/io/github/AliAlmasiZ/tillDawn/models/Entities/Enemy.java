package io.github.AliAlmasiZ.tillDawn.models.Entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import io.github.AliAlmasiZ.tillDawn.models.enums.EnemyType;
import io.github.AliAlmasiZ.tillDawn.views.screens.GameScreen;

public class Enemy {
    public Vector2 position;
    public float speed;
    public int health;
    public int maxHealth;
    public int damage;
    private Sprite sprite;
    public Rectangle bounds;
    public EnemyType type;

    // For Eyebat shooting
    private long lastShotTimeEyebat;
    private static final float EYEBAT_SHOOT_COOLDOWN = 3000; // 3 seconds in milliseconds
    public static final int EYEBAT_BULLET_DAMAGE = 1;

    // For Elder Boss dashing
    private long lastDashTimeElder;
    private static final float ELDER_DASH_COOLDOWN = 5000; // 5 seconds
    private static final float ELDER_DASH_SPEED_MULTIPLIER = 3.0f;
    private boolean isDashingElder = false;
    private Vector2 dashTargetPositionElder;
    private static final float ELDER_DASH_DURATION = 0.5f; // How long the dash itself lasts
    private float dashTimerElder = 0f;

    private Animation<TextureRegion> animation;
    private float stateTime = 0f;

    public Enemy(EnemyType type, Animation<TextureRegion> animation, Vector2 startPosition) {
        this.type = type;
        this.animation = animation;
        position = new Vector2(startPosition);

        this.sprite = new Sprite(animation.getKeyFrame(0));
        this.sprite.setPosition(position.x, position.y);
        this.sprite.setOriginCenter();

        switch (type) {
            case TENTACLE_MONSTER :
                this.maxHealth = 25;
                this.speed = 80f;
                this.damage = 10;
                break;
            case EYEBAT :
                this.maxHealth = 50;
                this.speed = 60f;
                this.damage = 5;
                this.lastShotTimeEyebat = TimeUtils.millis();
                break;
            case ELDER_BOSS :
                this.maxHealth = 400;
                this.speed = 50f;
                this.damage = 20;
                this.lastDashTimeElder = TimeUtils.millis();
                this.dashTargetPositionElder = new Vector2();
                break;
        }
        this.health = this.maxHealth;
        this.bounds = new Rectangle(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    public void update(float delta, Vector2 playerCenterPosition, GameScreen gameScreen) {
        stateTime += delta;
        TextureRegion currentRegion = animation.getKeyFrame(stateTime, true);

        sprite.setRegion(currentRegion);
        sprite.setSize(currentRegion.getRegionWidth(), currentRegion.getRegionHeight());
        sprite.setOriginCenter();

        float currentSpeed = this.speed;

        switch (type) {
            case TENTACLE_MONSTER :

                break;
            case EYEBAT:
                if (TimeUtils.millis() - lastShotTimeEyebat > EYEBAT_SHOOT_COOLDOWN) {
                    shootAtPlayer(playerCenterPosition, gameScreen);
                    lastShotTimeEyebat = TimeUtils.millis();
                }
                break;
            case ELDER_BOSS:
                if (isDashingElder) {
                    currentSpeed = speed * ELDER_DASH_SPEED_MULTIPLIER;
                    dashTimerElder += delta;
                    if (dashTimerElder >= ELDER_DASH_DURATION || position.dst(dashTargetPositionElder) < 10f) {
                        isDashingElder = false;
                        dashTimerElder = 0f;
                        lastDashTimeElder = TimeUtils.millis(); // Start cooldown after dash ends
                    }
                } else if (TimeUtils.millis() - lastDashTimeElder > ELDER_DASH_COOLDOWN) {
                    isDashingElder = true;
                    dashTargetPositionElder.set(playerCenterPosition); // Target current player pos
                    dashTimerElder = 0f; // Reset dash timer
                }
                break;
        }

        Vector2 targetPos = isDashingElder ? dashTargetPositionElder : playerCenterPosition;
        float angle = MathUtils.atan2(
            playerCenterPosition.y - (position.y + getHeight() / 2f),
            playerCenterPosition.x - (position.x + getWidth() / 2f)
        );
        position.x += MathUtils.cos(angle) * speed * delta;
        position.y += MathUtils.sin(angle) * speed * delta;

        sprite.setPosition(position.x, position.y);
        bounds.set(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());

    }

    private void shootAtPlayer(Vector2 playerCenterPosition, GameScreen gameScreen) {
        if (gameScreen == null) return; // Safety check

        float enemyCenterX = position.x + getWidth() / 2f;
        float enemyCenterY = position.y + getHeight() / 2f;
        float angleRad = MathUtils.atan2(playerCenterPosition.y - enemyCenterY, playerCenterPosition.x - enemyCenterX);


        gameScreen.spawnEnemyBullet(new Vector2(enemyCenterX, enemyCenterY), angleRad, EYEBAT_BULLET_DAMAGE);
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch); // Draw the sprite
    }

    public float getWidth() {
        return sprite.getWidth();
    }

    public float getHeight() {
        return sprite.getHeight();
    }

    public void drawHealthBar(ShapeRenderer shapeRenderer) {
        if (health < maxHealth) {
            float barWidth = getWidth();
            float barHeight = 5;
            float barX = position.x;
            float barY = position.y + getHeight() + 2;

            shapeRenderer.setColor(Color.DARK_GRAY);
            shapeRenderer.rect(barX, barY, barWidth, barHeight);

            float healthPercentage = (float) health / maxHealth;
            shapeRenderer.setColor(Color.ORANGE);
            shapeRenderer.rect(barX, barY, barWidth * healthPercentage, barHeight);
        }
    }


    public Rectangle getBounds() {
        return bounds;
    }

    public void takeDamage(int amount) {
        this.health -= amount;
        if (this.health < 0) {
            this.health = 0;
        }
    }
}
