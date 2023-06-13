package com.example.medease;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.medease.Adapter.MyCartAdapter;
import com.example.medease.Adapter.ViewOrderAdapter;
import com.example.medease.Model.Products;
import com.example.medease.databinding.ActivityChangePasswordBinding;
import com.example.medease.databinding.ActivityViewOrderBinding;
import com.example.medease.ui.ViewOrderFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewOrderActivity extends AppCompatActivity {
    ActivityViewOrderBinding binding;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<Products> productsList = new ArrayList<>();
    int totalPrice;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewOrderBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        totalPrice = 0;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("MyPastOrders");

        databaseReference.child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    //                Toast.makeText(ViewOrderActivity.this,dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                    Products products = dataSnapshot.getValue(Products.class);
                    Log.e("NAME VIEW ORDER",products.getProductName());
                    address = dataSnapshot.child("Address").getValue().toString();
                    Log.i("Address",address);
                    Log.i("Price",products.getProductPrice());
                    productsList.add(products);
                    totalPrice += (Integer.parseInt(products.getProductQuantity())*Integer.parseInt(products.getProductPrice()));

                }

                binding.totalCost.setText(Integer.toString(totalPrice));
                binding.deliveryAddress.setText(address);
                setProdItemRecycler();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    public void setProdItemRecycler() {
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(ViewOrderActivity.this, RecyclerView.VERTICAL, false);

        binding.recyclerViewOrder.setLayoutManager(layoutManager);
        ViewOrderAdapter viewOrderAdapter = new ViewOrderAdapter(ViewOrderActivity.this, productsList);
        binding.recyclerViewOrder.setAdapter(viewOrderAdapter);
        viewOrderAdapter.notifyDataSetChanged();

    }
}