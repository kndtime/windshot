package com.app.windshot.api.rest.serializer;

import com.app.snapshot.api.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by banal_a on 09/12/2016.
 */

public class ListUserDeserializer implements JsonDeserializer<ArrayList<User>> {

    public ListUserDeserializer() {
    }

    @Override
    public ArrayList<User> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        // Get the "content" element from the parsed JSON
        JsonArray datauser = json.getAsJsonObject().getAsJsonArray("users");

        ArrayList<User> myList = new ArrayList<User>();
        if (datauser!=null) {
            for (JsonElement e : datauser) {
                myList.add(new Gson().fromJson(e, User.class));
            }
        }
        return myList;
    }
}
