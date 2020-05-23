package com.example.guswn.allthatlyrics.api;

import com.example.guswn.allthatlyrics.response.FollowingResponse;
import com.example.guswn.allthatlyrics.response.userResponse3;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FriendAPI {
    @GET("Register_Login/getjson_userinfo.php")
    Call<userResponse3> getOneInfo(@Query("idx") String user_idx);

    @GET("Register_Login/getjson_one.php")
    Call<userResponse3> getOneInfo2_email (@Query("Email") String email);

    @FormUrlEncoded
    @POST("Chatting/make_follow_unfollow.php")
    Call<FollowingResponse> make_follow_unfollow(
            @FieldMap Map<String,String> fields
    );

    @GET("Register_Login/getjson_follow_unfollow.php")
    Call<userResponse3> get_follow_unfollow (@Query("er_idx") String follower_idx , @Query("ed_idx") String followed_idx);
}
