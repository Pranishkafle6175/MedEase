package com.example.medease;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.medease.Adapter.MyCartAdapter;
import com.example.medease.Adapter.SpecificCategoryAdapter;
import com.example.medease.Model.Products;
import com.example.medease.databinding.ActivityCheckoutBinding;
import com.example.medease.databinding.ActivitySpecificCategoryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SpecificCategoryActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<Products> productsList = new ArrayList<>();
    SpecificCategoryAdapter specificCategoryAdapter;
    RecyclerView recyclerView;
    ActivitySpecificCategoryBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpecificCategoryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        recyclerView = binding.recyclerVIew;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        Bundle bundle = getIntent().getExtras();
        String type = bundle.getString("Product_Category");

        databaseReference.child("Products").child(type).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Products products = dataSnapshot.getValue(Products.class);
                    productsList.add(products);

                }
                setProdItemRecycler();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void setProdItemRecycler() {
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(SpecificCategoryActivity.this, RecyclerView.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        specificCategoryAdapter = new SpecificCategoryAdapter(SpecificCategoryActivity.this, productsList);
        recyclerView.setAdapter(specificCategoryAdapter);
        specificCategoryAdapter.notifyDataSetChanged();

    }
}