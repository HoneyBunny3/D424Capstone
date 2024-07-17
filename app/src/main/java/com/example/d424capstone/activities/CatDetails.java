package com.example.d424capstone.activities;

import android.content.Intent;
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

import com.example.d424capstone.MyApplication;
import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.Cat;

public class CatDetails extends BaseActivity {
    private static final int PICK_IMAGE_REQUEST = 1; // Request code for image picker
    private Repository repository;
    private EditText catNameEditText, catAgeEditText, catBioEditText;
    private ImageView catImageView;
    private Uri catImageUri;
    private Button saveButton, backButton;
    private int catID = -1; // Default value for new cat profiles
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cat_details);

        repository = MyApplication.getInstance().getRepository(); // Initialize repository instance

        initializeDrawer(); // Initialize the DrawerLayout and ActionBarDrawerToggle
        initViews(); // Initialize UI components
        initializeButtons(); // Initialize buttons and set their click listeners

        // Get catID and userID from intent extras
        catID = getIntent().getIntExtra("catID", -1);
        userID = getIntent().getIntExtra("userID", -1);

        if (catID != -1) {
            loadCatDetails(); // Load cat details if catID is valid
        } else {
            showToast("Invalid cat ID");
            finish();
        }

        if (userID == -1) {
            showToast("Invalid user ID");
            finish();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Initialize UI components
    private void initViews() {
        catNameEditText = findViewById(R.id.cat_name_details);
        catAgeEditText = findViewById(R.id.cat_age_details);
        catBioEditText = findViewById(R.id.cat_bio_details);
        catImageView = findViewById(R.id.cat_image_details);
        catImageView.setImageResource(R.drawable.baseline_image_search_24); // Set default image
        saveButton = findViewById(R.id.save_button);
        backButton = findViewById(R.id.back_button);
    }

    // Load cat details from the repository
    private void loadCatDetails() {
        new Thread(() -> {
            Cat cat = repository.getCatByID(catID);
            runOnUiThread(() -> {
                if (cat != null) {
                    catNameEditText.setText(cat.getName());
                    catAgeEditText.setText(String.valueOf(cat.getAge()));
                    catBioEditText.setText(cat.getBio());
                    if (cat.getImage() != null && !cat.getImage().isEmpty()) {
                        catImageUri = Uri.parse(cat.getImage());
                        catImageView.setImageURI(catImageUri);
                    }
                } else {
                    showToast("Error loading cat details");
                }
            });
        }).start();
    }

    // Set up button click listeners
    private void initializeButtons() {
        saveButton.setOnClickListener(view -> saveCatDetails());
        backButton.setOnClickListener(view -> finish());
        catImageView.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        });
    }

    // Save cat details to the repository
    private void saveCatDetails() {
        String catName = catNameEditText.getText().toString();
        String catAgeStr = catAgeEditText.getText().toString();
        String catBio = catBioEditText.getText().toString();

        if (catName.isEmpty()) {
            showToast("Please enter your cat's name");
            return;
        }

        if (catAgeStr.isEmpty()) {
            showToast("Please enter your cat's age");
            return;
        }

        int catAge;
        try {
            catAge = Integer.parseInt(catAgeStr);
            if (catAge < 0) {
                showToast("Please enter a valid age for your cat");
                return;
            }
        } catch (NumberFormatException e) {
            showToast("Please enter a valid age for your cat");
            return;
        }

        if (catBio.isEmpty()) {
            showToast("Please enter a bio for your cat");
            return;
        }

        String imageUriString = (catImageUri != null) ? catImageUri.toString() : "" + getPackageName() + "/" + R.drawable.baseline_image_search_24;

        new Thread(() -> {
            Cat cat = repository.getCatByID(catID);
            if (cat != null) {
                cat.setName(catName);
                cat.setAge(catAge);
                cat.setBio(catBio);
                cat.setImage(imageUriString);
                repository.updateCat(cat);
                runOnUiThread(() -> {
                    showToast("Cat profile updated successfully");
                    setResult(RESULT_OK);
                    finish();
                });
            } else {
                runOnUiThread(() -> showToast("Error updating cat profile"));
            }
        }).start();
    }

    // Handle the result from the image picker intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            catImageUri = data.getData();
            catImageView.setImageURI(catImageUri);
        }
    }

    // Show a toast message
    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(CatDetails.this, message, Toast.LENGTH_LONG).show());
    }

    @Override
    protected boolean shouldShowSearch() {
        return false; // Disable the search feature on this activity
    }
}