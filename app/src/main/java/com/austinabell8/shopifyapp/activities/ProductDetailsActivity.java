package com.austinabell8.shopifyapp.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.austinabell8.shopifyapp.R;
import com.austinabell8.shopifyapp.model.Product;
import com.austinabell8.shopifyapp.model.ProductJSON;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by austi on 2017-12-24.
 */

public class ProductDetailsActivity extends AppCompatActivity {

    private Long mProductId;
    private Product mProduct;

    private TextView mTxtTitle;
    private TextView mTxtDescription;
    private TextView mTxtProductType;
    private TextView mTxtTags;
    private TextView mTxtVendor;
    private ImageView image;
    private Toolbar mToolbar;

    private RetrieveProductAsync mAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        mTxtTitle = findViewById(R.id.txtProductName);
        mTxtDescription = findViewById(R.id.txtProductDescription);
        mTxtProductType = findViewById(R.id.txtProductType);
        mTxtTags = findViewById(R.id.txtTags);
        mTxtVendor = findViewById(R.id.txtVendor);
        image = findViewById(R.id.productImage);
        //change id to Your id
        mToolbar = findViewById(R.id.AppBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle data = getIntent().getExtras();

        mProductId = 0L;
        if (data != null){
            mProductId = data.getLong("product_item");
        }
        if (mProductId == 0 && savedInstanceState != null){
            mProductId = savedInstanceState.getLong("saved_product");
        }
        if (mProductId!=0L){
            mAsyncTask = new RetrieveProductAsync();
            mAsyncTask.execute("" + mProductId);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAsyncTask!=null){
            mAsyncTask.cancel(true);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("saved_product", mProduct.getId());
    }

    private class RetrieveProductAsync extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            String reqURL = "https://shopicruit.myshopify.com/admin/products";

            Request request;
            request = new Request.Builder()
                    .url(reqURL + "/" + mProductId +
                            ".json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6&fields=id,title,body_html,image,product_type,tags,vendor")
                    .build();
            Response response = null;

            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (response!=null){
                    return response.body().string();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Gson gson = new Gson();
            ProductJSON test = gson.fromJson(s, ProductJSON.class);
            mProduct = test.getProduct();
            mTxtTitle.setText(mProduct.getTitle());
            mTxtDescription.setText(mProduct.getDescription());
            mTxtProductType.setText(mProduct.getProductType());
            mTxtTags.setText(mProduct.getTags());
            mTxtVendor.setText(mProduct.getVendor());
            Glide.with(image.getContext())
                    .load(mProduct.getImage().getSrc())
                    .into(image);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

}

