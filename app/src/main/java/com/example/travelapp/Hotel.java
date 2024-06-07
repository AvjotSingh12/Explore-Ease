package com.example.travelapp;
public class Hotel {
    private String name;
    private double latitude;
    private double longitude;
    private String photoReference;

    public Hotel(String name, double latitude, double longitude, String photoReference) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.photoReference = photoReference;
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getPhotoReference() {
        return photoReference;
    }
}
