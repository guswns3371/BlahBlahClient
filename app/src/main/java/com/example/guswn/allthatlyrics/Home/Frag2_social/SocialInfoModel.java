package com.example.guswn.allthatlyrics.Home.Frag2_social;

import java.util.ArrayList;

public class SocialInfoModel {
    String social_useridx;
    String social_idx;
    String social_userimg;
    String social_username;
    String social_location;
    String social_like_cnt;
    String social_content_txt;
    String social_time;
    boolean isLiked,isBookMarked;
    ArrayList<SocialImageModel>  socialImageModelList;

    public SocialInfoModel(String social_idx, String social_useridx, String social_userimg, String social_username, String social_location, String social_like_cnt, String social_content_txt, String social_time, ArrayList<SocialImageModel> socialImageModelList) {
        this.social_idx = social_idx;
        this.social_useridx = social_useridx;
        this.social_userimg = social_userimg;
        this.social_username = social_username;
        this.social_location = social_location;
        this.social_like_cnt = social_like_cnt;
        this.social_content_txt = social_content_txt;
        this.social_time = social_time;
        this.socialImageModelList = socialImageModelList;
    }

//    public SocialInfoModel(String social_idx, String social_userimg, String social_username, String social_location, String social_like_cnt, String social_content_txt, String social_time, ArrayList<SocialImageModel> socialImageModelList,Boolean isLiked,Boolean isBookMarked) {
//        this.social_idx = social_idx;
//        this.social_userimg = social_userimg;
//        this.social_username = social_username;
//        this.social_location = social_location;
//        this.social_like_cnt = social_like_cnt;
//        this.social_content_txt = social_content_txt;
//        this.social_time = social_time;
//        this.socialImageModelList = socialImageModelList;
//        this.isLiked = isLiked;
//        this.isBookMarked = isBookMarked;
//    }


    public String getSocial_useridx() {
        return social_useridx;
    }

    public void setSocial_useridx(String social_useridx) {
        this.social_useridx = social_useridx;
    }

    public boolean getLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean getBookMarked() {
        return isBookMarked;
    }

    public void setBookMarked(boolean bookMarked) {
        isBookMarked = bookMarked;
    }

    public String getSocial_idx() {
        return social_idx;
    }

    public void setSocial_idx(String social_idx) {
        this.social_idx = social_idx;
    }

    public String getSocial_userimg() {
        return social_userimg;
    }

    public void setSocial_userimg(String social_userimg) {
        this.social_userimg = social_userimg;
    }

    public String getSocial_username() {
        return social_username;
    }

    public void setSocial_username(String social_username) {
        this.social_username = social_username;
    }

    public String getSocial_location() {
        return social_location;
    }

    public void setSocial_location(String social_location) {
        this.social_location = social_location;
    }

    public String getSocial_like_cnt() {
        return social_like_cnt;
    }

    public void setSocial_like_cnt(String social_like_cnt) {
        this.social_like_cnt = social_like_cnt;
    }

    public String getSocial_content_txt() {
        return social_content_txt;
    }

    public void setSocial_content_txt(String social_content_txt) {
        this.social_content_txt = social_content_txt;
    }

    public String getSocial_time() {
        return social_time;
    }

    public void setSocial_time(String social_time) {
        this.social_time = social_time;
    }

    public ArrayList<SocialImageModel> getSocialImageModelList() {
        return socialImageModelList;
    }

    public void setSocialImageModelList(ArrayList<SocialImageModel> socialImageModelList) {
        this.socialImageModelList = socialImageModelList;
    }
}
