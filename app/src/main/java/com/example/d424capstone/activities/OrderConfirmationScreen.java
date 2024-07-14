package com.example.d424capstone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d424capstone.MyApplication;
import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.CartItem;
import com.example.d424capstone.entities.Order;

import java.util.List;
import java.util.Random;

public class OrderConfirmationScreen extends BaseActivity {

    private Repository repository;
    private TextView confirmationNumberTextView;
    private TextView purchasedItemsTextView;
    private TextView totalPaidTextView;
    private TextView creditCardTextView;
    private Button toUserProfileButton;
    private Button toShoppingButton;
    private Button toOrderDetailsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_confirmation_screen);

        repository = MyApplication.getInstance().getRepository(); // Use repository from MyApplication

        confirmationNumberTextView = findViewById(R.id.confirmationNumberTextView);
        purchasedItemsTextView = findViewById(R.id.purchasedItemsTextView);
        totalPaidTextView = findViewById(R.id.totalPaidTextView);
        creditCardTextView = findViewById(R.id.creditCardTextView);
        toUserProfileButton = findViewById(R.id.toUserProfileButton);
        toShoppingButton = findViewById(R.id.toShoppingButton);

        displayOrderConfirmation();

        toUserProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(OrderConfirmationScreen.this, UserProfileScreen.class);
            startActivity(intent);
        });

        toShoppingButton.setOnClickListener(v -> {
            Intent intent = new Intent(OrderConfirmationScreen.this, ShoppingScreen.class);
            startActivity(intent);
        });

        // Initialize the DrawerLayout and ActionBarDrawerToggle
        initializeDrawer();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void displayOrderConfirmation() {
        Intent intent = getIntent();
        String confirmationNumber = intent.getStringExtra("confirmationNumber");
        double totalPaid = intent.getDoubleExtra("totalPaid", 0.0);
        String last4Digits = intent.getStringExtra("last4Digits");
        List<CartItem> cartItems = intent.getParcelableArrayListExtra("cartItems");

        StringBuilder purchasedItems = new StringBuilder();
        for (CartItem item : cartItems) {
            purchasedItems.append(item.getItemName()).append(" x").append(item.getQuantity()).append("\n");
        }

            String finalPurchasedItems = purchasedItems.toString();

            runOnUiThread(() -> {
                confirmationNumberTextView.setText("Confirmation Number: " + confirmationNumber);
                purchasedItemsTextView.setText("Purchased Items:\n" + finalPurchasedItems);
                totalPaidTextView.setText("Total Paid: $" + String.format("%.2f", totalPaid));
                creditCardTextView.setText("Credit Card (Last 4): " + last4Digits);
            });
    }

    private String generateConfirmationNumber() {
        Random random = new Random();
        return String.valueOf(100000 + random.nextInt(900000));
    }
}