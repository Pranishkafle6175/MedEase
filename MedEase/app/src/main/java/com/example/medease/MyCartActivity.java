package com.example.medease;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medease.Adapter.MyCartAdapter;
import com.example.medease.Adapter.ProductAdapter;
import com.example.medease.Model.Products;
import com.example.medease.ui.ShopFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyCartActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    public MyCartAdapter myCartAdapter;
    List<Products> productsList = new ArrayList<>();
    RecyclerView recyclerView;
    TextView totalPriceText;
    int totalPrice;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        totalPrice = 0;
        totalPriceText = findViewById(R.id.totalPrice);
        recyclerView = findViewById(R.id.myCartRecycler);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        databaseReference.child("MyCart").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Products products = dataSnapshot.getValue(Products.class);
                    productsList.add(products);
                    totalPrice += (Integer.parseInt(products.getProductQuantity())*Integer.parseInt(products.getProductPrice()));
                }
                setProdItemRecycler();

                totalPriceText.setText(Integer.toString(totalPrice));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        findViewById(R.id.buttonCheckout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyCartActivity.this ,CheckoutActivity.class);
                intent.putExtra("TotalPrice",Integer.toString(totalPrice));
                startActivity(intent);
            }
        });
    }


    public void setProdItemRecycler() {
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(MyCartActivity.this, RecyclerView.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        myCartAdapter = new MyCartAdapter(MyCartActivity.this, productsList);
        recyclerView.setAdapter(myCartAdapter);
        myCartAdapter.notifyDataSetChanged();

    }

}