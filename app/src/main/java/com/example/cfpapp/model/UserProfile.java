package com.example.cfpapp.model;

public class UserProfile {

    private String username;

    // Required empty constructor for Firestore
    public UserProfile() {}

    public UserProfile(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
