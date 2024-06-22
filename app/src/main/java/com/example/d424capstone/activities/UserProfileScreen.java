package com.example.d424capstone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;

public class UserProfileScreen extends BaseActivity {

    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile_screen);

        repository = new Repository(getApplication());

        // Initialize the DrawerLayout and ActionBarDrawerToggle
        initializeDrawer();

        // Initialize the button that navigates to the Shopping screen.
        Button button_shopping = findViewById(R.id.toshoppingscreen);
        button_shopping.setOnClickListener(view -> {
            // Create an intent to start the ShoppingScreen activity.
            Intent intent = new Intent(UserProfileScreen.this, ShoppingScreen.class);
            startActivity(intent);
        });

        // Initialize the button that navigates to the Cat Social screen.
        Button button_social = findViewById(R.id.tocatsocialscreen);
        button_social.setOnClickListener(view -> {
            // Create an intent to start the CatSocialScreen activity.
            Intent intent = new Intent(UserProfileScreen.this, CatSocialScreen.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}