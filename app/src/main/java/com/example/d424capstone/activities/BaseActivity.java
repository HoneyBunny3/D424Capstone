package com.example.d424capstone.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.dialogs.LoginSignupDialogFragment;
import com.example.d424capstone.utilities.UserRoles;
import com.google.android.material.navigation.NavigationView;

/**
 * Base activity class that all other activities extend.
 * Provides common functionality such as drawer navigation, login check, and shared preferences listener.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private SparseArray<Class<?>> activityMap;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;
    private Repository repository;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new Repository(getApplication());
        initializeActivityMap();

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Initialize the SharedPreferences change listener
        listener = (sharedPreferences, key) -> {
            if ("UserRole".equals(key)) {
                recreate(); // or any other logic to refresh the UI
            }
        };

        // Register the shared preference change listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);

        // Check login status and show dialog if necessary
        checkLoginStatus();
    }

    /**
     * Checks the login status of the user and shows the login/signup dialog if not logged in.
     */
    private void checkLoginStatus() {
        boolean userLoggedIn = isUserLoggedIn();
        boolean skipDialog = shouldSkipLoginSignupDialog();

        Log.d(TAG, "User logged in: " + userLoggedIn);
        Log.d(TAG, "Should skip dialog: " + skipDialog);

        if (!userLoggedIn && !skipDialog) {
            showLoginSignupDialog();
        }
    }

    /**
     * Initializes the navigation drawer and sets up the drawer toggle.
     */
    protected void initializeDrawer() {
        drawerLayout = findViewById(R.id.main);
        NavigationView navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Enable the home button for opening the drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.logout) {
                handleLogout();
                return true;
            }
            Intent intent = null;
            Class<?> targetActivity = activityMap.get(id);
            if (targetActivity != null) {
                startActivity(new Intent(BaseActivity.this, targetActivity));
            }
            drawerLayout.closeDrawers();
            return true;
        });

        // Set window insets for EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Initializes the activity map with the navigation drawer item IDs and their corresponding activities.
     */
    private void initializeActivityMap() {
        activityMap = new SparseArray<>();
        activityMap.put(R.id.home, HomeScreen.class);
        activityMap.put(R.id.account_login, UserLoginScreen.class);
        activityMap.put(R.id.account_signup, UserSignUpScreen.class);
        activityMap.put(R.id.profile, UserProfileScreen.class);
        activityMap.put(R.id.premium_signup, PremiumSignUpScreen.class);
        activityMap.put(R.id.premium_user, PremiumSubscriptionManagementScreen.class);
        activityMap.put(R.id.cat_social, CatSocialScreen.class);
        activityMap.put(R.id.shopping, ShoppingScreen.class);
        activityMap.put(R.id.contact_us, ContactUsScreen.class);
        activityMap.put(R.id.admin_user, AdminUserManagementScreen.class);
        activityMap.put(R.id.admin_store, AdminStoreManagementScreen.class);
    }

    /**
     * Handles user logout by clearing shared preferences and redirecting to the home screen.
     */
    private void handleLogout() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Log.d(TAG, "User logged out.");
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, HomeScreen.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    /**
     * Checks if a user is logged in by verifying the presence of "LoggedInUserID" in shared preferences.
     *
     * @return true if a user is logged in, false otherwise.
     */
    protected boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean loggedIn = sharedPreferences.contains("LoggedInUserID");
        Log.d(TAG, "isUserLoggedIn: " + loggedIn);
        return loggedIn;
    }

    /**
     * Shows the login/signup dialog.
     */
    private void showLoginSignupDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        LoginSignupDialogFragment loginSignupDialog = LoginSignupDialogFragment.newInstance(false);
        loginSignupDialog.show(fragmentManager, "LoginSignupDialogFragment");
    }

    /**
     * Checks if the user has access to a specific role.
     *
     * @param requiredRole The required role.
     * @return true if the user has access, false otherwise.
     */
    public boolean hasAccess(String requiredRole) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userRole = sharedPreferences.getString("UserRole", UserRoles.GUEST);
        return userRole.equals(requiredRole);
    }

    /**
     * Checks if the user has access to a specific role and finishes the activity if access is denied.
     *
     * @param requiredRole The required role.
     */
    public void checkAccessOrFinish(String requiredRole) {
        if (!hasAccess(requiredRole)) {
            Toast.makeText(this, "Access Denied", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * Checks if the login/signup dialog should be skipped for the current activity.
     *
     * @return true if the dialog should be skipped, false otherwise.
     */
    private boolean shouldSkipLoginSignupDialog() {
        boolean skipDialog = this instanceof UserLoginScreen || this instanceof UserSignUpScreen;
        Log.d(TAG, "shouldSkipLoginSignupDialog: " + skipDialog);
        return skipDialog;
    }
}