package io.github.AliAlmasiZ.tillDawn.models.DataBase;

import io.github.AliAlmasiZ.tillDawn.models.User;

import javax.management.InstanceAlreadyExistsException;
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

    public User getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
    }
    public void addUser(User user) throws InstanceAlreadyExistsException {
        if(allUsers.contains(user))
            throw new InstanceAlreadyExistsException("this user already exists");
        allUsers.add(user);
    }

    public User getUserByUsername(String username) {
        for (User user : allUsers) {
            if (username.equals(user.getUsername()))
                return user;
        }
        return null;
    }
}
