package com.austinabell8.shopifyapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * ProductsJSON - Model class used for parsing json into Java object for list of products
 *                to comply with Shopify API structure
 * @author  Austin Abell
 */

public class ProductsJSON {
    private ArrayList<Product> products;

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}
