package com.example.d424capstone.activities;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
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

    private Repository repository;
    private TextView totalAmountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart_summary_screen);

        repository = MyApplication.getInstance().getRepository(); // Use repository from MyApplication

        totalAmountTextView = findViewById(R.id.totalAmountTextView);

        displayCartItems();

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
                CartItemAdapter adapter = new CartItemAdapter(this, cartItems, this::updateTotalAmount);
                listView.setAdapter(adapter);
                updateTotalAmount(cartItems);
            });
        }).start();
    }

    private void updateTotalAmount(List<CartItem> cartItems) {
        double totalAmount = 0;
        for (CartItem item : cartItems) {
            totalAmount += item.getQuantity() * item.getItemPrice();
        }
        totalAmountTextView.setText("Total Amount: $" + String.format("%.2f", totalAmount));
    }
}