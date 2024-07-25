package com.hearthy.d424capstone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hearthy.d424capstone.MyApplication;
import com.hearthy.d424capstone.R;
import com.hearthy.d424capstone.database.Repository;
import com.hearthy.d424capstone.entities.CartItem;

import java.util.List;

public class OrderConfirmationScreen extends BaseActivity {
    private Repository repository;
    private TextView confirmationNumberTextView;
    private TextView purchasedItemsTextView;
    private TextView totalPaidTextView;
    private TextView creditCardTextView;
    private Button toUserProfileButton;
    private Button toShoppingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_confirmation_screen);

        repository = MyApplication.getInstance().getRepository(); // Initialize repository instance

        initializeDrawer(); // Initialize the DrawerLayout and ActionBarDrawerToggle
        initViews(); // Initialize UI components
        displayOrderConfirmation(); // Display order confirmation details

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Initialize UI components
    private void initViews() {
        confirmationNumberTextView = findViewById(R.id.confirmationNumberTextView);
        purchasedItemsTextView = findViewById(R.id.purchasedItemsTextView);
        totalPaidTextView = findViewById(R.id.totalPaidTextView);
        creditCardTextView = findViewById(R.id.creditCardTextView);
        toUserProfileButton = findViewById(R.id.toUserProfileButton);
        toShoppingButton = findViewById(R.id.toShoppingButton);

        // Set click listeners for buttons
        toUserProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(OrderConfirmationScreen.this, UserProfileScreen.class);
            startActivity(intent);
        });

        toShoppingButton.setOnClickListener(v -> {
            Intent intent = new Intent(OrderConfirmationScreen.this, ShoppingScreen.class);
            startActivity(intent);
        });
    }

    // Display order confirmation details
    private void displayOrderConfirmation() {
        Intent intent = getIntent();
        String confirmationNumber = intent.getStringExtra("confirmationNumber");
        double totalPaid = intent.getDoubleExtra("totalPaid", 0.0);
        String last4Digits = intent.getStringExtra("last4Digits");
        List<CartItem> cartItems = intent.getParcelableArrayListExtra("cartItems");

        StringBuilder purchasedItems = new StringBuilder();
        for (CartItem item : cartItems) {
            purchasedItems.append(item.getName()).append(" x").append(item.getQuantity()).append("\n");
        }

        String finalPurchasedItems = purchasedItems.toString();

        runOnUiThread(() -> {
            confirmationNumberTextView.setText("Confirmation Number: " + confirmationNumber);
            purchasedItemsTextView.setText("Purchased Items:\n" + finalPurchasedItems);
            totalPaidTextView.setText("Total Paid: $" + String.format("%.2f", totalPaid));
            creditCardTextView.setText("Credit Card (Last 4): " + last4Digits);
        });
    }

    @Override
    protected boolean shouldShowSearch() {
        return false; // Disable the search feature on this activity
    }
}