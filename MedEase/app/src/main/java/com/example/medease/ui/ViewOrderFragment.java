package com.example.medease.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.medease.Adapter.ViewOrderAdapter;
import com.example.medease.Model.Products;
import com.example.medease.R;
import com.example.medease.ViewOrderActivity;
import com.example.medease.databinding.ActivityViewOrderBinding;
import com.example.medease.databinding.FragmentMyCartBinding;
import com.example.medease.databinding.FragmentViewOrderBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewOrderFragment extends Fragment {
    FragmentViewOrderBinding binding;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<Products> productsList = new ArrayList<>();
    int totalPrice;
    String address;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentViewOrderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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


        return root;
    }

    public void setProdItemRecycler() {
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(ViewOrderFragment.this.getActivity(), RecyclerView.VERTICAL, false);

        binding.recyclerViewOrder.setLayoutManager(layoutManager);
        ViewOrderAdapter viewOrderAdapter = new ViewOrderAdapter(ViewOrderFragment.this.getActivity(), productsList);
        binding.recyclerViewOrder.setAdapter(viewOrderAdapter);
        viewOrderAdapter.notifyDataSetChanged();

    }
}