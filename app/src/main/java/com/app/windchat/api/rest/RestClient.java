package com.app.windchat.api.rest;


import com.app.windchat.api.model.RestCode;
import com.app.windchat.api.model.User;
import com.app.windchat.api.model.Wind;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by banal_a on 06/12/2016.
 */

public interface RestClient {
    @POST("/api/user/register")
    Call<User> register(
            @Body User user);

    @POST("/api/user/login")
    Call<User> login(@Body User user);

    @GET("/api/wind/list")
    Call<ArrayList<Wind>> get_winds();

    @POST("/api/wind")
    Call<RestCode> wind_post(@Body Wind wind);
}
