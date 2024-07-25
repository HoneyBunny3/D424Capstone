package com.hearthy.d424capstone.activities;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hearthy.d424capstone.MyApplication;
import com.hearthy.d424capstone.R;
import com.hearthy.d424capstone.database.Repository;
import com.hearthy.d424capstone.entities.User;

import java.util.regex.Pattern;

public class AddEditUserScreen extends BaseActivity {
    private Repository repository;
    private EditText firstNameEditText, lastNameEditText, emailEditText, phoneEditText, passwordEditText;
    private Spinner roleSpinner;
    private int userId = -1; // Default value for new user

    // Patterns for email and phone validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit_user_screen);

        repository = MyApplication.getInstance().getRepository(); // Initialize repository instance

        initializeDrawer(); // Initialize the DrawerLayout and ActionBarDrawerToggle
        initViews(); // Initialize UI components

        // Load user details if editing an existing user
        if (getIntent().hasExtra("user_id")) {
            userId = getIntent().getIntExtra("user_id", -1);
            if (userId != -1) {
                loadUserDetails(userId);
            }
        }

        findViewById(R.id.save_user_button).setOnClickListener(this::onSaveUser);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Initialize UI components and set input filters
    private void initViews() {
        firstNameEditText = findViewById(R.id.firstName);
        lastNameEditText = findViewById(R.id.lastName);
        emailEditText = findViewById(R.id.email);
        phoneEditText = findViewById(R.id.phone_number);
        passwordEditText = findViewById(R.id.password);
        roleSpinner = findViewById(R.id.role_spinner);
        setupPasswordVisibilityToggle();

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

        // Set up input filter for phone number to restrict to 10 digits
        phoneEditText.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(10),
                (source, start, end, dest, dstart, dend) -> {
                    for (int i = start; i < end; i++) {
                        if (!Character.isDigit(source.charAt(i))) {
                            return "";
                        }
                    }
                    return null;
                }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);
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

    // Load user details into the UI components
    private void loadUserDetails(int userId) {
        new Thread(() -> {
            User user = repository.getUserByID(userId);
            if (user != null) {
                runOnUiThread(() -> {
                    firstNameEditText.setText(user.getFirstName());
                    lastNameEditText.setText(user.getLastName());
                    emailEditText.setText(user.getEmail());
                    phoneEditText.setText(user.getPhone());
                    passwordEditText.setText(user.getPassword());

                    int spinnerPosition = ((ArrayAdapter<String>) roleSpinner.getAdapter())
                            .getPosition(user.getRole());
                    roleSpinner.setSelection(spinnerPosition); // Set the role spinner position
                });
            }
        }).start();
    }

    // Handle save user button click event
    private void onSaveUser(View view) {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String role = roleSpinner.getSelectedItem().toString();

        // Validate inputs
        if (!validateInputs(firstName, lastName, email, phone, password)) {
            return;
        }

        // Check if email already exists in the database
        new Thread(() -> {
            User existingUser = repository.getUserByEmail(email);
            runOnUiThread(() -> {
                if (existingUser != null && existingUser.getUserID() != userId) {
                    showAlert("Registration Error", "Email already exists.");
                } else {
                    saveOrUpdateUser(firstName, lastName, email, phone, password, role);
                }
            });
        }).start();
    }

    // Save or update the user in the database
    private void saveOrUpdateUser(String firstName, String lastName, String email, String phone, String password, String role) {
        new Thread(() -> {
            if (userId == -1) {
                // Insert new user
                User user = new User(0, firstName, lastName, email, phone, password, role);
                repository.insertUser(user);
            } else {
                // Update existing user
                User user = repository.getUserByID(userId);
                if (user != null) {
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setEmail(email);
                    user.setPhone(phone);
                    user.setPassword(password);
                    user.setRole(role);
                    repository.updateUser(user);
                }
            }
            runOnUiThread(() -> {
                setResult(RESULT_OK);
                finish();
            });
        }).start();
    }

    // Validate input fields and show appropriate error messages
    private boolean validateInputs(String firstName, String lastName, String email, String phone, String password) {
        if (firstName.isEmpty()) {
            showToast("Please enter your first name.");
            return false;
        }

        if (!isAlphabetic(firstName)) {
            showToast("First name should contain only alphabetic characters.");
            return false;
        }

        if (lastName.isEmpty()) {
            showToast("Please enter your last name.");
            return false;
        }

        if (!isAlphabetic(lastName)) {
            showToast("Last name should contain only alphabetic characters.");
            return false;
        }

        if (email.isEmpty()) {
            showToast("Please enter your email.");
            return false;
        }

        if (!isValidEmail(email)) {
            showToast("Please enter a valid email address.");
            return false;
        }

        if (phone.isEmpty()) {
            showToast("Please enter your phone number.");
            return false;
        }

        if (!isPhoneValid(phone)) {
            showToast("Please enter a valid phone number.");
            return false;
        }

        if (password.isEmpty()) {
            showToast("Please enter your password.");
            return false;
        }

        if (!isPasswordValid(password)) {
            showToast("Password must be 8-12 characters long and include at least one digit, one uppercase letter, one lowercase letter, and one special character.");
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    private boolean isAlphabetic(String text) {
        return text.matches("[a-zA-Z]+");
    }

    private boolean isPhoneValid(String phone) {
        return PHONE_PATTERN.matcher(phone).matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8 &&
                password.length() <= 12 &&
                password.matches(".*\\d.*") &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*[!@#$%^&+=?-].*");
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(AddEditUserScreen.this, message, Toast.LENGTH_SHORT).show());
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

    @Override
    protected boolean shouldShowSearch() {
        return false; // Disable the search feature on this activity
    }
}