package com.example.medease.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medease.Model.ProductCategory;
import com.example.medease.Model.Products;
import com.example.medease.ProductDetails;
import com.example.medease.R;
import com.example.medease.SpecificCategoryActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SpecificCategoryAdapter extends RecyclerView.Adapter<SpecificCategoryAdapter.SpecificCategoryViewHolder> {

    Context context;
    List<Products> productList;

    public SpecificCategoryAdapter(Context context, List<Products> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public SpecificCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.specific_category_items, parent, false);
        // lets create a recyclerview row item layout file
        return new SpecificCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecificCategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.prodName.setText(productList.get(position).getProductName());
        holder.prodPrice.setText(productList.get(position).getProductPrice());
        Picasso.get().load(productList.get(position).getImageUrl()).into(holder.prodImage);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    public static final class SpecificCategoryViewHolder extends RecyclerView.ViewHolder{


        ImageView prodImage;
        TextView prodName;
        TextView prodPrice;
        Button button;

        public SpecificCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            prodImage = itemView.findViewById(R.id.prodImageSpecificCategory);
            prodName = itemView.findViewById(R.id.prodNameSpecificCategory);
            prodPrice = itemView.findViewById(R.id.prodPriceSpecificCategory);
            button = itemView.findViewById(R.id.buttonSpecificCategory);

        }
    }

}