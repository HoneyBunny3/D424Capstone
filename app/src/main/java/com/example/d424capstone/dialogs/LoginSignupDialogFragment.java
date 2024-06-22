package com.example.d424capstone.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

/**
 * A DialogFragment that prompts the user to log in or sign up to access more features.
 */
public class LoginSignupDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Create an AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Login or Sign Up")
                .setMessage("Please log in or sign up to access more features.")
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle login action
                        onLogin();
                    }
                })
                .setNegativeButton("Sign Up", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle sign up action
                        onSignUp();
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle cancel action
                        onCancel();
                    }
                })
                .setCancelable(false); // Makes the dialog non-cancelable by back button or outside touch
        return builder.create();
    }

    /**
     * Method to handle login action.
     */
    private void onLogin() {
        // Implement login logic here
    }

    /**
     * Method to handle sign-up action.
     */
    private void onSignUp() {
        // Implement sign-up logic here
    }

    /**
     * Method to handle cancel action.
     */
    private void onCancel() {
        // Implement cancel logic here
    }
}