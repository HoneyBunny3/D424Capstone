package com.example.d424capstone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424capstone.R;
import com.example.d424capstone.entities.*;
import com.example.d424capstone.database.*;

import java.util.ArrayList;
import java.util.List;

public class SocialPostAdapter extends RecyclerView.Adapter<SocialPostAdapter.SocialPostViewHolder> {
    private List<SocialPost> socialPosts;
    private LayoutInflater inflater;
    private Repository repository;

    public SocialPostAdapter(Context context, List<SocialPost> socialPosts, Repository repository) {
        this.inflater = LayoutInflater.from(context);
        this.socialPosts = socialPosts != null ? socialPosts : new ArrayList<>();
        this.repository = repository;
    }

    @NonNull
    @Override
    public SocialPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.social_post_item, parent, false);
        return new SocialPostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SocialPostViewHolder holder, int position) {
        SocialPost currentPost = socialPosts.get(position);
        holder.contentTextView.setText(currentPost.getContent());
        holder.likesTextView.setText("Likes: " + currentPost.getLikes());

        holder.itemView.setOnClickListener(v -> {
            currentPost.incrementLikes();
            repository.updateSocialPost(currentPost);
            updatePost(currentPost);
            holder.likesTextView.setText("Likes: " + currentPost.getLikes());
            Toast.makeText(holder.itemView.getContext(), "Liked!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return socialPosts.size();
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

    public class SocialPostViewHolder extends RecyclerView.ViewHolder {
        private TextView contentTextView;
        private TextView likesTextView;

        public SocialPostViewHolder(@NonNull View itemView) {
            super(itemView);
            contentTextView = itemView.findViewById(R.id.postContentTextView);
            likesTextView = itemView.findViewById(R.id.postLikesTextView);
        }
    }
}