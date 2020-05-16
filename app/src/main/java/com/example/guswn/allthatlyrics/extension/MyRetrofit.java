package com.example.guswn.allthatlyrics.extension;

import com.example.guswn.allthatlyrics.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


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
                .baseUrl(App.getInstance().getString(R.string.URL)+"talky/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }
    public <T> T create(final Class<T> service) {
       return retrofit.create(service);
    }
}
