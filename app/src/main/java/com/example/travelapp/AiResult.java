package com.example.travelapp;
public class AiResult {
    private String title;
    private String description;

    // Constructor
    public AiResult(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // Getter for title
    public String getTitle() {
        return title;
    }

    // Setter for title
    public void setTitle(String title) {
        this.title = title;
    }

    // Getter for description
    public String getDescription() {
        return description;
    }

    // Setter for description
    public void setDescription(String description) {
        this.description = description;
    }
}

