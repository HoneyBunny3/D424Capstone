package com.example.d424capstone.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.SocialPost;
import com.example.d424capstone.entities.User;

import java.util.ArrayList;
import java.util.List;

public class SocialPostAdapter extends RecyclerView.Adapter<SocialPostAdapter.SocialPostViewHolder> {

    private List<SocialPost> socialPosts;
    private LayoutInflater inflater;
    private Repository repository;
    private OnItemClickListener listener;
    private boolean isModerationMode;
    private Context context;
    private int currentUserID;

    public SocialPostAdapter(Context context, List<SocialPost> socialPosts, Repository repository, OnItemClickListener listener, boolean isModerationMode, int currentUserID) {
        this.inflater = LayoutInflater.from(context);
        this.socialPosts = socialPosts != null ? socialPosts : new ArrayList<>();
        this.repository = repository;
        this.listener = listener;
        this.isModerationMode = isModerationMode;
        this.context = context;
        this.currentUserID = currentUserID;
    }

    @NonNull
    @Override
    public SocialPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_social_post, parent, false);
        return new SocialPostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SocialPostViewHolder holder, int position) {
        SocialPost currentPost = socialPosts.get(position);
        holder.contentTextView.setText(currentPost.getContent());
        holder.likesTextView.setText("Likes: " + currentPost.getLikes());

        if (!isModerationMode) {
            holder.itemView.setOnClickListener(v -> {
                User currentUser = repository.getCurrentUser();
                if (currentUser != null) {
                    if (currentPost.getUserID() == currentUser.getUserID()) {
                        Toast.makeText(holder.itemView.getContext(), "You cannot like your own post", Toast.LENGTH_SHORT).show();
                    } else if (!currentUser.hasLikedPost(currentPost.getSocialPostID())) {
                        repository.likePost(currentUser.getUserID(), currentPost);
                        holder.likesTextView.setText("Likes: " + currentPost.getLikes());
                        Toast.makeText(holder.itemView.getContext(), "Liked!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(holder.itemView.getContext(), "You have already liked this post", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        holder.editButton.setOnClickListener(v -> listener.onEditClick(currentPost));
        holder.deleteButton.setOnClickListener(v -> listener.onDeleteClick(currentPost));

        holder.shareButton.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareBody = currentPost.getContent();
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this post!");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            context.startActivity(Intent.createChooser(shareIntent, "Share via"));
        });
    }

    @Override
    public int getItemCount() {
        return socialPosts.size();
    }

    public void updateData(List<SocialPost> newSocialPosts) {
        this.socialPosts = newSocialPosts;
        notifyDataSetChanged();
    }

    public void addPost(SocialPost post) {
        socialPosts.add(post);
        notifyItemInserted(socialPosts.size() - 1);
    }

    public void updatePost(SocialPost updatedPost) {
        for (int i = 0; i < socialPosts.size(); i++) {
            if (socialPosts.get(i).getSocialPostID() == updatedPost.getSocialPostID()) {
                socialPosts.set(i, updatedPost);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public interface OnItemClickListener {
        void onEditClick(SocialPost socialPost);
        void onDeleteClick(SocialPost socialPost);
    }

    public class SocialPostViewHolder extends RecyclerView.ViewHolder {
        private TextView contentTextView;
        private TextView likesTextView;
        private Button editButton;
        private Button deleteButton;
        private Button shareButton;

        public SocialPostViewHolder(@NonNull View itemView) {
            super(itemView);
            contentTextView = itemView.findViewById(R.id.postContentTextView);
            likesTextView = itemView.findViewById(R.id.postLikesTextView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            shareButton = itemView.findViewById(R.id.shareButton);
        }
    }
}