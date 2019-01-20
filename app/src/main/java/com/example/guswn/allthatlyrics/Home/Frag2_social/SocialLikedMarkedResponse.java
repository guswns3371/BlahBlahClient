package com.example.guswn.allthatlyrics.Home.Frag2_social;

import com.google.gson.annotations.SerializedName;

public class SocialLikedMarkedResponse {
    @SerializedName("Social_Liked_myidx")
    String Social_Liked_myidx;
    @SerializedName("Social_Marked_myidx")
    String Social_Marked_myidx;

    public String getSocial_Liked_myidx() {
        return Social_Liked_myidx;
    }

    public String getSocial_Marked_myidx() {
        return Social_Marked_myidx;
    }
}
