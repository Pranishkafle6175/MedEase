package com.example.medease.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.medease.Adapter.SearchShopAdapter;
import com.example.medease.Model.Products;
import com.example.medease.databinding.FragmentSearchShopBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchShopFragment extends Fragment {
    FragmentSearchShopBinding binding;
    List<Products> productsList;

    SearchShopAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSearchShopBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        productsList = new ArrayList<>();

        adapter = new SearchShopAdapter(getContext(),productsList);
        binding.shoplistrecyclerview.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.shoplistrecyclerview.setLayoutManager(layoutManager);

        binding.doctorSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUser(charSequence.toString());
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUser(charSequence.toString());
                Log.i("textt changed","searchtext");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        readitem();



        return root;
    }


    private void searchUser(String s) {

        Query databaseQuery = FirebaseDatabase.getInstance().getReference().child("Products");
        databaseQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productsList.clear();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot : childSnapshot.getChildren()) {
                        Products products = dataSnapshot.getValue(Products.class);
                        if (products != null && products.getProductName().startsWith(s)) {
                            productsList.add(products);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void readitem() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Products");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(TextUtils.isEmpty(binding.doctorSearch.getText().toString())){
                    productsList.clear();
                    for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                        for(DataSnapshot dataSnapshot : dataSnapshot1.getChildren()){
                            Products products= dataSnapshot.getValue(Products.class);
                            Log.i("SearchShopFRagment",products.getProductName());
                            productsList.add(products);
                        }
                        adapter.notifyDataSetChanged();

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}