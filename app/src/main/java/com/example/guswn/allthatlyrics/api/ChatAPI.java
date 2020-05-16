package com.example.guswn.allthatlyrics.api;

import com.example.guswn.allthatlyrics.response.UserResponse_3;
import com.example.guswn.allthatlyrics.response.ChatResponse;
import com.example.guswn.allthatlyrics.response.InnerChatResponse;
import com.example.guswn.allthatlyrics.response.UnReadCountResponse;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface ChatAPI {
    @FormUrlEncoded
    @POST("Chatting/makechatroom.php")
    Call<ChatResponse> makeChatRoom(
            @FieldMap Map<String,String> fields
    );

    @GET("Chatting/getjson_chatroom_userinfo.php")
    Call<ChatResponse> getChatRoomList();

    @GET("Register_Login/getjson_one_idx.php")
    Call<UserResponse_3> getOneInfo3_idx (@Query("idx") String user_idx);

    @FormUrlEncoded
    @POST("Chatting/getjson_chathistory_userinfo.php")
    Call<InnerChatResponse> getchatHistory_chatroomidx (@Field("chatroom_idx") String chatroom_idx);

    @FormUrlEncoded
    @POST("Chatting/makechat_history.php")
    Call<InnerChatResponse> makechatHistory(
            @FieldMap Map<String,String> fields
    );

    @Multipart
    @POST("Chatting/makechat_history_withfile.php")
    Call<InnerChatResponse> makechatHistory_withfile(
            @Header("Authorization") String authorization,
            @PartMap Map<String, RequestBody> map
    );

    @FormUrlEncoded
    @POST("Chatting/updatechat_history.php")
    Call<InnerChatResponse> updatechatHistory(
            @FieldMap Map<String,String> fields
    );


    @FormUrlEncoded
    @POST("Chatting/update_chatroom_i_read_it.php")
    Call<UnReadCountResponse> makeireadit_perRoomidx(
            @FieldMap Map<String,String> fields
    );
}
