package com.example.d424capstone.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d424capstone.MyApplication;
import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.User;
import com.example.d424capstone.utilities.UserRoles;

public class PremiumSignUpScreen extends BaseActivity {

    private Repository repository;
    private SharedPreferences sharedPreferences;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_premium_sign_up_screen);

        repository = MyApplication.getInstance().getRepository(); // Use repository from MyApplication
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        currentUser = repository.getCurrentUser();

        // Initialize the DrawerLayout and ActionBarDrawerToggle
        initializeDrawer();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupSubscriptionSignUp();
    }

    private void setupSubscriptionSignUp() {
        EditText firstNameEditText = findViewById(R.id.first_name);
        EditText lastNameEditText = findViewById(R.id.last_name);
        EditText emailEditText = findViewById(R.id.email);
        EditText phoneNumberEditText = findViewById(R.id.phone_number);
        EditText creditCardEditText = findViewById(R.id.credit_card);
        EditText creditCardExpiryEditText = findViewById(R.id.expiry);
        EditText creditCardCVVEditText = findViewById(R.id.cvv);

        // Pre-fill user information
        if (currentUser != null) {
            firstNameEditText.setText(currentUser.getFirstName());
            lastNameEditText.setText(currentUser.getLastName());
            emailEditText.setText(currentUser.getEmail());
            phoneNumberEditText.setText(currentUser.getPhone());
        }

        Button subscribeButton = findViewById(R.id.subscribe_button);
        subscribeButton.setOnClickListener(v -> {
            // Save subscription info to the database
            currentUser.setFirstName(firstNameEditText.getText().toString());
            currentUser.setLastName(lastNameEditText.getText().toString());
            currentUser.setEmail(emailEditText.getText().toString());
            currentUser.setPhone(phoneNumberEditText.getText().toString());
            currentUser.setRole(UserRoles.PREMIUM);

            repository.updateUser(currentUser);

            // Update user role in shared preferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("UserRole", UserRoles.PREMIUM);
            editor.apply();

            // Notify the user and navigate to Premium Subscription Management screen
            Toast.makeText(this, "Subscription successful!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PremiumSignUpScreen.this, PremiumSubscriptionManagementScreen.class));
            finish();
        });
    }

    @Override
    protected boolean shouldShowSearch() {
        return false; // Disable the search feature on this activity
    }
}