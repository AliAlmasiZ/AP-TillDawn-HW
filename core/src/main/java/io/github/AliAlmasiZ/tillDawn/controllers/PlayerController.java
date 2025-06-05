package io.github.AliAlmasiZ.tillDawn.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.AliAlmasiZ.tillDawn.models.Player;

public class PlayerController {
    private Player player;
    private Viewport viewport;


    public PlayerController(Player player, Viewport viewport) {
        this.player = player;
        this.viewport = viewport;
    }

    public void handleInput(float delta) {
        Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(mousePos);


    }
}
