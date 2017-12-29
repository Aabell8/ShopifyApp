package com.austinabell8.shopifyapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Image - Model class for image object based on Shopify API
 * @author  Austin Abell
 */

public class Image implements Parcelable {
    private final Long id;
    private final String src;

    public Long getId() {
        return id;
    }

    public String getSrc() {
        return src;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeLong(id);
        out.writeString(src);
    }

    private Image(Parcel in) {
        id = in.readLong();
        src = in.readString();
    }

}
