package com.example.d424capstone.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d424capstone.MyApplication;
import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.utilities.UserRoles;

public class HomeScreen extends BaseActivity {
    private Repository repository;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_screen);

        repository = MyApplication.getInstance().getRepository(); // Initialize repository instance
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

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

        // Show or hide the Admin button based on the user role
        setAdminButtonVisibility();
    }

    private void initializeButtons() {
        // Initialize buttons and set their click listeners
        Button buttonAdmin = findViewById(R.id.toadminscreen);
        buttonAdmin.setOnClickListener(view -> startActivity(new Intent(HomeScreen.this, AdminScreen.class)));

        Button buttonLogin = findViewById(R.id.touserloginscreen);
        buttonLogin.setOnClickListener(view -> startActivity(new Intent(HomeScreen.this, UserLoginScreen.class)));

        Button buttonSignup = findViewById(R.id.tousersignupscreen);
        buttonSignup.setOnClickListener(view -> startActivity(new Intent(HomeScreen.this, UserSignUpScreen.class)));

        Button buttonShopping = findViewById(R.id.toshoppingscreen);
        buttonShopping.setOnClickListener(view -> startActivity(new Intent(HomeScreen.this, ShoppingScreen.class)));

        Button buttonSocial = findViewById(R.id.tocatsocialscreen);
        buttonSocial.setOnClickListener(view -> startActivity(new Intent(HomeScreen.this, CatSocialScreen.class)));

        Button buttonCatLove = findViewById(R.id.tocatlovescreen);
        buttonCatLove.setOnClickListener(view -> startActivity(new Intent(HomeScreen.this, LoveYourCatScreen.class)));
    }

    private void setAdminButtonVisibility() {
        Button buttonAdmin = findViewById(R.id.toadminscreen);
        String userRole = sharedPreferences.getString("UserRole", UserRoles.GUEST);
        if (UserRoles.ADMIN.equals(userRole)) {
            buttonAdmin.setVisibility(Button.VISIBLE);
        } else {
            buttonAdmin.setVisibility(Button.GONE);
        }
    }

    @Override
    protected boolean shouldShowSearch() {
        return false; // Disable the search feature on this activity
    }
}