package com.example.d424capstone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d424capstone.MyApplication;
import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.SocialPost;

public class CatSocialScreen extends BaseActivity {

    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cat_social_screen);

        repository = MyApplication.getInstance().getRepository(); // Use repository from MyApplication

        displayFeaturedContent();

        // Initialize buttons and set their click listeners
        initializeButtons();

        // Initialize the DrawerLayout and ActionBarDrawerToggle
        initializeDrawer();

        // Set window insets for EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void displayFeaturedContent() {
        new Thread(() -> {
            SocialPost mostLikedPost = repository.getMostLikedPost();
            runOnUiThread(() -> {
                TextView mostLikedPostTextView = findViewById(R.id.mostLikedPostTextView);
                if (mostLikedPost != null) {
                    mostLikedPostTextView.setText("Most Liked Post: " + mostLikedPost.getContent());
                }
            });
        }).start();
    }

    private void initializeButtons() {
        // Initialize buttons and set their click listeners
        Button buttonLogin = findViewById(R.id.touserloginscreen);
        buttonLogin.setOnClickListener(view -> startActivity(new Intent(CatSocialScreen.this, UserLoginScreen.class)));

        Button buttonSignup = findViewById(R.id.tousersignupscreen);
        buttonSignup.setOnClickListener(view -> startActivity(new Intent(CatSocialScreen.this, UserProfileScreen.class)));

        Button buttonShopping = findViewById(R.id.toshoppingscreen);
        buttonShopping.setOnClickListener(view -> startActivity(new Intent(CatSocialScreen.this, ShoppingScreen.class)));

        Button buttonSocial = findViewById(R.id.tocatsocialscreen);
        buttonSocial.setOnClickListener(view -> startActivity(new Intent(CatSocialScreen.this, CatSocialScreen.class)));
    }
}