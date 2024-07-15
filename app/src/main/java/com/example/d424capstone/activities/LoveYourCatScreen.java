package com.example.d424capstone.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424capstone.MyApplication;
import com.example.d424capstone.R;
import com.example.d424capstone.models.TipsAdapter;
import com.example.d424capstone.entities.Tip;
import com.example.d424capstone.entities.User;
import com.example.d424capstone.database.Repository;

import java.util.List;

public class LoveYourCatScreen extends BaseActivity {
    private RecyclerView recyclerView;
    private TipsAdapter tipsAdapter;
    private List<Tip> tipList;
    private Repository repository;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_love_your_cat_screen);

        // Initialize the DrawerLayout and ActionBarDrawerToggle
        initializeDrawer();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize repository and current user
        repository = MyApplication.getInstance().getRepository();
        currentUser = repository.getCurrentUser();

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.love_your_cat_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load tips from the repository
        tipList = repository.getAllTips();
        String userRole = currentUser != null ? currentUser.getRole() : "";
        tipsAdapter = new TipsAdapter(tipList, tip -> {}, tip -> {}, userRole); // Passing empty consumers for edit and delete
        recyclerView.setAdapter(tipsAdapter);
    }

    @Override
    protected boolean shouldShowSearch() {
        return false; // Disable the search feature on this activity
    }
}