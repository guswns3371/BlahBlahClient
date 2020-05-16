package com.example.guswn.allthatlyrics.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SocialImageModel implements Parcelable{
    String url,filter,mimetype;

    public SocialImageModel(String url, String filter, String mimetype) {
        this.url = url;
        this.filter = filter;
        this.mimetype = mimetype;
    }

    protected SocialImageModel(Parcel in) {
        url = in.readString();
        filter = in.readString();
        mimetype = in.readString();
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

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(filter);
        dest.writeString(mimetype);
    }
}
