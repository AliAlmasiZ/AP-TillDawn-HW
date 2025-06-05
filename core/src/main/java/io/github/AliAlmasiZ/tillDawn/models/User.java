package io.github.AliAlmasiZ.tillDawn.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

import javax.persistence.*;
import java.util.logging.FileHandler;

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
    @Transient
    private Player player;
    @Transient
    private Texture avatar;


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
        this.player.setUserID(id);
        setRandomAvatar();
    }


    private void setRandomAvatar() {
        FileHandle dir = Gdx.files.internal("avatars");
        Array<FileHandle> files = Array.with(dir.list());
        FileHandle pick = files.random();
        Texture avatar = new Texture(pick);
        this.setAvatar(avatar);
    }

    public void loadPlayer() {

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

    public void setPlayer(Player player) {
        this.player = player;
    }


    public Texture getAvatar() {
        return avatar;
    }

    public void setAvatar(Texture avatar) {
        this.avatar = avatar;
    }
}
