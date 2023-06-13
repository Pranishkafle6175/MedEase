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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.medease.Model.Products;
import com.example.medease.ProductDetails;
import com.example.medease.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    Context context;
    List<Products> productsList;


    public ProductAdapter(Context context, List<Products> productsList) {
        this.context = context;
        this.productsList = productsList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.products_row_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Picasso.get().load(productsList.get(position).getImageUrl()).into(holder.prodImage);
        holder.prodName.setText(productsList.get(position).getProductName());
        holder.prodPrice.setText(productsList.get(position).getProductPrice());

        holder.prodImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ProductDetails.class);
                i.putExtra("Product_Name",productsList.get(position).getProductName());
                i.putExtra("Product_Price", productsList.get(position).getProductPrice());
                i.putExtra("Image_Url",productsList.get(position).getImageUrl());
                i.putExtra("Product_Description",productsList.get(position).getProductDescription());

/*
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(holder.prodImage, "image");
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity) context, pairs);
               */
                context.startActivity(i/*, activityOptions.toBundle()*/);
            }
        });

        holder.addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference();
                productsList.get(position).setProductQuantity("1");

                databaseReference.child("MyCart").child(FirebaseAuth.getInstance().getUid()).child(holder.prodName.getText().toString()).setValue(productsList.get(position)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "Added To Cart Successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public static final class ProductViewHolder extends RecyclerView.ViewHolder{

        ImageView prodImage;
        TextView prodName, prodQty, prodPrice;
        Button addToCartBtn;
        CardView cardView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            prodImage = itemView.findViewById(R.id.prodImageMyCart);
            prodName = itemView.findViewById(R.id.itemNameViewOrder);
            prodPrice = itemView.findViewById(R.id.productPriceMyCart);
             // prodQty = itemView.findViewById(R.id.prod_qty);
            addToCartBtn = itemView.findViewById(R.id.addToCartBtn);


        }
    }

}