package com.example.habit_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private ImageView avatarImageView;
    private TextView nicknameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if it's the first run
        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean isFirstRun = preferences.getBoolean("isFirstRun", true);

        if (isFirstRun) {
            // If it's the first time, launch the AvatarActivity
            Intent intent = new Intent(this, AvatarActivity.class);
            startActivity(intent);
            finish(); // Close the MainActivity
            return; // Exit the onCreate method early to prevent further execution
        }

        setContentView(R.layout.activity_main);

        // Set up the navigation controller and configuration
        setupNavigation();

        // Optionally, use the avatar and nickname in this activity
        String nickname = preferences.getString("NICKNAME", "User");
        int avatarId = preferences.getInt("AVATAR_ID", -1);

        // Initialize the UI elements
        avatarImageView = findViewById(R.id.profile_picture); // Your ImageView for the avatar
        nicknameTextView = findViewById(R.id.user_name); // Your TextView for the nickname

        // Set the avatar and nickname in the UI
        if (avatarId != -1) {
            avatarImageView.setImageResource(avatarId);
        }
        nicknameTextView.setText(nickname);
    }

    private void setupNavigation() {
        // Get the NavHostFragment and NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navHostFragment);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        } else {
            throw new IllegalStateException("NavHostFragment not found.");
        }

        // Set up the BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Create an AppBarConfiguration with the IDs of the fragments you want to consider as top-level destinations
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.profileFragment // Replace with your actual fragment IDs
        ).build();

        // Link the NavController with the BottomNavigationView
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Handle the up button in the action bar
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}
