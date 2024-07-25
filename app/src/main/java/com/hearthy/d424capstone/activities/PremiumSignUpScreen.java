package com.hearthy.d424capstone.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import com.hearthy.d424capstone.MyApplication;
import com.hearthy.d424capstone.R;
import com.hearthy.d424capstone.database.Repository;
import com.hearthy.d424capstone.entities.PremiumStorefront;
import com.hearthy.d424capstone.entities.User;
import com.hearthy.d424capstone.utilities.UserRoles;

import java.util.regex.Pattern;

public class PremiumSignUpScreen extends BaseActivity {
    private Repository repository;
    private SharedPreferences sharedPreferences;
    private EditText storefrontNameEditText;
    private EditText storefrontEmailEditText;
    private EditText creditCardNumber;
    private EditText creditCardExpiry;
    private EditText creditCardCVV;
    private User currentUser;

    // Patterns for email, phone, credit card, expiry, and CVV validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10}$");
    private static final Pattern CREDIT_CARD_PATTERN = Pattern.compile("^\\d{16}$");
    private static final Pattern EXPIRY_PATTERN = Pattern.compile("^(0[1-9]|1[0-2])\\/(\\d{2})$");
    private static final Pattern CVV_PATTERN = Pattern.compile("^\\d{3}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_premium_sign_up_screen);

        repository = MyApplication.getInstance().getRepository(); // Initialize repository instance
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE); // Initialize SharedPreferences listener
        currentUser = repository.getCurrentUser();

