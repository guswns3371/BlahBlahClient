package com.example.guswn.allthatlyrics.api;

import com.example.guswn.allthatlyrics.response.UserResponse;

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
    Call<UserResponse> createUser(
            @FieldMap Map<String,String> fields
    );

    @FormUrlEncoded
    @POST("Register_Login/login_userinfo.php")
    Call<UserResponse> loginUser(
            @FieldMap Map<String,String> fields
    );

    @GET("Register_Login/getjson_one_idx.php")
    Call<UserResponse> getuserInfo_withidx(@Query("idx") String user_idx);
}
