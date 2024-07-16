package com.example.d424capstone.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d424capstone.MyApplication;
import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.User;
import com.example.d424capstone.utilities.UserRoles;

import java.util.regex.Pattern;

public class UserLoginScreen extends BaseActivity {
    private Repository repository;
    private SharedPreferences sharedPreferences;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_login_screen);

        repository = MyApplication.getInstance().getRepository(); // Initialize repository instance
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE); // Initialize SharedPreferences

        initializeDrawer(); // Initialize the DrawerLayout and ActionBarDrawerToggle
        initViews(); // Initialize UI components

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Initialize UI components
    private void initViews() {
        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(v -> handleLogin());

        setupPasswordVisibilityToggle();
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
            passwordEditText.setSelection(passwordEditText.getText().length());
        });
    }

    private void handleLogin() {
        EditText emailEditText = findViewById(R.id.login_email);
        EditText passwordEditText = findViewById(R.id.password);
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (sharedPreferences.contains("LoggedInUser")) {
            Toast.makeText(this, "You are already logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            User user = repository.getUserByEmail(email);
            runOnUiThread(() -> {

                if (!EMAIL_PATTERN.matcher(email).matches()) {
                    Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (user != null && user.getPassword().equals(password)) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("LoggedInUser", user.getEmail());
                    editor.putString("UserRole", user.getRole());
                    editor.putInt("LoggedInUserID", user.getUserID());
                    editor.apply();

                    String roleMessage = getRoleMessage(user.getRole());
                    Toast.makeText(UserLoginScreen.this, roleMessage, Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(UserLoginScreen.this, HomeScreen.class));
                    finish();
                } else {
                    Toast.makeText(UserLoginScreen.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private String getRoleMessage(String role) {
        switch (role) {
            case UserRoles.ADMIN:
                return "Login successful as Administrator";
            case UserRoles.PREMIUM:
                return "Login successful as Premium User";
            case UserRoles.REGULAR:
                return "Login successful as Regular User";
            default:
                return "Login successful as Guest";
        }
    }

    @Override
    protected boolean shouldShowSearch() {
        return false; // Disable the search feature on this activity
    }
}