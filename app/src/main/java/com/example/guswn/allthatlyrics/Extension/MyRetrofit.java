package com.example.guswn.allthatlyrics.Extension;

import android.util.Log;

import com.example.guswn.allthatlyrics.Home.Frag1_friends.FriendAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.guswn.allthatlyrics.Extension.Extension.log;
import static com.example.guswn.allthatlyrics.MainActivity.URL;

public class MyRetrofit {
    private Retrofit retrofit;

    public MyRetrofit(){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }
    public <T> T create(final Class<T> service) {
       return retrofit.create(service);
    }
}
