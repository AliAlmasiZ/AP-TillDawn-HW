package io.github.AliAlmasiZ.tillDawn.controllers;

import io.github.AliAlmasiZ.tillDawn.models.DataBase.AppData;
import io.github.AliAlmasiZ.tillDawn.models.User;
import io.github.AliAlmasiZ.tillDawn.models.Result;

import javax.management.InstanceAlreadyExistsException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupMenuController {
    /*public Result signup(String username, String password, String securityAnswer) {
//        try {
//            DataBaseManager.connect();
//            DataBaseManager.initializeFromSchema();
            /*PlayerDAO playerDAO = new PlayerDAO(DataBaseManager.getConnection());
            User p = playerDAO.loadPlayer(username);
            if (p != null) {
                return new Result(false, "User Already exists!");
            }
            User user = new User(username, password, securityAnswer);
            AppData.getAppData().setActiveUser(user);
            playerDAO.savePlayer(user);
            return new Result(true, "User with username \"" + username + "\" saved successfully!");




        //TODO: for DEBUG : remove below line and uncomment above lines(after fix save and load users)
        {
            Result result = new Result(true, "signup error");
            return result;
        }

//        }
//        catch (SQLException e) {
//            e.printStackTrace();
//            return new Result(false, "something wrong happened!");
//        }
    }
    */

    public Result signup(String username, String password, String securityAnswer) {


        try {
            Result result = checkUsername(username);
            if(!result.isSuccessful())
                return result;
            result =  checkPassword(password);
            if(!result.isSuccessful())
                return result;
            User user = new User(username, password, securityAnswer);
            AppData.getAppData().addUser(user);
            AppData.getAppData().setActiveUser(user);
            return new Result(true, "user created successfully!");
        } catch (Exception e) {
            return new Result(false, e.getMessage());
        }
    }

    private  Result checkPassword(String password) {
        final Pattern special = Pattern.compile(".*(?=[_()*&%$#@]).*");
        final Pattern number = Pattern.compile(".*(?=\\d).*");
        final Pattern capitalLetter = Pattern.compile(".*(=?[A-Z]).*");

        if(password.length() < 8)
            return new Result(false, "password should be at least 8 characters!");
        if(!special.matcher(password).matches())
            return new Result(false, "password should have at least one special character");
        if(!number.matcher(password).matches())
            return new Result(false, "password should have at least one number");
        if(!capitalLetter.matcher(password).matches())
            return new Result(false, "password should have at list one capita letter");


        return new Result(true, "");
    }

    private Result checkUsername(String username) {
        if("guest".equals(username)) {
            return new Result(false, "you can't make a user with guest name");
        }
        if(AppData.getAppData().getUserByUsername(username) != null) {
            return new Result(false, "user already exists!");
        }
        if(username.length() < 3)
            return new Result(false, "username should be at least 3 characters");
        return new Result(true, "");
    }


    public void guest() {
        AppData.getAppData().setActiveUser(new User("guest", "", ""));

    }




}
