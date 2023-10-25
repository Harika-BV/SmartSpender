package com.harika.smartspender;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        // Set up the ActionBarDrawerToggle
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // Enable the "up" button in the ActionBar for drawer open/close
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Handle the ActionBarDrawerToggle when the user clicks the ActionBar home button
        actionBarDrawerToggle.setToolbarNavigationClickListener(view -> {
            if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // Set the NavigationItemSelectedListener
        navigationView.setNavigationItemSelectedListener(item -> {
            // Handle item selection in the navigation drawer
            selectNavigationItem(item);
            return true;
        });

        // Load the HomeFragment initially
        loadFragment(new HomeFragment());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle ActionBar clicks, e.g., opening the drawer when the ActionBar home button is clicked
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectNavigationItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                loadFragment(new HomeFragment());
                break;
            case R.id.menu_categories:
                loadFragment(new CategoriesFragment());
                break;
            case R.id.menu_analytics:
                loadFragment(new AnalyticsFragment());
                break;
//            case R.id.menu_settings:
//                loadFragment(new SettingsFragment());
//                break;
            case R.id.menu_about:
                loadFragment(new AboutUsFragment());
                break;
            case R.id.menu_logout:
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
