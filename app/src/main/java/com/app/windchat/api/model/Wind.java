package com.app.windchat.api.model;

import java.util.ArrayList;

/**
 * Created by banal_a on 06/12/2016.
 */

public class Wind {
    private int duration;
    private double latitude;
    private double longitude;
    private ArrayList<Integer> recipients;
    private String image;
    private User user;


    public Wind() {
        this.duration = 0;
        this.latitude = 0;
        this.longitude = 0;
        this.recipients = new ArrayList<>();
        this.image = "";
        this.user = new User();
    }

    public int getDuration() {
        return duration;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public ArrayList<Integer> getRecipients() {
        return recipients;
    }

    public String getImage() {
        return image;
    }

    public User getUser() {
        return user;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setRecipients(ArrayList<Integer> recipients) {
        this.recipients = recipients;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
