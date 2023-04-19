package com.example.worker.Authentication;

public class UserForm {
    private String userName;
    private String token;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "UserForm{" +
                "userName='" + userName + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    public UserForm(String userName, String token) {
        this.userName = userName;
        this.token = token;
    }
}
