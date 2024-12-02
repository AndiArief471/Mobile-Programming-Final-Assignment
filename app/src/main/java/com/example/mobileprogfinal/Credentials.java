package com.example.mobileprogfinal;

public class Credentials {
    private String userEmail;
    private String userName;

    public Credentials(String userEmail, String userName) {
        this.userEmail = userEmail;
        this.userName = userName;
    }

    public Credentials(String userName) {
        this.userName = userName;
    }

    public Credentials() {}

    public String getUserEmail() {
        return this.userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
