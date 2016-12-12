package com.app.windchat.api.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by banal_a on 06/12/2016.
 */
@Parcel
public class Wind {

    @SerializedName("duration")
    private int duration;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("sendDate")
    private String sendDate;
    @SerializedName("recipients")
    private ArrayList<Integer> recipients;
    @SerializedName("image")
    private String image;
    private User user;

    public Wind() {
        this.duration = 0;
        this.latitude = 0;
        this.longitude = 0;
        this.recipients = new ArrayList<>();
        this.image = "";
        this.sendDate = "";
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

    public String getFrom(){
        return user.getCompleteName();
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

    public String getSendDate() {
        return sendDate;
    }
}
