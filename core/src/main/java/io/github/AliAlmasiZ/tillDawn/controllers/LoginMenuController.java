package io.github.AliAlmasiZ.tillDawn.controllers;

import io.github.AliAlmasiZ.tillDawn.models.DataBase.AppData;
import io.github.AliAlmasiZ.tillDawn.models.DataBase.DataBaseManager;
import io.github.AliAlmasiZ.tillDawn.models.DataBase.PlayerDAO;
import io.github.AliAlmasiZ.tillDawn.models.User;
import io.github.AliAlmasiZ.tillDawn.models.Result;
import io.github.AliAlmasiZ.tillDawn.views.Text;

public class LoginMenuController {
    /*public Result login(String username, String password) {
        PlayerDAO playerDAO = new PlayerDAO(DataBaseManager.getConnection());
        User user = playerDAO.loadPlayer(username);

        if(!user.getPassword().equals(password)) {
            return new Result(false, "Wrong Password");
        }

        AppData.getAppData().setActiveUser(user);
        return new Result(true, "logged in successfully");
    }*/

    public Result login(String username, String password) {
        User user = AppData.getAppData().getUserByUsername(username);
        if(user == null)
            return new Result(false, Text.USERNAME_NOT_FOUND.getText());
        if(!user.getPassword().equals(password))
            return new Result(false, Text.INCORRECT_PASSWORD.getText());
        AppData.getAppData().setActiveUser(user);
        return new Result(true, Text.USER_LOGGED_IN.getText());
    }

    public Result forgetPass(String username, String answer) {
        User user = AppData.getAppData().getUserByUsername(username);
        if (user == null)
            return new Result(false, Text.USERNAME_NOT_FOUND.getText());
        if (user.getSecurityAnswer().equals(answer))
            return new Result(true, Text.YOUR_PASS_IS.getText() + "'" + user.getPassword() + "'");

        return new Result(false, Text.SECURITY_ANSWER_DOESNT_MATCH.getText());

    }
}
