package com.example.d424capstone.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.d424capstone.MyApplication;
import com.example.d424capstone.R;
import com.example.d424capstone.adapters.StoreItemManagementAdapter;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.StoreItem;

import java.util.List;

public class StoreManagementScreen extends BaseActivity {
    private Repository repository;
    private SharedPreferences sharedPreferences;
    private StoreItemManagementAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_store_management_screen);

        repository = MyApplication.getInstance().getRepository(); // Use repository from MyApplication
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Initialize the DrawerLayout and ActionBarDrawerToggle
        initializeDrawer();

        recyclerView = findViewById(R.id.store_management_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        refreshStoreItems();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void refreshStoreItems() {
        new Thread(() -> {
            List<StoreItem> storeItems = repository.getAllStoreItems();
            runOnUiThread(() -> {
                if (adapter == null) {
                    adapter = new StoreItemManagementAdapter(this, storeItems);
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.updateData(storeItems);
                }
            });
        }).start();
    }

    @Override
    protected boolean shouldShowSearch() {
        return false; // Disable the search feature on this activity
    }
}