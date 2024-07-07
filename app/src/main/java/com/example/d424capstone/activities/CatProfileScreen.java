package com.example.d424capstone.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
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
import com.google.android.material.textfield.TextInputEditText;

public class CatProfileScreen extends BaseActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Repository repository;
    private TextInputEditText editName, editAge, editBio;
    private ImageView catImageView;
    private Uri catImageUri;
    private Button saveButton, cancelButton;
    private int catID = -1; // Default value for new cat profiles
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cat_profile_screen);

        initViews();
        repository = MyApplication.getInstance().getRepository();

        catID = getIntent().getIntExtra("catID", -1);
        userID = getIntent().getIntExtra("userID", -1);

        if (userID == -1) {
            showToast("Invalid user ID");
            finish();
            return;
        }

        if (catID != -1) {
            loadCatDetails();
        }

        initializeButtons();
        initializeDrawer();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initViews() {
        editName = findViewById(R.id.cat_name);
        editAge = findViewById(R.id.cat_age);
        editBio = findViewById(R.id.cat_bio);
        catImageView = findViewById(R.id.cat_image);
        catImageView.setImageResource(R.drawable.baseline_image_search_24);
        saveButton = findViewById(R.id.save_cat);
        cancelButton = findViewById(R.id.cancel_cat);
    }

    private void loadCatDetails() {
        new Thread(() -> {
            Cat cat = repository.getCatByID(catID);
            runOnUiThread(() -> {
                if (cat != null) {
                    editName.setText(cat.getCatName());
                    editAge.setText(String.valueOf(cat.getCatAge()));
                    editBio.setText(cat.getCatBio());
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
        saveButton.setOnClickListener(view -> saveCatProfile());
        cancelButton.setOnClickListener(view -> finish());
        catImageView.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        });
    }

    private void saveCatProfile() {
        String catName = editName.getText().toString();
        String catAgeStr = editAge.getText().toString();
        String catBio = editBio.getText().toString();

        if (catName.isEmpty() || catAgeStr.isEmpty() || catBio.isEmpty()) {
            showToast("Please fill all fields");
            return;
        }

        int catAge;
        try {
            catAge = Integer.parseInt(catAgeStr);
        } catch (NumberFormatException e) {
            showToast("Please enter a valid age");
            return;
        }

        String imageUriString = (catImageUri != null) ? catImageUri.toString() : "";

        Cat cat = new Cat(catID == -1 ? 0 : catID, catName, catAge, imageUriString, catBio, userID);

        new Thread(() -> {
            if (catID == -1) {
                long newCatID = repository.insertCat(cat);
                if (newCatID != -1) {
                    runOnUiThread(() -> {
                        showToast("Cat profile created");
                        finish();
                    });
                } else {
                    runOnUiThread(() -> showToast("Error saving cat profile"));
                }
            } else {
                repository.updateCat(cat);
                runOnUiThread(() -> {
                    showToast("Cat profile updated");
                    finish();
                });
            }
        }).start();
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(CatProfileScreen.this, message, Toast.LENGTH_LONG).show());
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