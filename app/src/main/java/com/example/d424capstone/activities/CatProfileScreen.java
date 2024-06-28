package com.example.d424capstone.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.AssociatedCats;

/**
 * Activity class for displaying and managing the cat profile screen.
 */
public class CatProfileScreen extends BaseActivity {

    // Request code for picking an image
    private static final int PICK_IMAGE_REQUEST = 1;

    // Repository instance for data operations
    private Repository repository;

    // UI components for cat profile details
    private EditText catNameEditText, catAgeEditText, catBioEditText;
    private ImageView catImageView;
    private Uri catImageUri;
    private Button saveButton, cancelButton;
    private int catID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cat_profile_screen);

        // Initialize repository
        repository = new Repository(getApplication());

        // Initialize the DrawerLayout and ActionBarDrawerToggle
        initializeDrawer();

        // Initialize views
        initializeViews();

        // Initialize buttons and set click listeners
        initializeButtons();

        // Set window insets for EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Check if editing an existing cat profile
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("catID")) {
            catID = intent.getIntExtra("catID", -1);
            if (catID != -1) {
                loadCatProfile(catID);
            }
        }
    }

    /**
     * Initialize views for cat profile screen.
     */
    private void initializeViews() {
        catNameEditText = findViewById(R.id.cat_name);
        catAgeEditText = findViewById(R.id.cat_age);
        catBioEditText = findViewById(R.id.cat_bio);
        catImageView = findViewById(R.id.cat_image);
        catImageView.setImageResource(R.drawable.baseline_image_search_24); // Set default image
        saveButton = findViewById(R.id.save);
        cancelButton = findViewById(R.id.cancel);
    }

    /**
     * Initialize buttons and set click listeners for saving and canceling cat profile changes.
     */
    private void initializeButtons() {
        // Initialize the save button
        saveButton.setOnClickListener(view -> saveCatProfile());

        // Initialize the cancel button
        cancelButton.setOnClickListener(view -> finish());

        // Set an OnClickListener to the catImageView to allow picking an image
        catImageView.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        });
    }

    /**
     * Save the cat profile to the database.
     */
//    private void saveCatProfile() {
//        String catName = catNameEditText.getText().toString();
//        String catAgeStr = catAgeEditText.getText().toString();
//        String catBio = catBioEditText.getText().toString();
//
//        // Validate input fields
//        if (catName.isEmpty() || catAgeStr.isEmpty()) {
//            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        int catAge = Integer.parseInt(catAgeStr);
//        String imageUriString = (catImageUri != null) ? catImageUri.toString() : "android.resource://" + getPackageName() + "/" + R.drawable.baseline_image_search_24;
//        int loggedInUserId = getLoggedInUserId();  // Assuming this method fetches the logged-in user's ID
//
//        if (catID == -1) {
//            // New cat profile
//            AssociatedCats cat = new AssociatedCats(0, catName, catAge, imageUriString, catBio, loggedInUserId);
//            repository.insertCat(cat, id -> {
//                if (id != -1) {
//                    runOnUiThread(() -> {
//                        Toast.makeText(CatProfileScreen.this, "Cat profile saved", Toast.LENGTH_SHORT).show();
//                        finish();
//                    });
//                } else {
//                    runOnUiThread(() -> Toast.makeText(CatProfileScreen.this, "Failed to save cat profile", Toast.LENGTH_SHORT).show());
//                }
//            });
//        } else {
//            // Update existing cat profile
//            AssociatedCats cat = new AssociatedCats(catID, catName, catAge, imageUriString, catBio, loggedInUserId);
//            repository.updateCat(cat);
//            runOnUiThread(() -> {
//                Toast.makeText(CatProfileScreen.this, "Cat profile updated", Toast.LENGTH_SHORT).show();
//                finish();
//            });
//        }
//    }

    private void saveCatProfile() {
        String catName = catNameEditText.getText().toString();
        String catAgeStr = catAgeEditText.getText().toString();
        String catBio = catBioEditText.getText().toString();

        if (catName.isEmpty() || catAgeStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int catAge = Integer.parseInt(catAgeStr);
        String imageUriString = (catImageUri != null) ? catImageUri.toString() : "android.resource://" + getPackageName() + "/" + R.drawable.baseline_image_search_24;
        int loggedInUserId = getLoggedInUserId();  // Assuming this method fetches the logged-in user's ID

        // New cat profile
        AssociatedCats cat = new AssociatedCats(catID, catName, catAge, imageUriString, catBio, loggedInUserId);
        repository.insertOrUpdateCat(cat, id -> {
            if (id != -1) {
                runOnUiThread(() -> {
                    Toast.makeText(CatProfileScreen.this, "Cat profile saved", Toast.LENGTH_SHORT).show();
                    finish();
                });
            } else {
                runOnUiThread(() -> Toast.makeText(CatProfileScreen.this, "Failed to save cat profile", Toast.LENGTH_SHORT).show());
            }
        });
    }

    /**
     * Load the cat profile from the database.
     *
     * @param catID The ID of the cat to be loaded.
     */
    private void loadCatProfile(int catID) {
        repository.executeAsync(() -> {
            AssociatedCats cat = repository.getCatByID(catID);
            if (cat != null) {
                runOnUiThread(() -> {
                    catNameEditText.setText(cat.getCatName());
                    catAgeEditText.setText(String.valueOf(cat.getCatAge()));
                    catBioEditText.setText(cat.getCatBio());
                    catImageUri = Uri.parse(cat.getImageUri());
                    catImageView.setImageURI(catImageUri);
                });
            }
        });
    }

    /**
     * Get the user ID of the currently logged-in user from SharedPreferences.
     *
     * @return The ID of the logged-in user.
     */
    private int getLoggedInUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return sharedPreferences.getInt("LoggedInUserID", -1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            catImageUri = data.getData();
            catImageView.setImageURI(catImageUri);
        }
    }
}