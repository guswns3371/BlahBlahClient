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
    boolean isReReply = false;
    String replyit ;
    String whereReplyto = "null";

    public String getWhereReplyto() {
        return whereReplyto;
    }

    public void setWhereReplyto(String whereReplyto) {
        this.whereReplyto = whereReplyto;
    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    public String getReplyit() {
        return replyit;
    }

    public void setReplyit(String replyit) {
        this.replyit = replyit;
    }

    public boolean isReReply() {
        return isReReply;
    }

    public void setReReply(boolean reReply) {
        isReReply = reReply;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setisLoaded(boolean isloaded) {
        isLoaded = isloaded;
    }

    public SocialReplyModel(String idx, String useridx, String roomidx,String username, String userimg, String replycontent, String replytime, String replyit ) {
        this.idx = idx;
        this.useridx = useridx;
        this.roomidx = roomidx;
        this.username = username;
        this.userimg = userimg;
        this.replycontent = replycontent;
        this.replytime = replytime;
        this.replyit = replyit;
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
