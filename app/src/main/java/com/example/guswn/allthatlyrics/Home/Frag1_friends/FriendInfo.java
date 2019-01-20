package com.example.guswn.allthatlyrics.Home.Frag1_friends;

import com.example.guswn.allthatlyrics.MainActivity;

import java.net.URL;

public class FriendInfo {
    public static final int A_TYPE=0;
    public static final int B_TYPE=1;

    int type;
     String idx;
     String img;
     String name;
     String description;
     String email;
     String birthday;
    public Boolean isAddChecked;



    public FriendInfo(int type, String idx, String img, String name, String description, String email, String birthday) {
        this.type = type;
        this.idx = idx;
        this.img = img;
        this.name = name;
        this.description = description;
        this.email = email;
        this.birthday = birthday;
    }

    public Boolean getAddChecked() {
        return isAddChecked;
    }

    public void setAddChecked(Boolean addChecked) {
        isAddChecked = addChecked;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
