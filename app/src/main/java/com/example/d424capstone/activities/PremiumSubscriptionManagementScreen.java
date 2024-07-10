package com.example.d424capstone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d424capstone.MyApplication;
import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.User;

import java.util.List;

public class PremiumSubscriptionManagementScreen extends BaseActivity {

    private Repository repository;
    private TextView firstNameTextView;
    private TextView lastNameTextView;
    private TextView emailTextView;
    private TextView phoneTextView;

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
    }

    private void displayPremiumUserInfo() {
        new Thread(() -> {
            List<User> users = repository.getAllUsers();
            if (users != null && !users.isEmpty()) {
                for (User user : users) {
                    if ("PREMIUM".equals(user.getRole())) {
                        runOnUiThread(() -> {
                            firstNameTextView.setText(user.getFirstName());
                            lastNameTextView.setText(user.getLastName());
                            emailTextView.setText(user.getEmail());
                            phoneTextView.setText(user.getPhone());
                        });
                        break;
                    }
                }
            } else {
                runOnUiThread(() -> {
                    firstNameTextView.setText("N/A");
                    lastNameTextView.setText("N/A");
                    emailTextView.setText("N/A");
                    phoneTextView.setText("N/A");
                });
            }
        }).start();
    }
}