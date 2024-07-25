package com.hearthy.d424capstone.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hearthy.d424capstone.MyApplication;
import com.hearthy.d424capstone.R;
import com.hearthy.d424capstone.database.Repository;
import com.hearthy.d424capstone.entities.Order;

public class OrderDetailsScreen extends BaseActivity {
    private Repository repository;
    private TextView orderDetailsTextView;
    private Button shareButton, setAlertButton;
    private Order currentOrder;
    public static final String CHANNEL_ID = "order_tracking_alerts";
    public static final int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_details_screen);

        repository = MyApplication.getInstance().getRepository(); // Initialize repository instance

        initializeDrawer(); // Initialize the DrawerLayout and ActionBarDrawerToggle
        initViews(); // Initialize UI components

        int orderID = getIntent().getIntExtra("orderID", -1);
        if (orderID != -1) {
            displayOrderDetails(orderID); // Display order details if valid order ID
        } else {
            orderDetailsTextView.setText("Order ID is invalid");
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Initialize UI components
    private void initViews() {
        orderDetailsTextView = findViewById(R.id.orderDetailsTextView);
        shareButton = findViewById(R.id.shareButton);
        setAlertButton = findViewById(R.id.set_alert_button);

        // Set click listeners for buttons
        shareButton.setOnClickListener(v -> shareOrderDetails());
        setAlertButton.setOnClickListener(v -> setOrderTrackingAlert());
    }

    // Display order details
    private void displayOrderDetails(int orderID) {
        new Thread(() -> {
            currentOrder = repository.getOrderByID(orderID);
            if (currentOrder != null) {
                String trackingNumber = currentOrder.getTrackingNumber() != null ? currentOrder.getTrackingNumber() : "Not available";
                String carrierName = currentOrder.getCarrierName() != null ? currentOrder.getCarrierName() : "Not available";
                String orderDetails = "Order ID: " + currentOrder.getOrderID() + "\n"
                        + "Confirmation Number: " + currentOrder.getConfirmationNumber() + "\n"
                        + "Order Date: " + currentOrder.getOrderDate().toString() + "\n\n"
                        + "Purchased Items:\n" + currentOrder.getPurchasedItems() + "\n"
                        + "Total Paid: $" + String.format("%.2f", currentOrder.getTotalPaid()) + "\n"
                        + "Credit Card (Last 4): " + currentOrder.getCardNumber().substring(currentOrder.getCardNumber().length() - 4) + "\n\n"
                        + "Tracking Number: " + trackingNumber + "\n"
                        + "Carrier Name: " + carrierName;

                runOnUiThread(() -> orderDetailsTextView.setText(orderDetails));
            } else {
                runOnUiThread(() -> {
                    orderDetailsTextView.setText("No order found");
                    Toast.makeText(OrderDetailsScreen.this, "No order found", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    // Share order details
    private void shareOrderDetails() {
        String orderDetails = orderDetailsTextView.getText().toString();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, orderDetails);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    // Set order tracking alert
    private void setOrderTrackingAlert() {
        String message = "Your order with confirmation number " + currentOrder.getConfirmationNumber() +
                " has been shipped via " + currentOrder.getCarrierName() + " with tracking number " + currentOrder.getTrackingNumber() +
                ". Please be on the lookout for your package.";

        sendNotification(message);
    }

    // Send notification
    private void sendNotification(String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("Order Tracking Alert")
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Request permission if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_ID);
            return;
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                if (currentOrder != null) {
                    setOrderTrackingAlert();
                }
            } else {
                // Permission denied
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected boolean shouldShowSearch() {
        return false; // Disable the search feature on this activity
    }
}