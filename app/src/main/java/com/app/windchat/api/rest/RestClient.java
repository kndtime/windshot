package com.app.windchat.api.rest;


import com.app.windchat.api.model.RestCode;
import com.app.windchat.api.model.User;
import com.app.windchat.api.model.Wind;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by banal_a on 06/12/2016.
 */

public interface RestClient {
    @POST("/api/user/register")
    Call<User> register(
            @Body User user);

    @POST("/api/user/register")
    Call<User> register(
            @Body JsonObject object);


    @GET("/api/user/restoredSession")
    Call<User> refresh();


    @POST("/api/user/login")
    Call<User> login(@Body User user);

    @GET("/api/user/profile")
    Call<User> get_profile();

    @PUT("/api/user/profile")
    Call<User> update(@Body JsonObject object);

    @GET("/api/user/search/{username}")
    Call<ArrayList<User>> search(@Path("username") String username);

    @GET("/api/user/search/{username}")
    Call<JsonElement> rawsearch(@Path("username") String username);

    @GET("/api/wind/list")
    Call<ArrayList<User>> get_winds();

    @GET("/api/wind/list")
    Call<JsonElement> get_rawwinds();

    @POST("/api/wind")
    Call<RestCode> wind_post(@Body Wind wind);

    @PUT("/api/wind/{windId}/open")
    Call<RestCode> viewing(@Path("windId") int id);

    @GET("/api/friend/list")
    Call<ArrayList<User>> get_friends();

    @GET("/api/friend/list")
    Call<JsonElement> get_rawfriends();

    @POST("/api/friend")
    Call<RestCode> add_friend(@Body JsonObject object);

    @PUT("/api/friend/{id}/accept")
    Call<RestCode> accept(@Path("id") int id, @Body JsonObject object);

    @DELETE("/api/friend/{id}")
    Call<RestCode> delete(@Path("id") int id);

    @GET("/api/friend/pendingList")
    Call<ArrayList<User>> get_pendingList();

    @GET("/api/friend/pendingList")
    Call<JsonElement> get_rawpendingList();

    @GET("/api/friend/blockedList")
    Call<ArrayList<User>> get_blockedList();

    @GET("/api/story/myList")
    Call<JsonElement> get_mystory();

    @GET("/api/story/list")
    Call<JsonElement> get_story();

    @POST("/api/story")
    Call<RestCode> sendStory(@Body Wind wind);
}
