package com.example.d424capstone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.d424capstone.MyApplication;
import com.example.d424capstone.R;
import com.example.d424capstone.adapters.StoreItemAdapter;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.StoreItem;

import java.util.List;

public class ShoppingScreen extends BaseActivity {

    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_screen);

        repository = MyApplication.getInstance().getRepository(); // Use repository from MyApplication

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
                ListView listView = findViewById(R.id.storeItemListView);
                StoreItemAdapter adapter = new StoreItemAdapter(this, storeItems);
                listView.setAdapter(adapter);
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