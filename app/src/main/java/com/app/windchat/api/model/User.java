package com.app.windchat.api.model;

import com.app.windchat.Utils;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 * Created by banal_a on 06/12/2016.
 */
@Parcel
public class User implements Comparator<User>{

    @SerializedName("id")
    private int id;
    @SerializedName("token")
    private String token;
    @SerializedName("userName")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("firstName")
    private String firstname;
    @SerializedName("lastName")
    private String lastname;
    @SerializedName("email")
    private String email;
    @SerializedName("birthday")
    private String birthday;
    @SerializedName("subscribeDay")
    private String subscribeDay;
    @SerializedName("pictureUrl")
    private String pictureUrl;
    @SerializedName("pictureUrlSmall")
    private String pictureUrlSmall;
    @SerializedName("imageStr64")
    String imageStr64;
    @SerializedName("winds")
    ArrayList<Wind> winds;
    boolean selected;

    public User() {
        this.id = 0;
        this.token = "";
        this.username = "";
        this.firstname = "";
        this.lastname = "";
        this.email = "";
        this.birthday = "1975-12-01T00:00:00";
        this.subscribeDay = "";
        this.pictureUrl = "";
        this.pictureUrlSmall = "";
        this.imageStr64 = "";
        this.winds = new ArrayList<>();
        this.selected = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSubscribeDay() {
        return subscribeDay;
    }

    public void setSubscribeDay(String subscribeDay) {
        this.subscribeDay = subscribeDay;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getPictureUrlSmall() {
        return pictureUrlSmall;
    }

    public void setPictureUrlSmall(String pictureUrlSmall) {
        this.pictureUrlSmall = pictureUrlSmall;
    }

    public String getCompleteName(){
        return firstname + " " + lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImageStr64(String imageStr64) {
        this.imageStr64 = imageStr64;
    }

    public String getImageStr64() {
        return imageStr64;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Wind> getWinds() {
        return winds;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public User clone(){
        User user = new User();
        user.setFirstname(this.firstname);
        user.setLastname(this.lastname);
        user.setPictureUrl(this.pictureUrl);
        user.setUsername(this.username);
        user.setId(this.id);
        return user;
    }

    public long getTime(){
        if (winds.isEmpty())
            return 0;
        DateFormat format = new SimpleDateFormat(Utils.dateFormat, Locale.getDefault());
        try {
            Date date = format.parse(winds.get(0).getSendDate());
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void apply(){
        for (Wind wind: winds
             ) {
            wind.setUser(this);
        }
    }

    @Override
    public int compare(User user, User t1) {
        if (user.getTime() < t1.getTime())
            return 0;
        return 1;
    }
}
