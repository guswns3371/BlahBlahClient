package com.example.guswn.allthatlyrics.model;

import java.util.ArrayList;

public class InnerChatModel {

    String chat_useridx;
    String chat_img;
    String chat_username;
    String chat_content;
    String chat_time;
    String chat_readpeople;
    Boolean isMine;

    String isLoaded="default";
    String isFile="default";
    ArrayList<String> isReadPeopleList;


    public String getIsFile() {
        return isFile;
    }

    public void setIsFile(String isFile) {
        this.isFile = isFile;
    }

    public ArrayList<String> getIsReadPeopleList() {
        return isReadPeopleList;
    }

    public void setIsReadPeopleList(ArrayList<String> isReadPeopleList) {
        this.isReadPeopleList = isReadPeopleList;
    }


    public String getIsLoaded() {
        return isLoaded;
    }

    public void setIsLoaded(String last) {
        isLoaded = last;
    }

    public InnerChatModel(Boolean isMine, String chat_useridx, String chat_img, String chat_username, String chat_content, String chat_time, String chat_readpeople) {
        this.chat_useridx = chat_useridx;
        this.chat_img = chat_img;
        this.chat_username = chat_username;
        this.chat_content = chat_content;
        this.chat_time = chat_time;
        this.isMine = isMine;
        this.chat_readpeople = chat_readpeople;
    }

    public String getChat_readpeople() {
        return chat_readpeople;
    }

    public void setChat_readpeople(String chat_readpeople) {
        this.chat_readpeople = chat_readpeople;
    }

    public String getChat_useridx() {
        return chat_useridx;
    }

    public void setChat_useridx(String chat_idx) {
        this.chat_useridx = chat_idx;
    }

    public String getChat_img() {
        return chat_img;
    }

    public void setChat_img(String chat_img) {
        this.chat_img = chat_img;
    }

    public String getChat_username() {
        return chat_username;
    }

    public void setChat_username(String chat_username) {
        this.chat_username = chat_username;
    }

    public String getChat_content() {
        return chat_content;
    }

    public void setChat_content(String chat_content) {
        this.chat_content = chat_content;
    }

    public String getChat_time() {
        return chat_time;
    }

    public void setChat_time(String chat_time) {
        this.chat_time = chat_time;
    }

    public Boolean getisMine() {
        return isMine;
    }

    public void setisMine(Boolean mine) {
        isMine = mine;
    }
}
