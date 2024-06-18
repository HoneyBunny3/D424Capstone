package com.example.d424capstone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.SocialPost;
import com.example.d424capstone.entities.StoreItem;

public class HomeScreen extends AppCompatActivity {

    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_screen);

        repository = new Repository(getApplication());

        displayFeaturedContent();

        // Initialize the button that navigates to the User Login screen.
        Button button_login = findViewById(R.id.touserloginscreen);
        button_login.setOnClickListener(view -> {
            // Create an intent to start the UserLoginScreen activity.
            Intent intent = new Intent(HomeScreen.this, UserLoginScreen.class);
            startActivity(intent);
        });

        // Initialize the button that navigates to the Sign Up screen.
        Button button_signup = findViewById(R.id.tousersignupscreen);
        button_signup.setOnClickListener(view -> {
            // Create an intent to start the UserSignUpScreen activity.
            Intent intent = new Intent(HomeScreen.this, UserSignUpScreen.class);
            startActivity(intent);
        });

        // Initialize the button that navigates to the Shopping screen.
        Button button_shopping = findViewById(R.id.toshoppingscreen);
        button_shopping.setOnClickListener(view -> {
            // Create an intent to start the ShoppingScreen activity.
            Intent intent = new Intent(HomeScreen.this, ShoppingScreen.class);
            startActivity(intent);
        });

        // Initialize the button that navigates to the Cat Social screen.
        Button button_social = findViewById(R.id.tocatsocialscreen);
        button_social.setOnClickListener(view -> {
            // Create an intent to start the CatSocialScreen activity.
            Intent intent = new Intent(HomeScreen.this, CatSocialScreen.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void displayFeaturedContent() {
        repository.executeAsync(() -> {
            StoreItem featuredItem = repository.getFeaturedItem();
            SocialPost mostLikedPost = repository.getMostLikedPost();

            runOnUiThread(() -> {
                // Assuming you have TextViews in your layout to display the content
                TextView featuredItemTextView = findViewById(R.id.featuredItemTextView);
                TextView mostLikedPostTextView = findViewById(R.id.mostLikedPostTextView);

                if (featuredItem != null) {
                    featuredItemTextView.setText("Featured Item: " + featuredItem.getName());
                }
                if (mostLikedPost != null) {
                    mostLikedPostTextView.setText("Most Liked Post: " + mostLikedPost.getContent());
                }
            });
        });
    }
}