package com.example.d424capstone.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d424capstone.MyApplication;
import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.Order;

public class ProductShippingManagement extends BaseActivity {
    private EditText orderIDEditText, trackingNumberEditText, carrierNameEditText;
    private Button saveTrackingInfoButton;
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_prodcut_shipping_management);

        repository = MyApplication.getInstance().getRepository(); // Initialize repository instance

        initViews();

        // Initialize the DrawerLayout and ActionBarDrawerToggle
        initializeDrawer();

        saveTrackingInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTrackingInformation();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initViews() {
        orderIDEditText = findViewById(R.id.order_id);
        trackingNumberEditText = findViewById(R.id.tracking_number);
        carrierNameEditText = findViewById(R.id.carrier_name);
        saveTrackingInfoButton = findViewById(R.id.save_tracking_info_button);
    }

    private void saveTrackingInformation() {
        String orderIDStr = orderIDEditText.getText().toString().trim();
        String trackingNumber = trackingNumberEditText.getText().toString().trim();
        String carrierName = carrierNameEditText.getText().toString().trim();

        if (orderIDStr.isEmpty() || trackingNumber.isEmpty() || carrierName.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int orderID = Integer.parseInt(orderIDStr);

        new Thread(() -> {
            Order order = repository.getOrderByID(orderID);
            if (order != null) {
                order.setTrackingNumber(trackingNumber);
                order.setCarrierName(carrierName);
                repository.updateOrder(order);
                runOnUiThread(() -> Toast.makeText(ProductShippingManagement.this, "Tracking information saved", Toast.LENGTH_SHORT).show());
            } else {
                runOnUiThread(() -> Toast.makeText(ProductShippingManagement.this, "Order not found", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    @Override
    protected boolean shouldShowSearch() {
        return false; // Disable the search feature on this activity
    }
}