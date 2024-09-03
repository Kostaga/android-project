package com.example.habit_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

    // TextViews for level, xp, health, and coins
    private TextView levelTextView;
    private TextView xpTextView;
    private TextView healthTextView;
    private TextView coinsTextView;

    // ProgressBars for XP and Health
    private ProgressBar xpProgressBar;
    private ProgressBar healthProgressBar;

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

        // Initialize level, xp, health, and coins TextViews
        levelTextView = findViewById(R.id.user_level);
        xpTextView = findViewById(R.id.xp);
        healthTextView = findViewById(R.id.hp);
        coinsTextView = findViewById(R.id.coinstext);

        // Initialize ProgressBars
        xpProgressBar = findViewById(R.id.progressBar2);
        healthProgressBar = findViewById(R.id.progressBar);

        // Retrieve and display user data
        int level = preferences.getInt("LEVEL", 1);
        int xp = preferences.getInt("XP", 0);
        int health = preferences.getInt("HEALTH", 100);
        int coins = preferences.getInt("COINS", 0);

        // Assume max XP and Health values
        int maxXP = 100;
        int maxHealth = 100;

        // Set the values in the UI
        levelTextView.setText(getString(R.string.level, level));
        xpTextView.setText(getString(R.string.xp_format, maxXP));
        healthTextView.setText(getString(R.string.hp_format, maxHealth));
        coinsTextView.setText(String.valueOf(coins));

        // Set progress bar values
        xpProgressBar.setMax(maxXP);
        xpProgressBar.setProgress(xp);

        healthProgressBar.setMax(maxHealth);
        healthProgressBar.setProgress(health);

        // Handle BottomNavigationView item selection
        setupBottomNavigation();
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
    }

    private void setupBottomNavigation() {
        // Set up the BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.home);

        // Link the NavController with the BottomNavigationView
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // Handle BottomNavigationView item selection
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            // Fetch the avatar and nickname from SharedPreferences
            SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String nickname = preferences.getString("NICKNAME", "User");
            int avatarId = preferences.getInt("AVATAR_ID", -1);

            if (itemId == R.id.home) {
                return true;
            } else if (itemId == R.id.inventory) {
                // Launch InventoryActivity with avatar and nickname
                Intent intent = new Intent(getApplicationContext(), InventoryActivity.class);
                intent.putExtra("NICKNAME", nickname);
                intent.putExtra("AVATAR_ID", avatarId);
                startActivity(intent);
                finish();
                return true;
            }
            // Handle other items if needed
            return false;
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Handle the up button in the action bar
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}
