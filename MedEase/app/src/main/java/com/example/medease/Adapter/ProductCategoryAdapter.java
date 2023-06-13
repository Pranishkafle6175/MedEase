package com.example.medease.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medease.Model.ProductCategory;
import com.example.medease.ProductDetails;
import com.example.medease.R;
import com.example.medease.SpecificCategoryActivity;

import java.util.List;

public class ProductCategoryAdapter extends RecyclerView.Adapter<ProductCategoryAdapter.ProductViewHolder> {

    Context context;
    List<ProductCategory> productCategoryList;

    public ProductCategoryAdapter(Context context) {
        this(context, null);
    }

    public ProductCategoryAdapter(Context context, List<ProductCategory> productCategoryList) {
        this.context = context;
        this.productCategoryList = productCategoryList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.category_row_item, parent, false);
        // lets create a recyclerview row item layout file
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.catagoryName.setText(productCategoryList.get(position).getProductName());
        holder.catagoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, SpecificCategoryActivity.class);
                i.putExtra("Product_Category",productCategoryList.get(position).getProductName());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productCategoryList.size();
    }


    public static final class ProductViewHolder extends RecyclerView.ViewHolder{


        TextView catagoryName;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            catagoryName = itemView.findViewById(R.id.cat_name);

        }
    }

}