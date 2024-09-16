package com.example.usuarios_del_mundo.Models;

public class User {
    private Name name;
    private Location location;
    private String email;
    private Picture picture;

    // Getters
    public Name getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public String getEmail() {
        return email;
    }

    public Picture getPicture() {
        return picture;
    }
}

