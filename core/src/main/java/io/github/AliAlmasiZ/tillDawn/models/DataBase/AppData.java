package io.github.AliAlmasiZ.tillDawn.models.DataBase;

import io.github.AliAlmasiZ.tillDawn.models.User;

import java.util.ArrayList;
import java.util.List;

public class AppData {
    private static AppData appData;
    public User activeUser;

    private List<User> allUsers;

    private AppData() {
        allUsers = new ArrayList<>();
    }

    public static AppData getAppData() {
        if(appData == null)
            appData = new AppData();
        return appData;
    }

    public User getActivePlayer() {
        return activeUser;
    }

    public void setActivePlayer(User activeUser) {
        this.activeUser = activeUser;
    }
}
