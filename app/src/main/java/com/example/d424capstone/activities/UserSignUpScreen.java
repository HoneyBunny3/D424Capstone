package com.example.d424capstone.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.User;
import com.example.d424capstone.utilities.UserRoles;

public class UserSignUpScreen extends BaseActivity {
    private Repository repository;
    private TextView userBanner;

    private EditText emailEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText passwordEditText;
    private EditText phoneNumberEditText;
    private EditText usernameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_sign_up_screen);

        repository = new Repository(getApplication());
        initializeDrawer();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emailEditText = findViewById(R.id.email);
        firstNameEditText = findViewById(R.id.firstName);
        lastNameEditText = findViewById(R.id.lastName);
        passwordEditText = findViewById(R.id.password);
        phoneNumberEditText = findViewById(R.id.phone_number);
        usernameEditText = findViewById(R.id.username);

        Button signUpButton = findViewById(R.id.signup_button);
        signUpButton.setOnClickListener(this::handleSignUp);

        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(v -> startActivity(new Intent(UserSignUpScreen.this, UserLoginScreen.class)));

        setupEmailUsernameSync();
        setupPasswordVisibilityToggle();
        userBanner = findViewById(R.id.user_banner);
        checkAndShowUserBanner();
    }

    private void setupEmailUsernameSync() {
        EditText emailEditText = findViewById(R.id.email);
        EditText usernameEditText = findViewById(R.id.username);

        emailEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String email = emailEditText.getText().toString();
                if (!email.isEmpty()) {
                    usernameEditText.setText(email);
                }
            }
        });
    }

    private void setupPasswordVisibilityToggle() {
        EditText passwordEditText = findViewById(R.id.password);
        ImageButton togglePasswordVisibility = findViewById(R.id.toggle_password_visibility);

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

    private void handleSignUp(View view) {
        String email = emailEditText.getText().toString();
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String phone = phoneNumberEditText.getText().toString();

        if (!validateInput(email, firstName, lastName, password)) {
            return;
        }

        repository.getUserByEmailAsync(email, existingUser -> {
            runOnUiThread(() -> {
                if (existingUser != null) {
                    showAlert("Registration Error", "Email already exists.");
                } else {
                    User user = new User(0, firstName, lastName, email, email, phone, password, UserRoles.REGULAR);
                    repository.insertUser(user);

                    Toast.makeText(UserSignUpScreen.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                    showUserBanner(email);
                    startActivity(new Intent(UserSignUpScreen.this, HomeScreen.class));
                }
            });
        });
    }

    private boolean validateInput(String email, String firstName, String lastName, String password) {
        if (email.isEmpty() || !isValidEmail(email)) {
            showAlert("Email Input Error", "Please enter a valid email.\nEnsure format is test@test.test");
            return false;
        }
        if (firstName.isEmpty() || !isAlphabetic(firstName)) {
            showAlert("First Name Input Error", "Please enter a valid first name containing only alphabetic characters.");
            return false;
        }
        if (lastName.isEmpty() || !isAlphabetic(lastName)) {
            showAlert("Last Name Input Error", "Please enter a valid last name containing only alphabetic characters.");
            return false;
        }
        if (password.isEmpty()) {
            showAlert("Password Input Error", "Please enter a password.");
            return false;
        }
        if (!isPasswordValid(password)) {
            showAlert("Password Format Error", "Password must be at least 8 characters, contain at least one digit, one upper case letter, one lower case letter, and one special character.");
            return false;
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isAlphabetic(String text) {
        return text.matches("[a-zA-Z]+");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8 &&
                password.length() <= 12 &&
                password.matches(".*\\d.*") &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*[!@#$%^&+=?-].*");
    }

    private void showAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void checkAndShowUserBanner() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("UserEmail", null);
        if (userEmail != null) {
            showUserBanner(userEmail);
        }
    }

    private void showUserBanner(String email) {
        String firstName = email.split("\\.")[0];
        userBanner.setText("Welcome, " + firstName + "!");
        userBanner.setVisibility(View.VISIBLE);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UserEmail", email);
        editor.apply();
    }
}