package com.example.guswn.allthatlyrics.Home;

import com.example.guswn.allthatlyrics.Home.Frag4_chat.UnReadCountResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface HomeAPI {

    @FormUrlEncoded
    @POST("Chatting/update_chatroom_unreadcount.php")
    Call<UnReadCountResponse> makeunreadCount_perRoomidx(
            @FieldMap Map<String,String> fields
    );

    @FormUrlEncoded
    @POST("Chatting/update_unreadcount_peridx.php")
    Call<UnReadCountResponse> updateunreadCount_peridx(
            @FieldMap Map<String,String> fields
    );
}
