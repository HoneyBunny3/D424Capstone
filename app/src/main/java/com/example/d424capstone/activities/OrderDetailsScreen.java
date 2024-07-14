package com.example.d424capstone.activities;

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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.d424capstone.MyApplication;
import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.Order;
import com.example.d424capstone.entities.User;

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

        repository = MyApplication.getInstance().getRepository(); // Use repository from MyApplication

        orderDetailsTextView = findViewById(R.id.orderDetailsTextView);
        shareButton = findViewById(R.id.shareButton);
        setAlertButton = findViewById(R.id.set_alert_button);

        displayOrderDetails();

        shareButton.setOnClickListener(v -> shareOrderDetails());
        setAlertButton.setOnClickListener(v -> setOrderTrackingAlert());

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
            User currentUser = repository.getCurrentUser();
            if (currentUser != null) {
                Order order = repository.getLatestOrderForUser(currentUser.getUserID());
                if (order != null) {
                    String trackingNumber = order.getTrackingNumber() != null ? order.getTrackingNumber() : "Not available";
                    String carrierName = order.getCarrierName() != null ? order.getCarrierName() : "Not available";
                    String orderDetails = "Order ID: " + order.getOrderID() + "\n"
                            + "Confirmation Number: " + order.getConfirmationNumber() + "\n"
                            + "Order Date: " + order.getOrderDate().toString() + "\n\n"
                            + "Purchased Items:\n" + order.getPurchasedItems() + "\n"
                            + "Total Paid: $" + String.format("%.2f", order.getTotalPaid()) + "\n"
                            + "Credit Card (Last 4): " + order.getCardNumber().substring(order.getCardNumber().length() - 4) + "\n\n"
                            + "Tracking Number: " + trackingNumber + "\n"
                            + "Carrier Name: " + carrierName;

                    runOnUiThread(() -> orderDetailsTextView.setText(orderDetails));
                } else {
                    runOnUiThread(() -> {
                        orderDetailsTextView.setText("No order found for the current user.");
                        Toast.makeText(OrderDetailsScreen.this, "No order found for the current user.", Toast.LENGTH_SHORT).show();
                    });
                }
            } else {
                runOnUiThread(() -> {
                    orderDetailsTextView.setText("No user is currently logged in.");
                    Toast.makeText(OrderDetailsScreen.this, "No user is currently logged in.", Toast.LENGTH_SHORT).show();
                });
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

    private void setOrderTrackingAlert() {
        String orderDetails = orderDetailsTextView.getText().toString();
        // Send notification logic here
        Toast.makeText(this, "Order tracking alert set.", Toast.LENGTH_SHORT).show();
    }


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
}