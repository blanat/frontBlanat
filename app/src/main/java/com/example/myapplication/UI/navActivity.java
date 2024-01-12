package com.example.myapplication.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.HomeFragment;
import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class navActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        Toolbar toolbar = findViewById(R.id.toolbar); // Toolbar for the navigation drawer
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(menuItemClickListener);
            Log.e("NavActivity", "NavigationView is not null");
        } else {
            Log.e("NavActivity", "NavigationView is null");
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_settings);
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            // Check which item was clicked based on its ID
            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.add) {
                startActivity(new Intent(navActivity.this, CreateDActivity.class));
            } else if (itemId == R.id.profil) {
                startActivity(new Intent(navActivity.this, DiscussionScreen.class));
            } else {
                Log.w("Navmenu", "Unhandled item ID: " + itemId);
            }

            return true;
        });

    }

    //======================================

    private final NavigationView.OnNavigationItemSelectedListener menuItemClickListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int itemId = item.getItemId();
            Log.d("NavActivity", "Selected item ID: " + itemId);

            handleNavigationItemSelected(itemId);

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
    };

    // Add this method to handle clicks on BottomNavigationView items
    public void onBottomNavItemClicked(View view) {
        int itemId = view.getId();

        if (itemId == R.id.home) {
            replaceFragment(new HomeFragment());
        } else if (itemId == R.id.add) {
            startActivity(new Intent(navActivity.this, CreateDActivity.class));
        } else if (itemId == R.id.profil) {
            startActivity(new Intent(navActivity.this, DiscussionScreen.class));
        } else {
            Log.w("NavActivity", "Unhandled item ID: " + itemId);
        }
    }

    private void handleNavigationItemSelected(int itemId) {
        if (itemId == R.id.nav_settings) {
            Log.d("NavActivity", "Replacing fragment with SettingsFragment");
            replaceFragment(new SettingsFragment());
        } else if (itemId == R.id.nav_discussion) {
            Log.d("NavActivity", "Replacing fragment with DiscussionFragment");
            replaceFragment(new DiscussionFragment());
        } else if (itemId == R.id.nav_logout) {

            removeJwtTokenFromSharedPreferences();
            // Create a new task and clear the existing task (activity stack)
            Intent intent = new Intent(getBaseContext(), EntryPOINTactivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            // Finish the current activity
            finish();
            Log.d("NavActivity", "Logging out");
            Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show();
        } else {
            Log.w("NavActivity", "Unhandled item ID: " + itemId);
        }
    }

    private void replaceFragment(Fragment fragment) {
        Log.d("NavActivity", "Replacing fragment: " + fragment.getClass().getSimpleName());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void removeJwtTokenFromSharedPreferences() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("jwtToken");
        editor.apply();

    }
}
