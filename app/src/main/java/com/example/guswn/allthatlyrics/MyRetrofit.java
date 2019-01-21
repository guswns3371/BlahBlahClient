package com.example.guswn.allthatlyrics;

import com.example.guswn.allthatlyrics.Home.Frag3_account.EditAPI;
import com.example.guswn.allthatlyrics.Home.Frag4_chat.ChatAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyRetrofit {

    Retrofit retrofit;

   public  MyRetrofit( Retrofit retrofit2){
       retrofit = retrofit2;
       HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
       interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
       OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

       retrofit = new Retrofit.Builder()
               .baseUrl(MainActivity.URL)
               .addConverterFactory(GsonConverterFactory.create())
               .client(client)
               .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
