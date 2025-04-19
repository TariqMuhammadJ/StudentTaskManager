package com.studytask.models;

import java.io.Serializable;

@SuppressWarnings("serial")
public class User implements Serializable {
    private int id;
    private String login;
    private String password;
    private boolean isActive; // added this to check for Online Users

    // Default constructor
    public User() {
    }

    // Constructor with fields
    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }


    public boolean getIsActive(){
        return isActive;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIsActive(boolean isActive){
        this.isActive = isActive;
    }
    // For proper object comparison in lists/sets
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}