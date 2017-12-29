package com.austinabell8.shopifyapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.austinabell8.shopifyapp.adapters.ProductsRecyclerAdapter;
import com.austinabell8.shopifyapp.model.Product;
import com.austinabell8.shopifyapp.R;
import com.austinabell8.shopifyapp.model.ProductsJSON;
import com.austinabell8.shopifyapp.utilities.RecyclerViewClickListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * MainActivity - Base activity that lists products from Shopify API
 * @author  Austin Abell
 */

public class MainActivity extends AppCompatActivity {

    private static final String ACCESS_TOKEN = "c32313df0d0ef512ca64d5b336a0d7c6";

    private ArrayList<Product> mProducts;
    private RecyclerView mRecyclerView;
    private SearchView mSearchView;

    private InputMethodManager mInputManager;
    private GridLayoutManager mLayoutManager;
    private ProductsRecyclerAdapter mProductsAdapter;
    private Toolbar mToolbar;
    private RetrieveProductListAsync mAsyncTask;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.rvProducts);
        mToolbar = findViewById(R.id.toolbar);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout_recycler_view);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshProductList(true);
            }
        });

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Austin's Products");

        //Initialize Products from saved instance state, if it exists
        if(savedInstanceState!=null){
            mProducts = savedInstanceState.getParcelableArrayList("products_saved");
            initializeData();
        }

        //If it does not have saved state, retrieve products which will initialize data on complete
        if(mProducts == null){
            refreshProductList(false);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Reset search to text input to search bar
        if(mProductsAdapter != null && mSearchView != null){
            mProductsAdapter.filter(mSearchView.getQuery().toString());
        }
        //Clear refreshing in case call was not completed after pause
        if (mSwipeRefreshLayout != null){
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Cancel background thread task when activity is destroyed
        if (mAsyncTask!=null){
            mAsyncTask.cancel(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if(mProductsAdapter!=null){
            //If search limits product list, reset to full list before exiting
            mProductsAdapter.reset();
        }
        //Save product list
        savedInstanceState.putParcelableArrayList("products_saved", mProducts);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        super.onCreateOptionsMenu(menu);
        menu.clear();
        inflater.inflate(R.menu.actionbar_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView = new SearchView(this.getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, mSearchView);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Hide keyboard on submit to view products queried
                mInputManager.hideSoftInputFromWindow(mRecyclerView.getWindowToken(), 0);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(mProductsAdapter!=null){
                    //Filter product list based on text input
                    mProductsAdapter.filter(newText);
                }
                return true;
            }
        });
        return true;
    }

    /**
     * Method refreshes the list of Products and calls Async call to API
     *
     * @param reset A boolean to override if the list is reset
     */
    private void refreshProductList(boolean reset){
        if(reset || mProducts == null){
            // Async task to retrieve data and place in ArrayList
            mAsyncTask = new RetrieveProductListAsync();
            mAsyncTask.execute();
        }
    }

    /**
     * This Async task calls the products API Call and retrieves necessary fields
     */
    private class RetrieveProductListAsync extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            String reqURL = "https://shopicruit.myshopify.com/admin/products";

            Request request;
            request = new Request.Builder()
                    .url(reqURL + ".json?page=1&access_token=" + ACCESS_TOKEN +"&fields=id,image,title,body_html,product_type,tags")
                    .build();

            Response response = null;

            //GET request call
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //GET request return body, return json string
            try {
                if(response != null) {
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
            if (mSwipeRefreshLayout!=null){
                //If method was called with refresh layout, set refreshing to false
                mSwipeRefreshLayout.setRefreshing(false);
            }
            if (s==null){
                //If response back was invalid or failed, display toast
                Toast.makeText(mRecyclerView.getContext(), "Could not retrieve data, try again", Toast.LENGTH_SHORT).show();
            }
            else {
                //Build product list from json if successful
                Gson gson = new Gson();
                ProductsJSON test = gson.fromJson(s, ProductsJSON.class);
                mProducts = new ArrayList<>();
                mProducts = test.getProducts();
                initializeData();
            }
        }
    }

    /**
     * Method initializes all of the packages variables used within the activity,
     * including the adapters, layout managers, and listeners.
     */
    private void initializeData() {

        mProductsAdapter = new ProductsRecyclerAdapter(mRecyclerView.getContext(),
                mProducts, new RecyclerViewClickListener() {
            @Override
            public void recyclerViewListClicked(View v, int position) {
                if (position != -1){
                    //Retrieve Id from item clicked, and pass it into an intent
                    Intent intent = new Intent(v.getContext(), ProductDetailsActivity.class);
                    intent.putExtra("product_item", mProducts.get(position).getId());
                    startActivity(intent);
                }
            }
        });

        mLayoutManager = new GridLayoutManager(mRecyclerView.getContext(), 2);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setNestedScrollingEnabled(false);
        ViewCompat.setNestedScrollingEnabled(mRecyclerView, false);
        mRecyclerView.setAdapter(mProductsAdapter);

        //Initialize once for closing soft keyboard on scroll
        mInputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

    }


}
