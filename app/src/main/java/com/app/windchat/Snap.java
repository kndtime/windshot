package com.app.windchat;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.app.windchat.api.model.User;
import com.google.gson.Gson;

/**
 * Created by banal_a on 05/12/2016.
 */

public class Snap extends Application{

    private static Application Instance;
    public static String PASS = "";

    private static User current;

    public static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    @Override
    public void onCreate() {
        super.onCreate();
        Instance = this;
        preferences = getSharedPreferences("com.app.windshot", Context.MODE_PRIVATE);
        editor = preferences.edit();
        Gson gson = new Gson();
        current = new User();
        String nullUser = gson.toJson(current);
        String tmpUser = preferences.getString("User", nullUser);
        current = gson.fromJson(tmpUser, User.class);
    }

    public static Application getInstance() {
        return Instance;
    }

    public static  User getCurrent() {
        if (current == null)
            return new User();
        return current;
    }

    public static void setCurrent(User user) {
        current = user;
        Gson gson = new Gson();
        String usar = gson.toJson(user);
        editor.putString("User",usar);
        editor.apply();
    }

}
