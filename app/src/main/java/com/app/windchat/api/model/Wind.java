package com.app.windchat.api.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by banal_a on 06/12/2016.
 */
@Parcel
public class Wind {

    @SerializedName("id")
    private int id;
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
    @SerializedName("imageUrl")
    private String imageUrl;
    @SerializedName("isOpened")
    private boolean isOpened;
    private User user;

    public Wind() {
        this.id = 0;
        this.duration = 0;
        this.latitude = 48.8155223;
        this.longitude = 2.3607846;
        this.recipients = new ArrayList<>();
        this.image = "";
        this.imageUrl = "";
        this.sendDate = "";
        this.user = new User();
        this.isOpened = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
