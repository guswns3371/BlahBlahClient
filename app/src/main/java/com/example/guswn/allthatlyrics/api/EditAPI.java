package com.example.guswn.allthatlyrics.api;

import com.example.guswn.allthatlyrics.response.ServerResponse;
import com.example.guswn.allthatlyrics.response.UserResponse2;
import com.example.guswn.allthatlyrics.response.userResponse3;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface EditAPI {
    @FormUrlEncoded
    @POST("UserInfo/edit_userinfo.php")
    Call<UserResponse2> editUserInfo(
            @FieldMap Map<String,String> fields
    );

    @Multipart
    @POST("UserInfo/img_upload.php")
    Call<ServerResponse> uploadPhoto(
            @Header("Authorization") String authorization,
            @PartMap Map<String, RequestBody> map
            );

    @Multipart
    @POST("UserInfo/img_info_userinfo.php")
    Call<UserResponse2> uploadPhoto_Info(
            @Part("Email") RequestBody email,
            @Part("Username") RequestBody Username,
            @Part("Birthday") RequestBody Birthday,
            @Part("Introduce") RequestBody Introduce,
            @Part MultipartBody.Part photo
    );

    @GET("Register_Login/getjson_one.php")
    Call<userResponse3> getOneInfo (@Query("Email") String email);

    @GET("Register_Login/getjson_userinfo.php")
    Call<userResponse3> getallInfo (@Query("idx") String user_idx);

//    @GET("Register_Login/getjson_one.php")
//    Call<ResponseBody> getOneInfo (@Query("Email") String email);

}
