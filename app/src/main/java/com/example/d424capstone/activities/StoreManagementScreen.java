package com.example.d424capstone.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424capstone.MyApplication;
import com.example.d424capstone.R;
import com.example.d424capstone.adapters.StoreItemManagementAdapter;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.StoreItem;

import java.util.List;

public class StoreManagementScreen extends BaseActivity {
    private Repository repository;
    private SharedPreferences sharedPreferences;
    private StoreItemManagementAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_store_management_screen);

        repository = MyApplication.getInstance().getRepository(); // Initialize repository instance
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE); // Initialize SharedPreferences

        initializeDrawer(); // Initialize the DrawerLayout and ActionBarDrawerToggle
        initViews(); // Initialize UI components
        refreshStoreItems(); // Load store items from the database

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Initialize UI components
    private void initViews() {
        recyclerView = findViewById(R.id.store_management_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button addButton = findViewById(R.id.addProductButton);
        addButton.setOnClickListener(v -> openAddItemDialog());
    }

    private void refreshStoreItems() {
        new Thread(() -> {
            List<StoreItem> storeItems = repository.getAllStoreItems();
            runOnUiThread(() -> {
                if (adapter == null) {
                    adapter = new StoreItemManagementAdapter(this, storeItems);
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.updateData(storeItems);
                }
            });
        }).start();
    }

    private void openAddItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.item_add_store_item, null);
        builder.setView(dialogView);

        EditText nameEditText = dialogView.findViewById(R.id.nameEditText);
        EditText descriptionEditText = dialogView.findViewById(R.id.descriptionEditText);
        EditText priceEditText = dialogView.findViewById(R.id.priceEditText);
        CheckBox isPremiumCheckBox = dialogView.findViewById(R.id.isPremiumCheckBox);
        Button saveButton = dialogView.findViewById(R.id.saveButton);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);

        AlertDialog dialog = builder.create();

        saveButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String description = descriptionEditText.getText().toString().trim();
            String priceText = priceEditText.getText().toString().trim();
            boolean isPremium = isPremiumCheckBox.isChecked();

            if (name.isEmpty() || description.isEmpty() || priceText.isEmpty()) {
                Toast.makeText(StoreManagementScreen.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceText);
            } catch (NumberFormatException e) {
                Toast.makeText(StoreManagementScreen.this, "Please enter a valid price", Toast.LENGTH_SHORT).show();
                return;
            }

            if (price == 0) {
                Toast.makeText(StoreManagementScreen.this, "Price cannot be $0", Toast.LENGTH_SHORT).show();
                return;
            }

            StoreItem newItem = new StoreItem(0, name, description, price, isPremium);
            new Thread(() -> {
                repository.insertStoreItem(newItem);
                runOnUiThread(() -> {
                    refreshStoreItems();
                    dialog.dismiss();
                    Toast.makeText(StoreManagementScreen.this, "Item added", Toast.LENGTH_SHORT).show();
                });
            }).start();
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    @Override
    protected boolean shouldShowSearch() {
        return false; // Disable the search feature on this activity
    }
}