        initializeDrawer(); // Initialize the DrawerLayout and ActionBarDrawerToggle
        initializeButtons(); // Initialize buttons and set their click listeners
        setupSubscriptionSignUp(); // Set up the subscription sign-up form
    }

    // Initialize buttons and set their click listeners
    private void initializeButtons() {
        findViewById(R.id.backToProfile).setOnClickListener(view -> startActivity(new Intent(PremiumSignUpScreen.this, UserProfileScreen.class)));
    }

    // TextWatcher class for digits only input
    private class DigitsTextWatcher implements TextWatcher {
        private int maxLength;

        DigitsTextWatcher(int maxLength) {
            this.maxLength = maxLength;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String input = s.toString();
            if (!input.matches("\\d*")) {
                creditCardNumber.setText(input.replaceAll("[^\\d]", ""));
                creditCardNumber.setSelection(creditCardNumber.getText().length());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    // TextWatcher class for credit card expiry input
    private class ExpiryTextWatcher implements TextWatcher {
        private String current = "";
        private String mmYY = "MMYY";

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.toString().equals(current)) {
                String clean = s.toString().replaceAll("[^\\d.]", "");
                String cleanC = current.replaceAll("[^\\d.]", "");

                int cl = clean.length();
                int sel = cl;
                for (int i = 2; i <= cl && i < 4; i += 2) {
                    sel++;
                }

                if (clean.equals(cleanC)) sel--;

                if (clean.length() < 4) {
                    clean = clean + mmYY.substring(clean.length());
                } else {
                    int mon = Integer.parseInt(clean.substring(0, 2));
                    if (mon > 12) mon = 12;
                    clean = String.format("%02d%02d", mon, Integer.parseInt(clean.substring(2, 4)));
                }

                clean = String.format("%s/%s", clean.substring(0, 2),
                        clean.substring(2, 4));

                sel = Math.max(sel, 0);
                current = clean;
                creditCardExpiry.setText(current);
                creditCardExpiry.setSelection(Math.min(sel, current.length()));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    // Validate credit card number
    private void validateCardNumber() {
        String cardNumber = creditCardNumber.getText().toString();
        if (!CREDIT_CARD_PATTERN.matcher(cardNumber).matches()) {
            Toast.makeText(this, "Invalid credit card number", Toast.LENGTH_SHORT).show();
        }
    }

    // Set up the subscription sign-up form
    private void setupSubscriptionSignUp() {
        // Initialize form fields
        EditText firstNameEditText = findViewById(R.id.first_name);
        EditText lastNameEditText = findViewById(R.id.last_name);
        EditText emailEditText = findViewById(R.id.email);
        EditText phoneNumberEditText = findViewById(R.id.phone_number);
        storefrontNameEditText = findViewById(R.id.storefront_name);
        storefrontEmailEditText = findViewById(R.id.storefront_email);
        creditCardNumber = findViewById(R.id.credit_card);
        creditCardExpiry = findViewById(R.id.expiry);
        creditCardCVV = findViewById(R.id.cvv);

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

        // Set up input filter for credit card number to restrict to 16 digits
        creditCardNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        creditCardNumber.addTextChangedListener(new DigitsTextWatcher(16));
        creditCardNumber.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validateCardNumber();
            }
        });

        // Add text watcher for credit card expiry input
        creditCardExpiry.addTextChangedListener(new ExpiryTextWatcher());

        // Set input filter for credit card CVV to restrict to 3 digits
        creditCardCVV.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
        creditCardCVV.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);

        // Pre-fill user information
        if (currentUser != null) {
            firstNameEditText.setText(currentUser.getFirstName());
            lastNameEditText.setText(currentUser.getLastName());
            emailEditText.setText(currentUser.getEmail());
            phoneNumberEditText.setText(currentUser.getPhone());
        }

        Button subscribeButton = findViewById(R.id.subscribe_button);
        subscribeButton.setOnClickListener(v -> {
            String firstName = firstNameEditText.getText().toString().trim();
            String lastName = lastNameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String phone = phoneNumberEditText.getText().toString().trim();
            String storefrontName = storefrontNameEditText.getText().toString().trim();
            String storefrontEmail = storefrontEmailEditText.getText().toString().trim();
            String creditCard = creditCardNumber.getText().toString().trim();
            String expiry = creditCardExpiry.getText().toString().trim();
            String cvv = creditCardCVV.getText().toString().trim();

            // Validate input fields
            if (!validateInputs(firstName, lastName, email, phone, creditCard, expiry, cvv, storefrontName, storefrontEmail)) {
                return;
            }

            // Check if email already exists in the database
            new Thread(() -> {
                User existingUser = repository.getUserByEmail(email);
                runOnUiThread(() -> {
                    if (existingUser != null && existingUser.getUserID() != currentUser.getUserID()) {
                        showAlert("Registration Error", "Email already exists.");
                    } else {
                        // Save subscription info to the database
                        currentUser.setFirstName(firstName);
                        currentUser.setLastName(lastName);
                        currentUser.setEmail(email);
                        currentUser.setPhone(phone);
                        currentUser.setRole(UserRoles.PREMIUM);

                        repository.updateUser(currentUser); // Update the user in the repository

                        // Create and save the PremiumStorefront entity
                        PremiumStorefront premiumStorefront = new PremiumStorefront(
                                0,
                                storefrontName,
                                storefrontEmail,
                                currentUser.getUserID(),
                                creditCard,
                                expiry,
                                cvv
                        );
                        repository.insertPremiumStorefront(premiumStorefront);

                        // Update user role in shared preferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("UserRole", UserRoles.PREMIUM);
                        editor.apply();

                        // Notify the user and navigate to Premium Subscription Management screen
                        Toast.makeText(this, "Subscription successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(PremiumSignUpScreen.this, PremiumSubscriptionManagementScreen.class));
                        finish();
                    }
                });
            }).start();
        });
    }

    // Validate input fields and show appropriate error messages
    private boolean validateInputs(String firstName, String lastName, String email, String phone, String creditCard, String expiry, String cvv, String storefrontName, String storefrontEmail) {
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

        if (storefrontName.isEmpty()) {
            showToast("Please enter your storefront name.");
            return false;
        }

        if (storefrontEmail.isEmpty()) {
            showToast("Please enter your storefront email.");
            return false;
        }

        if (!isValidEmail(storefrontEmail)) {
            showToast("Please enter a valid storefront email address.");
            return false;
        }

        if (creditCard.isEmpty()) {
            showToast("Please enter your credit card number.");
            return false;
        }

        if (!isValidCreditCard(creditCard)) {
            showToast("Please enter a valid 16-digit credit card number.");
            return false;
        }

        if (expiry.isEmpty()) {
            showToast("Please enter your credit card expiry date.");
            return false;
        }

        if (!isValidExpiry(expiry)) {
            showToast("Please enter a valid expiry date in MM/YY format.");
            return false;
        }

        if (cvv.isEmpty()) {
            showToast("Please enter your CVV.");
            return false;
        }

        if (!isValidCVV(cvv)) {
            showToast("Please enter a valid 3-digit CVV.");
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

    private boolean isValidCreditCard(String creditCard) {
        return CREDIT_CARD_PATTERN.matcher(creditCard).matches();
    }

    private boolean isValidExpiry(String expiry) {
        return EXPIRY_PATTERN.matcher(expiry).matches();
    }

    private boolean isValidCVV(String cvv) {
        return CVV_PATTERN.matcher(cvv).matches();
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(PremiumSignUpScreen.this, message, Toast.LENGTH_SHORT).show());
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