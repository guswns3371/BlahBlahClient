package com.example.guswn.allthatlyrics.Home.Frag2_social.Reply;

import com.example.guswn.allthatlyrics.Home.Frag3_account.Value_3;
import com.google.gson.annotations.SerializedName;

public class SocialReReplyReponse {
    @SerializedName("idx")
    String idx;
    @SerializedName("Rere_contentidx")
    String Rere_contentidx;
    @SerializedName("Rere_useridx")
    String Rere_useridx;
    @SerializedName("Rere_roomidx")
    String Rere_roomidx;
    @SerializedName("Rere_content")
    String Rere_content;
    @SerializedName("Rere_time")
    String Rere_time;
    @SerializedName("Rere_UserIdx_Info")
    Value_3 Rere_UserIdx_Info;

    public String getIdx() {
        return idx;
    }

    public String getRere_contentidx() {
        return Rere_contentidx;
    }

    public String getRere_useridx() {
        return Rere_useridx;
    }

    public String getRere_roomidx() {
        return Rere_roomidx;
    }

    public String getRere_content() {
        return Rere_content;
    }

    public String getRere_time() {
        return Rere_time;
    }

    public Value_3 getRere_UserIdx_Info() {
        return Rere_UserIdx_Info;
    }
}
