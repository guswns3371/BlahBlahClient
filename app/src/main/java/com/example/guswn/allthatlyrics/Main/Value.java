package com.example.guswn.allthatlyrics.Main;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Value {
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
    @SerializedName("idx")
    String idx;

    public String getIdx() {
        return idx;
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

    List<Person> result;

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public List<Person> getResult() {
        return result;
    }
}
