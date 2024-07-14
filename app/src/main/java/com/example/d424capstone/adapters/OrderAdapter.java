// OrderAdapter.java
package com.example.d424capstone.adapters;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424capstone.R;
import com.example.d424capstone.activities.OrderDetailsScreen;
import com.example.d424capstone.entities.Order;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orders;
    private Context context;
    public static final String CHANNEL_ID = "order_tracking_alerts";
    public static final int NOTIFICATION_ID = 1;

    public OrderAdapter(List<Order> orders, Context context) {
        this.orders = orders;
        this.context = context;
        createNotificationChannel();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.confirmationNumber.setText(order.getConfirmationNumber());
        holder.orderDate.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(order.getOrderDate()));

        holder.setAlertButton.setOnClickListener(v -> {
            // Handle setting order tracking alert
            String message = "Your order with confirmation number " + order.getConfirmationNumber() +
                    " has been shipped via " + order.getCarrierName() + " with tracking number " + order.getTrackingNumber() +
                    ". Please be on the lookout for your package.";

            // Send notification
            sendNotification(message);
        });

        // Set OnClickListener to navigate to OrderDetailsScreen
            holder.itemView.setOnClickListener(v ->

        {
            Intent intent = new Intent(context, OrderDetailsScreen.class);
            intent.putExtra("orderID", order.getOrderID());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView confirmationNumber, orderDate;
        Button setAlertButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            confirmationNumber = itemView.findViewById(R.id.confirmation_number);
            orderDate = itemView.findViewById(R.id.order_date);
            setAlertButton = itemView.findViewById(R.id.set_alert_button);
        }
    }

    private void sendNotification(String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("Order Tracking Alert")
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Request permission if not granted
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_ID);
            return;
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Order Tracking Alerts";
            String description = "Notifications for order tracking alerts";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}