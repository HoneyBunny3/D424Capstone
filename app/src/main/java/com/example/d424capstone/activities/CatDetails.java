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

    private static final int PICK_IMAGE_REQUEST = 1;
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

        repository = MyApplication.getInstance().getRepository();

        initViews();

        // Initialize the DrawerLayout and ActionBarDrawerToggle
        initializeDrawer();

        initializeButtons();

        catID = getIntent().getIntExtra("catID", -1);
        userID = getIntent().getIntExtra("userID", -1);

        if (catID != -1) {
            loadCatDetails();
        } else {
            showToast("Invalid cat ID");
            finish();
        }

        if (userID == -1) {
            showToast("Invalid user ID");
            finish();
        }

        // Set window insets for EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initViews() {
        catNameEditText = findViewById(R.id.cat_name_details);
        catAgeEditText = findViewById(R.id.cat_age_details);
        catBioEditText = findViewById(R.id.cat_bio_details);
        catImageView = findViewById(R.id.cat_image_details);
        catImageView.setImageResource(R.drawable.baseline_image_search_24);
        saveButton = findViewById(R.id.save_button);
        backButton = findViewById(R.id.back_button);
    }

    private void loadCatDetails() {
        new Thread(() -> {
            Cat cat = repository.getCatByID(catID);
            runOnUiThread(() -> {
                if (cat != null) {
                    catNameEditText.setText(cat.getCatName());
                    catAgeEditText.setText(String.valueOf(cat.getCatAge()));
                    catBioEditText.setText(cat.getCatBio());
                    if (cat.getCatImage() != null && !cat.getCatImage().isEmpty()) {
                        catImageUri = Uri.parse(cat.getCatImage());
                        catImageView.setImageURI(catImageUri);
                    }
                } else {
                    showToast("Error loading cat details");
                }
            });
        }).start();
    }

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
                cat.setCatName(catName);
                cat.setCatAge(catAge);
                cat.setCatBio(catBio);
                cat.setCatImage(imageUriString);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            catImageUri = data.getData();
            catImageView.setImageURI(catImageUri);
        }
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(CatDetails.this, message, Toast.LENGTH_LONG).show());
    }

    @Override
    protected boolean shouldShowSearch() {
        return false; // Disable the search feature on this activity
    }
}