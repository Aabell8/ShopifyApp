package com.austinabell8.shopifyapp.utilities;

import android.view.View;

/**
 * RecyclerViewClickListener - Interface class used with ProductsRecyclerAdapter to notify
 *                              which recycler view item is clicked in MainActivity
 * @author  Austin Abell
 */

public interface RecyclerViewClickListener {
    void recyclerViewListClicked(View v, int position);
}
