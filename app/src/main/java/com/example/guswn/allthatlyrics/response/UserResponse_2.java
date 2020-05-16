package com.example.guswn.allthatlyrics.response;

import com.google.gson.annotations.SerializedName;

public class UserResponse_2 {
    @SerializedName("value")
    String value;
    @SerializedName("message")
    String message;
    @SerializedName("username")
    String username;
    @SerializedName("email")
    String email;
    @SerializedName("birthday")
    String birthday;
    @SerializedName("photo")
    String photo;
    @SerializedName("introduce")
    String introduce;

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

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

}
