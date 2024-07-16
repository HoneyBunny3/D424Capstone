package com.example.d424capstone.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d424capstone.MyApplication;
import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.StoreItem;

public class PremiumProductManagementScreen extends BaseActivity {

    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_premium_product_management_screen); // Ensure the correct layout is set

        repository = MyApplication.getInstance().getRepository(); // Initialize repository instance

        // Initialize buttons and set their click listeners
        initializeButtons();

        // Initialize the DrawerLayout and ActionBarDrawerToggle
        initializeDrawer();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeButtons() {
        // Initialize buttons and set their click listeners
        Button buttonSave = findViewById(R.id.save);
        buttonSave.setOnClickListener(view -> saveProduct());

        Button buttonCancel = findViewById(R.id.cancel);
        buttonCancel.setOnClickListener(view -> finish());
    }

    private void saveProduct() {
        EditText productNameEditText = findViewById(R.id.product_name);
        EditText productDescriptionEditText = findViewById(R.id.product_description);
        EditText productPriceEditText = findViewById(R.id.product_price);
        CheckBox productIsPremiumCheckBox = findViewById(R.id.product_is_premium);

        String name = productNameEditText.getText().toString().trim();
        String description = productDescriptionEditText.getText().toString().trim();
        String priceText = productPriceEditText.getText().toString().trim();
        boolean isPremium = productIsPremiumCheckBox.isChecked();

        if (name.isEmpty() || description.isEmpty() || priceText.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceText);

        // Validate the premium item price
        if (isPremium && price == 0) {
            Toast.makeText(this, "Premium items cannot have a price of $0", Toast.LENGTH_SHORT).show();
            return;
        }

        StoreItem storeItem = new StoreItem(0, name, description, price, isPremium);
        repository.insertStoreItem(storeItem);

        Toast.makeText(this, "Product saved", Toast.LENGTH_SHORT).show();
        finish(); // Close the activity and return to the previous screen
    }

    @Override
    protected boolean shouldShowSearch() {
        return false; // Disable the search feature on this activity
    }
}