package com.example.d424capstone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d424capstone.MyApplication;
import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.User;
import com.example.d424capstone.utilities.UserRoles;

import java.util.regex.Pattern;

public class UserSignUpScreen extends BaseActivity {
    private Repository repository;
    private EditText emailEditText, firstNameEditText, lastNameEditText, passwordEditText, phoneNumberEditText;
    private Button signUpButton, cancelButton;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_sign_up_screen);

        repository = MyApplication.getInstance().getRepository(); // Initialize repository instance

        // Initialize UI components
        initViews();
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

    // Initialize UI components and set input filters
    private void initViews() {
        emailEditText = findViewById(R.id.email);
        firstNameEditText = findViewById(R.id.firstName);
        lastNameEditText = findViewById(R.id.lastName);
        passwordEditText = findViewById(R.id.password);
        phoneNumberEditText = findViewById(R.id.phone_number);

        // Set input filters for first name and last name to accept only alphabetic characters
        InputFilter alphabeticFilter = (source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++) {
                if (!Character.isLetter(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        };

        firstNameEditText.setFilters(new InputFilter[]{alphabeticFilter});
        lastNameEditText.setFilters(new InputFilter[]{alphabeticFilter});

        // Set input filter for phone number to restrict to 10 digits
        phoneNumberEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10), new DigitsInputFilter()});

        signUpButton = findViewById(R.id.sign_up_user);
        cancelButton = findViewById(R.id.cancel_user);

        setupPasswordVisibilityToggle();
    }

    // Initialize buttons and set their click listeners
    private void initializeButtons() {
        signUpButton.setOnClickListener(view -> handleSignUp());
        cancelButton.setOnClickListener(view -> finish());
    }

    // Toggle password visibility
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

    // Handle sign up process
    private void handleSignUp() {
        String email = emailEditText.getText().toString();
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String phone = phoneNumberEditText.getText().toString();

        // Validate input fields
        if (!validateInput(email, firstName, lastName, password, phone)) {
            return;
        }

        // Check if email already exists in the database
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

    // Validate input fields and show appropriate error messages
    private boolean validateInput(String email, String firstName, String lastName, String password, String phone) {
        if (email.isEmpty()) {
            showAlert("Email Input Error", "Please enter your email.");
            return false;
        }

        if (!isValidEmail(email)) {
            showAlert("Email Format Error", "Please enter a valid email.\nEnsure format is test@test.test");
            return false;
        }

        if (firstName.isEmpty()) {
            showAlert("First Name Input Error", "Please enter your first name.");
            return false;
        }

        if (!isAlphabetic(firstName)) {
            showAlert("First Name Format Error", "Please enter a valid first name containing only alphabetic characters.");
            return false;
        }

        if (lastName.isEmpty()) {
            showAlert("Last Name Input Error", "Please enter your last name.");
            return false;
        }

        if (!isAlphabetic(lastName)) {
            showAlert("Last Name Format Error", "Please enter a valid last name containing only alphabetic characters.");
            return false;
        }

        if (phone.isEmpty()) {
            showAlert("Phone Number Input Error", "Please enter your phone number.");
            return false;
        }

        if (!isPhoneValid(phone)) {
            showAlert("Phone Number Format Error", "Please enter a valid 10-digit phone number.");
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

    // Validate email format
    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    // Validate alphabetic characters
    private boolean isAlphabetic(String text) {
        return text.matches("[a-zA-Z]+");
    }

    // Validate password format
    private boolean isPasswordValid(String password) {
        return password.length() >= 8 &&
                password.length() <= 12 &&
                password.matches(".*\\d.*") &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*[!@#$%^&+=?-].*");
    }

    // Validate phone number format
    private boolean isPhoneValid(String phone) {
        return PHONE_PATTERN.matcher(phone).matches();
    }

    // Show alert dialog with a message
    private void showAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    // Show toast message
    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(UserSignUpScreen.this, message, Toast.LENGTH_LONG).show());
    }

    @Override
    protected boolean shouldShowSearch() {
        return false; // Disable the search feature on this activity
    }

    // Custom input filter to allow only digits
    private class DigitsInputFilter implements InputFilter {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (!Character.isDigit(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        }
    }
}