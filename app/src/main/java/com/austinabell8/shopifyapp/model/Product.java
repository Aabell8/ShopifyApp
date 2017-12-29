package com.austinabell8.shopifyapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Product - Model class for product object based on Shopify API
 * @author  Austin Abell
 */

public class Product implements Parcelable {

    private Long id;
    private String title;
    @SerializedName("body_html")
    private String description;
    private Image image;
    @SerializedName("product_type")
    private String productType;
    private String tags;
    private String vendor;

    private ArrayList<Variant> variants;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Image getImage() {
        return image;
    }
    public void setImage(Image image) {
        this.image = image;
    }

    public String getProductType() {
        return productType;
    }
    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getTags() {
        return tags;
    }
    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getVendor() {
        return vendor;
    }
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public ArrayList<Variant> getVariants() {
        return variants;
    }

    public void setVariants(ArrayList<Variant> variants) {
        this.variants = variants;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(title);
        out.writeString(description);
        out.writeParcelable(image, flags);
        out.writeString(productType);
        out.writeString(tags);
        out.writeString(vendor);
        out.writeList(variants);
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @SuppressWarnings("unchecked")
    private Product(Parcel in) {
        title = in.readString();
        description = in.readString();
        image = in.readParcelable(Image.class.getClassLoader());
        productType = in.readString();
        tags = in.readString();
        vendor = in.readString();
        variants = new ArrayList<>();
        variants = in.readArrayList(Variant.class.getClassLoader());
    }

}
