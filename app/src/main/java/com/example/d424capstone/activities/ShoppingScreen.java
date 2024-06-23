package com.example.d424capstone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.SocialPost;
import com.example.d424capstone.entities.StoreItem;

public class ShoppingScreen extends BaseActivity {

    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shopping_screen);

        repository = new Repository(getApplication());

        displayFeaturedContent();

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

    private void displayFeaturedContent() {
        repository.executeAsync(() -> {
            StoreItem featuredItem = repository.getFeaturedItem();

            runOnUiThread(() -> {
                TextView featuredItemTextView = findViewById(R.id.featuredItemTextView);

                if (featuredItem != null) {
                    featuredItemTextView.setText("Featured Item: " + featuredItem.getName());
                }
            });
        });
    }

    private void initializeButtons() {
        // Initialize buttons and set their click listeners
        Button buttonLogin = findViewById(R.id.touserloginscreen);
        buttonLogin.setOnClickListener(view -> startActivity(new Intent(ShoppingScreen.this, UserLoginScreen.class)));

        Button buttonSignup = findViewById(R.id.tousersignupscreen);
        buttonSignup.setOnClickListener(view -> startActivity(new Intent(ShoppingScreen.this, UserSignUpScreen.class)));

        Button buttonShopping = findViewById(R.id.toshoppingscreen);
        buttonShopping.setOnClickListener(view -> startActivity(new Intent(ShoppingScreen.this, ShoppingScreen.class)));

        Button buttonSocial = findViewById(R.id.tocatsocialscreen);
        buttonSocial.setOnClickListener(view -> startActivity(new Intent(ShoppingScreen.this, CatSocialScreen.class)));
    }
}