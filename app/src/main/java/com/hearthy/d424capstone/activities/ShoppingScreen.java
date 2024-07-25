package com.hearthy.d424capstone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hearthy.d424capstone.MyApplication;
import com.hearthy.d424capstone.R;
import com.hearthy.d424capstone.adapters.StoreItemAdapter;
import com.hearthy.d424capstone.database.Repository;
import com.hearthy.d424capstone.entities.StoreItem;

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

        initializeDrawer(); // Initialize the DrawerLayout and ActionBarDrawerToggle
        initViews(); // Initialize UI components
        initializeButtons(); // Initialize buttons and set their click listeners
        displayStoreItems(); // Display store items

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Initialize UI components
    private void initViews() {
        recyclerView = findViewById(R.id.storeItemRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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