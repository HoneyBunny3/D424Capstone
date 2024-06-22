package com.example.d424capstone.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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
import com.example.d424capstone.entities.User;
import com.example.d424capstone.utilities.UserRoles;
import com.google.android.material.navigation.NavigationView;

public class UserLoginScreen extends AppCompatActivity {

    private Repository repository;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_login_screen);

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
                    intent = new Intent(UserLoginScreen.this, HomeScreen.class);
                } else if (id == R.id.profile) {
                    intent = new Intent(UserLoginScreen.this, UserProfileScreen.class);
                }
                else if (id == R.id.cat_social) {
                    intent = new Intent(UserLoginScreen.this, CatSocialScreen.class);
                }
                else if (id == R.id.shopping) {
                    intent = new Intent(UserLoginScreen.this, ShoppingScreen.class);
                }
                else if (id == R.id.contact_us) {
                    intent = new Intent(UserLoginScreen.this, ContactUsScreen.class);
                }
                else if (id == R.id.admin_user) {
                    intent = new Intent(UserLoginScreen.this, AdminUserManagementScreen.class);
                }
                else if (id == R.id.admin_store) {
                    intent = new Intent(UserLoginScreen.this, AdminStoreManagementScreen.class);
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

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Set up the login button click listener
        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });

        // Set up password visibility toggle
        EditText passwordEditText = findViewById(R.id.password);
        ImageButton togglePasswordVisibility = findViewById(R.id.toggle_password_visibility2);
        togglePasswordVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordEditText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    togglePasswordVisibility.setImageResource(R.drawable.baseline_visibility_24);
                } else {
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    togglePasswordVisibility.setImageResource(R.drawable.baseline_visibility_off_24);
                }
                // Move the cursor to the end of the text
                passwordEditText.setSelection(passwordEditText.getText().length());
            }
        });
    }

    private void handleLogin() {
        EditText usernameEditText = findViewById(R.id.login_username);
        EditText passwordEditText = findViewById(R.id.password);
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // Check if already logged in
        if (sharedPreferences.contains("LoggedInUser")) {
            Toast.makeText(this, "You are already logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate credentials against the database
        repository.getUserByUsernameAsync(username, new Repository.UserCallback() {
            @Override
            public void onUserRetrieved(User user) {
                runOnUiThread(() -> {
                    if (user != null && user.getPassword().equals(password)) {
                        // Successful login
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("LoggedInUser", user.getUserName());
                        editor.putString("UserRole", user.getRole());
                        editor.apply();

                        String roleMessage;
                        switch (user.getRole()) {
                            case UserRoles.ADMIN:
                                roleMessage = "Login successful as Admin";
                                break;
                            case UserRoles.PREMIUM:
                                roleMessage = "Login successful as Premium User";
                                break;
                            case UserRoles.REGULAR:
                                roleMessage = "Login successful as Regular User";
                                break;
                            case UserRoles.GUEST:
                            default:
                                roleMessage = "Login successful as Guest";
                                break;
                        }

                        Toast.makeText(UserLoginScreen.this, roleMessage, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UserLoginScreen.this, HomeScreen.class));
                    } else {
                        // Failed login
                        Toast.makeText(UserLoginScreen.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                });
            }
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