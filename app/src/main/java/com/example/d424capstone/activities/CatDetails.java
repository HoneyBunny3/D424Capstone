package com.example.d424capstone.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d424capstone.MyApplication;
import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.Cat;

public class CatDetails extends BaseActivity {

    private TextView catNameTextView, catAgeTextView, catBioTextView;
    private ImageView catImageView;
    private Button backButton;
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cat_details);

        repository = MyApplication.getInstance().getRepository();

        initViews();

        // Initialize the DrawerLayout and ActionBarDrawerToggle
        initializeDrawer();

        int catID = getIntent().getIntExtra("catID", -1);

        if (catID != -1) {
            loadCatDetails(catID);
        }

        backButton.setOnClickListener(view -> finish());

        // Set window insets for EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initViews() {
        catNameTextView = findViewById(R.id.cat_name_details);
        catAgeTextView = findViewById(R.id.cat_age_details);
        catBioTextView = findViewById(R.id.cat_bio_details);
        catImageView = findViewById(R.id.cat_image_details);
        backButton = findViewById(R.id.back_button);
    }

    private void loadCatDetails(int catID) {
        new Thread(() -> {
            Cat cat = repository.getCatByID(catID);
            runOnUiThread(() -> {
                if (cat != null) {
                    catNameTextView.setText(cat.getCatName());
                    catAgeTextView.setText(String.valueOf(cat.getCatAge()));
                    catBioTextView.setText(cat.getCatBio());
                    if (cat.getCatImage() != null && !cat.getCatImage().isEmpty()) {
                        catImageView.setImageURI(Uri.parse(cat.getCatImage()));
                    } else {
                        catImageView.setImageResource(R.drawable.baseline_image_search_24);
                    }
                }
            });
        }).start();
    }
}