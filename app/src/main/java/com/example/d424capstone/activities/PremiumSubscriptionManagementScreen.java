package com.example.d424capstone.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.User;
import com.example.d424capstone.utilities.UserRoles;

public class PremiumSubscriptionManagementScreen extends BaseActivity {

    private Repository repository;
    private SharedPreferences sharedPreferences;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new Repository(getApplication());
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        currentUser = repository.getCurrentUser();

        EdgeToEdge.enable(this);

        currentUser = repository.getCurrentUser();

        if (currentUser == null) {
            // If there's no logged-in user, finish the activity
            showLoginSignupDialog();
            finish();
            return;
        }

        if (isPremiumUser()) {
            setContentView(R.layout.activity_premium_subscription_management_screen);
            setupPremiumUserManagement();
        } else {
            setContentView(R.layout.activity_premium_sign_up_screen);
            setupSubscriptionSignUp();
        }

        // Initialize the DrawerLayout and ActionBarDrawerToggle
        initializeDrawer();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private boolean isPremiumUser() {
        String userRole = sharedPreferences.getString("UserRole", UserRoles.REGULAR);
        return UserRoles.PREMIUM.equals(userRole);
    }

    private void setupSubscriptionSignUp() {
        EditText firstNameEditText = findViewById(R.id.first_name);
        EditText lastNameEditText = findViewById(R.id.last_name);
        EditText emailEditText = findViewById(R.id.email);
        EditText phoneNumberEditText = findViewById(R.id.phone_number);
        EditText creditCardEditText = findViewById(R.id.credit_card);

        // Pre-fill user information
        if (currentUser != null) {
            firstNameEditText.setText(currentUser.getFirstName());
            lastNameEditText.setText(currentUser.getLastName());
            emailEditText.setText(currentUser.getEmail());
            phoneNumberEditText.setText(currentUser.getPhoneNumber());
        }

        Button subscribeButton = findViewById(R.id.subscribe_button);
        subscribeButton.setOnClickListener(v -> {
            // Save subscription info to the database
            currentUser.setFirstName(firstNameEditText.getText().toString());
            currentUser.setLastName(lastNameEditText.getText().toString());
            currentUser.setEmail(emailEditText.getText().toString());
            currentUser.setPhoneNumber(phoneNumberEditText.getText().toString());
            // currentUser.setCreditCard(creditCardEditText.getText().toString()); // Assuming there is a field for this
            currentUser.setRole(UserRoles.PREMIUM);

            repository.updateUser(currentUser);

            // Update user role in shared preferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("UserRole", UserRoles.PREMIUM);
            editor.apply();

            // Reload the activity to display the premium user management page
            recreate();
        });
    }

    private void setupPremiumUserManagement() {
        // Display user information
        TextView firstNameTextView = findViewById(R.id.first_name);
        TextView lastNameTextView = findViewById(R.id.last_name);
        TextView emailTextView = findViewById(R.id.email);

        if (currentUser != null) {
            firstNameTextView.setText(currentUser.getFirstName());
            lastNameTextView.setText(currentUser.getLastName());
            emailTextView.setText(currentUser.getEmail());
        }

        // Set up storefront management
        EditText storefrontNameEditText = findViewById(R.id.storefront_name);
        EditText storefrontContactEmailEditText = findViewById(R.id.storefront_contact_email);

        if (currentUser != null) {
            // Pre-fill storefront info if available
            storefrontNameEditText.setText(currentUser.getStorefrontName());
            storefrontContactEmailEditText.setText(currentUser.getStorefrontContactEmail());
        }

        Button addProductButton = findViewById(R.id.add_product_button);
        addProductButton.setOnClickListener(v -> {
            // Handle adding products
            String storefrontName = storefrontNameEditText.getText().toString();
            String storefrontContactEmail = storefrontContactEmailEditText.getText().toString();

            // Save storefront info to the database
            currentUser.setStorefrontName(storefrontName);
            currentUser.setStorefrontContactEmail(storefrontContactEmail);

            repository.updateUser(currentUser);

            // Navigate to a product management page (if needed)
            Toast.makeText(this, "Product management setup", Toast.LENGTH_SHORT).show();
        });
    }
}