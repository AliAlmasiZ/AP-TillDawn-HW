package io.github.AliAlmasiZ.tillDawn.models;

import javax.persistence.*;

@Entity
public class User {
    static int playersCount = 0;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id = ++playersCount;
    private String username;
    private String password;
//    private String securityQuestion;
    private String securityAnswer;
    private int langID;
    private int score;
    private Player player;


    private User(){}


    public User(String username, String password, String securityAnswer) {
        this.username = username;
        this.password = password;
//        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.langID = 1;
        this.id = ++playersCount;
        score = 0;
        this.player = new Player();
    }




    public static int getPlayersCount() {
        return playersCount;
    }

    public static void setPlayersCount(int playersCount) {
        User.playersCount = playersCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public int getLangID() {
        return langID;
    }

    public void setLangID(int langID) {
        this.langID = langID;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Player getPlayer() {
        return player;
    }
}
