package com.example.d424capstone.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.d424capstone.activities.UserLoginScreen;
import com.example.d424capstone.activities.UserSignUpScreen;

public class LoginSignupDialogFragment extends DialogFragment {

    private static final String ARG_SKIP_DIALOG = "skip_dialog";

    public static LoginSignupDialogFragment newInstance(boolean skipDialog) {
        LoginSignupDialogFragment fragment = new LoginSignupDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_SKIP_DIALOG, skipDialog);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Check if the dialog should be skipped
        if (getArguments() != null && getArguments().getBoolean(ARG_SKIP_DIALOG, false)) {
            dismiss();
            return super.onCreateDialog(savedInstanceState);
        }

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
                        // Handle sign-up action
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

    private void onLogin() {
        // Navigate to the login screen
        Intent loginIntent = new Intent(requireActivity(), UserLoginScreen.class);
        startActivity(loginIntent);
    }

    private void onSignUp() {
        // Navigate to the sign-up screen
        Intent signUpIntent = new Intent(requireActivity(), UserSignUpScreen.class);
        startActivity(signUpIntent);
    }

    private void onCancel() {
        // Dismiss the dialog
        dismiss();
    }
}