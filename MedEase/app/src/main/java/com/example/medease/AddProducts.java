package com.example.medease;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.medease.Model.Products;
import com.example.medease.databinding.ActivityAddProductsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class AddProducts extends AppCompatActivity {
    ActivityResultLauncher<Intent> activityResultLauncher;
    EditText productName, productPrice, productDescription;
    Uri imageUri;
    ImageView productImage;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    String typeOfProduct;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);
        productImage = findViewById(R.id.uploadImage);
        productPrice = findViewById(R.id.productPrice);
        productDescription = findViewById(R.id.productDescription);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        productName = findViewById(R.id.productName);
        radioGroup = findViewById(R.id.radioGroup);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id=group.getCheckedRadioButtonId();
                RadioButton rb=(RadioButton) findViewById(id);

                typeOfProduct=rb.getText().toString();

            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK && result.getData() != null){
                    imageUri = result.getData().getData();
                    productImage.setImageURI(imageUri);
                }
            }
        });

        findViewById(R.id.uploadImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                activityResultLauncher.launch(gallery);
            }
        });

        findViewById(R.id.doneBtn).setOnClickListener(view -> {
            if(productName.getText().toString().isEmpty() || productPrice.getText().toString().isEmpty() || productDescription.getText().toString().isEmpty()){
                productName.setError("Cannot be empty");
                productPrice.setError("Cannot be empty");
                productDescription.setError("Cannot be empty");
            }else{
                //Get a reference to the Firebase Storage location where you want to upload the image
                StorageReference imageRef = storageReference.child("products/"+productName.getText().toString()+".jpg");


                // Upload the image to Firebase Storage
                imageRef.putFile(imageUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            // Get the download URL of the uploaded image
                                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    // Save the download URL in Firebase Realtime Database as the image URL for the current user
                                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Products").child(typeOfProduct+"/"+productName.getText().toString());

                                    //Products product = new Products(productName.getText().toString(), productPrice.getText().toString(),uri.toString(), typeOfProduct, productDescription.getText().toString());

                                    HashMap<String,Object> map = new HashMap<>();
                                    map.put("productName", productName.getText().toString());
                                    map.put("productPrice",productPrice.getText().toString());
                                    map.put("imageUrl",uri.toString());
                                    map.put("Type",typeOfProduct);
                                    map.put("productDescription",productDescription.getText().toString());
                                    map.put("UserId",FirebaseAuth.getInstance().getCurrentUser().getUid());

                                    databaseReference.setValue(map).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(AddProducts.this, "Failed to set in database", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                });
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(AddProducts.this, "Error Sending image", Toast.LENGTH_SHORT).show();
                        });
            }

        });
    }
}