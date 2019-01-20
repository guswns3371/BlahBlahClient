package com.example.guswn.allthatlyrics.Home.Frag2_social;

import android.os.Parcel;
import android.os.Parcelable;

public class SocialImageModel implements Parcelable{
    String url,filter;

    public SocialImageModel(String url, String filter) {
        this.url = url;
        this.filter = filter;
    }

    protected SocialImageModel(Parcel in) {
        url = in.readString();
        filter = in.readString();
    }

    public static final Creator<SocialImageModel> CREATOR = new Creator<SocialImageModel>() {
        @Override
        public SocialImageModel createFromParcel(Parcel in) {
            return new SocialImageModel(in);
        }

        @Override
        public SocialImageModel[] newArray(int size) {
            return new SocialImageModel[size];
        }
    };

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(filter);
    }
}
