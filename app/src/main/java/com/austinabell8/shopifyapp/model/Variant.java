package com.austinabell8.shopifyapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Variant - Model class for variant object based on Shopify API
 * @author  Austin Abell
 */

public class Variant implements Parcelable {
    private final String title;
    private final Double price;
    private final Long position;


    public String getTitle() {
        return title;
    }

    public Double getPrice() {
        return price;
    }

    public Long getPosition() {
        return position;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Variant> CREATOR = new Parcelable.Creator<Variant>() {
        public Variant createFromParcel(Parcel in) {
            return new Variant(in);
        }

        public Variant[] newArray(int size) {
            return new Variant[size];
        }
    };


    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(title);
        out.writeDouble(price);
        out.writeLong(position);
    }

    private Variant(Parcel in) {
        title = in.readString();
        price = in.readDouble();
        position = in.readLong();
    }

}
