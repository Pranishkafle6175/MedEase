package com.example.medease.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.medease.Adapter.MyCartAdapter;
import com.example.medease.CheckoutActivity;
import com.example.medease.Model.Products;
import com.example.medease.MyCartActivity;
import com.example.medease.R;
import com.example.medease.databinding.FragmentMyCartBinding;
import com.example.medease.databinding.FragmentShopBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyCartFragment extends Fragment {

    FragmentMyCartBinding binding;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    public MyCartAdapter myCartAdapter;
    List<Products> productsList = new ArrayList<>();
    RecyclerView recyclerView;
    TextView totalPriceText;
    int totalPrice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        totalPrice = 0;
        totalPriceText = binding.totalPrice;
        recyclerView = binding.myCartRecycler;
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

        binding.buttonCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyCartFragment.this.getActivity() , CheckoutActivity.class);
                intent.putExtra("TotalPrice",Integer.toString(totalPrice));
                startActivity(intent);
            }
        });

        return  root;
    }

    public void setProdItemRecycler() {
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(MyCartFragment.this.getActivity(), RecyclerView.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        myCartAdapter = new MyCartAdapter(MyCartFragment.this.getActivity(), productsList);
        recyclerView.setAdapter(myCartAdapter);
        myCartAdapter.notifyDataSetChanged();

    }
}