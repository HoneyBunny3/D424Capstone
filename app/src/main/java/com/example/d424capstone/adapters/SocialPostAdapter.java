package com.example.d424capstone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424capstone.R;
import com.example.d424capstone.entities.SocialPost;

import java.util.ArrayList;
import java.util.List;

public class SocialPostAdapter extends RecyclerView.Adapter<SocialPostAdapter.SocialPostViewHolder> {
    private List<SocialPost> socialPosts;
    private LayoutInflater inflater;

    public SocialPostAdapter(Context context, List<SocialPost> socialPosts) {
        this.inflater = LayoutInflater.from(context);
        this.socialPosts = socialPosts != null ? socialPosts : new ArrayList<>();
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
    }

    @Override
    public int getItemCount() {
        return socialPosts.size();
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