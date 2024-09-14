package com.example.habit_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.habit_app.data.database.HabitDatabase;
import com.example.habit_app.data.models.Character;
import com.example.habit_app.logic.dao.CharacterDao;
import com.example.habit_app.logic.repository.CharacterRepository;
import com.example.habit_app.logic.repository.HabitRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    private SQLiteDatabase database;


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
            finish();
            return;
        }



        setContentView(R.layout.activity_main);


        // Initialize the database helper
        // Database helper
        HabitDatabase dbHelper = new HabitDatabase(this);
        database = dbHelper.getReadableDatabase();


        // Set up the navigation controller and configuration
        setupNavigation();

        // Retrieve the avatar and nickname from SharedPreferences
        String nickname = preferences.getString("NICKNAME", "User");
        int avatarId = preferences.getInt("AVATAR_ID", -1);

        // Initialize the UI elements
        ImageView avatarImageView = findViewById(R.id.profile_picture);
        TextView nicknameTextView = findViewById(R.id.user_name);

        // Set the avatar and nickname in the UI
        if (avatarId != -1) {
            avatarImageView.setImageResource(avatarId);
        }
        nicknameTextView.setText(nickname);

        // Initialize level, xp, health, and coins TextViews
        // TextViews for level, xp, health, and coins
        TextView levelTextView = findViewById(R.id.user_level);
        TextView xpTextView = findViewById(R.id.xp);
        TextView healthTextView = findViewById(R.id.hp);
        TextView coinsTextView = findViewById(R.id.coinstext);

        // Initialize ProgressBars
        // ProgressBars for XP and Health
        ProgressBar xpProgressBar = findViewById(R.id.progressBar2);
        ProgressBar healthProgressBar = findViewById(R.id.progressBar);

        // Fetch character data from the database
        Character character = getCharacterFromDatabase();



        // If character data exists, set the values in the UI
        if (character != null) {
            levelTextView.setText(getString(R.string.level, character.getLevel()));
            xpTextView.setText(getString(R.string.xp_format, character.getXp(), character.getMaxXp()));
            healthTextView.setText(getString(R.string.hp_format, character.getHp(), character.getMaxHp()));
            coinsTextView.setText(String.valueOf(character.getCoins()));

            // Set progress bar values
            xpProgressBar.setMax(character.getMaxXp());
            xpProgressBar.setProgress(character.getXp());

        } else {
            // Handle the case where the character data is not found
            levelTextView.setText(getString(R.string.level, 1));
            xpTextView.setText(getString(R.string.xp_format, 0, character.getMaxXp()));
            healthTextView.setText(getString(R.string.hp_format, 100, character.getMaxHp()));
            coinsTextView.setText("0");

            xpProgressBar.setMax(character.getMaxXp());
            xpProgressBar.setProgress(0);

        }
        healthProgressBar.setMax(character.getMaxHp());
        healthProgressBar.setProgress(character.getHp());

        // Handle BottomNavigationView item selection
        setupBottomNavigation();
    }

    private Character getCharacterFromDatabase() {
        // Query the database for the first character
        Cursor cursor = database.query("character_table", null, null, null, null, null, null);
        Character character = null;
        if (cursor != null && cursor.moveToFirst()) {
            // If data is available, create a Character object from the cursor
            character = Character.fromCursor(cursor);
            cursor.close();
        }
        return character;
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
    protected void onDestroy() {
        super.onDestroy();
        if (database != null) {
            database.close();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Handle the up button in the action bar
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    public void refreshUI() {
            Character character = getCharacterFromDatabase();
            // Initialize UI elements
            TextView levelTextView = findViewById(R.id.user_level);
            TextView xpTextView = findViewById(R.id.xp);
            TextView healthTextView = findViewById(R.id.hp);
            TextView coinsTextView = findViewById(R.id.coinstext);
            ProgressBar xpProgressBar = findViewById(R.id.progressBar2);
            ProgressBar healthProgressBar = findViewById(R.id.progressBar);

            // Update UI elements with character data
            levelTextView.setText(getString(R.string.level, character.getLevel()));
            xpTextView.setText(getString(R.string.xp_format, character.getXp(), character.getMaxXp()));
            healthTextView.setText(getString(R.string.hp_format, character.getHp(), character.getMaxHp()));
            coinsTextView.setText(String.valueOf(character.getCoins()));

            // Update progress bars
            xpProgressBar.setMax(character.getMaxXp());
            xpProgressBar.setProgress(character.getXp());
            healthProgressBar.setMax(character.getMaxHp());
            healthProgressBar.setProgress(character.getHp());



    }



    // Method to refresh the activity
    public void refreshActivity() {
        finish(); // Finish the current activity
        startActivity(getIntent()); // Restart the activity
    }




}



