package io.github.AliAlmasiZ.tillDawn.views;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.AliAlmasiZ.tillDawn.models.GameAssetManager;
import io.github.AliAlmasiZ.tillDawn.models.Player;

public class PlayerView {
    private Sprite currentFrame;
    private GameAssetManager assets;

    public PlayerView() {
        assets = GameAssetManager.getGameAssetManager();
        currentFrame = new Sprite(assets.characterIdleAnim.getKeyFrame(0));
        currentFrame.setOriginCenter();
    }

    public void update(Player player, float delta) {
        Animation<TextureRegion> currentAnim = player.isMoving() ?
            assets.characterRunAnim : assets.characterIdleAnim;

        currentFrame.setRegion(currentAnim.getKeyFrame(player.getStateTime()));
        currentFrame.setPosition(
            player.getPosition().x - currentFrame.getWidth() / 2,
            player.getPosition().y - currentFrame.getWidth() / 2
        );
        currentFrame.setRotation(player.getRotation());
    }

    public void draw(SpriteBatch batch) {
        currentFrame.draw(batch);
    }
}
