package com.example.d424capstone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

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
import com.google.android.material.navigation.NavigationView;

public class UserSignUpScreen extends AppCompatActivity {

    private Repository repository;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_sign_up_screen);

        repository = new Repository(getApplication());

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
                    intent = new Intent(UserSignUpScreen.this, HomeScreen.class);
                } else if (id == R.id.profile) {
                    intent = new Intent(UserSignUpScreen.this, UserProfileScreen.class);
                }
                else if (id == R.id.cat_social) {
                    intent = new Intent(UserSignUpScreen.this, CatSocialScreen.class);
                }
                else if (id == R.id.shopping) {
                    intent = new Intent(UserSignUpScreen.this, ShoppingScreen.class);
                }
                else if (id == R.id.contact_us) {
                    intent = new Intent(UserSignUpScreen.this, ContactUsScreen.class);
                }
                else if (id == R.id.admin_user) {
                    intent = new Intent(UserSignUpScreen.this, AdminUserManagementScreen.class);
                }
                else if (id == R.id.admin_store) {
                    intent = new Intent(UserSignUpScreen.this, AdminStoreManagementScreen.class);
                }
                if (intent != null) {
                    startActivity(intent);
                }
                // Close the drawer
                drawerLayout.closeDrawers();
                return true;
            }
        });

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
}