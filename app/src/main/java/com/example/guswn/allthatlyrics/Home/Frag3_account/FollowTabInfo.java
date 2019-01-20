package com.example.guswn.allthatlyrics.Home.Frag3_account;

public class FollowTabInfo {

    String follow_img;
    String follow_username;
    String follow_email;
    String idx;

    String AmIFollowHim = "default";

    public String getAmIFollowHim() {
        return AmIFollowHim;
    }

    public void setAmIFollowHim(String amIFollowHim) {
        AmIFollowHim = amIFollowHim;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public FollowTabInfo( String idx,String follow_img, String follow_username, String follow_email) {
        this.idx = idx;
        this.follow_img = follow_img;
        this.follow_username = follow_username;
        this.follow_email = follow_email;
    }

    public String getFollow_img() {
        return follow_img;
    }

    public void setFollow_img(String follow_img) {
        this.follow_img = follow_img;
    }

    public String getFollow_username() {
        return follow_username;
    }

    public void setFollow_username(String follow_username) {
        this.follow_username = follow_username;
    }

    public String getFollow_email() {
        return follow_email;
    }

    public void setFollow_email(String follow_email) {
        this.follow_email = follow_email;
    }
}
