package com.example.adminshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private ImageView ImageViewLogout;
    private Button NewCategoryButton, NewProductButton;
    private FirebaseAuth mAuth;
    private OrdersAdapter ordersAdapter;
    private ListView listView;
    private ArrayList<Order> ordersList;
    private EditText edSearchOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        NewCategoryButton = findViewById(R.id.new_category_btn);
        NewProductButton = findViewById(R.id.new_product_btn);
        ImageViewLogout = findViewById(R.id.btn_logout);
        edSearchOrder = findViewById(R.id.edSearch);
        listView = findViewById(R.id.ListView);

        mAuth = FirebaseAuth.getInstance();
        ordersList = new ArrayList<>();
        ordersAdapter = new OrdersAdapter(HomeActivity.this, ordersList);
        listView.setAdapter(ordersAdapter);

        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Orders");

        // Load orders initially
        loadOrders(ordersRef);

        // Set up search functionality
        edSearchOrder.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchText = s.toString();
                if (TextUtils.isEmpty(searchText)) {
                    loadOrders(ordersRef);
                } else {
                    searchOrdersByPhoneNumber(ordersRef, searchText);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ImageViewLogout.setOnClickListener(view -> {
            mAuth.signOut();
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        NewCategoryButton.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, new_Category.class); // Ensure this is your new category activity
            startActivity(intent);
        });

        NewProductButton.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, NewProductActivity.class); // Ensure this is your new product activity
            startActivity(intent);
        });
    }

    private void loadOrders(DatabaseReference ordersRef) {
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ordersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("DataSnapshot", "Order data: " + snapshot.toString()); // Log the entire snapshot

                    // Extract values safely
                    String phoneNumber = snapshot.child("phoneNumber").getValue(String.class);
                    Float cost = snapshot.child("cost").getValue(Float.class);
                    String date = snapshot.child("date").getValue(String.class);

                    // Log the values before creating an Order
                    Log.d("OrderDetails", "Phone: " + phoneNumber + ", Cost: " + cost + ", Date: " + date);

                    // Create the Order object only if values are not null
                    if (phoneNumber != null && cost != null && date != null) {
                        Order order = new Order(phoneNumber, cost, date);
                        ordersList.add(order);
                    } else {
                        Log.w("OrderWarning", "Missing data for order: " + snapshot.getKey());
                    }
                }
                ordersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, "Failed to load orders", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void searchOrdersByPhoneNumber(DatabaseReference ordersRef, String phoneNumber) {
        Query query = ordersRef.orderByChild("phoneNumber").startAt(phoneNumber).endAt(phoneNumber + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ordersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("DataSnapshot", "Order data: " + snapshot.toString()); // Log DataSnapshot content
                    Order order = snapshot.getValue(Order.class);
                    if (order != null) {
                        ordersList.add(order);
                    }
                }
                ordersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, "Failed to search orders", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
