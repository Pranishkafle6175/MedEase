package com.example.medease.ui;

import static org.webrtc.ContextUtils.getApplicationContext;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medease.Adapter.DoctorListAdapter;
import com.example.medease.Adapter.ParentItemAdapter;
import com.example.medease.Adapter.ProductAdapter;
import com.example.medease.Adapter.ProductCategoryAdapter;
import com.example.medease.Adapter.SearchAdapter;
import com.example.medease.AddProducts;
import com.example.medease.Model.Doctors;
import com.example.medease.Model.ParentItem;
import com.example.medease.Model.ProductCategory;
import com.example.medease.Model.Products;
import com.example.medease.MyCartActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.example.medease.databinding.FragmentShopBinding;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShopFragment extends Fragment {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private FragmentShopBinding binding;

    List<Products> productsList;

    List<Products> productsList1 = new ArrayList<>();
    List<Products> productsList2 = new ArrayList<>();
    List<Products> productsList3 = new ArrayList<>();

    SearchAdapter searchAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentShopBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        productsList = new ArrayList<>();
        searchAdapter = new SearchAdapter(getContext(),productsList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShopFragment.this.getActivity());

        GenericTypeIndicator<HashMap<String, Object>> typeIndicator = new GenericTypeIndicator<HashMap<String, Object>>() {};


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Products");

        binding.addProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShopFragment.this.getActivity(), AddProducts.class));
            }
        });





        List<ProductCategory> productCategoryList = new ArrayList<>();
        productCategoryList.add(new ProductCategory(3, "Medicines"));
        productCategoryList.add(new ProductCategory(4, "Nutrition"));
        productCategoryList.add(new ProductCategory(5, "Other"));
        productCategoryList.add(new ProductCategory(1, "Trending"));
        productCategoryList.add(new ProductCategory(2, "Most Popular"));


        setProductRecycler(productCategoryList);




        databaseReference.child("Medicines").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Products products = dataSnapshot.getValue(Products.class);
                    productsList1.add(products);
                }
                databaseReference.child("Nutrition").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Products products = dataSnapshot.getValue(Products.class);
                            productsList2.add(products);

                        }
                        databaseReference.child("Other").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Products products = dataSnapshot.getValue(Products.class);
                                    productsList3.add(products);


                                }
                                LinearLayoutManager layoutManager = new LinearLayoutManager(ShopFragment.this.getActivity());

                                // Pass the arguments
                                // to the parentItemAdapter.
                                // These arguments are passed
                                // using a method ParentItemList()
                                ParentItemAdapter parentItemAdapter = new ParentItemAdapter(ShopFragment.this.getActivity(),ParentItemList());

                                // Set the layout manager
                                // and adapter for items
                                // of the parent recyclerview
                                binding.parentRecyclerview.setAdapter(parentItemAdapter);
                                binding.parentRecyclerview.setLayoutManager(layoutManager);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(ShopFragment.this.getActivity(), "Error:"+error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ShopFragment.this.getActivity(), "Error:"+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        readUser();

        return root;
    }


    private List<ParentItem> ParentItemList()
    {
        List<ParentItem> itemList = new ArrayList<>();

        ParentItem item1 = new ParentItem("Medicines", productsList1);
        itemList.add(item1);
        ParentItem item2 = new ParentItem("Nutrition", productsList2);
        itemList.add(item2);
        ParentItem item3 = new ParentItem("Others", productsList3);
        itemList.add(item3);

        return itemList;
    }

    private void setProductRecycler(List<ProductCategory> productCategoryList){

        RecyclerView productCatRecycler = binding.catRecycler;
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(ShopFragment.this.getActivity(), RecyclerView.HORIZONTAL, false);
        productCatRecycler.setLayoutManager(layoutManager);
        ProductCategoryAdapter productCategoryAdapter = new ProductCategoryAdapter(ShopFragment.this.getActivity(), productCategoryList);
        productCatRecycler.setAdapter(productCategoryAdapter);

    }






    private void searchUser(String s) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {

                    Query databasequery = productRef.child(productSnapshot.getKey()).orderByChild("productName").startAt(s).endAt(s +"\uf8ff");
                    databasequery.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            productsList.clear();
                            for(DataSnapshot shot : snapshot.getChildren()){
                                Products products= shot.getValue(Products.class);
                                productsList.add(products);
                                Log.i("search found","search found");
                                Log.i("Product Search",products.getProductName());
                            }
                            searchAdapter.notifyDataSetChanged();
                            searchAdapter.updateData(productsList);
//                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShopFragment.this.getActivity());
//                            binding.searchRecycler.setLayoutManager(linearLayoutManager);
//                            binding.searchRecycler.setAdapter(searchAdapter);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                    searchAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // handle error
            }
        });


    }

//
//    private void readUser() {
//
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child("Doctor");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(TextUtils.isEmpty(binding.searchView.getText().toString())){
//                    doctorsList.clear();
//                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                        Doctors doctors = dataSnapshot.getValue(Doctors.class);
//                        doctorsList.add(doctors);
//                    }
//                    doctorListAdapter = new DoctorListAdapter(ShopFragment.this,doctorsList);
//                    binding.userlistrecyclerview.setAdapter(doctorListAdapter);
//                    doctorListAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}