package com.example.guswn.allthatlyrics.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SocialReplyResponse {
    @SerializedName("value")
    String value;
    @SerializedName("message")
    String message;
    @SerializedName("idx")
    String idx;
    @SerializedName("Social_Reply_useridx")
    String Social_Reply_useridx;
    @SerializedName("Social_Reply_roomidx")
    String Social_Reply_roomidx;
    @SerializedName("Social_Reply_content")
    String Social_Reply_content;
    @SerializedName("Social_Reply_time")
    String Social_Reply_time;

    @SerializedName("UserIdx_Info")
    List<userResponse3> UserIdx_Info;
    /**ㅁㅊ놈ㅋㅋㅋㅋ*/
    @SerializedName("ReReply_info")
    List<SocialReReplyReponse> ReReply_info;
    @SerializedName("social_replylist")
    List<SocialReplyResponse> social_replylist;

    public List<SocialReReplyReponse> getReReply_info() {
        return ReReply_info;
    }

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public String getIdx() {
        return idx;
    }

    public String getSocial_Reply_useridx() {
        return Social_Reply_useridx;
    }

    public String getSocial_Reply_roomidx() {
        return Social_Reply_roomidx;
    }

    public String getSocial_Reply_content() {
        return Social_Reply_content;
    }

    public String getSocial_Reply_time() {
        return Social_Reply_time;
    }

    public List<userResponse3> getUserIdx_Info() {
        return UserIdx_Info;
    }

    public List<SocialReplyResponse> getSocial_replylist() {
        return social_replylist;
    }
}
