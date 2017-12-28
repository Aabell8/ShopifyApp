package com.austinabell8.shopifyapp.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private ImageView mImage;
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
        mImage = findViewById(R.id.productImage);
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
            if(test!=null){
                mProduct = test.getProduct();
                if(mProduct.getTitle()!=null)
                    mTxtTitle.setText(mProduct.getTitle());

                if (mProduct.getDescription()!=null)
                    mTxtDescription.setText(mProduct.getDescription());

                if (mProduct.getProductType()!=null)
                    mTxtProductType.setText(mProduct.getProductType());

                if (mProduct.getTags()!=null)
                    mTxtTags.setText(mProduct.getTags());

                if (mProduct.getVendor()!=null)
                    mTxtVendor.setText(mProduct.getVendor());

                if (mProduct.getImage()!=null && mProduct.getImage().getSrc()!=null) {
                    Glide.with(mImage.getContext())
                            .load(mProduct.getImage().getSrc())
                            .into(mImage);
                }
            }
            else {
                Toast.makeText(mTxtTitle.getContext(), "Could not retrieve specified product!", Toast.LENGTH_LONG).show();
//                finish();
            }
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

