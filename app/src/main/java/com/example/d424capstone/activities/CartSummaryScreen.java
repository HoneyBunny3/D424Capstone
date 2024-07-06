package com.example.d424capstone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d424capstone.MyApplication;
import com.example.d424capstone.R;
import com.example.d424capstone.adapters.CartItemAdapter;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.CartItem;

import java.util.List;

public class CartSummaryScreen extends AppCompatActivity {

    private static final double TAX_RATE = 0.08; // 8% tax rate

    private Repository repository;
    private TextView subtotalTextView;
    private TextView taxTextView;
    private TextView totalAmountTextView;
    private Button placeOrderButton;
    private Button clearCartButton;
    private Button backToShoppingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_summary_screen);

        repository = MyApplication.getInstance().getRepository();

        subtotalTextView = findViewById(R.id.subtotalTextView);
        taxTextView = findViewById(R.id.taxTextView);
        totalAmountTextView = findViewById(R.id.totalAmountTextView);
        placeOrderButton = findViewById(R.id.placeOrderButton);
        clearCartButton = findViewById(R.id.clearCartButton);
        backToShoppingButton = findViewById(R.id.backToShoppingButton);

        displayCartItems();

        placeOrderButton.setOnClickListener(v -> {
            Intent intent = new Intent(CartSummaryScreen.this, CheckoutScreen.class);
            startActivity(intent);
        });

        clearCartButton.setOnClickListener(v -> {
            clearCart();
            displayCartItems();
        });

        backToShoppingButton.setOnClickListener(v -> {
            Intent intent = new Intent(CartSummaryScreen.this, ShoppingScreen.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void displayCartItems() {
        new Thread(() -> {
            List<CartItem> cartItems = repository.getAllCartItems();

            runOnUiThread(() -> {
                ListView listView = findViewById(R.id.cartItemListView);
                CartItemAdapter adapter = new CartItemAdapter(this, cartItems, this::updateAmounts);
                listView.setAdapter(adapter);
                updateAmounts(cartItems);
            });
        }).start();
    }

    private void updateAmounts(List<CartItem> cartItems) {
        double subtotal = 0;
        for (CartItem item : cartItems) {
            subtotal += item.getQuantity() * item.getItemPrice();
        }
        double tax = subtotal * TAX_RATE;
        double total = subtotal + tax;

        subtotalTextView.setText("Subtotal: $" + String.format("%.2f", subtotal));
        taxTextView.setText("Tax: $" + String.format("%.2f", tax));
        totalAmountTextView.setText("Total: $" + String.format("%.2f", total));
    }

    private void clearCart() {
        new Thread(() -> {
            repository.clearCartItems();
            runOnUiThread(this::displayCartItems);
        }).start();
    }
}