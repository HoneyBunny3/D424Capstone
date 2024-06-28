package com.example.d424capstone.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.utilities.UserRoles;

public class UserLoginScreen extends BaseActivity {

    private Repository repository;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_login_screen);

        // Initialize repository
        repository = new Repository(getApplication());

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Initialize the DrawerLayout and ActionBarDrawerToggle
        initializeDrawer();

        // Set up the login button click listener
        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(v -> handleLogin());

        // Set up password visibility toggle
        setupPasswordVisibilityToggle();

        // Set window insets for EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupPasswordVisibilityToggle() {
        EditText passwordEditText = findViewById(R.id.password);
        ImageButton togglePasswordVisibility = findViewById(R.id.toggle_password_visibility2);
        togglePasswordVisibility.setOnClickListener(v -> {
            if (passwordEditText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                togglePasswordVisibility.setImageResource(R.drawable.baseline_visibility_24);
            } else {
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                togglePasswordVisibility.setImageResource(R.drawable.baseline_visibility_off_24);
            }
            // Move the cursor to the end of the text
            passwordEditText.setSelection(passwordEditText.getText().length());
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
            Log.d(TAG, "User already logged in.");
            return;
        }

        // Validate credentials against the database
        repository.getUserByUsernameAsync(username, user -> runOnUiThread(() -> {
            if (user != null && user.getPassword().equals(password)) {
                // Successful login
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("LoggedInUser", user.getUserName());
                editor.putString("UserRole", user.getRole());
                editor.putInt("LoggedInUserID", user.getUserID());  // Store the user ID
                editor.apply();

                String roleMessage = getRoleMessage(user.getRole());
                Toast.makeText(UserLoginScreen.this, roleMessage, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "User logged in successfully: " + user.getUserName());

                startActivity(new Intent(UserLoginScreen.this, HomeScreen.class));
                finish();
            } else {
                // Failed login
                Toast.makeText(UserLoginScreen.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Invalid login attempt for username: " + username);
            }
        }));
    }

    private String getRoleMessage(String role) {
        switch (role) {
            case UserRoles.ADMIN:
                return "Login successful as Admin";
            case UserRoles.PREMIUM:
                return "Login successful as Premium User";
            case UserRoles.REGULAR:
                return "Login successful as Regular User";
            case UserRoles.GUEST:
            default:
                return "Login successful as Guest";
        }
    }
}