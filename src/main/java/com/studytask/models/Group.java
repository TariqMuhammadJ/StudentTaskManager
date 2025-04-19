package com.studytask.models;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class Group implements Serializable {
    private int id;
    private String name;
    private List<User> members;

    // Original constructor
    public Group(int id, String name) {
        this.id = id;
        this.name = name;
        this.members = new ArrayList<>();
    }

    // New constructor for initial creation
    public Group(String name) {
        this.name = name;
        this.members = new ArrayList<>();
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<User> getMembers() {
        return members;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    // Helper methods
    public void addMember(User user) {
        if (this.members == null) {
            this.members = new ArrayList<>();
        }
        this.members.add(user);
    }

    public void removeMember(User user) {
        if (this.members != null) {
            this.members.remove(user);
        }
    }

    public boolean hasMember(User user) {
        return this.members != null && this.members.contains(user);
    }
}