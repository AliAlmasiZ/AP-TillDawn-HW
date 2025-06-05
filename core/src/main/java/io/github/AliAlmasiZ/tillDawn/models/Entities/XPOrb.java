package io.github.AliAlmasiZ.tillDawn.models.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class XPOrb {
    public Vector2 position;
    public Sprite sprite;
    public Rectangle bounds;
    public int xpValue = 3;

    public XPOrb(Texture texture, Vector2 startPosition) {
        this.sprite = new Sprite(texture);

        position = new Vector2(startPosition);
        this.sprite.setPosition(position.x, position.y);
        this.sprite.setOriginCenter();

        this.bounds = new Rectangle(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    public void update(float delta) {
        sprite.setPosition(position.x, position.y); // Sync sprite if position is changed externally
        bounds.setPosition(sprite.getX(), sprite.getY());
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

    public Rectangle getBounds() {
        return bounds;
    }
}
