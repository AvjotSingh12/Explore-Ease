package com.example.travelapp;

import java.util.List;

public class revModel {

    public revModel() {
        // Required empty constructor for Firestore
    }



    String destination, title, review, with, date, reviewid, userid, username, user_image;
    List<String> imageUri;
    double rating;


    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public revModel(String user_image, String username , String reviewid, String destination, String title, List<String> imageUri, String review, String with, String date, double rating) {
        this.destination = destination;
        this.username = username;
        this.reviewid = reviewid;
        this.title = title;
        this.review = review;
        this.with = with;
        this.date = date;
        this.imageUri = imageUri;
        this.rating = rating;
        this.user_image = user_image;

    }

//    public revModel(String reviewid, String userid, String destination, String title, List<String> imageUri, String review, String with, String date, double rating) {
//        this.destination = destination;
//        this.reviewid = reviewid;
//        this.userid = userid;
//        this.title = title;
//        this.review = review;
//        this.with = with;
//        this.date = date;
//        this.imageUri = imageUri;
//        this.rating = rating;
//
//    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getWith() {
        return with;
    }

    public void setWith(String with) {
        this.with = with;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public List<String> getImageUri() {
        return imageUri;
    }

    public void setImageUri(List<String> imageUri) {
        this.imageUri = imageUri;
    }

    public String getReviewid() {
        return reviewid;
    }

    public void setReviewid(String reviewid) {
        this.reviewid = reviewid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
