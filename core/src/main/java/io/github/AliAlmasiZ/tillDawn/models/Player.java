package io.github.AliAlmasiZ.tillDawn.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

public class Player {
    public Vector2 position;
    public float speed = 200f;
    public int health = 100;
    public int maxHealth = 100;
    public int damage = 10;

    private Sprite sprite;
    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> idleAnimation;
    private float stateTime = 0f;
    public boolean isMoving = false;
    private float aimAngleDegrees = 0f;
    public static final float PLAYER_SCALE = 1.5f;

    public Rectangle bounds;
//////////////////////////////////////////////////////////
    public float rotation;
//////////////////////////////////////////////////////////

    public int level = 1;
    public int xp = 0;
    public int xpToNextLevel = 100;

    public long lastShotTime = 0;
    public float shootCooldown = 300;

    public long lastHitTime = 0;
    public long invincibilityDuration = 1000;


    public Player() {
        position = new Vector2();

        this.idleAnimation = GameAssetManager.getGameAssetManager().getCharacterIdleAnim(); // Get idle animation
        this.walkAnimation = GameAssetManager.getGameAssetManager().getCharacterRunAnim();

        TextureRegion textureRegion = this.idleAnimation.getKeyFrame(0);
        this.sprite = new Sprite();
        this.sprite.setOriginCenter();
        this.sprite.setSize(textureRegion.getRegionWidth() * 3, textureRegion.getRegionHeight() * 3);
//        this.sprite.setScale(PLAYER_SCALE);
        this.bounds = new Rectangle(); // Will be set in update()

    }

    public void update(float delta) {
        stateTime += delta; // Increment stateTime for both animations
        TextureRegion currentRegion = null;

        if (isMoving && walkAnimation != null) {
            currentRegion = walkAnimation.getKeyFrame(stateTime, true);
        } else if (idleAnimation != null) { // If not moving, use idle animation
            currentRegion = idleAnimation.getKeyFrame(stateTime, true);
        } else if (walkAnimation != null) { // Fallback if only walk animation exists
            currentRegion = walkAnimation.getKeyFrame(stateTime, true);
        }
        // Else, currentRegion remains null if no animations are loaded.

        if (currentRegion != null) {
            // Check if the region has changed before setting it, to potentially optimize
            boolean regionChanged = sprite.getTexture() != currentRegion.getTexture() ||
                sprite.getRegionX() != currentRegion.getRegionX() ||
                sprite.getRegionY() != currentRegion.getRegionY() ||
                sprite.getRegionWidth() != currentRegion.getRegionWidth() ||
                sprite.getRegionHeight() != currentRegion.getRegionHeight();
            if(regionChanged) {
                sprite.setRegion(currentRegion);
                sprite.setSize(currentRegion.getRegionWidth() * 3, currentRegion.getRegionHeight() * 3);
                sprite.setOriginCenter();
            }
        } else {
            // Handle case where no frame could be determined (e.g., assets not loaded)
            // Gdx.app.debug("Player", "No current animation frame to set for sprite.");
        }

        sprite.setPosition(position.x, position.y);
        sprite.setRotation(aimAngleDegrees);

        bounds.set(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    public void draw(SpriteBatch batch) {
        if (TimeUtils.millis() - lastHitTime < invincibilityDuration) {
            if ((TimeUtils.millis() / 100) % 2 == 0) {
                return;
            }
        }
        if (sprite != null) { // Ensure sprite is not null before drawing
            sprite.draw(batch);
        }
    }

    public float getWidth() {
        return sprite != null ? sprite.getWidth() : 0;
    }

    public float getHeight() {
        return sprite != null ? sprite.getHeight() : 0;
    }

    public void drawHealthBar(ShapeRenderer shapeRenderer) {
        if (sprite == null) return; // Don't draw if sprite isn't initialized
        float barWidth = getWidth();
        float barHeight = 10;
        float barX = position.x;
        float barY = position.y + getHeight() + 5;

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(barX, barY, barWidth, barHeight);

        float healthPercentage = (float) health / maxHealth;
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(barX, barY, barWidth * healthPercentage, barHeight);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void takeDamage(int amount) {
        if (TimeUtils.millis() - lastHitTime > invincibilityDuration) {
            this.health -= amount;
            if (this.health < 0) {
                this.health = 0;
            }
            this.lastHitTime = TimeUtils.millis();
        }
    }

    public void gainXP(int amount) {
        this.xp += amount;
    }

    public void levelUp() {
        this.level++;
        this.xp = 0;
        this.xpToNextLevel = (int) (this.xpToNextLevel * 1.5f);
        this.maxHealth += 20;
        this.health = this.maxHealth;
        this.damage += 2;
        this.speed += 10f;
        this.shootCooldown = Math.max(100, this.shootCooldown - 20);
    }


    public void setAimAngle(float degrees) {
        this.aimAngleDegrees = degrees;
    }
    public void updateStateTime(float delta) {
        stateTime += delta;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        this.isMoving = moving;
    }

    public float getStateTime() {
        return stateTime;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getSpeed() {
        return speed;
    }

    public float getRotation() {
        return rotation;
    }
}
