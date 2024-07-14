package com.example.d424capstone.activities;

import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d424capstone.MyApplication;
import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.SocialPost;
import com.example.d424capstone.entities.StoreItem;
import com.example.d424capstone.entities.User;
import com.example.d424capstone.utilities.CSVUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ReportGenerationScreen extends BaseActivity {
    private Repository repository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report_generation_screen);

        repository = MyApplication.getInstance().getRepository(); // Use repository from MyApplication

        // Initialize buttons and set their click listeners
        initializeButtons();

        // Initialize the DrawerLayout and ActionBarDrawerToggle
        initializeDrawer();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeButtons() {
        Button userReportButton = findViewById(R.id.generate_user_report);
        Button shoppingReportButton = findViewById(R.id.generate_shopping_report);
        Button socialReportButton = findViewById(R.id.generate_social_report);

        userReportButton.setOnClickListener(v -> generateUserReport());
        shoppingReportButton.setOnClickListener(v -> generateShoppingReport());
        socialReportButton.setOnClickListener(v -> generateSocialReport());
    }

    private void generateUserReport() {
        List<User> users = repository.getAllUsers();
        String userCSV = CSVUtil.generateCSVFromUsers(users);
        displayData(userCSV);
        exportToCSVFile(userCSV, "users_report.csv");
    }

    private void generateShoppingReport() {
        List<StoreItem> storeItems = repository.getAllStoreItems();
        String storeCSV = CSVUtil.generateCSVFromStoreItems(storeItems);
        displayData(storeCSV);
        exportToCSVFile(storeCSV, "store_report.csv");
    }

    private void generateSocialReport() {
        List<SocialPost> socialPosts = repository.getAllSocialPosts();
        String socialCSV = CSVUtil.generateCSVFromSocialPosts(socialPosts);
        displayData(socialCSV);
        exportToCSVFile(socialCSV, "social_report.csv");
    }

    private void displayData(String data) {
        // Display the data in a TextView or similar widget
        Toast.makeText(this, "Data generated and displayed", Toast.LENGTH_SHORT).show();
    }

    private void exportToCSVFile(String data, String fileName) {
        File file = new File(Environment.getExternalStorageDirectory(), fileName);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(data);
            Toast.makeText(this, "CSV file saved: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error saving CSV file", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}