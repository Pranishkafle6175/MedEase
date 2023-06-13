package com.example.medease.Adapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.medease.Model.Products;
import com.example.medease.ProductDetails;
import com.example.medease.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    Context context;
    List<Products> productsList;



    public SearchAdapter(Context context, List<Products> productsList) {
        this.context = context;
        this.productsList = productsList;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.product_search, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchViewHolder holder, int position) {

        Log.i("SearchAdapter class","SearchAdapter class");
        Products products = productsList.get(position);
        Log.i("Product List",products.getProductName());
        Picasso.get().load(products.getImageUrl()).into(holder.prodImage);
        holder.prodName.setText(products.getProductName());
        holder.prodPrice.setText(products.getProductPrice());

        holder.prodImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ProductDetails.class);
/*
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(holder.prodImage, "image");
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity) context, pairs);
               */
                context.startActivity(i/*, activityOptions.toBundle()*/);
            }
        });
    }

    @Override
    public int getItemCount() {

        Log.i("Search Adapter", String.valueOf(productsList.size()));
        return productsList.size();
    }

    public static final class SearchViewHolder extends RecyclerView.ViewHolder{

        ImageView prodImage;
        TextView prodName, prodQty, prodPrice;
        Button addToCartBtn;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            prodImage = itemView.findViewById(R.id.prodImageSpecificCategory);
            prodName = itemView.findViewById(R.id.itemNameViewOrder);
            prodPrice = itemView.findViewById(R.id.productPriceMyCart);
           // prodQty = itemView.findViewById(R.id.prod_qty);
            addToCartBtn = itemView.findViewById(R.id.addToCartBtn);


        }
    }

    public void updateData(List<Products> productsList) {
        this.productsList = productsList;
        notifyDataSetChanged();
    }

}

