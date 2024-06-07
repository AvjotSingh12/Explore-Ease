package com.example.travelapp;

public class demoplace {
    private String name;
    private static String photoUrl;

    public demoplace(String name, String photoUrl) {
        this.name = name;
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public static String getPhotoUrl() {
        return photoUrl;
    }
}
