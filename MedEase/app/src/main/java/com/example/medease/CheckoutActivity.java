package com.example.medease;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.medease.Model.Products;
import com.example.medease.databinding.ActivityCheckoutBinding;
import com.example.medease.databinding.ActivityProductDetailsBinding;
import com.example.medease.ui.ShopFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    ActivityCheckoutBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        DatabaseReference orderRef = databaseReference.child("MyOrders").child(FirebaseAuth.getInstance().getUid());
        DatabaseReference cartRef = databaseReference.child("MyCart").child(FirebaseAuth.getInstance().getUid());

        Bundle bundle =getIntent().getExtras();
        String totalPrice = bundle.getString("TotalPrice");
        binding.productCost.setText("Rs "+totalPrice);
        String totalPayment = Integer.toString(Integer.parseInt(totalPrice) + 150);
        binding.totalPayment.setText("Rs "+totalPayment);

        HashMap<String,Object> map = new HashMap<>();

        HashMap<String,Object> map1 = new HashMap<>();

        binding.medeasepointcardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.address.getText().toString().isEmpty()){
                    binding.address.setError("Empty Address");
                }else{

                    cartRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Products products = dataSnapshot.getValue(Products.class);
                                Log.e("Name of PRODUCT:",products.getProductName());
                                Log.e("PRICE:",products.getProductPrice());


                                HashMap<String,Object> data = new HashMap<>();
                                data.put("imageUrl",products.getImageUrl());
                                data.put("UserID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                data.put("Address",binding.address.getText().toString());
                                data.put("productName",products.getProductName());
                                data.put("productPrice",products.getProductPrice());
                                data.put("productQuantity",products.getProductQuantity());
                                FirebaseDatabase.getInstance().getReference().child("MyPastOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(products.getProductName()).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.i("MY pastOrder","MY pastOrder");
                                        cartRef.removeValue();

                                    }
                                });




                            }

                            //cartRef.removeValue();
                            Toast.makeText(CheckoutActivity.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CheckoutActivity.this,PaymentSuccessActivity.class));

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });

        binding.esewacardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CheckoutActivity.this, "Feature available soon", Toast.LENGTH_SHORT).show();
            }
        });

        binding.imaepaycardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CheckoutActivity.this, "Feature available soon", Toast.LENGTH_SHORT).show();

            }
        });

        binding.khalticardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CheckoutActivity.this, "Feature available soon", Toast.LENGTH_SHORT).show();

            }
        });
    }
}