package com.example.d424capstone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d424capstone.MyApplication;
import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.User;
import com.example.d424capstone.utilities.UserRoles;

public class UserSignUpScreen extends BaseActivity {
    private Repository repository;
    private EditText emailEditText, firstNameEditText, lastNameEditText, passwordEditText, phoneNumberEditText;
    private Button signUpButton, cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_sign_up_screen);

        repository = MyApplication.getInstance().getRepository(); // Use repository from MyApplication

        initViews();
        initializeButtons();

        // Initialize the DrawerLayout and ActionBarDrawerToggle
        initializeDrawer();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initViews() {
        emailEditText = findViewById(R.id.email);
        firstNameEditText = findViewById(R.id.firstName);
        lastNameEditText = findViewById(R.id.lastName);
        passwordEditText = findViewById(R.id.password);
        phoneNumberEditText = findViewById(R.id.phone_number);
        signUpButton = findViewById(R.id.sign_up_user);
        cancelButton = findViewById(R.id.cancel_user);
        setupPasswordVisibilityToggle();
    }

    private void initializeButtons() {
        signUpButton.setOnClickListener(view -> handleSignUp());
        cancelButton.setOnClickListener(view -> finish());
    }

    private void setupPasswordVisibilityToggle() {
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

    private void handleSignUp() {
        String email = emailEditText.getText().toString();
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String phone = phoneNumberEditText.getText().toString();

        if (!validateInput(email, firstName, lastName, password)) {
            return;
        }

        new Thread(() -> {
            User existingUser = repository.getUserByEmail(email);
            runOnUiThread(() -> {
                if (existingUser != null) {
                    showAlert("Registration Error", "Email already exists.");
                } else {
                    User user = new User(0, firstName, lastName, email, phone, password, UserRoles.REGULAR);
                    repository.insertUser(user);
                    showToast("Sign up successful");
                    startActivity(new Intent(UserSignUpScreen.this, HomeScreen.class));
                    finish();
                }
            });
        }).start();
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

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(UserSignUpScreen.this, message, Toast.LENGTH_LONG).show());
    }

    @Override
    protected boolean shouldShowSearch() {
        return false; // Disable the search feature on this activity
    }
}