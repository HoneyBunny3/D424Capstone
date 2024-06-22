package com.example.d424capstone.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.d424capstone.R;
import com.example.d424capstone.dialogs.LoginSignupDialogFragment;
import com.example.d424capstone.utilities.UserRoles;

public abstract class BaseActivity extends AppCompatActivity {

    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals("UserRole")) {
                    // Handle role change
                    recreate(); // or any other logic to refresh the UI
                }
            }
        };

        // Check if the user is a guest and show the login/signup dialog
        if (isGuestUser()) {
            showLoginSignupDialog();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    private boolean isGuestUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userRole = sharedPreferences.getString("UserRole", UserRoles.GUEST);
        return userRole.equals(UserRoles.GUEST);
    }

    private void showLoginSignupDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        LoginSignupDialogFragment dialog = new LoginSignupDialogFragment();
        dialog.show(fragmentManager, "LoginSignupDialog");
    }

    public boolean hasAccess(String requiredRole) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userRole = sharedPreferences.getString("UserRole", UserRoles.GUEST);
        return userRole.equals(requiredRole);
    }

    public void checkAccessAndRedirect(String requiredRole, Class<?> targetActivity) {
        if (hasAccess(requiredRole)) {
            Intent intent = new Intent(this, targetActivity);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Access Denied", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkAccessOrFinish(String requiredRole) {
        if (!hasAccess(requiredRole)) {
            Toast.makeText(this, "Access Denied", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}