package com.example.guswn.allthatlyrics.Home.Frag4_chat;

public class ChatInfo {
    String idx;
    String title;
    String innercontent;
    String time;
    String img;
    String peopleNum;
    String unreadMessage;

    public String getUnreadMessage() {
        return unreadMessage;
    }

    public void setUnreadMessage(String unreadMessage) {
        this.unreadMessage = unreadMessage;
    }

    String isLoaded = "default";

    public String getIsLoaded() {
        return isLoaded;
    }

    public void setIsLoaded(String isLoaded) {
        this.isLoaded = isLoaded;
    }

    public ChatInfo(String idx, String title, String innercontent, String time, String img, String peopleNum,String unreadMessage) {
        this.idx = idx;
        this.title = title;
        this.innercontent = innercontent;
        this.time = time;
        this.img = img;
        this.peopleNum = peopleNum;
        this.unreadMessage = unreadMessage;
    }

    public String getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(String peopleNum) {
        this.peopleNum = peopleNum;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInnercontent() {
        return innercontent;
    }

    public void setInnercontent(String innercontent) {
        this.innercontent = innercontent;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
