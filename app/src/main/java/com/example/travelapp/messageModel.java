package com.example.travelapp;

public class messageModel {

    String message;
    String senderid;
      long timestamp;

    public messageModel() {
    }

    public messageModel(String message, String senderid, long timestamp) {
        this.message = message;
        this.senderid = senderid;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
