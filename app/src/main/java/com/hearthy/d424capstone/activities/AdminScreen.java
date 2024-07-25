package com.hearthy.d424capstone.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hearthy.d424capstone.MyApplication;
import com.hearthy.d424capstone.R;
import com.hearthy.d424capstone.database.Repository;

public class AdminScreen extends BaseActivity {
    private Repository repository;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_screen);

        repository = MyApplication.getInstance().getRepository(); // Initialize repository instance
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE); // Initialize SharedPreferences

        initializeDrawer(); // Initialize the DrawerLayout and ActionBarDrawerToggle
        initializeButtons(); // Initialize buttons and set their click listeners

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Method to initialize buttons and their click listeners
    private void initializeButtons() {
        Button buttonToContactMessages = findViewById(R.id.to_contact_messages_screen);
        buttonToContactMessages.setOnClickListener(v -> startActivity(new Intent(AdminScreen.this, ContactMessageScreen.class)));

        Button buttonToUserManagement = findViewById(R.id.to_user_management_screen);
        buttonToUserManagement.setOnClickListener(v -> startActivity(new Intent(AdminScreen.this, UserManagementScreen.class)));

        Button buttonToSocialPostModeration = findViewById(R.id.to_social_post_moderation_screen);
        buttonToSocialPostModeration.setOnClickListener(v -> startActivity(new Intent(AdminScreen.this, SocialPostModerationScreen.class)));

        Button buttonToStoreManagement = findViewById(R.id.to_store_management_screen);
        buttonToStoreManagement.setOnClickListener(v -> startActivity(new Intent(AdminScreen.this, StoreManagementScreen.class)));

        Button buttonToLoveYourCatManagement = findViewById(R.id.to_love_your_cat_management_screen);
        buttonToLoveYourCatManagement.setOnClickListener(v -> startActivity(new Intent(AdminScreen.this, LoveYourCatManagementScreen.class)));

        Button buttonToProductShippingManagement = findViewById(R.id.to_product_shipping_screen);
        buttonToProductShippingManagement.setOnClickListener(v -> startActivity(new Intent(AdminScreen.this, ProductShippingManagement.class)));

        Button buttonToReportGeneration = findViewById(R.id.to_report_generation_screen);
        buttonToReportGeneration.setOnClickListener(v -> startActivity(new Intent(AdminScreen.this, ReportGenerationScreen.class)));
    }

    @Override
    protected boolean shouldShowSearch() {
        return false; // Disable the search feature on this activity
    }
}