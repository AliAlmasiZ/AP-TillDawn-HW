package io.github.AliAlmasiZ.tillDawn.models.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Tree {
    public Vector2 position;
    private Sprite sprite;
    public Rectangle bounds;
    public static final int TREE_DAMAGE = 5;

    public Tree(Texture texture, Vector2 startPosition) {
        this.sprite = new Sprite(texture);
        this.position = new Vector2(startPosition);
        this.sprite.setPosition(position.x, position.y);
        this.sprite.setOriginCenter();

        this.bounds = new Rectangle(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    public void update() {
        sprite.setPosition(position.x, position.y);
        bounds.setPosition(sprite.getX(), sprite.getY());
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
}
