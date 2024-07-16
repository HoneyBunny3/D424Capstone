package com.example.d424capstone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424capstone.MyApplication;
import com.example.d424capstone.R;
import com.example.d424capstone.adapters.StoreItemAdapter;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.StoreItem;

import java.util.List;

public class ShoppingScreen extends BaseActivity {
    private Repository repository;
    private RecyclerView recyclerView;
    private StoreItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_screen);

        repository = MyApplication.getInstance().getRepository(); // Initialize repository instance

        recyclerView = findViewById(R.id.storeItemRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        displayStoreItems();

        // Initialize buttons and set their click listeners
        initializeButtons();

        // Initialize the DrawerLayout and ActionBarDrawerToggle
        initializeDrawer();
    }

    private void displayStoreItems() {
        new Thread(() -> {
            List<StoreItem> storeItems = repository.getAllStoreItems();

            runOnUiThread(() -> {
                if (storeItems != null && !storeItems.isEmpty()) {
                    adapter = new StoreItemAdapter(this, storeItems);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(this, "No store items available", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void initializeButtons() {
        Button toCartButton = findViewById(R.id.toCartButton);
        toCartButton.setOnClickListener(v -> {
            Intent intent = new Intent(ShoppingScreen.this, CartSummaryScreen.class);
            startActivity(intent);
        });
    }
}