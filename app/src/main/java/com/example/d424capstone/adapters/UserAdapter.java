package com.example.d424capstone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424capstone.R;
import com.example.d424capstone.entities.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> users; // List of users to display
    private final LayoutInflater mInflater; // LayoutInflater to inflate the user list items

    // Constructor for the UserAdapter.
    public UserAdapter(Context context, List<User> users) {
        mInflater = LayoutInflater.from(context);
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for individual user list items.
        View itemView = mInflater.inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        if (users != null) {
            // Get the current User and set its email in the TextView.
            User current = users.get(position);
            holder.userEmailView.setText(current.getEmail());
        } else {
            // If there are no users, display a placeholder text.
            holder.userEmailView.setText("No user found");
        }
    }

    @Override
    public int getItemCount() {
        return (users != null) ? users.size() : 0;
    }

    /**
     * Updates the list of users and notifies the adapter of the data change.
     *
     * @param users The updated list of users.
     */
    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class for managing individual user items in the RecyclerView.
     */
    class UserViewHolder extends RecyclerView.ViewHolder {
        private final TextView userEmailView; // TextView to display the user email.

        /**
         * Constructor for the UserViewHolder.
         *
         * @param itemView The view for the individual user item.
         */
        private UserViewHolder(View itemView) {
            super(itemView);
            userEmailView = itemView.findViewById(R.id.email);
        }
    }
}