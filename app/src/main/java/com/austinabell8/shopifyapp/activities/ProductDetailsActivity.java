package com.austinabell8.shopifyapp.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.austinabell8.shopifyapp.R;
import com.austinabell8.shopifyapp.adapters.VariantsRecyclerAdapter;
import com.austinabell8.shopifyapp.model.Product;
import com.austinabell8.shopifyapp.model.ProductJSON;
import com.austinabell8.shopifyapp.model.Variant;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * ProductDetailsActivity - Activity that shows detailed information of
 *                          a product through Shopify API
 * @author  Austin Abell
 */

public class ProductDetailsActivity extends AppCompatActivity {

    private static final String ACCESS_TOKEN = "c32313df0d0ef512ca64d5b336a0d7c6";

    private Long mProductId;
    private Product mProduct;

    private TextView mTxtTitle;
    private TextView mTxtDescription;
    private TextView mTxtProductType;
    private TextView mTxtTags;
    private TextView mTxtVendor;
    private ImageView mImage;
    private RecyclerView mVariantRecyclerView;
    private VariantsRecyclerAdapter mVariantAdapter;
    private ArrayList<Variant> mVariants;
    private Toolbar mToolbar;

    private RetrieveProductAsync mAsyncTask;

    @SuppressWarnings("ConstantConditions")
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
        mVariantRecyclerView = findViewById(R.id.rvVariants);
        mVariants = new ArrayList<>();
        //Layout manager for variants
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        mVariantRecyclerView.setLayoutManager(gridLayoutManager);
        mVariantRecyclerView.setNestedScrollingEnabled(false);

        mToolbar = findViewById(R.id.AppBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle data = getIntent().getExtras();

        mProductId = 0L;
        if (savedInstanceState != null){
            //If not retrieved from Intent, get from savedInstanceState
            mProductId = savedInstanceState.getLong("saved_product");
        }
        if (data != null){
            //Retrieve product Id from intent from MainActivity
            mProductId = data.getLong("product_item");
        }
        if (mProductId!=0L){
            //Call API for specific product if a product Id is retrieved
            mAsyncTask = new RetrieveProductAsync();
            mAsyncTask.execute("" + mProductId);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Clear possible in progress API call
        if (mAsyncTask!=null){
            mAsyncTask.cancel(true);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save product to savedInstanceState bundle
        outState.putLong("saved_product", mProduct.getId());
    }

    /**
     * Call to retrieve specific product from API, retrieves all fields indicated in string
     */
    private class RetrieveProductAsync extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            String reqURL = "https://shopicruit.myshopify.com/admin/products";

            //GET request string builder
            Request request;
            request = new Request.Builder()
                    .url(reqURL + "/" + mProductId +
                            ".json?page=1&access_token="+ ACCESS_TOKEN +"&fields=id,title,body_html,image,product_type,tags,vendor,variants")
                    .build();
            Response response = null;

            //GET request call
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //GET request response handling
            try {
                if (response!=null){
                    //noinspection ConstantConditions
                    return response.body().string();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ProductJSON test = null;
            if (s==null){
                Toast.makeText(mTxtTitle.getContext(), "Request for product failed", Toast.LENGTH_LONG).show();
            }
            else {
                Gson gson = new Gson();
                test = gson.fromJson(s, ProductJSON.class);
            }
            if(test!=null && test.getErrors()==null) {
                mProduct = test.getProduct();

                //Initialize all data in activity, if retrieved from API call
                if (mProduct.getTitle() != null) {
                    mTxtTitle.setText(mProduct.getTitle());
                }
                if (mProduct.getDescription() != null) {
                    mTxtDescription.setText(mProduct.getDescription());
                }
                if (mProduct.getProductType() != null) {
                    mTxtProductType.setText(mProduct.getProductType());
                }
                if (mProduct.getTags()!=null) {
                    mTxtTags.setText(mProduct.getTags());
                }
                if (mProduct.getVendor()!=null) {
                    mTxtVendor.setText(mProduct.getVendor());
                }
                if (mProduct.getImage()!=null && mProduct.getImage().getSrc()!=null) {
                    Glide.with(mImage.getContext())
                            .load(mProduct.getImage().getSrc())
                            .into(mImage);
                }
                if(mProduct.getVariants()!=null){
                    mVariants = mProduct.getVariants();
                    mVariantAdapter = new VariantsRecyclerAdapter(mVariants);
                    mVariantRecyclerView.setAdapter(mVariantAdapter);
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

