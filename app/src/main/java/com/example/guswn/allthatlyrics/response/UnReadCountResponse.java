package com.example.guswn.allthatlyrics.response;

import com.google.gson.annotations.SerializedName;

public class UnReadCountResponse {
    @SerializedName("value")
    String value;
    @SerializedName("message")
    String message;
    @SerializedName("user_idx")
    String user_idx;
    @SerializedName("chatroom_idx")
    String chatroom_idx;
    @SerializedName("unreadcount_list")
    String unreadcount_list;

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public String getUser_idx() {
        return user_idx;
    }

    public String getChatroom_idx() {
        return chatroom_idx;
    }

    public String getUnread_count() {
        return unreadcount_list;
    }
}
