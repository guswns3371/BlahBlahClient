package com.example.guswn.allthatlyrics.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class userResponse3 {

    @SerializedName("idx")
    String idx;
    @SerializedName("Username")
    String username;
    @SerializedName("Email")
    String email;
    @SerializedName("Birthday")
    String birthday;
    @SerializedName("Photo")
    String photo;
    @SerializedName("Introduce")
    String introduce;
    @SerializedName("Password")
    String password;
    @SerializedName("Token")
    String token;
    @SerializedName("SocialHistoryCount")
    String SocialHistoryCount;

    @SerializedName("userinfo")
    List<userResponse3> userinfolist;
    @SerializedName("UserFollower")
    List<FollowingResponse> userfollower;
    @SerializedName("UserFollowing")
    List<FollowingResponse> userfollowing;

    public String getSocialHistoryCount() {
        return SocialHistoryCount;
    }

    public List<FollowingResponse> getUserfollowing() {
        return userfollowing;
    }

    public List<FollowingResponse> getUserfollower() {
        return userfollower;
    }

    public String getToken() {
        return token;
    }

    public List<userResponse3> getUserinfolist() {
        return userinfolist;
    }

    public String getIdx() {
        return idx;
    }

    public String getPassword() {
        return password;
    }

    public String getIntroduce() {
        return introduce;
    }

    public String getPhoto() {
        return photo;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }


}
