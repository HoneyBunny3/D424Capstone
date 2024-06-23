package com.example.d424capstone.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.AssociatedCats;
import com.example.d424capstone.entities.UserWithCats;
import com.example.d424capstone.entities.User;

import java.util.List;

public class UserProfileScreen extends BaseActivity {

    private Repository repository;
    private TextView associatedCatsTextView;
    private TextView firstNameTextView;
    private TextView lastNameTextView;
    private TextView emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile_screen);

        repository = new Repository(getApplication());
        associatedCatsTextView = findViewById(R.id.associated_cats);
        firstNameTextView = findViewById(R.id.first_name);
        lastNameTextView = findViewById(R.id.last_name);
        emailTextView = findViewById(R.id.email_address);

        int userID = getUserID();
        repository.getUserByIDAsync(userID, new Repository.UserCallback() {
            @Override
            public void onUserRetrieved(User user) {
                if (user != null) {
                    runOnUiThread(() -> {
                        firstNameTextView.setText(user.getFirstName());
                        lastNameTextView.setText(user.getLastName());
                        emailTextView.setText(user.getEmail());
                    });
                }
            }
        });

        // Initialize the DrawerLayout and ActionBarDrawerToggle
        initializeDrawer();

        // Initialize buttons and set click listeners
        initializeButtons();

        // Load associated cats for the current user
        loadAssociatedCats();

        // Set window insets for EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeButtons() {
        // Initialize the button that navigates to the Shopping screen.
        Button buttonShopping = findViewById(R.id.toshoppingscreen);
        buttonShopping.setOnClickListener(view -> {
            // Create an intent to start the ShoppingScreen activity.
            Intent intent = new Intent(UserProfileScreen.this, ShoppingScreen.class);
            startActivity(intent);
        });

        // Initialize the button that navigates to the Cat Social screen.
        Button buttonSocial = findViewById(R.id.tocatsocialscreen);
        buttonSocial.setOnClickListener(view -> {
            // Create an intent to start the CatSocialScreen activity.
            Intent intent = new Intent(UserProfileScreen.this, CatSocialScreen.class);
            startActivity(intent);
        });

        // Initialize the button that navigates to the Cat Profile screen.
        Button buttonCatProfile = findViewById(R.id.tocatprofilescreen);
        buttonCatProfile.setOnClickListener(view -> {
            // Create an intent to start the CatProfileScreen activity.
            Intent intent = new Intent(UserProfileScreen.this, CatProfileScreen.class);
            startActivity(intent);
        });
    }

    private void loadAssociatedCats() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String currentUserName = sharedPreferences.getString("LoggedInUser", "");
        repository.getUserByUsernameAsync(currentUserName, user -> {
            if (user != null) {
                List<UserWithCats> userWithCatsList = repository.getCatsForUser(user.getUserID());
                StringBuilder sb = new StringBuilder("Associated Cats:\n");
                for (UserWithCats userWithCats : userWithCatsList) {
                    for (AssociatedCats cat : userWithCats.getCats()) {
                        sb.append(cat.getCatName()).append("\n");
                    }
                }
                runOnUiThread(() -> associatedCatsTextView.setText(sb.toString()));
            }
        });
    }

    private int getUserID() {
        // Implement the logic to get the user ID
        // This can be retrieved from shared preferences or passed as an intent extra
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return sharedPreferences.getInt("LoggedInUserID", 1); // Replace with actual logic
    }
}