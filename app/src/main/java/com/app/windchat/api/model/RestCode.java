package com.app.windchat.api.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by banal_a on 09/12/2016.
 */

@Parcel
public class RestCode {
    @SerializedName("code")
    int code ;
    @SerializedName("value")
    boolean value;
    @SerializedName("message")
    String message;


    public RestCode() {
        this.code = 0;
        this.value = true;
        this.message = "";
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
