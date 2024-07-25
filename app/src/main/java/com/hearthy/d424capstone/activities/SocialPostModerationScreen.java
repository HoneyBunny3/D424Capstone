package com.hearthy.d424capstone.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hearthy.d424capstone.MyApplication;
import com.hearthy.d424capstone.R;
import com.hearthy.d424capstone.adapters.SocialPostAdapter;
import com.hearthy.d424capstone.database.Repository;
import com.hearthy.d424capstone.entities.SocialPost;

import java.util.List;

public class SocialPostModerationScreen extends BaseActivity {
    private Repository repository;
    private SharedPreferences sharedPreferences;
    private SocialPostAdapter socialPostAdapter;
    private List<SocialPost> socialPostList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_social_post_moderation_screen);

        repository = MyApplication.getInstance().getRepository(); // Initialize repository instance
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE); // Initialize SharedPreferences

        initializeDrawer(); // Initialize the DrawerLayout and ActionBarDrawerToggle
        initViews(); // Initialize UI components
        refreshPosts(); // Load social posts from the database

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Initialize UI components
    private void initViews() {
        recyclerView = findViewById(R.id.social_post_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void showEditDialog(SocialPost socialPost) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Post");
        final View customLayout = getLayoutInflater().inflate(R.layout.item_edit_social_post, null);
        builder.setView(customLayout);

        EditText contentEditText = customLayout.findViewById(R.id.edit_post_content);
        contentEditText.setText(socialPost.getContent());

        AlertDialog dialog = builder.create();

        Button saveButton = customLayout.findViewById(R.id.saveButton);
        Button cancelButton = customLayout.findViewById(R.id.cancelButton);

        saveButton.setOnClickListener(v -> {
            String updatedContent = contentEditText.getText().toString();
            if (updatedContent.isEmpty()) {
                Toast.makeText(this, "Content must not be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            socialPost.setContent(updatedContent);
            repository.updateSocialPost(socialPost);
            refreshPosts();
            Toast.makeText(SocialPostModerationScreen.this, "Post updated", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void refreshPosts() {
        new Thread(() -> {
            socialPostList = repository.getAllSocialPosts();
            runOnUiThread(() -> {
                if (socialPostAdapter == null) {
                    socialPostAdapter = new SocialPostAdapter(this, socialPostList, repository, new SocialPostAdapter.OnItemClickListener() {
                        @Override
                        public void onEditClick(SocialPost socialPost) {
                            showEditDialog(socialPost);
                        }

                        @Override
                        public void onDeleteClick(SocialPost socialPost) {
                            new Thread(() -> {
                                repository.deleteSocialPost(socialPost.getSocialPostID());
                                runOnUiThread(() -> {
                                    refreshPosts();
                                    Toast.makeText(SocialPostModerationScreen.this, "Post deleted", Toast.LENGTH_SHORT).show();
                                });
                            }).start();
                        }
                    }, true, 0, true); // Pass true to indicate moderation mode and isManagementPage
                    recyclerView.setAdapter(socialPostAdapter);
                } else {
                    socialPostAdapter.updateData(socialPostList);
                }
            });
        }).start();
    }

    @Override
    protected boolean shouldShowSearch() {
        return false; // Disable the search feature on this activity
    }
}