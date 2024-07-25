package com.hearthy.d424capstone.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hearthy.d424capstone.MyApplication;
import com.hearthy.d424capstone.R;
import com.hearthy.d424capstone.adapters.OrderAdapter;
import com.hearthy.d424capstone.database.Repository;
import com.hearthy.d424capstone.entities.Order;
import com.hearthy.d424capstone.entities.User;
import com.hearthy.d424capstone.utilities.UserRoles;

import java.util.List;
import java.util.regex.Pattern;

public class UserProfileScreen extends BaseActivity {
    private Repository repository;
    private SharedPreferences sharedPreferences;
    private EditText emailEditText, firstNameEditText, lastNameEditText, passwordEditText, phoneNumberEditText;
    private Button saveButton, cancelButton, catButton;
    private RecyclerView ordersRecyclerView;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile_screen);

        // Initialize repository and shared preferences
        repository = MyApplication.getInstance().getRepository(); // Initialize repository instance
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Initialize UI components
        initViews();
        initializeButtons();
        initializeDrawer();

        // Load user profile and orders
        loadUserProfile();
        loadUserOrders();

        // Set window insets for EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Initialize UI components
    private void initViews() {
        emailEditText = findViewById(R.id.email);
        firstNameEditText = findViewById(R.id.firstName);
        lastNameEditText = findViewById(R.id.lastName);
        passwordEditText = findViewById(R.id.password);
        phoneNumberEditText = findViewById(R.id.phone_number);
        saveButton = findViewById(R.id.save_user);
        cancelButton = findViewById(R.id.cancel_user);
        catButton = findViewById(R.id.cat_button);
        ordersRecyclerView = findViewById(R.id.orders_recycler_view);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        setupPasswordVisibilityToggle();

        // Set input filters to restrict inputs
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
        phoneNumberEditText.setFilters(new InputFilter[]{
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
    }

    // Initialize buttons and set their click listeners
    private void initializeButtons() {
        saveButton.setOnClickListener(view -> handleProfileUpdate());
        cancelButton.setOnClickListener(view -> finish());
        catButton.setOnClickListener(view -> {
            int userID = sharedPreferences.getInt("LoggedInUserID", -1);
            if (userID == -1) {
                showToast("Invalid user ID");
                return;
            }
            Intent intent = new Intent(UserProfileScreen.this, CatProfileScreen.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
        });
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

    // Handle profile update
    private void handleProfileUpdate() {
        int userID = sharedPreferences.getInt("LoggedInUserID", -1);
        if (userID == -1) {
            showToast("Invalid user ID");
            return;
        }

        String email = emailEditText.getText().toString();
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String phone = phoneNumberEditText.getText().toString();

        // Validate input
        if (!validateInput(email, firstName, lastName, password, phone)) {
            return;
        }

        new Thread(() -> {
            User user = new User(userID, firstName, lastName, email, phone, password, UserRoles.REGULAR);
            repository.updateUser(user);
            runOnUiThread(() -> {
                showToast("Profile updated successfully");
                finish();
            });
        }).start();
    }

    // Load user profile data
    private void loadUserProfile() {
        int userID = sharedPreferences.getInt("LoggedInUserID", -1);
        if (userID == -1) {
            showToast("Invalid user ID");
            finish();
            return;
        }

        new Thread(() -> {
            User user = repository.getUserByID(userID);
            runOnUiThread(() -> {
                if (user != null) {
                    firstNameEditText.setText(user.getFirstName());
                    lastNameEditText.setText(user.getLastName());
                    emailEditText.setText(user.getEmail());
                    phoneNumberEditText.setText(user.getPhone());
                    passwordEditText.setText(user.getPassword());
                } else {
                    showToast("User not found");
                    finish();
                }
            });
        }).start();
    }

    // Load user orders data
    private void loadUserOrders() {
        int userID = sharedPreferences.getInt("LoggedInUserID", -1);
        if (userID == -1) {
            showToast("Invalid user ID");
            return;
        }

        new Thread(() -> {
            try {
                List<Order> orders = repository.getOrdersByUserID(userID);
                if (orders != null) {
                    runOnUiThread(() -> {
                        if (!orders.isEmpty()) {
                            OrderAdapter orderAdapter = new OrderAdapter(orders, UserProfileScreen.this);
                            ordersRecyclerView.setAdapter(orderAdapter);
                        } else {
                            showToast("No orders found");
                        }
                    });
                } else {
                    runOnUiThread(() -> showToast("No orders found"));
                }
            } catch (Exception e) {
                runOnUiThread(() -> showToast("Error loading orders: " + e.getMessage()));
            }
        }).start();
    }

    // Validate input fields
    private boolean validateInput(String email, String firstName, String lastName, String password, String phone) {
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

        if (email.isEmpty()) {
            showAlert("Email Input Error", "Please enter your email.");
            return false;
        }

        if (!isValidEmail(email)) {
            showAlert("Email Format Error", "Please enter a valid email.\nEnsure format is test@test.test");
            return false;
        }

        if (phone.isEmpty()) {
            showAlert("Phone Number Input Error", "Please enter your phone number.");
            return false;
        }

        if (!isPhoneValid(phone)) {
            showAlert("Phone Number Format Error", "Please enter a valid phone number.");
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
        runOnUiThread(() -> Toast.makeText(UserProfileScreen.this, message, Toast.LENGTH_LONG).show());
    }

    @Override
    protected boolean shouldShowSearch() {
        return false; // Disable the search feature on this activity
    }
}