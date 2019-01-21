package com.example.guswn.allthatlyrics.Home.Frag2_social.Reply;

public class SocialReplyModel {

    String idx;
    String useridx;
    String roomidx;
    String username;
    String userimg;
    String replycontent;
    String replytime;
    boolean isLoaded = false;

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setisLoaded(boolean isloaded) {
        isLoaded = isloaded;
    }

    public SocialReplyModel(String idx, String useridx, String roomidx,String username, String userimg, String replycontent, String replytime) {
        this.idx = idx;
        this.useridx = useridx;
        this.roomidx = roomidx;
        this.username = username;
        this.userimg = userimg;
        this.replycontent = replycontent;
        this.replytime = replytime;
    }

    public String getRoomidx() {
        return roomidx;
    }

    public void setRoomidx(String roomidx) {
        this.roomidx = roomidx;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getUseridx() {
        return useridx;
    }

    public void setUseridx(String useridx) {
        this.useridx = useridx;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserimg() {
        return userimg;
    }

    public void setUserimg(String userimg) {
        this.userimg = userimg;
    }

    public String getReplycontent() {
        return replycontent;
    }

    public void setReplycontent(String replycontent) {
        this.replycontent = replycontent;
    }

    public String getReplytime() {
        return replytime;
    }

    public void setReplytime(String replytime) {
        this.replytime = replytime;
    }
}
