package com.example.guswn.allthatlyrics.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SocialUploadResponse {

    @SerializedName("value")
    String value;
    @SerializedName("message")
    String message;
    @SerializedName("idx")
    String idx;
    @SerializedName("Social_useridx")
    String social_useridx;
    @SerializedName("Social_username")
    String social_username;
    @SerializedName("Social_time")
    String social_time;
    @SerializedName("Social_content")
    String social_content;
    @SerializedName("Social_likecnt")
    String social_likecnt;
    @SerializedName("Social_location")
    String social_location;
    @SerializedName("Social_imagepath_list")
    String social_imagepath_list;

    /**얘네 필요해*/
    @SerializedName("Social_isLiked")
    String Social_isLiked;
    @SerializedName("Social_isBookMarked")
    String Social_isBookMarked;

    @SerializedName("SocialHistory")
    List<SocialUploadResponse> SocialHistoryList;
    @SerializedName("UserInfo")
    List<userResponse3> socialUserInfoList;

    /**로드 할때 필요한것 */
    @SerializedName("Social_Liked_List")
    List<SocialLikedMarkedResponse> Social_Liked_List;
    @SerializedName("Social_Marked_List")
    List<SocialLikedMarkedResponse> Social_Marked_List;

    public List<SocialLikedMarkedResponse> getSocial_Liked_List() {
        return Social_Liked_List;
    }

    public List<SocialLikedMarkedResponse> getSocial_Marked_List() {
        return Social_Marked_List;
    }

    public String getIdx() {
        return idx;
    }

    public String getSocial_isLiked() {

        return Social_isLiked;
    }

    public String getSocial_isBookMarked() {
        return Social_isBookMarked;
    }

    public List<userResponse3> getSocialUserInfoList() {
        return socialUserInfoList;
    }

    public List<SocialUploadResponse> getSocialHistoryList() {
        return SocialHistoryList;
    }

    public String getSocial_username() {
        return social_username;
    }

    public String getSocial_time() {
        return social_time;
    }

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public String getSocial_useridx() {
        return social_useridx;
    }

    public String getSocial_content() {
        return social_content;
    }

    public String getSocial_likecnt() {
        return social_likecnt;
    }

    public String getSocial_location() {
        return social_location;
    }

    public String getSocial_imagepath_list() {
        return social_imagepath_list;
    }
}
