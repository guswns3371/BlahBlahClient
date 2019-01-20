package com.example.guswn.allthatlyrics.Home.Frag1_friends;

import com.google.gson.annotations.SerializedName;

public class FollowingResponse {
    @SerializedName("value")
    String value;
    @SerializedName("message")
    String message;

    @SerializedName("isfollow")
    String isfollow;
    @SerializedName("Follower_idx")
    String follower_idx;
    @SerializedName("Followed_idx")
    String followed_idx;

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public String getIsfollow() {
        return isfollow;
    }

    public String getFollower_idx() {
        return follower_idx;
    }

    public String getFollowed_idx() {
        return followed_idx;
    }
}
