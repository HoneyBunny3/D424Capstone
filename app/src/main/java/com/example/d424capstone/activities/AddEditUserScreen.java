package com.example.d424capstone.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d424capstone.MyApplication;
import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.User;

public class AddEditUserScreen extends BaseActivity {
    private Repository repository;
    private EditText firstNameEditText, lastNameEditText, emailEditText, phoneEditText, passwordEditText;
    private Spinner roleSpinner;
    private int userId = -1; // Default value for new user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit_user_screen);

        repository = MyApplication.getInstance().getRepository(); // Initialize repository instance

        initializeDrawer(); // Initialize the DrawerLayout and ActionBarDrawerToggle

        // Initialize UI elements
        firstNameEditText = findViewById(R.id.firstName);
        lastNameEditText = findViewById(R.id.lastName);
        emailEditText = findViewById(R.id.email);
        phoneEditText = findViewById(R.id.phone_number);
        passwordEditText = findViewById(R.id.password);
        roleSpinner = findViewById(R.id.role_spinner);

        // Set up the role spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        // Check if this activity was started with an existing user ID
        if (getIntent().hasExtra("user_id")) {
            userId = getIntent().getIntExtra("user_id", -1);
            if (userId != -1) {
                loadUserDetails(userId); // Load user details if editing an existing user
            }
        }

        // Set the save button click listener
        findViewById(R.id.save_user_button).setOnClickListener(this::onSaveUser);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Load the user details into the UI elements
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

    // Handle the save button click event
    private void onSaveUser(View view) {
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String role = roleSpinner.getSelectedItem().toString();

        // Validate input fields
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || role.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save or update the user in the database
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
            // Return to the previous screen
            runOnUiThread(() -> {
                setResult(RESULT_OK);
                finish();
            });
        }).start();
    }

    @Override
    protected boolean shouldShowSearch() {
        return false; // Disable the search feature on this activity
    }
}