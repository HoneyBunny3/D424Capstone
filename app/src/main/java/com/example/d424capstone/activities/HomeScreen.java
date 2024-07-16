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
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE); // Initialize SharedPreferences listener

        initializeDrawer(); // Initialize the DrawerLayout and ActionBarDrawerToggle
        initViews(); // Initialize UI components
        initializeButtons(); // Initialize buttons and set their click listeners

        setAdminButtonVisibility(); // Show or hide the Admin button based on the user role

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Initialize UI components
    private void initViews() {
        Button buttonAdmin = findViewById(R.id.toadminscreen);
        Button buttonLogin = findViewById(R.id.touserloginscreen);
        Button buttonSignup = findViewById(R.id.tousersignupscreen);
        Button buttonShopping = findViewById(R.id.toshoppingscreen);
        Button buttonSocial = findViewById(R.id.tocatsocialscreen);
        Button buttonCatLove = findViewById(R.id.tocatlovescreen);
    }

    // Initialize buttons and set their click listeners
    private void initializeButtons() {
        findViewById(R.id.toadminscreen).setOnClickListener(view -> startActivity(new Intent(HomeScreen.this, AdminScreen.class)));
        findViewById(R.id.touserloginscreen).setOnClickListener(view -> startActivity(new Intent(HomeScreen.this, UserLoginScreen.class)));
        findViewById(R.id.tousersignupscreen).setOnClickListener(view -> startActivity(new Intent(HomeScreen.this, UserSignUpScreen.class)));
        findViewById(R.id.toshoppingscreen).setOnClickListener(view -> startActivity(new Intent(HomeScreen.this, ShoppingScreen.class)));
        findViewById(R.id.tocatsocialscreen).setOnClickListener(view -> startActivity(new Intent(HomeScreen.this, CatSocialScreen.class)));
        findViewById(R.id.tocatlovescreen).setOnClickListener(view -> startActivity(new Intent(HomeScreen.this, LoveYourCatScreen.class)));
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