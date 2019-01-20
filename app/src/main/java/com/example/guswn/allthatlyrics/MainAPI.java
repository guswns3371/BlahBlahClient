package com.example.guswn.allthatlyrics;

import com.example.guswn.allthatlyrics.Home.Frag3_account.Value_2;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MainAPI {

    @GET("Register_Login/getjson_one.php")
    Call<Value_2> getOneInfo (@Path("Email") String email);
}
