package com.example.guswn.allthatlyrics.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InnerChatResponse {
    @SerializedName("value")
    String value;
    @SerializedName("message")
    String message;

    @SerializedName("ChatHistoryIdx")
    String chathistory_idx;
    @SerializedName("ChatRoom_idx")
    String chatroom_idx;
    @SerializedName("ChatHistory_useridx")
    String chat_useridx;
    @SerializedName("ChatHistory_content")
    String chat_message;
    @SerializedName("ChatHistory_time")
    String chat_time;
    @SerializedName("ChatHistory_readpeople")
    String chat_readpeople;
    @SerializedName("ChatHistory_readpeople_list")
    String chat_readpeople_list;
    @SerializedName("ChatHistory_isfile")
    String chat_isfile;

    @SerializedName("chathistorylist")
    List<InnerChatResponse> chathistorylist;

    @SerializedName("UserIdx_Info")
    List<UserResponse_3> innerchatuserinfolist;

    public String getChat_isfile() {
        return chat_isfile;
    }

    public String getChat_readpeople_list() {
        return chat_readpeople_list;
    }

    public String getChat_readpeople() {
        return chat_readpeople;
    }

    public String getChathistory_idx() {
        return chathistory_idx;
    }

    public List<InnerChatResponse> getChathistorylist() {
        return chathistorylist;
    }

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public String getChatroom_idx() {
        return chatroom_idx;
    }

    public String getChat_useridx() {
        return chat_useridx;
    }

    public String getChat_message() {
        return chat_message;
    }

    public String getChat_time() {
        return chat_time;
    }

    public List<UserResponse_3> getInnerchatuserinfolist() {
        return innerchatuserinfolist;
    }
}
