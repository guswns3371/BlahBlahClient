package com.example.guswn.allthatlyrics.Home.Frag2_social;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

public class AdvancedImgModel implements Parcelable {
    Uri img;
    File file;
    Integer type;
    String filepath;
    String mimetype;

    public AdvancedImgModel(Uri img, File file, String filepath,Integer type, String mimetype) {
        this.img = img;
        this.file = file;
        this.type = type;
        this.filepath = filepath;
        this.mimetype = mimetype;
    }

    protected AdvancedImgModel(Parcel in) {
        img = in.readParcelable(Uri.class.getClassLoader());
        if (in.readByte() == 0) {
            type = null;
        } else {
            type = in.readInt();
        }
        filepath = in.readString();
        mimetype = in.readString();
    }

    public static final Creator<AdvancedImgModel> CREATOR = new Creator<AdvancedImgModel>() {
        @Override
        public AdvancedImgModel createFromParcel(Parcel in) {
            return new AdvancedImgModel(in);
        }

        @Override
        public AdvancedImgModel[] newArray(int size) {
            return new AdvancedImgModel[size];
        }
    };

    public Uri getImg() {
        return img;
    }

    public void setImg(Uri img) {
        this.img = img;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
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
        dest.writeParcelable(img, flags);
        if (type == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(type);
        }
        dest.writeString(filepath);
        dest.writeString(mimetype);
    }
}
