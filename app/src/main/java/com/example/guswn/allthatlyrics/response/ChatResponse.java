package com.example.guswn.allthatlyrics.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatResponse {

    @SerializedName("value")
    String value;
    @SerializedName("message")
    String message;

    @SerializedName("ChatRoomIdx")
    String chatroom_idx;
    @SerializedName("ChatPeopleNum")
    String chatpeoplenum;
    @SerializedName("ChatPeople") // jsonArray 이다
    String chatpeople;
    @SerializedName("ChatRoomName")
    String chatroomname;
    @SerializedName("ChatRoomPhoto")
    String chatroomphoto;
    @SerializedName("ChatRoomOutTime")
    String chatroomouttime;
    @SerializedName("ChatRoomOutMessage")
    String chatroomoutmessage;
    @SerializedName("ChatUnreadList")
    String chatunreadlist;

    @SerializedName("chatroomlist")
    List<ChatResponse> ChatList;

    @SerializedName("UserIdx_Info")
    List<UserResponse_3> ChatUserInfoList;

    public String getChatunreadlist() {
        return chatunreadlist;
    }

    public List<UserResponse_3> getChatUserInfoList() {
        return ChatUserInfoList;
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

    public String getChatpeoplenum() {
        return chatpeoplenum;
    }

    public String getChatpeople() {
        return chatpeople;
    }

    public String getChatroomname() {
        return chatroomname;
    }

    public String getChatroomphoto() {
        return chatroomphoto;
    }

    public String getChatroomouttime() {
        return chatroomouttime;
    }

    public String getChatroomoutmessage() {
        return chatroomoutmessage;
    }

    public List<ChatResponse> getChatList() {
        return ChatList;
    }
}
