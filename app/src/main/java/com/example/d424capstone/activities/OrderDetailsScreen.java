package com.example.d424capstone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class OrderDetailsScreen extends BaseActivity {

    private Repository repository;
    private TextView orderDetailsTextView;
    private Button shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_details_screen);

        repository = MyApplication.getInstance().getRepository(); // Use repository from MyApplication

        orderDetailsTextView = findViewById(R.id.orderDetailsTextView);
        shareButton = findViewById(R.id.shareButton);

        displayOrderDetails();

        shareButton.setOnClickListener(v -> shareOrderDetails());

        // Initialize the DrawerLayout and ActionBarDrawerToggle
        initializeDrawer();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void displayOrderDetails() {
        new Thread(() -> {
            Order order = repository.getLatestOrder();

            if (order != null) {
                String orderDetails = "Order ID: " + order.getOrderId() + "\n"
                        + "Confirmation Number: " + order.getConfirmationNumber() + "\n\n"
                        + "Purchased Items:\n" + order.getPurchasedItems() + "\n"
                        + "Total Paid: $" + String.format("%.2f", order.getTotalPaid()) + "\n"
                        + "Credit Card (Last 4): " + order.getCardNumber().substring(order.getCardNumber().length() - 4);

                runOnUiThread(() -> orderDetailsTextView.setText(orderDetails));
            }
        }).start();
    }

    private void shareOrderDetails() {
        String orderDetails = orderDetailsTextView.getText().toString();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, orderDetails);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }
}