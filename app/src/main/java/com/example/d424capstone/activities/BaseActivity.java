package com.example.d424capstone.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.example.d424capstone.MyApplication;
import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.dialogs.LoginSignupDialogFragment;
import com.example.d424capstone.utilities.UserRoles;
import com.google.android.material.navigation.NavigationView;

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
        EdgeToEdge.enable(this);

        repository = MyApplication.getInstance().getRepository(); // Initialize repository instance
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE); // Initialize SharedPreferences

        initializeActivityMap(); // Initialize the activity map for navigation

        // Listener for SharedPreferences changes
        listener = (sharedPreferences, key) -> {
            if ("UserRole".equals(key)) {
                recreate();
            }
        };

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);

        checkLoginStatus(); // Check the login status of the user
    }

    // Determine if the search feature should be shown
    protected boolean shouldShowSearch() {
        return true; // By default, show the search feature
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (shouldShowSearch()) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_search, menu);

            MenuItem searchItem = menu.findItem(R.id.action_search);
            SearchView searchView = (SearchView) searchItem.getActionView();

            // Set query text listener for the search view
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Intent intent = new Intent(BaseActivity.this, SearchResultsActivity.class);
                    intent.putExtra("QUERY", query);
                    startActivity(intent);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
        return true;
    }

    // Check if the user is logged in and whether to show the login/signup dialog
    private void checkLoginStatus() {
        boolean userLoggedIn = isUserLoggedIn();
        boolean skipDialog = shouldSkipLoginSignupDialog();

        if (!userLoggedIn && !skipDialog) {
            showLoginSignupDialog();
        }
    }

    // Initialize the navigation drawer
    protected void initializeDrawer() {
        drawerLayout = findViewById(R.id.main);
        NavigationView navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable the home button in the action bar

        // Set navigation item selected listener
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.user_logout) {
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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Initialize the activity map with the available activities
    private void initializeActivityMap() {
        activityMap = new SparseArray<>();
        activityMap.put(R.id.cat_home, HomeScreen.class);
        activityMap.put(R.id.account_login, UserLoginScreen.class);
        activityMap.put(R.id.account_signup, UserSignUpScreen.class);
        activityMap.put(R.id.user_profile, UserProfileScreen.class);
        activityMap.put(R.id.cat_social, CatSocialScreen.class);
        activityMap.put(R.id.cat_shopping, ShoppingScreen.class);
        activityMap.put(R.id.love_your_cat, LoveYourCatScreen.class);
        activityMap.put(R.id.contact_us, ContactUsScreen.class);
        activityMap.put(R.id.premium_signup, PremiumSignUpScreen.class);
        activityMap.put(R.id.premium_user, PremiumSubscriptionManagementScreen.class);
    }

    // Handle user logout
    private void handleLogout() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
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
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    // Check if the user is logged in
    protected boolean isUserLoggedIn() {
        return sharedPreferences.contains("LoggedInUserID");
    }

    // Determine if the login/signup dialog should be skipped
    private boolean shouldSkipLoginSignupDialog() {
        boolean skipDialog = this instanceof UserLoginScreen || this instanceof UserSignUpScreen;
        return skipDialog;
    }

    // Show the login/signup dialog
    private void showLoginSignupDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        LoginSignupDialogFragment loginSignupDialog = LoginSignupDialogFragment.newInstance(false);
        loginSignupDialog.show(fragmentManager, "LoginSignupDialogFragment");
    }

    // Check if the user has access based on their role
    public boolean hasAccess(String requiredRole) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userRole = sharedPreferences.getString("UserRole", UserRoles.GUEST);
        return userRole.equals(requiredRole);
    }
}