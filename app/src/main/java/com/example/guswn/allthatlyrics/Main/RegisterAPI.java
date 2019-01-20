package com.example.guswn.allthatlyrics.Main;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RegisterAPI {

    @FormUrlEncoded
    @POST("Register_Login/insert_userinfo.php")
    Call<Value> createUser(
            @FieldMap Map<String,String> fields
    );

    @FormUrlEncoded
    @POST("Register_Login/login_userinfo.php")
    Call<Value> loginUser(
            @FieldMap Map<String,String> fields
    );

    @GET("Register_Login/getjson_one_idx.php")
    Call<Value> getuserInfo_withidx(@Query("idx") String user_idx);
}
