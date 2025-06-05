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

public class Enemy {
    public Vector2 position;
    public float speed = 100f;
    public int health = 30;
    public int maxHealth = 30;
    public int damage = 10;
    private Sprite sprite;
    public Rectangle bounds;
    private float stateTime = 0f;
    private Animation<TextureRegion> animation;

    public Enemy(Animation<TextureRegion> animation, Vector2 startPosition) {
        this.animation = animation;
        this.sprite = new Sprite(animation.getKeyFrame(0));
        position = new Vector2(startPosition);
        this.sprite.setPosition(position.x, position.y);

        this.sprite.setOriginCenter();

        this.bounds = new Rectangle(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    public void update(float delta, Vector2 playerCenterPosition) {
        stateTime += delta;
        TextureRegion currentRegion = animation.getKeyFrame(stateTime, true);

        sprite.setRegion(currentRegion);
        sprite.setSize(currentRegion.getRegionWidth(), currentRegion.getRegionHeight());
        sprite.setOriginCenter();

        float angle = MathUtils.atan2(
            playerCenterPosition.y - (position.y + getHeight() / 2f),
            playerCenterPosition.x - (position.x + getWidth() / 2f)
        );
        position.x += MathUtils.cos(angle) * speed * delta;
        position.y += MathUtils.sin(angle) * speed * delta;

        sprite.setPosition(position.x, position.y);
        bounds.set(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());

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
