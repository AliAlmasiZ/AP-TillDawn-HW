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
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.TimeUtils;
import io.github.AliAlmasiZ.tillDawn.models.enums.AbilityType;
import io.github.AliAlmasiZ.tillDawn.models.enums.WeaponType;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.Serializable;

@Entity
public class Player implements Serializable {
    private int UserID;
    public Vector2 position;
    public float baseSpeed = 200f;
    public float speed;
    public int health ;
    public int maxHealth = 100;
//    public int damage = 10;

    @Transient
    private Sprite sprite;
    @Transient
    private Animation<TextureRegion> walkAnimation;
    @Transient
    private Animation<TextureRegion> idleAnimation;
    private float stateTime = 0f;
    public boolean isMoving = false;
    private float aimAngleDegrees = 0f;

    @Transient
    public Rectangle bounds;
//////////////////////////////////////////////////////////
    public float rotation;
//////////////////////////////////////////////////////////

    public int level = 1;
    public int xp = 0;
    public int xpToNextLevel = 100;

    public long lastShotTime = 0;
    public float shootCooldown = 200;

    public long lastHitTime = 0;
    public long invincibilityDuration = 1000;

    //Weapon
    public WeaponType currentWeapon;
    public int currentAmmo;
    public int currentMaxAmmo;
    public int baseWeaponDamage;
    public int projectilesPerShot;
    public float weaponReloadTime;
    public boolean isReloading = false;
    private long reloadStartTime;

    //Abilities
    private ObjectSet<AbilityType> permanentAbilities;
    private float damageBoostEndTime = 0;
    private float speedBoostEndTime = 0;
    private static final float BUFF_DURATION = 10000;


    public Player() {
        this.position = new Vector2();
        this.permanentAbilities = new ObjectSet<>();
        this.speed = baseSpeed;

        this.idleAnimation = GameAssetManager.getGameAssetManager().getCharacterIdleAnim(); // Get idle animation
        this.walkAnimation = GameAssetManager.getGameAssetManager().getCharacterRunAnim();

        TextureRegion textureRegion = this.idleAnimation.getKeyFrame(0);
        this.sprite = new Sprite(textureRegion);
        this.sprite.setOriginCenter();
        this.sprite.setSize(textureRegion.getRegionWidth() * 3, textureRegion.getRegionHeight() * 3);

        this.bounds = new Rectangle(); // Will be set in update()

        equipWeapon(WeaponType.REVOLVER); // Start with revolver
        this.health = this.maxHealth;

    }

    public void equipWeapon(WeaponType weaponType) {
        this.currentWeapon = weaponType;
        this.baseWeaponDamage = weaponType.damage;
        this.projectilesPerShot = weaponType.projectilesPerShot;
        this.currentMaxAmmo = weaponType.maxAmmo;
        this.currentAmmo = this.currentMaxAmmo;
        this.weaponReloadTime = weaponType.reloadTimeMillis;
        this.isReloading = false;


        if (permanentAbilities.contains(AbilityType.AMOCREASE)) {
            this.currentMaxAmmo += 5;
            this.currentAmmo = Math.min(this.currentAmmo, this.currentMaxAmmo); // Adjust current ammo if it exceeds new max
        }
//        Gdx.app.log("Player", "Equipped " + weaponType.name() + ". Ammo: " + currentAmmo + "/" + currentMaxAmmo);
    }

    public void startReload() {
        if (!isReloading && currentAmmo < currentMaxAmmo) {
            isReloading = true;
            reloadStartTime = TimeUtils.millis();
            Gdx.app.log("Player", "Reloading " + currentWeapon.name() + "...");
        }
    }

    private void finishReload() {
        currentAmmo = currentMaxAmmo;
        isReloading = false;
        Gdx.app.log("Player", currentWeapon.name() + " reloaded. Ammo: " + currentAmmo + "/" + currentMaxAmmo);
    }

    public boolean canShoot() {
        return !isReloading && currentAmmo > 0;
    }

