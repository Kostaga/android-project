package com.example.habit_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class InventoryActivity extends AppCompatActivity {

    private ImageView avatarImageView;
    private TextView nicknameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        // Initialize the UI elements
        avatarImageView = findViewById(R.id.profile_picture); // ImageView in your inventory layout
        nicknameTextView = findViewById(R.id.user_name); // TextView in your inventory layout

        // Get the avatar and nickname from the Intent
        Intent intent = getIntent();
        String nickname = intent.getStringExtra("NICKNAME");
        int avatarId = intent.getIntExtra("AVATAR_ID", -1);

        // Set the avatar and nickname in the UI
        if (avatarId != -1) {
            avatarImageView.setImageResource(avatarId);
        }
        nicknameTextView.setText(nickname);

        // Set up the BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.inventory);

        // Handle BottomNavigationView item selection
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
                homeIntent.putExtra("NICKNAME", nickname);
                homeIntent.putExtra("AVATAR_ID", avatarId);
                startActivity(homeIntent);
                finish();
                return true;
            } else if (item.getItemId() == R.id.inventory) {
                // We're already in InventoryActivity, no need to do anything
                return true;
            }
            // Handle other items if needed
            return false;
        });
    }
}
