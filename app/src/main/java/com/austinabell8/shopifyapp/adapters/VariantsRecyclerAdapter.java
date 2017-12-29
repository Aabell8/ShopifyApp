package com.austinabell8.shopifyapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.austinabell8.shopifyapp.R;
import com.austinabell8.shopifyapp.model.Variant;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * VariantsRecyclerAdapter - Adapter class for variant list in product details page
 * @author  Austin Abell
 */

public class VariantsRecyclerAdapter extends RecyclerView.Adapter<VariantsRecyclerAdapter.VariantViewHolder> {

    private List<Variant> mVariants;

    public VariantsRecyclerAdapter(ArrayList<Variant> variants) {
        mVariants = variants;
    }

    @Override
    public VariantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.object_variant, parent, false);
        return new VariantViewHolder(v);
    }


    @Override
    public void onBindViewHolder(VariantViewHolder holder, int position) {
        //Initialize data on each ViewHolder
        holder.setTitle(mVariants.get(position).getTitle());
        holder.setPrice("$" + new DecimalFormat("#.00").format(mVariants.get(position).getPrice()));
    }

    @Override
    public int getItemCount() {
        return mVariants.size();
    }

    static class VariantViewHolder extends RecyclerView.ViewHolder {

        VariantViewHolder(View cardView) {
            super(cardView);
            mTextViewTitle = cardView.findViewById(R.id.tvVariantTitle);
            mTextViewPrice = cardView.findViewById(R.id.tvVariantPrice);
            mImageView = cardView.findViewById(R.id.ivVariantImage);
        }

        private TextView mTextViewTitle;
        private TextView mTextViewPrice;
        private ImageView mImageView;

        void setTitle(String text) {
            mTextViewTitle.setText(text);
        }

        void setPrice(String text) {
            mTextViewPrice.setText(text);
        }

    }
}
