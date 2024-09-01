package com.example.habit_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AvatarActivity extends AppCompatActivity {

    private ImageView avatar1, avatar2, avatar3, avatar4;
    private EditText nicknameInput;
    private int selectedAvatarId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_avatar);

        // Initialize views
        avatar1 = findViewById(R.id.avatar1);
        avatar2 = findViewById(R.id.avatar2);
        avatar3 = findViewById(R.id.avatar3);
        avatar4 = findViewById(R.id.avatar4);
        nicknameInput = findViewById(R.id.nicknameInput);

        // Set up avatar selection listeners
        avatar1.setOnClickListener(v -> selectAvatar(R.drawable.avatar1, avatar1));
        avatar2.setOnClickListener(v -> selectAvatar(R.drawable.avatar2, avatar2));
        avatar3.setOnClickListener(v -> selectAvatar(R.drawable.avatar3, avatar3));
        avatar4.setOnClickListener(v -> selectAvatar(R.drawable.avatar4, avatar4));

        // Set up confirm button listener
        findViewById(R.id.confirmButton).setOnClickListener(v -> confirmSelection());
    }

    private void selectAvatar(int avatarId, ImageView selectedAvatar) {
        selectedAvatarId = avatarId;

        // Reset all avatars' borders to indicate selection
        avatar1.setBackgroundResource(0);
        avatar2.setBackgroundResource(0);
        avatar3.setBackgroundResource(0);
        avatar4.setBackgroundResource(0);

        // Highlight the selected avatar
        selectedAvatar.setBackgroundResource(R.drawable.avatar_border);
    }

    private void confirmSelection() {
        String nickname = nicknameInput.getText().toString().trim();

        // Validate avatar selection and nickname input
        if (selectedAvatarId == -1 || nickname.isEmpty()) {
            Toast.makeText(this, "Please select an avatar and enter a nickname", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save the avatar ID and nickname in SharedPreferences
        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("AVATAR_ID", selectedAvatarId);
        editor.putString("NICKNAME", nickname);
        editor.putBoolean("isFirstRun", false);  // Mark that the app is no longer in the first run
        editor.apply();

        // Start MainActivity and pass avatar ID and nickname
        Intent intent = new Intent(AvatarActivity.this, MainActivity.class);
        intent.putExtra("NICKNAME", nickname);
        intent.putExtra("AVATAR_ID", selectedAvatarId);
        startActivity(intent);
        finish();
    }
}
