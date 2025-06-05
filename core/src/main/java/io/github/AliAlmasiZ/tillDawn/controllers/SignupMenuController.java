package io.github.AliAlmasiZ.tillDawn.controllers;

import io.github.AliAlmasiZ.tillDawn.models.DataBase.AppData;
import io.github.AliAlmasiZ.tillDawn.models.DataBase.DataBaseManager;
import io.github.AliAlmasiZ.tillDawn.models.DataBase.PlayerDAO;
import io.github.AliAlmasiZ.tillDawn.models.User;
import io.github.AliAlmasiZ.tillDawn.models.Result;

public class SignupMenuController {
    public Result signup(String username, String password, String securityAnswer) {
//        try {
//            DataBaseManager.connect();
//            DataBaseManager.initializeFromSchema();
            PlayerDAO playerDAO = new PlayerDAO(DataBaseManager.getConnection());
            User p = playerDAO.loadPlayer(username);
            if (p != null) {
                return new Result(false, "User Already exists!");
            }
            User user = new User(username, password, securityAnswer);
            AppData.getAppData().setActivePlayer(user);
            playerDAO.savePlayer(user);
            return new Result(true, "User with username \"" + username + "\" saved successfully!");
//        }
//        catch (SQLException e) {
//            e.printStackTrace();
//            return new Result(false, "something wrong happened!");
//        }
    }

    public Result forgetPass() {
        return null;
    }
}
