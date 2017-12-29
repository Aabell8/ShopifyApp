package com.austinabell8.shopifyapp.adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.austinabell8.shopifyapp.R;
import com.austinabell8.shopifyapp.model.Product;
import com.austinabell8.shopifyapp.utilities.RecyclerViewClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * ProductsRecyclerAdapter - Adapter for recycler view of products from MainActivity
 * @author  Austin Abell
 */

public class ProductsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Product> staticProducts;
    private List<Product> products;

    private Context mContext;
    private static RecyclerViewClickListener itemListener;

    public ProductsRecyclerAdapter(Context context, List<Product> products,
                                   RecyclerViewClickListener itemListener) {
        this.mContext = context;
        this.itemListener = itemListener;
        this.products = products;
        this.staticProducts = new ArrayList<>(products);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_product, parent, false);

        final ProductViewHolder mViewHolder = new ProductViewHolder(mView);

        mView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Calls interface on item clicked
                itemListener.recyclerViewListClicked(v, mViewHolder.getLayoutPosition());
            }
        });

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Product p = products.get(position);
        ProductViewHolder pHolder = (ProductViewHolder) holder;

        //Update data in ProductViewHolder
        pHolder.itemName.setText(p.getTitle());
        pHolder.textProductCode.setText(p.getDescription());
        Glide.with(pHolder.productThumbnail)
                .load(p.getImage().getSrc())
                .into(pHolder.productThumbnail);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    private class ProductViewHolder extends RecyclerView.ViewHolder {

        private TextView itemName;
        private TextView textProductCode;
        private ImageView productThumbnail;

        public ProductViewHolder(View view) {
            super(view);
            itemName = view.findViewById(R.id.txtProductName);
            textProductCode = view.findViewById(R.id.txtProductDescription);
            productThumbnail = view.findViewById(R.id.productThumbnail);
        }

    }

    /**
     * Method queries product list based on input text,
     * searches by title, description, tags, and product type currently
     * @param text  input text
     */
    public void filter(String text) {
        products.clear();
        if(text.isEmpty()){
            //Add products back when text is cleared
            products.addAll(staticProducts);
        } else{
            text = text.toLowerCase();
            for (Product item: staticProducts) {
                if(item.getTitle().toLowerCase().contains(text)
                        || item.getDescription().toLowerCase().contains(text)
                        || item.getTags().toLowerCase().contains(text)
                        || item.getProductType().toLowerCase().contains(text)){
                    products.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    /**
     * Method is used when exiting MainActivity not to save the queried list instead of full
     */
    public void reset() {
        products.clear();
        products.addAll(staticProducts);
        notifyDataSetChanged();
    }

}
