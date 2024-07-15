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

import java.util.ArrayList;
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

        // Load tips
        tipList = loadTips();
        String userRole = currentUser != null ? currentUser.getRole() : "";
        tipsAdapter = new TipsAdapter(tipList, tip -> {}, tip -> {}, userRole); // Passing empty consumers for edit and delete
        recyclerView.setAdapter(tipsAdapter);
    }

    private List<Tip> loadTips() {
        List<Tip> tips = new ArrayList<>();

        // Add cat care tips
        tips.add(new Tip(0, "Feeding", "Ensure your cat has a balanced diet with proper nutrition. Provide fresh water at all times.", "Source: American Society for the Prevention of Cruelty to Animals (ASPCA)"));
        tips.add(new Tip(0, "Grooming", "Regular brushing and grooming are essential for your cat's health.", "Source: Humane Society of the United States"));

        // Add cat socialization tips
        tips.add(new Tip(0, "Introducing New Cats", "Take it slow and provide separate spaces for new cats to get comfortable.", "Source: International Cat Care"));
        tips.add(new Tip(0, "Interaction with Humans", "Spend quality time playing and interacting with your cat daily.", "Source: American Association of Feline Practitioners (AAFP)"));

        // Add promoting positivity tips
        tips.add(new Tip(0, "Myth-Busting", "Cats are not aloof; they can be very affectionate and loving.", "Source: Cat Behavior Associates"));
        tips.add(new Tip(0, "Success Stories", "Share your cat's success story and inspire others.", "Source: Your Family!"));

        return tips;
    }

    @Override
    protected boolean shouldShowSearch() {
        return false; // Disable the search feature on this activity
    }
}