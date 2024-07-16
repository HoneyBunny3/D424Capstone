package com.example.d424capstone.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424capstone.MyApplication;
import com.example.d424capstone.R;
import com.example.d424capstone.models.TipsAdapter;
import com.example.d424capstone.entities.Tip;
import com.example.d424capstone.entities.User;
import com.example.d424capstone.database.Repository;

import java.util.List;

public class LoveYourCatScreen extends BaseActivity implements TipsAdapter.OnTipInteractionListener {
    private RecyclerView recyclerView;
    private TipsAdapter tipsAdapter;
    private List<Tip> tipList;
    private Repository repository;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_love_your_cat_screen);

        // Initialize the DrawerLayout and ActionBarDrawerToggle
        initializeDrawer();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        repository = MyApplication.getInstance().getRepository(); // Initialize repository instance
        currentUser = repository.getCurrentUser();

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.love_your_cat_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load tips from the repository
        tipList = repository.getAllTips();
        String userRole = currentUser != null ? currentUser.getRole() : "";
        tipsAdapter = new TipsAdapter(tipList, this, userRole); // Passing 'this' as the listener
        recyclerView.setAdapter(tipsAdapter);
    }

    @Override
    protected boolean shouldShowSearch() {
        return false; // Disable the search feature on this activity
    }

    @Override
    public void onEditTip(int position) {
        Tip tip = tipList.get(position);
        showEditTipDialog(tip, position);
    }

    @Override
    public void onDeleteTip(int position) {
        Tip tip = tipList.get(position);
        repository.deleteTip(tip);
        tipList.remove(tip);
        tipsAdapter.notifyItemRemoved(position);
        Toast.makeText(this, "Tip deleted", Toast.LENGTH_SHORT).show();
    }

    private void showEditTipDialog(Tip tip, int position) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_tip, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("Edit Tip");

        EditText tipTitleEditText = dialogView.findViewById(R.id.tip_title);
        EditText tipContentEditText = dialogView.findViewById(R.id.tip_content);
        EditText tipSourceEditText = dialogView.findViewById(R.id.tip_source);

        tipTitleEditText.setText(tip.getTitle());
        tipContentEditText.setText(tip.getContent());
        tipSourceEditText.setText(tip.getSource());

        builder.setPositiveButton("Save", (dialog, which) -> {
            String title = tipTitleEditText.getText().toString();
            String content = tipContentEditText.getText().toString();
            String source = tipSourceEditText.getText().toString();

            if (title.isEmpty() || content.isEmpty() || source.isEmpty()) {
                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            tip.setTitle(title);
            tip.setContent(content);
            tip.setSource(source);
            repository.updateTip(tip);
            tipsAdapter.notifyItemChanged(position);
            Toast.makeText(this, "Tip updated", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}