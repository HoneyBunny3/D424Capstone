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
    private int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit_user_screen);

        repository = MyApplication.getInstance().getRepository(); // Use repository from MyApplication

        firstNameEditText = findViewById(R.id.firstName);
        lastNameEditText = findViewById(R.id.lastName);
        emailEditText = findViewById(R.id.email);
        phoneEditText = findViewById(R.id.phone_number);
        passwordEditText = findViewById(R.id.password);
        roleSpinner = findViewById(R.id.role_spinner);

        // Initialize the DrawerLayout and ActionBarDrawerToggle
        initializeDrawer();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

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
                    roleSpinner.setSelection(spinnerPosition);
                });
            }
        }).start();
    }

    private void onSaveUser(View view) {
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String role = roleSpinner.getSelectedItem().toString();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || role.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            if (userId == -1) {
                User user = new User(0, firstName, lastName, email, phone, password, role);
                repository.insertUser(user);
            } else {
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

    @Override
    protected boolean shouldShowSearch() {
        return false; // Disable the search feature on this activity
    }
}