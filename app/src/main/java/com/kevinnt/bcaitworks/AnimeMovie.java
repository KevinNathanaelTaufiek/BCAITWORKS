package com.kevinnt.bcaitworks;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class AnimeMovie implements Parcelable {

    @SerializedName("title")
    private String title;

    @SerializedName("averageRating")
    private Double averageRating;

    @SerializedName("image")
    private String image;

    @SerializedName("eps")
    private Integer eps;

    @SerializedName("desc")
    private String desc;

    public AnimeMovie() {
    }

    @Override
    public String toString() {
        return "AnimeMovie{" +
                "title='" + title + '\'' +
                ", averageRating=" + averageRating +
                ", image='" + image + '\'' +
                ", eps=" + eps +
                ", desc='" + desc + '\'' +
                '}';
    }

    protected AnimeMovie(Parcel in) {
        title = in.readString();
        if (in.readByte() == 0) {
            averageRating = null;
        } else {
            averageRating = in.readDouble();
        }
        image = in.readString();
        if (in.readByte() == 0) {
            eps = null;
        } else {
            eps = in.readInt();
        }
        desc = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        if (averageRating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(averageRating);
        }
        dest.writeString(image);
        if (eps == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(eps);
        }
        dest.writeString(desc);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AnimeMovie> CREATOR = new Creator<AnimeMovie>() {
        @Override
        public AnimeMovie createFromParcel(Parcel in) {
            return new AnimeMovie(in);
        }

        @Override
        public AnimeMovie[] newArray(int size) {
            return new AnimeMovie[size];
        }
    };

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getEps() {
        return eps;
    }

    public void setEps(Integer eps) {
        this.eps = eps;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
