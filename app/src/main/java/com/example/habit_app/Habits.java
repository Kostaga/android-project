package com.example.habit_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

public class Habits extends AppCompatActivity {

    private ProgressBar progressBar;
    private ProgressBar progressBarXP;
    private TextView progressText;
    private TextView xpProgressText;
    private int currentHp;
    private int maximumHp;
    private int currentXp = 0;
    private int maximumXp = 100;
    private TextView levelText;
    private TextView coinsText;
    private int level = 1;
    private int coins = 0;
    private TextView username;
    private ImageView userAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habits);

        username = findViewById(R.id.user_name);
        userAvatar = findViewById(R.id.profile_picture);

        // Retrieve data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String nickname = sharedPreferences.getString("NICKNAME", "User");
        int avatarId = sharedPreferences.getInt("AVATAR_ID", R.drawable.avatar1);


        if (username != null) {
            username.setText(nickname);
        } else {
            Log.e("HabitsActivity", "TextView username is null");
        }

        if (userAvatar != null) {
            userAvatar.setImageResource(avatarId);
        } else {
            Log.e("HabitsActivity", "ImageView userAvatar is null");
        }

        List<String> habitList = Arrays.asList("Drink Water", "Exercise", "Read a Book");
        LinearLayout habitContainer = findViewById(R.id.habitContainer);

        createHabit(habitList, habitContainer);

        progressBar = findViewById(R.id.progressBar);
        progressBarXP = findViewById(R.id.progressBar2);
        progressText = findViewById(R.id.hp);
        xpProgressText = findViewById(R.id.xp);
        levelText = findViewById(R.id.user_level);
        coinsText = findViewById(R.id.coinstext);

        currentHp = 100;
        maximumHp = 100;

        currentXp = 0;
        maximumXp = (level - 1) * 12 + 100;

        updateProgress(progressBar, progressText, currentHp, maximumHp);
        updateProgress(progressBarXP, xpProgressText, currentXp, maximumXp);
        setLevelText(level);
        setCoinsText(coins);
    }

    private void createHabit(List<String> habitList, LinearLayout habitContainer) {
        for (String habit : habitList) {
            View habitCard = getLayoutInflater().inflate(R.layout.habit, null);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 25);
            habitCard.setLayoutParams(params);

            TextView habitName = habitCard.findViewById(R.id.habitName);
            habitName.setText(habit);

            habitContainer.addView(habitCard);
        }
    }

    private void setLevelText(int level) {
        String levelString = getString(R.string.Level, level);
        levelText.setText(levelString);
    }

    private void setCoinsText(int coins) {
        String coinString = getString(R.string.currentCoins, coins);
        coinsText.setText(coinString);
    }

    private void updateProgress(ProgressBar pBar, TextView prText, int currentValue, int maximumValue) {
        int progress = (int) ((float) currentValue / maximumValue * 100);
        pBar.setProgress(progress);
        prText.setText(currentValue + "/" + maximumValue);
    }

    public void restoreHp() {
        currentHp = maximumHp;
        updateProgress(progressBar, progressText, currentHp, maximumHp);
    }

    public void increaseXp(int increaseAmount) {
        currentXp += increaseAmount;

        if (currentXp >= maximumXp) {
            increaseLevel(1);
        }

        updateProgress(progressBarXP, xpProgressText, currentXp, maximumXp);
    }

    public void increaseLevel(int increaseAmount) {
        level += increaseAmount;
        currentXp = 0;
        maximumXp = 100 + (level - 1) * 25;
        maximumHp += 10 + (level - 1) * 1.5;
        coins += level * 3 + 10;

        restoreHp();
        setCoinsText(coins);
        setLevelText(level);
        updateProgress(progressBarXP, xpProgressText, currentXp, maximumXp);
    }

    public void decreaseHp(int decreaseAmount) {
        if (currentHp - decreaseAmount >= 0) {
            currentHp -= decreaseAmount;
        } else {
            decreaseCoins(5);
            currentHp = 0;
        }

        updateProgress(progressBar, progressText, currentHp, maximumHp);
    }

    public void decreaseXp(int decreaseAmount) {
        if (currentXp - decreaseAmount >= 0) {
            currentXp -= decreaseAmount;
        } else {
            currentXp = 0;
            decreaseHp(10 + (level - 1) * 2);
            updateProgress(progressBar, progressText, currentHp, maximumHp);
        }
        updateProgress(progressBarXP, xpProgressText, currentXp, maximumXp);
    }

    public void decreaseCoins(int decreaseAmount) {
        if (coins >= decreaseAmount) {
            coins -= decreaseAmount;
        } else {
            coins = 0;
        }
        setCoinsText(coins);
    }

    public void exitApp(View view) {
        finish();
    }
}
