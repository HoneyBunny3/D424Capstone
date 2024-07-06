package com.example.d424capstone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d424capstone.MyApplication;
import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.Order;
import com.example.d424capstone.entities.CartItem;
import com.example.d424capstone.entities.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class CheckoutScreen extends BaseActivity {

    private Repository repository;
    private EditText creditCardNumber;
    private EditText creditCardExpiry;
    private EditText creditCardCVV;
    private Button submitPaymentButton;
    private Button backToCartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout_screen);

        repository = MyApplication.getInstance().getRepository(); // Use repository from MyApplication

        creditCardNumber = findViewById(R.id.creditCardNumber);
        creditCardExpiry = findViewById(R.id.creditCardExpiry);
        creditCardCVV = findViewById(R.id.creditCardCVV);
        submitPaymentButton = findViewById(R.id.submitPaymentButton);
        backToCartButton = findViewById(R.id.backToCartButton);

        creditCardNumber.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String cardNumber = creditCardNumber.getText().toString();
                if (cardNumber.length() != 16) {
                    Toast.makeText(this, "Invalid credit card number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        creditCardExpiry.addTextChangedListener(new TextWatcher() {
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
        });

        submitPaymentButton.setOnClickListener(v -> submitPayment());

        backToCartButton.setOnClickListener(v -> {
            finish(); // Go back to the previous activity (CartSummaryScreen)
        });

        // Initialize the DrawerLayout and ActionBarDrawerToggle
        initializeDrawer();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void submitPayment() {
        String cardNumber = creditCardNumber.getText().toString();
        String cardExpiry = creditCardExpiry.getText().toString();
        String cardCVV = creditCardCVV.getText().toString();

        if (validateCard(cardNumber, cardExpiry, cardCVV)) {
            String confirmationNumber = generateConfirmationNumber();
            double totalPaid = getTotalPaid();
            String purchasedItems = getPurchasedItems();
            User currentUser = repository.getCurrentUser();
            if (currentUser != null) {
                int userID = currentUser.getUserID();
                List<CartItem> cartItems = repository.getAllCartItems(); // Get cart items before clearing
                Date orderDate = new Date(); // Current date and time
                saveOrder(userID, cardNumber, cardExpiry, cardCVV, confirmationNumber, totalPaid, purchasedItems, orderDate);
                clearCart();
                Intent intent = new Intent(CheckoutScreen.this, OrderConfirmationScreen.class);
                intent.putExtra("confirmationNumber", confirmationNumber);
                intent.putExtra("totalPaid", totalPaid);
                intent.putExtra("last4Digits", cardNumber.substring(cardNumber.length() - 4));
                intent.putParcelableArrayListExtra("cartItems", new ArrayList<>(cartItems)); // Pass cart items
                startActivity(intent);
            } else {
                Toast.makeText(this, "No user is currently logged in.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Invalid credit card information", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateCard(String cardNumber, String cardExpiry, String cardCVV) {
        return cardNumber.equals("1234567812345678") && cardExpiry.equals("12/34") && cardCVV.equals("123");
    }

    private void saveOrder(int userID, String cardNumber, String cardExpiry, String cardCVV, String confirmationNumber, double totalPaid, String purchasedItems, Date orderDate) {
        new Thread(() -> {
            Order order = new Order(userID, cardNumber, cardExpiry, cardCVV, totalPaid, purchasedItems, orderDate);
            order.setConfirmationNumber(confirmationNumber);
            repository.insertOrderForCurrentUser(order);
        }).start();
    }

    private void clearCart() {
        new Thread(() -> repository.clearCartItems()).start();
    }

    private String generateConfirmationNumber() {
        Random random = new Random();
        return String.valueOf(100000 + random.nextInt(900000));
    }

    private double getTotalPaid() {
        List<CartItem> cartItems = repository.getAllCartItems();
        double totalPaid = 0;
        for (CartItem item : cartItems) {
            totalPaid += item.getQuantity() * item.getItemPrice();
        }
        return totalPaid;
    }

    private String getPurchasedItems() {
        List<CartItem> cartItems = repository.getAllCartItems();
        StringBuilder purchasedItems = new StringBuilder();
        for (CartItem item : cartItems) {
            purchasedItems.append(item.getItemName()).append(" x").append(item.getQuantity()).append("\n");
        }
        return purchasedItems.toString();
    }
}