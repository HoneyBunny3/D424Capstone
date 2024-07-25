package com.hearthy.d424capstone.activities;

import android.os.Bundle;
import android.text.Html;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hearthy.d424capstone.MyApplication;
import com.hearthy.d424capstone.R;
import com.hearthy.d424capstone.database.Repository;
import com.hearthy.d424capstone.entities.ContactMessage;
import com.hearthy.d424capstone.entities.User;

import java.util.Date;

public class ContactUsScreen extends BaseActivity {
    private Repository repository;
    private EditText firstName, lastName, email, subject, message;
    private TextView confirmationMessage, helpTextBox;
    private Spinner dropdownMenu;
    private ImageView successImage;
    private boolean isSpinnerInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact_us_screen);

        repository = MyApplication.getInstance().getRepository(); // Initialize repository instance

        initializeDrawer(); // Initialize the DrawerLayout and ActionBarDrawerToggle

        initViews(); // Initialize UI components
        initializeButtons(); // Initialize buttons and set their click listeners
        initializeSpinner(); // Initialize the Spinner

        loadUserInfo(); // Load the logged-in user's information

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Initialize UI components
    private void initViews() {
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        subject = findViewById(R.id.subject);
        message = findViewById(R.id.message);
        confirmationMessage = findViewById(R.id.confirmationMessage);
        dropdownMenu = findViewById(R.id.dropdown_menu);
        successImage = findViewById(R.id.success_image);
    }

    // Initialize buttons and their click listeners
    private void initializeButtons() {
        Button cancelBtn = findViewById(R.id.contact_cancel_button);
        cancelBtn.setOnClickListener(v -> clearInputs());

        Button submitBtn = findViewById(R.id.contact_submit_button);
        submitBtn.setOnClickListener(v -> {
            if (validateInputs()) {
                String confirmationMessageText = "Thank you for contacting <b>Hearth's Stop and Shop!</b><br>Our support team will email you within <b>2 business days</b>.";
                confirmationMessage.setText(Html.fromHtml(confirmationMessageText));
                confirmationMessage.setTextColor(getResources().getColor(R.color.rich_black));
                confirmationMessage.setVisibility(View.VISIBLE);

                // Show success image
                successImage.setVisibility(View.VISIBLE);

                // Save message to database
                ContactMessage contactMessage = new ContactMessage(
                        repository.getCurrentUser().getUserID(),
                        firstName.getText().toString(),
                        lastName.getText().toString(),
                        email.getText().toString(),
                        subject.getText().toString(),
                        message.getText().toString(),
                        new Date()
                );
                repository.insertContactMessage(contactMessage);
            }
        });
    }

    // Initialize the Spinner
    private void initializeSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dropdown_items_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownMenu.setAdapter(adapter);

        // Set Spinner item selection listener
        dropdownMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isSpinnerInitialized) {
                    isSpinnerInitialized = true;
                    return;
                }
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (!selectedItem.equals("Select a reason for contacting us")) {
                    subject.setText(selectedItem);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    // Load user information into the form fields
    private void loadUserInfo() {
        new Thread(() -> {
            User currentUser = repository.getCurrentUser();
            if (currentUser != null) {
                runOnUiThread(() -> {
                    firstName.setText(currentUser.getFirstName());
                    lastName.setText(currentUser.getLastName());
                    email.setText(currentUser.getEmail());
                });
            }
        }).start();
    }

    // Validate form inputs
    private boolean validateInputs() {
        if (firstName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter your first name.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!firstName.getText().toString().matches("[a-zA-Z]+")) {
            Toast.makeText(this, "Use only alphabetic characters for your first name.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (lastName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter your last name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!lastName.getText().toString().matches("[a-zA-Z]+")) {
            Toast.makeText(this, "Use only alphabetic characters for your last name.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (email.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            Toast.makeText(this, "Please enter a valid email, xxx@xxx.xxx", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (subject.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please select a reason for contacting us.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (subject.getText().toString().equals("Select a reason for contacting us")) {
            Toast.makeText(this, "Please select a reason for contacting us.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!subject.getText().toString().matches("[a-zA-Z ]+")) {
            Toast.makeText(this, "Use only alphabetic characters for your subject line.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (message.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter your message.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!message.getText().toString().matches("[a-zA-Z0-9.,!?;:()\\-\\s]+")) {
            Toast.makeText(this, "Use alpha-numeric and common punctuation characters in message.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Clear form inputs
    private void clearInputs() {
        firstName.setText("");
        lastName.setText("");
        email.setText("");
        message.setText("");
        subject.setText("Select a reason for contacting us");
        confirmationMessage.setVisibility(View.GONE);
        successImage.setVisibility(View.GONE);
    }

    @Override
    protected boolean shouldShowSearch() {
        return false; // Disable the search feature on this activity
    }
}