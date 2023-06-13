package com.example.medease.Adapter;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medease.Model.Products;
import com.example.medease.MyCartActivity;
import com.example.medease.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyCartViewHolder> {

    Context context;
    List<Products> productsList;


    public MyCartAdapter(Context context, List<Products> productsList) {
        this.context = context;
        this.productsList = productsList;
    }

    @NonNull
    @Override
    public MyCartAdapter.MyCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.product_vertical, parent, false);
        return new MyCartAdapter.MyCartViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyCartAdapter.MyCartViewHolder holder, int position) {
        Picasso.get().load(productsList.get(position).getImageUrl()).into(holder.prodImage);
        holder.prodName.setText(productsList.get(position).getProductName());
        holder.prodPrice.setText(productsList.get(position).getProductPrice());
        holder.prodQuantity.setText(productsList.get(position).getProductQuantity());

        Log.e("Image Url",productsList.get(position).getImageUrl());
        Log.e("Name",productsList.get(position).getProductName());
        Log.e("Price",productsList.get(position).getProductPrice());


        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("MyCart").child(FirebaseAuth.getInstance().getUid()).child(holder.prodName.getText().toString());
                    databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent i = new Intent(context, MyCartActivity.class);
                            i.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
                            Toast.makeText(context, "Item Removed Successfully", Toast.LENGTH_SHORT).show();
                            context.startActivity(i);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "ITEM REMOVE FAILED", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public static final class MyCartViewHolder extends RecyclerView.ViewHolder{

        ImageView prodImage;
        TextView prodName, prodPrice,prodQuantity,prodRs;
        ImageView deleteBtn;

        public MyCartViewHolder(@NonNull View itemView) {
            super(itemView);

            prodImage = itemView.findViewById(R.id.prodImageMyCart);
            prodName = itemView.findViewById(R.id.itemNameViewOrder);
            prodPrice = itemView.findViewById(R.id.productPriceMyCart);
            deleteBtn = itemView.findViewById(R.id.deleteItemBtn);
            prodQuantity = itemView.findViewById(R.id.quantityMyCart);
            prodRs = itemView.findViewById(R.id.nrs);


        }
    }
}
