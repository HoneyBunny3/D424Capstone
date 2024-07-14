package com.example.d424capstone.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.content.FileProvider;
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
    private TextView reportTextView;
    private Button shareButton;
    private File csvFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report_generation_screen);

        repository = MyApplication.getInstance().getRepository();

        reportTextView = findViewById(R.id.report_text_view);
        shareButton = findViewById(R.id.share_button);
        shareButton.setOnClickListener(v -> shareCSVFile());

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
        reportTextView.setText(data);
    }

    private void exportToCSVFile(String data, String fileName) {
        csvFile = new File(getFilesDir(), fileName);
        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.write(data);
            Toast.makeText(this, "CSV file saved: " + csvFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error saving CSV file", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void shareCSVFile() {
        if (csvFile != null && csvFile.exists()) {
            Uri fileUri = FileProvider.getUriForFile(this, "com.example.d424capstone.fileprovider", csvFile);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/csv");
            intent.putExtra(Intent.EXTRA_STREAM, fileUri);
            startActivity(Intent.createChooser(intent, "Share CSV file"));
        } else {
            Toast.makeText(this, "No CSV file to share", Toast.LENGTH_SHORT).show();
        }
    }
}