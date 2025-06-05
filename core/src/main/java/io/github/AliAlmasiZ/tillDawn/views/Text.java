package io.github.AliAlmasiZ.tillDawn.views;

public enum Text {
    //signup menu
    CREATE_ACCOUNT("Create account", "sakht akant"),
    USERNAME("Username", "name karbari"),
    ENTER_USERNAME("Enter username", "name karbari vared konid"),
    PASSWORD("Password", "ramz"),
    ENTER_PASSWORD("Enter password", "ramz vared konid"),
    SECURITY_ANSWER("Security Answer", "porsesh amniaty"),
    ENTER_ANSWER("Enter Answer", "pasokh vared konid"),
    SIGNUP("Signup", "sabte nam"),
    PLAY_AS_GUEST("Play as Guest", "vorood be onvan mehman"),
    GOTO_LOGIN_MENU("Go to login menu", "raftan be menue login"),
    //LOGIN
    LOGIN("Login", "vorood"),
    FORGET_PASSWORD("Forget Password", "faramooshi ramz"),
    ENTER_YOUR_SECURITY_ANSWER("Enter your security answer", "pasokh amniati khod ro vared konid"),
    SUBMIT("Submit", "sabt"),
    USERNAME_NOT_FOUND("username not found", "name karbari peyda nashod"),
    SECURITY_ANSWER_DOESNT_MATCH("security answer doesnt match", "javab soal amniaty dorost nist"),
    YOUR_PASS_IS("your password is: ", "ramz shoma hast: "),
    INCORRECT_PASSWORD("password is incorrect!", "password nadorost ast"),
    USER_LOGGED_IN("user logged in successfully!", "ba movafaghiat vared shodid!"),
    //PROFILE MENU
    GO_BACK("go back", "bazgasht"),



    ;

    public static boolean isFirstActive = true;

    private final String firstLang, secondLang;

    Text(String firstLang, String secondLang) {
        this.firstLang = firstLang;
        this.secondLang = secondLang;
    }

    public String getText() {
        if(isFirstActive)
            return firstLang;
        return secondLang;
    }

    @Override
    public String toString() {
        return getText();
    }
}
