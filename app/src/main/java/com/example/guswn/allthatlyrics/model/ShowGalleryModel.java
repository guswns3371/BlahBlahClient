package com.example.guswn.allthatlyrics.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ShowGalleryModel implements Parcelable {
    String filepath;
    String mimtype;

    public ShowGalleryModel(String filepath, String mimtype) {
        this.filepath = filepath;
        this.mimtype = mimtype;
    }

    protected ShowGalleryModel(Parcel in) {
        filepath = in.readString();
        mimtype = in.readString();
    }

    public static final Creator<ShowGalleryModel> CREATOR = new Creator<ShowGalleryModel>() {
        @Override
        public ShowGalleryModel createFromParcel(Parcel in) {
            return new ShowGalleryModel(in);
        }

        @Override
        public ShowGalleryModel[] newArray(int size) {
            return new ShowGalleryModel[size];
        }
    };

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getMimtype() {
        return mimtype;
    }

    public void setMimtype(String mimtype) {
        this.mimtype = mimtype;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(filepath);
        dest.writeString(mimtype);
    }
}
