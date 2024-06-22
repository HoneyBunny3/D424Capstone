package com.example.d424capstone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.SocialPost;
import com.example.d424capstone.entities.StoreItem;
import com.google.android.material.navigation.NavigationView;

public class HomeScreen extends BaseActivity {

    private Repository repository;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_screen);

        repository = new Repository(getApplication());

        displayFeaturedContent();

        // Initialize the button that navigates to the User Login screen.
        Button button_login = findViewById(R.id.touserloginscreen);
        button_login.setOnClickListener(view -> {
            // Create an intent to start the UserLoginScreen activity.
            Intent intent = new Intent(HomeScreen.this, UserLoginScreen.class);
            startActivity(intent);
        });

        // Initialize the button that navigates to the Sign Up screen.
        Button button_signup = findViewById(R.id.tousersignupscreen);
        button_signup.setOnClickListener(view -> {
            // Create an intent to start the UserSignUpScreen activity.
            Intent intent = new Intent(HomeScreen.this, UserSignUpScreen.class);
            startActivity(intent);
        });

        // Initialize the button that navigates to the Shopping screen.
        Button button_shopping = findViewById(R.id.toshoppingscreen);
        button_shopping.setOnClickListener(view -> {
            // Create an intent to start the ShoppingScreen activity.
            Intent intent = new Intent(HomeScreen.this, ShoppingScreen.class);
            startActivity(intent);
        });

        // Initialize the button that navigates to the Cat Social screen.
        Button button_social = findViewById(R.id.tocatsocialscreen);
        button_social.setOnClickListener(view -> {
            // Create an intent to start the CatSocialScreen activity.
            Intent intent = new Intent(HomeScreen.this, CatSocialScreen.class);
            startActivity(intent);
        });

        // Initialize the button that navigates to the Admin - User Management screen.
        Button admin_user_management_button = findViewById(R.id.admin_user_management_button);
        admin_user_management_button.setOnClickListener(view -> {
            // Create an intent to start the UserManagementScreen activity.
            Intent intent = new Intent(HomeScreen.this, AdminUserManagementScreen.class);
            startActivity(intent);
        });

        // Initialize the button that navigates to the Admin - Store Management screen.
        Button admin_store_management_button = findViewById(R.id.admin_store_management_button);
        admin_store_management_button.setOnClickListener(view -> {
            // Create an intent to start the StoreManagementScreen activity.
            Intent intent = new Intent(HomeScreen.this, AdminStoreManagementScreen.class);
            startActivity(intent);
        });

        // Initialize the DrawerLayout and ActionBarDrawerToggle
        drawerLayout = findViewById(R.id.main);
        NavigationView navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Enable the home button for opening the drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set the navigation item selected listener
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent intent = null;
                if (id == R.id.home) {
                    intent = new Intent(HomeScreen.this, HomeScreen.class);
                } else if (id == R.id.profile) {
                    intent = new Intent(HomeScreen.this, UserProfileScreen.class);
                }
                else if (id == R.id.cat_social) {
                    intent = new Intent(HomeScreen.this, CatSocialScreen.class);
                }
                else if (id == R.id.shopping) {
                    intent = new Intent(HomeScreen.this, ShoppingScreen.class);
                }
                else if (id == R.id.contact_us) {
                    intent = new Intent(HomeScreen.this, ContactUsScreen.class);
                }
                else if (id == R.id.admin_user) {
                    intent = new Intent(HomeScreen.this, AdminUserManagementScreen.class);
                }
                else if (id == R.id.admin_store) {
                    intent = new Intent(HomeScreen.this, AdminStoreManagementScreen.class);
                }
                if (intent != null) {
                    startActivity(intent);
                }
                // Close the drawer
                drawerLayout.closeDrawers();
                return true;
            }
        });

        // Set window insets for EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayFeaturedContent() {
        repository.executeAsync(() -> {
            StoreItem featuredItem = repository.getFeaturedItem();
            SocialPost mostLikedPost = repository.getMostLikedPost();

            runOnUiThread(() -> {
                // Assuming you have TextViews in your layout to display the content
                TextView featuredItemTextView = findViewById(R.id.featuredItemTextView);
                TextView mostLikedPostTextView = findViewById(R.id.mostLikedPostTextView);

                if (featuredItem != null) {
                    featuredItemTextView.setText("Featured Item: " + featuredItem.getName());
                }
                if (mostLikedPost != null) {
                    mostLikedPostTextView.setText("Most Liked Post: " + mostLikedPost.getContent());
                }
            });
        });
    }
}