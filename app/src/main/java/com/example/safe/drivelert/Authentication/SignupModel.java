package com.example.safe.drivelert.Authentication;

import java.io.Serializable;

public class SignupModel implements Serializable {

    private String userName;
    private String email;
    private String password;

    public SignupModel(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public SignupModel() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
