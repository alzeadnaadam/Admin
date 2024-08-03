package com.example.adminshop;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class new_Category extends AppCompatActivity {

    private EditText etTitle;
    private Button btnCreate;
    private ListView listViewCategories;
    private DatabaseReference databaseCategories;
    private ArrayList<String> categoryNames;
    private ArrayAdapter<String> categoryAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);

        etTitle = findViewById(R.id.etName);
        btnCreate = findViewById(R.id.btnCreate);
        listViewCategories = findViewById(R.id.listViewCategories);

        databaseCategories = FirebaseDatabase.getInstance().getReference("Categories");

        categoryNames = new ArrayList<>();
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categoryNames);
        listViewCategories.setAdapter(categoryAdapter);

        loadCategories();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategory();
            }
        });
    }

    private void addCategory() {
        String categoryName = etTitle.getText().toString().trim();
        if (!categoryName.isEmpty()) {
            String id = databaseCategories.push().getKey();
            Category category = new Category(categoryName);
            databaseCategories.child(categoryName).setValue(category);
            Toast.makeText(this, "Category added", Toast.LENGTH_SHORT).show();
            etTitle.setText("");
            loadCategories(); // Refresh the category list
        } else {
            Toast.makeText(this, "Please enter a category name", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadCategories() {
        databaseCategories.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}