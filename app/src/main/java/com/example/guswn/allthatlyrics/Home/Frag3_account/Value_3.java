package com.example.guswn.allthatlyrics.Home.Frag3_account;

import com.example.guswn.allthatlyrics.Home.Frag1_friends.FollowingResponse;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Value_3 {

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

    @SerializedName("userinfo")
    List<Value_3> userinfolist;
    @SerializedName("UserFollower")
    List<FollowingResponse> userfollower;
    @SerializedName("UserFollowing")
    List<FollowingResponse> userfollowing;

    public List<FollowingResponse> getUserfollowing() {
        return userfollowing;
    }

    public List<FollowingResponse> getUserfollower() {
        return userfollower;
    }

    public String getToken() {
        return token;
    }

    public List<Value_3> getUserinfolist() {
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
