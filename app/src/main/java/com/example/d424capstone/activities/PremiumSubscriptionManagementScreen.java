package com.example.d424capstone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d424capstone.MyApplication;
import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.PremiumStorefront;
import com.example.d424capstone.entities.User;

import java.util.List;
import java.util.regex.Pattern;

public class PremiumSubscriptionManagementScreen extends BaseActivity {
    private Repository repository;
    private TextView firstNameTextView, lastNameTextView, emailTextView, phoneTextView;
    private EditText storefrontNameEditText, storefrontEmailEditText, creditCardEditText, expiryEditText, cvvEditText;
    private User currentUser;
    private PremiumStorefront currentStorefront;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    private static final Pattern CREDIT_CARD_PATTERN = Pattern.compile("^[0-9]{16}$");
    private static final Pattern EXPIRY_PATTERN = Pattern.compile("^(0[1-9]|1[0-2])\\/([0-9]{2})$");
    private static final Pattern CVV_PATTERN = Pattern.compile("^[0-9]{3,4}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_premium_subscription_management_screen); // Ensure the correct layout is set

        repository = MyApplication.getInstance().getRepository(); // Initialize repository instance

        initializeDrawer(); // Initialize the DrawerLayout and ActionBarDrawerToggle
        initViews(); // Initialize UI components
        displayPremiumUserInfo(); // Fetch and display premium user information

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Initialize UI components
    private void initViews() {
        firstNameTextView = findViewById(R.id.first_name);
        lastNameTextView = findViewById(R.id.last_name);
        emailTextView = findViewById(R.id.email);
        phoneTextView = findViewById(R.id.phone);
        storefrontNameEditText = findViewById(R.id.storefront_name);
        storefrontEmailEditText = findViewById(R.id.storefront_contact_email);
        creditCardEditText = findViewById(R.id.storefront_credit_card);
        expiryEditText = findViewById(R.id.storefront_expiry);
        cvvEditText = findViewById(R.id.storefront_cvv);

        creditCardEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        expiryEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
        cvvEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});

        Button buttonAdd = findViewById(R.id.add_product_button);
        buttonAdd.setOnClickListener(view -> {
            Intent intent = new Intent(PremiumSubscriptionManagementScreen.this, PremiumProductManagementScreen.class);
            intent.putExtra("CREDIT_CARD", creditCardEditText.getText().toString());
            intent.putExtra("EXPIRY", expiryEditText.getText().toString());
            intent.putExtra("CVV", cvvEditText.getText().toString());
            startActivity(intent);
        });

        Button buttonSaveStorefront = findViewById(R.id.save_storefront_button);
        buttonSaveStorefront.setOnClickListener(view -> saveStorefrontInfo());
    }

    private void displayPremiumUserInfo() {
        new Thread(() -> {
            currentUser = repository.getCurrentUser();
            if (currentUser != null && "PREMIUM".equals(currentUser.getRole())) {
                runOnUiThread(() -> {
                    firstNameTextView.setText(currentUser.getFirstName());
                    lastNameTextView.setText(currentUser.getLastName());
                    emailTextView.setText(currentUser.getEmail());
                    phoneTextView.setText(currentUser.getPhone());
                });

                List<PremiumStorefront> storefronts = repository.getPremiumStorefrontsByUserID(currentUser.getUserID());
                if (storefronts != null && !storefronts.isEmpty()) {
                    currentStorefront = storefronts.get(0); // Assuming a user has one storefront
                    runOnUiThread(() -> {
                        storefrontNameEditText.setText(currentStorefront.getName());
                        storefrontEmailEditText.setText(currentStorefront.getEmail());
                        creditCardEditText.setText(currentStorefront.getCreditCard());
                        expiryEditText.setText(currentStorefront.getExpiry());
                        cvvEditText.setText(currentStorefront.getCvv());
                    });
                }
            } else {
                runOnUiThread(() -> {
                    firstNameTextView.setText("N/A");
                    lastNameTextView.setText("N/A");
                    emailTextView.setText("N/A");
                    phoneTextView.setText("N/A");
                    storefrontNameEditText.setText("");
                    storefrontEmailEditText.setText("");
                    creditCardEditText.setText("");
                    expiryEditText.setText("");
                    cvvEditText.setText("");
                });
            }
        }).start();
    }

    private void saveStorefrontInfo() {
        if (currentUser != null && "PREMIUM".equals(currentUser.getRole())) {
            String storefrontName = storefrontNameEditText.getText().toString().trim();
            String storefrontEmail = storefrontEmailEditText.getText().toString().trim();
            String creditCard = creditCardEditText.getText().toString().trim();
            String expiry = expiryEditText.getText().toString().trim();
            String cvv = cvvEditText.getText().toString().trim();

            if (!validateInputs(storefrontName, storefrontEmail, creditCard, expiry, cvv)) {
                return;
            }

            if (currentStorefront == null) {
                currentStorefront = new PremiumStorefront(0, storefrontName, storefrontEmail, currentUser.getUserID(), creditCard, expiry, cvv);
                repository.insertPremiumStorefront(currentStorefront);
            } else {
                currentStorefront.setName(storefrontName);
                currentStorefront.setEmail(storefrontEmail);
                currentStorefront.setCreditCard(creditCard);
                currentStorefront.setExpiry(expiry);
                currentStorefront.setCvv(cvv);
                repository.updatePremiumStorefront(currentStorefront);
            }

            Toast.makeText(this, "Storefront info saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error: User not premium or not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs(String storefrontName, String storefrontEmail, String creditCard, String expiry, String cvv) {
        if (storefrontName.isEmpty()) {
            showToast("Storefront name cannot be empty");
            return false;
        }

        if (storefrontEmail.isEmpty()) {
            showToast("Storefront contact email cannot be empty");
            return false;
        }

        if (!EMAIL_PATTERN.matcher(storefrontEmail).matches()) {
            showToast("Please enter a valid email address");
            return false;
        }

        if (!CREDIT_CARD_PATTERN.matcher(creditCard).matches()) {
            showToast("Please enter a valid 16-digit credit card number");
            return false;
        }

        if (!EXPIRY_PATTERN.matcher(expiry).matches()) {
            showToast("Please enter a valid expiry date in MM/YY format");
            return false;
        }

        if (!CVV_PATTERN.matcher(cvv).matches()) {
            showToast("Please enter a valid CVV");
            return false;
        }

        return true;
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(PremiumSubscriptionManagementScreen.this, message, Toast.LENGTH_SHORT).show());
    }

    @Override
    protected boolean shouldShowSearch() {
        return false; // Disable the search feature on this activity
    }
}