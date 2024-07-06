package com.example.d424capstone.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424capstone.MyApplication;
import com.example.d424capstone.R;
import com.example.d424capstone.adapters.*;
import com.example.d424capstone.database.*;
import com.example.d424capstone.entities.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class CatSocialScreen extends BaseActivity {

    private Repository repository;
    private SocialPostAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cat_social_screen);

        repository = MyApplication.getInstance().getRepository();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new Thread(() -> {
            List<SocialPost> socialPosts = repository.getAllSocialPosts();
            runOnUiThread(() -> {
                adapter = new SocialPostAdapter(this, socialPosts, repository);
                recyclerView.setAdapter(adapter);
            });
        }).start();

        FloatingActionButton fabAddPost = findViewById(R.id.fab_add_post);
        fabAddPost.setOnClickListener(v -> openAddPostDialog());

        initializeDrawer();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void openAddPostDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_post, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        EditText editPostContent = dialogView.findViewById(R.id.edit_post_content);
        Button buttonSavePost = dialogView.findViewById(R.id.button_save_post);

        buttonSavePost.setOnClickListener(v -> {
            String content = editPostContent.getText().toString();
            if (!content.isEmpty()) {
                SocialPost newPost = new SocialPost(0, 1, content, 0); // Assuming userID 1 for simplicity
                new Thread(() -> {
                    repository.insertSocialPost(newPost);
                    runOnUiThread(() -> {
                        adapter.addPost(newPost);
                        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                        dialog.dismiss();
                    });
                }).start();
            }
        });

        dialog.show();
    }
}