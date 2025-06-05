package io.github.AliAlmasiZ.tillDawn.models.DataBase;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import io.github.AliAlmasiZ.tillDawn.models.Player;
import io.github.AliAlmasiZ.tillDawn.models.User;

public class JsonSaver {
    private JsonSaver instance;
    private final Json json = new Json();


    private JsonSaver() {}
    public JsonSaver getInstance() {
        if (instance == null)
            instance = new JsonSaver();
        return instance;
    }

    public void savePlayer(Player player, User user) {
        String jsonText = json.toJson(player);
        Gdx.files.local("player" + user.getId());
    }

    public void loadPlayer(User user) {

    }



}
