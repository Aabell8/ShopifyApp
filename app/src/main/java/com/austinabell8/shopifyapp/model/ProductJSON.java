package com.austinabell8.shopifyapp.model;

/**
 * ProductJSON - Model class used for parsing json into Java object for specific products
 *               to comply with Shopify API structure
 * @author  Austin Abell
 */

public class ProductJSON {
    private Product product;
    private String errors;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }
}
