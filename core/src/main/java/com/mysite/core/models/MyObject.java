package com.mysite.core.models;

public class MyObject {
    private String username;
    private String password;
    private String role;

    public MyObject(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}






