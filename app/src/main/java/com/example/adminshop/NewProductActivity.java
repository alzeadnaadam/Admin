package com.example.adminshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NewProductActivity extends AppCompatActivity {

    private EditText productNameEditText, productPriceEditText;
    private Spinner categorySpinner;
    private Button addProductButton;
    private DatabaseReference categoriesRef;
    private ArrayList<String> categoryNames;
    private ArrayAdapter<String> categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);
        productNameEditText = findViewById(R.id.etName);
        productPriceEditText = findViewById(R.id.etPrice);
        categorySpinner = findViewById(R.id.spinnerCategory);
        addProductButton = findViewById(R.id.btnCreate);
        categoriesRef = FirebaseDatabase.getInstance().getReference("Categories");

        categoryNames = new ArrayList<>();
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        // Load categories from Firebase
        loadCategories();

        addProductButton.setOnClickListener(view -> {
            String productName = productNameEditText.getText().toString().trim();
            String productPriceString = productPriceEditText.getText().toString().trim();
            String selectedCategory = categorySpinner.getSelectedItem().toString();

            if (!TextUtils.isEmpty(productName) && !TextUtils.isEmpty(productPriceString)) {
                float productPrice = Float.parseFloat(productPriceString);
                Product product = new Product(productName, productPrice);

                categoriesRef.orderByChild("name").equalTo(selectedCategory)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        String categoryId = snapshot.getKey();
                                        categoriesRef.child(categoryId).child("products").push().setValue(product);
                                        finish();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle error
                            }
                        });
            } else {
                if (TextUtils.isEmpty(productName)) {
                    productNameEditText.setError("Product name is required");
                }
                if (TextUtils.isEmpty(productPriceString)) {
                    productPriceEditText.setError("Product price is required");
                }
            }
        });
    }

    private void loadCategories() {
        categoriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryNames.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String categoryName = snapshot.child("name").getValue(String.class);
                    if (categoryName != null) {
                        categoryNames.add(categoryName);
                    }
                }
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}