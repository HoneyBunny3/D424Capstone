package com.example.d424capstone.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
        repository = MyApplication.getInstance().getRepository();
        initializeActivityMap();

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        listener = (sharedPreferences, key) -> {
            if ("UserRole".equals(key)) {
                recreate();
            }
        };

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);

        checkLoginStatus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search query submission here
                Intent intent = new Intent(BaseActivity.this, SearchResultsActivity.class);
                intent.putExtra("QUERY", query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle search query text change here if needed
                return false;
            }
        });

        return true;
    }

    private void checkLoginStatus() {
        boolean userLoggedIn = isUserLoggedIn();
        boolean skipDialog = shouldSkipLoginSignupDialog();

        if (!userLoggedIn && !skipDialog) {
            showLoginSignupDialog();
        }
    }

    protected void initializeDrawer() {
        drawerLayout = findViewById(R.id.main);
        NavigationView navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    private void initializeActivityMap() {
        activityMap = new SparseArray<>();
        activityMap.put(R.id.cat_home, HomeScreen.class);
        activityMap.put(R.id.account_login, UserLoginScreen.class);
        activityMap.put(R.id.account_signup, UserSignUpScreen.class);
        activityMap.put(R.id.user_profile, UserProfileScreen.class);
        activityMap.put(R.id.premium_signup, PremiumSignUpScreen.class);
        activityMap.put(R.id.premium_user, PremiumSubscriptionManagementScreen.class);
        activityMap.put(R.id.cat_social, CatSocialScreen.class);
        activityMap.put(R.id.cat_shopping, ShoppingScreen.class);
        activityMap.put(R.id.contact_us, ContactUsScreen.class);
    }

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

    protected boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return sharedPreferences.contains("LoggedInUserID");
    }

    private void showLoginSignupDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        LoginSignupDialogFragment loginSignupDialog = LoginSignupDialogFragment.newInstance(false);
        loginSignupDialog.show(fragmentManager, "LoginSignupDialogFragment");
    }

    public boolean hasAccess(String requiredRole) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userRole = sharedPreferences.getString("UserRole", UserRoles.GUEST);
        return userRole.equals(requiredRole);
    }

    public void checkAccessOrFinish(String requiredRole) {
        if (!hasAccess(requiredRole)) {
            finish();
        }
    }

    private boolean shouldSkipLoginSignupDialog() {
        boolean skipDialog = this instanceof UserLoginScreen || this instanceof UserProfileScreen;
        return skipDialog;
    }
}