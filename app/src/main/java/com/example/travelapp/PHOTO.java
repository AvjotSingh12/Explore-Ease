package com.example.travelapp;


public class PHOTO {
    private String imageUrl;

    public  PHOTO() {
        // Empty constructor needed for Firestore
    }

    public PHOTO(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
