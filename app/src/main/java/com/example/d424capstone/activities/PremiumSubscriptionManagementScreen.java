package com.example.d424capstone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d424capstone.MyApplication;
import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.PremiumStorefront;
import com.example.d424capstone.entities.User;

import java.util.List;

public class PremiumSubscriptionManagementScreen extends BaseActivity {

    private Repository repository;
    private TextView firstNameTextView;
    private TextView lastNameTextView;
    private TextView emailTextView;
    private TextView phoneTextView;
    private EditText storefrontNameEditText;
    private EditText storefrontEmailEditText;
    private User currentUser;
    private PremiumStorefront currentStorefront;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_premium_subscription_management_screen); // Ensure the correct layout is set

        repository = MyApplication.getInstance().getRepository(); // Use repository from MyApplication

        firstNameTextView = findViewById(R.id.first_name);
        lastNameTextView = findViewById(R.id.last_name);
        emailTextView = findViewById(R.id.email);
        phoneTextView = findViewById(R.id.phone);
        storefrontNameEditText = findViewById(R.id.storefront_name);
        storefrontEmailEditText = findViewById(R.id.storefront_contact_email);

        // Fetch and display premium user information
        displayPremiumUserInfo();

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
        Button buttonAdd = findViewById(R.id.add_product_button);
        buttonAdd.setOnClickListener(view -> startActivity(new Intent(PremiumSubscriptionManagementScreen.this, PremiumProductManagementScreen.class)));

        Button buttonSaveStorefront = findViewById(R.id.save_storefront_button);
        buttonSaveStorefront.setOnClickListener(view -> saveStorefrontInfo());
    }

    private void displayPremiumUserInfo() {
        new Thread(() -> {
            currentUser = repository.getCurrentUser();
            if (currentUser != null && "PREMIUM".equals(currentUser.getRole())) {
                runOnUiThread(() -> {
                    firstNameTextView.setText(currentUser.getFirstName());
                    lastNameTextView.setText(currentUser.getLastName());
                    emailTextView.setText(currentUser.getEmail());
                    phoneTextView.setText(currentUser.getPhone());
                });

                List<PremiumStorefront> storefronts = repository.getPremiumStorefrontsByUserID(currentUser.getUserID());
                if (storefronts != null && !storefronts.isEmpty()) {
                    currentStorefront = storefronts.get(0); // Assuming a user has one storefront
                    runOnUiThread(() -> {
                        storefrontNameEditText.setText(currentStorefront.getName());
                        storefrontEmailEditText.setText(currentStorefront.getEmail());
                    });
                }
            } else {
                runOnUiThread(() -> {
                    firstNameTextView.setText("N/A");
                    lastNameTextView.setText("N/A");
                    emailTextView.setText("N/A");
                    phoneTextView.setText("N/A");
                    storefrontNameEditText.setText("");
                    storefrontEmailEditText.setText("");
                });
            }
        }).start();
    }

    private void saveStorefrontInfo() {
        if (currentUser != null && "PREMIUM".equals(currentUser.getRole())) {
            String storefrontName = storefrontNameEditText.getText().toString();
            String storefrontEmail = storefrontEmailEditText.getText().toString();

            if (currentStorefront == null) {
                currentStorefront = new PremiumStorefront(0, storefrontName, storefrontEmail, currentUser.getUserID());
                repository.insertPremiumStorefront(currentStorefront);
            } else {
                currentStorefront.setName(storefrontName);
                currentStorefront.setEmail(storefrontEmail);
                repository.updatePremiumStorefront(currentStorefront);
            }

            Toast.makeText(this, "Storefront info saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error: User not premium or not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected boolean shouldShowSearch() {
        return false; // Disable the search feature on this activity
    }
}