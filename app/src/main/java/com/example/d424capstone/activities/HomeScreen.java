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
import com.example.d424capstone.entities.SocialPost;
import com.example.d424capstone.entities.StoreItem;

public class HomeScreen extends BaseActivity {

    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_screen);

        repository = MyApplication.getInstance().getRepository(); // Use repository from MyApplication

        // Initialize buttons and set their click listeners
        initializeButtons();

        // Initialize the DrawerLayout and ActionBarDrawerToggle
        initializeDrawer();

        // Set window insets for EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeButtons() {
        // Initialize buttons and set their click listeners
        Button buttonLogin = findViewById(R.id.touserloginscreen);
        buttonLogin.setOnClickListener(view -> startActivity(new Intent(HomeScreen.this, UserLoginScreen.class)));

        Button buttonSignup = findViewById(R.id.tousersignupscreen);
        buttonSignup.setOnClickListener(view -> startActivity(new Intent(HomeScreen.this, UserProfileScreen.class)));

        Button buttonShopping = findViewById(R.id.toshoppingscreen);
        buttonShopping.setOnClickListener(view -> startActivity(new Intent(HomeScreen.this, ShoppingScreen.class)));

        Button buttonSocial = findViewById(R.id.tocatsocialscreen);
        buttonSocial.setOnClickListener(view -> startActivity(new Intent(HomeScreen.this, CatSocialScreen.class)));

        Button adminUserManagementButton = findViewById(R.id.admin_user_management_button);
        adminUserManagementButton.setOnClickListener(view -> startActivity(new Intent(HomeScreen.this, AdminUserManagementScreen.class)));

        Button adminStoreManagementButton = findViewById(R.id.admin_store_management_button);
        adminStoreManagementButton.setOnClickListener(view -> startActivity(new Intent(HomeScreen.this, AdminStoreManagementScreen.class)));
    }
}