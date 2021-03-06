package com.app.windchat.api.rest;

import android.content.Intent;

import com.app.windchat.Snap;
import com.app.windchat.api.model.User;
import com.app.windchat.api.model.Wind;
import com.app.windchat.api.rest.serializer.ListUserDeserializer;
import com.app.windchat.api.rest.serializer.ListWindDeserializer;
import com.app.windchat.api.rest.serializer.UserDeserializer;
import com.app.windchat.api.rest.serializer.WindDeserializer;
import com.app.windchat.ui.activity.LoginActivity;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

;import static org.parceler.guava.net.HttpHeaders.AUTHORIZATION;

public class Api {

    public static final String API_BASE_URL = "http://windchatapi.3ie.fr";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private RestClient restClient;

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());


    public Api(){
        Type user = new TypeToken<User>(){}.getType();
        Type listUser = new TypeToken<ArrayList<User>>(){}.getType();
        Type wind = new TypeToken<Wind>(){}.getType();
        Type listwind = new TypeToken<ArrayList<User>>(){}.getType();

        //TODO : Had Gson convert factories
        Gson api_models = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .serializeNulls()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setPrettyPrinting()
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .registerTypeAdapter(user, new UserDeserializer())
                .registerTypeAdapter(listUser, new ListUserDeserializer())
                .registerTypeAdapter(wind, new WindDeserializer())
                .registerTypeAdapter(listwind, new ListWindDeserializer())
                .create();

        //Client delcaration
        final String badic_auth = "Bearer "+ Snap.getCurrent().getToken();
        final String credentials = "{:YZVHSSWEQ-4806F7CQNG-RT6VR36PSG}";
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder()
                        .header("x-api-key", credentials)
                        .header("Accept", "application/json")
                        .method(original.method(), original.body());

                if (!Snap.getCurrent().getToken().isEmpty())
                    requestBuilder.header("Authorization", badic_auth);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        httpClient.authenticator(new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                if  (response.code() == 401){
                    Snap.setCurrent(new User());
                    Intent i = new Intent(Snap.getInstance(), LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
                    Snap.getInstance().startActivity(i);
                    return null;
                }

                if (response.code() != 200){
                    return null;
                }
                Call<User> call = new Api().getRestClient().refresh();
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                        if (response.isSuccessful()){
                            Snap.setCurrent(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
                final String badic_auth = "Bearer "+ Snap.getCurrent().getToken();
                return response.request().newBuilder()
                        .header(AUTHORIZATION, badic_auth)
                        .build();
            }
        });

        httpClient.interceptors().add(interceptor);
        httpClient.readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS);
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(api_models))
                .client(client)
                .build();
        restClient = retrofit.create(RestClient.class);
    }

    public static <S> S createService(Class<S> serviceClass) {
            final String credentials = "{:YZVHSSWEQ-4806F7CQNG-RT6VR36PSG}";
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request.Builder requestBuilder = original.newBuilder()
                            .header("x-api-key", credentials)
                            .header("Accept", "application/json")
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }

    public RestClient getRestClient() {
        return restClient;
    }
}