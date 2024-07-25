package com.hearthy.d424capstone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hearthy.d424capstone.R;
import com.hearthy.d424capstone.entities.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> users; // List of users to display
    private final LayoutInflater mInflater; // LayoutInflater to inflate the user list items
    private final OnEditUserListener editUserListener; // Listener for edit actions
    private final OnDeleteUserListener deleteUserListener; // Listener for delete actions

    // Constructor for the UserAdapter.
    public UserAdapter(Context context, List<User> users, OnEditUserListener editUserListener, OnDeleteUserListener deleteUserListener) {
        mInflater = LayoutInflater.from(context);
        this.users = users;
        this.editUserListener = editUserListener;
        this.deleteUserListener = deleteUserListener;
    }

    // Interfaces for edit and delete actions
    public interface OnEditUserListener {
        void onEditUser(User user);
    }

    public interface OnDeleteUserListener {
        void onDeleteUser(User user);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for individual user list items.
        View itemView = mInflater.inflate(R.layout.user_management_item, parent, false); // Update to new layout file
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        if (users != null) {
            // Get the current User and set its details in the TextViews.
            User current = users.get(position);
            holder.firstNameView.setText(current.getFirstName());
            holder.lastNameView.setText(current.getLastName());
            holder.userEmailView.setText(current.getEmail());
            holder.userRoleView.setText(current.getRole());

            holder.editButton.setOnClickListener(view -> editUserListener.onEditUser(current));
            holder.deleteButton.setOnClickListener(view -> deleteUserListener.onDeleteUser(current));
        } else {
            // If there are no users, display a placeholder text.
            holder.firstNameView.setText("No user found");
            holder.lastNameView.setText("");
            holder.userEmailView.setText("");
            holder.userRoleView.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return (users != null) ? users.size() : 0;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        private final TextView firstNameView; // TextView to display the user first name.
        private final TextView lastNameView; // TextView to display the user last name.
        private final TextView userEmailView; // TextView to display the user email.
        private final TextView userRoleView; // TextView to display the user role.
        private final Button editButton; // Button to edit the user.
        private final Button deleteButton; // Button to delete the user.

        private UserViewHolder(View itemView) {
            super(itemView);
            firstNameView = itemView.findViewById(R.id.first_name_text_view);
            lastNameView = itemView.findViewById(R.id.last_name_text_view);
            userEmailView = itemView.findViewById(R.id.user_email_text_view);
            userRoleView = itemView.findViewById(R.id.user_role_text_view);
            editButton = itemView.findViewById(R.id.edit_user_button);
            deleteButton = itemView.findViewById(R.id.delete_user_button);
        }
    }
}