    public int getEffectiveDamage() {
        float damageMultiplier = 1.0f;
        if (TimeUtils.millis() < damageBoostEndTime) {
            damageMultiplier += 0.25f; // DAMAGER ability: +25%
        }
        return (int)(baseWeaponDamage * damageMultiplier);
    }



    public void update(float delta) {
        stateTime += delta; // Increment stateTime for both animations
        TextureRegion currentRegion = null;


        if(isReloading) {
            if(TimeUtils.millis() - reloadStartTime >= weaponReloadTime) {
                finishReload();
            }
        }

        if (TimeUtils.millis() >= damageBoostEndTime && damageBoostEndTime != 0) {
            damageBoostEndTime = 0;
            Gdx.app.log("Player", "Damage boost expired.");
        }
        if (TimeUtils.millis() >= speedBoostEndTime && speedBoostEndTime != 0) {
            speed = baseSpeed;
            speedBoostEndTime = 0;
            Gdx.app.log("Player", "Speed boost expired. Speed: " + speed);
        } else if (speedBoostEndTime != 0) {
            speed = baseSpeed * 2f;
        } else {
            speed = baseSpeed;
        }


        if (isMoving && walkAnimation != null) {
            currentRegion = walkAnimation.getKeyFrame(stateTime, true);
        } else if (idleAnimation != null) { // If not moving, use idle animation
            currentRegion = idleAnimation.getKeyFrame(stateTime, true);
        } else if (walkAnimation != null) { // Fallback if only walk animation exists
            currentRegion = walkAnimation.getKeyFrame(stateTime, true);
        }

        if (currentRegion != null) {
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
             Gdx.app.debug("Player", "No current animation frame to set for sprite.");
        }

        sprite.setPosition(position.x, position.y);
        sprite.setRotation(aimAngleDegrees);

        bounds.set(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    public void applyAbility(AbilityType ability) {
        Gdx.app.log("Player", "Applying ability: " + ability.name());
        switch (ability) {
            case VITALITY:
                this.maxHealth += 25;
                this.health = Math.min(this.health + 25, this.maxHealth);
                permanentAbilities.add(ability);
                Gdx.app.log("Player", "VITALITY applied. Max HP: " + maxHealth);
                break;
            case DAMAGER:
                damageBoostEndTime = TimeUtils.millis() + BUFF_DURATION;
                Gdx.app.log("Player", "DAMAGER applied. Damage boost active.");
                break;
            case PROCREASE:
                this.projectilesPerShot += 1;
                permanentAbilities.add(ability);
                Gdx.app.log("Player", "PROCREASE applied. Projectiles per shot: " + projectilesPerShot);
                break;
            case AMOCREASE:
                this.currentMaxAmmo += 5;
                if (!isReloading) {
                    this.currentAmmo = Math.min(this.currentAmmo + 5, this.currentMaxAmmo);
                }
                permanentAbilities.add(ability);
                Gdx.app.log("Player", "AMOCREASE applied. Max ammo for " + currentWeapon.name() + ": " + currentMaxAmmo);
                break;
            case SPEEDY:
                this.speed = baseSpeed * 2f;
                speedBoostEndTime = TimeUtils.millis() + BUFF_DURATION;
                Gdx.app.log("Player", "SPEEDY applied. Speed: " + speed);
                break;
        }
    }

    public void draw(SpriteBatch batch) {
        if (TimeUtils.millis() - lastHitTime < invincibilityDuration) {
            if ((TimeUtils.millis() / 100) % 2 == 0) {
                return;
            }
        }
        if (sprite != null) {
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
        this.xp -= this.xpToNextLevel;
        if(this.xp < 0) this.xp = 0;
        this.xpToNextLevel = (int) (this.xpToNextLevel * 1.5f);
        //ability handle in gameScreen
    }


    public void setAimAngle(float degrees) {
        this.aimAngleDegrees = degrees;
    }

    public float getAimAngleDegrees() {
        return this.aimAngleDegrees;
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

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }
}
