package io.github.AliAlmasiZ.tillDawn.controllers.utils;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.ObjectMap;
import io.github.AliAlmasiZ.tillDawn.models.enums.GameAction;


public class ControlsManager {
    private static final String PREFS_NAME = "GameControls";
    private static final Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);

    private static final ObjectMap<GameAction, Integer> keyMappings = new ObjectMap<>();

    static {
        loadControls();
    }

    public static void loadControls() {
        keyMappings.clear();

        keyMappings.put(GameAction.MOVE_UP, prefs.getInteger(GameAction.MOVE_UP.name(), Input.Keys.W));
        keyMappings.put(GameAction.MOVE_DOWN, prefs.getInteger(GameAction.MOVE_DOWN.name(), Input.Keys.S));
        keyMappings.put(GameAction.MOVE_LEFT, prefs.getInteger(GameAction.MOVE_LEFT.name(), Input.Keys.A));
        keyMappings.put(GameAction.MOVE_RIGHT, prefs.getInteger(GameAction.MOVE_RIGHT.name(), Input.Keys.D));
        keyMappings.put(GameAction.PAUSE, prefs.getInteger(GameAction.PAUSE.name(), Input.Keys.P));
        keyMappings.put(GameAction.SHOOT, prefs.getInteger(GameAction.SHOOT.name(), Input.Buttons.LEFT));
        keyMappings.put(GameAction.TOGGLE_AUTO_AIM, prefs.getInteger(GameAction.TOGGLE_AUTO_AIM.name(), Input.Keys.SPACE));
        keyMappings.put(GameAction.RELOAD, prefs.getInteger(GameAction.RELOAD.name(), Input.Keys.R));
    }

    public static void saveControls() {
        for (GameAction action : keyMappings.keys()) {
            prefs.putInteger(action.name(), keyMappings.get(action));
        }
        prefs.flush();
        Gdx.app.log("ControlsManager", "Controls saved.");
    }

    public static void setKeyForAction(GameAction action, int keyCode) {
        //TODO: check conflicts
        keyMappings.put(action, keyCode);
        Gdx.app.log("ControlsManager", "Set " + action.name() + " to keycode: " + keyCode);
    }

    public static int getKeyForAction(GameAction action) {
        Integer keyCode = keyMappings.get(action);
        if (keyCode == null) {
            Gdx.app.error("ControlsManager", "No key mapping found for action: " + action.name() + ". Returning -1 (unmapped).");
            return -1;
        }
        return keyCode;
    }

    public static String getKeyNameForAction(GameAction action) {
        return Input.Keys.toString(getKeyForAction(action));
    }

    public static void resetToDefaults() {
        keyMappings.clear();
        keyMappings.put(GameAction.MOVE_UP, Input.Keys.W);
        keyMappings.put(GameAction.MOVE_DOWN, Input.Keys.S);
        keyMappings.put(GameAction.MOVE_LEFT, Input.Keys.A);
        keyMappings.put(GameAction.MOVE_RIGHT, Input.Keys.D);
        keyMappings.put(GameAction.PAUSE, Input.Keys.P);
        keyMappings.put(GameAction.SHOOT, Input.Buttons.LEFT);
        keyMappings.put(GameAction.TOGGLE_AUTO_AIM, Input.Keys.SPACE);
        keyMappings.put(GameAction.RELOAD, Input.Keys.R);

        saveControls();
        Gdx.app.log("ControlsManager", "Controls reset to defaults.");
    }

    public static void refreshControls() {
        loadControls();
    }
}
