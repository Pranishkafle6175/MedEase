package com.example.medease;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.medease.Model.Products;
import com.example.medease.databinding.ActivityProductDetailsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ProductDetails extends AppCompatActivity {
    ActivityProductDetailsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Bundle bundle = getIntent().getExtras();


        binding.currentNumber.setText("1");
        int currentNum =Integer.parseInt(binding.currentNumber.getText().toString());
        Toast.makeText(this, "currentNum:"+currentNum, Toast.LENGTH_SHORT).show();
        NumberPicker numberPicker = new NumberPicker(currentNum);

        Picasso.get().load(bundle.getString("Image_Url")).into(binding.prodImageProductDetais);
        binding.prodNameProductDetails.setText(bundle.getString("Product_Name"));
        binding.prodPriceProductDetails.setText(bundle.getString("Product_Price"));

        binding.prodDescriptionProductDetails.setText(bundle.getString("Product_Description"));

        binding.btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberPicker.decrease();
                //Toast.makeText(ProductDetails.this, "NUM:"+numberPicker.getDisplayNum(), Toast.LENGTH_SHORT).show();
                binding.currentNumber.setText(Integer.toString(numberPicker.getDisplayNum()));
            }
        });

        binding.btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberPicker.increase();
                //Toast.makeText(ProductDetails.this, "NUM:"+numberPicker.getDisplayNum(), Toast.LENGTH_SHORT).show();

                binding.currentNumber.setText(Integer.toString(numberPicker.getDisplayNum()));
            }
        });

        binding.addToCartProdDetais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference();
                Products product = new Products(bundle.getString("Product_Name"),bundle.getString("Product_Price"),bundle.getString("Image_Url"),bundle.getString("Product_Description"));
                product.setProductQuantity(Integer.toString(numberPicker.getDisplayNum()));
                databaseReference.child("MyCart").child(FirebaseAuth.getInstance().getUid()).child(bundle.getString("Product_Name")).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ProductDetails.this, "Added To Cart Successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProductDetails.this, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
;
    }


    public class NumberPicker{
        int min;
        int max;
        int displayNum;

        public NumberPicker(){
            min = 1;
            max = 10;
            displayNum = 1;
        }
        public NumberPicker(int currentNum){
            min = 1;
            max = 10;
            this.displayNum = currentNum;
        }

        public void setMin(int min){
            this.min = min;
        }
        public void setMax(int max) {
            this.max = max;
        }

        public int getDisplayNum(){
            return displayNum;
        }
        public void increase(){
            if(displayNum< max){
                displayNum++;
            }
        }

        public void decrease(){
            if(displayNum>min){
                displayNum--;
            }
        }
    }
}