package io.github.AliAlmasiZ.tillDawn.models.Entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Bullet {
    public Vector2 position;
    public float speed = 500f;
    public Sprite sprite;
    public Rectangle bounds;
    private Vector2 velocity;
    public int damage;
    public boolean isPlayerBullet;



    public Bullet(Texture texture, Vector2 startPosition, float angleRad, int damage, boolean isPlayerBullet) {
        this.sprite = new Sprite(texture);
        this.sprite.setSize(20, 20);
        this.position = new Vector2(startPosition.x - sprite.getWidth() / 2f,
            startPosition.y - sprite.getHeight() / 2f);
        this.sprite.setPosition(position.x, position.y);
        this.sprite.setOriginCenter();
        this.sprite.setRotation(angleRad * MathUtils.radiansToDegrees);

        this.damage = damage;
        this.isPlayerBullet = isPlayerBullet;

        this.bounds = new Rectangle();
        this.velocity = new Vector2(MathUtils.cos(angleRad) * speed, MathUtils.sin(angleRad) * speed);
        updateBounds();
    }

    private void updateBounds() {
        bounds.set(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    public void update(float delta) {
        position.mulAdd(velocity, delta);
        sprite.setPosition(position.x, position.y);
        updateBounds();

    }


    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public float getWidth() {
        return sprite.getWidth();
    }

    public float getHeight() {
        return sprite.getHeight();
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isOffScreen(OrthographicCamera camera, Viewport viewport) {
        float camX = camera.position.x;
        float camY = camera.position.y;
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        // Use sprite's bounding box for a slightly more generous off-screen check with rotation
        Rectangle spriteBounds = sprite.getBoundingRectangle(); // Gets AABB of the rotated sprite
        float margin = 0; // spriteBounds already accounts for rotation for AABB

        return spriteBounds.x + spriteBounds.width < camX - worldWidth / 2f - margin ||
            spriteBounds.x > camX + worldWidth / 2f + margin ||
            spriteBounds.y + spriteBounds.height < camY - worldHeight / 2f - margin ||
            spriteBounds.y > camY + worldHeight / 2f + margin;
    }
}
