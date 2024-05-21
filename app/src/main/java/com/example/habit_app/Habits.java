package com.example.habit_app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;
import androidx.appcompat.app.AlertDialog;

public class Habits extends AppCompatActivity {

    private ProgressBar progressBar;
    private ProgressBar progressBarXP;
    private TextView progressText;

    private TextView xpProgressText;

    private int currentHp;  // Current HP value
    private int maximumHp;  // Maximum HP value

    private int currentXp = 0;  // Current XP value
    private int maximumXp = 100;  // Maximum XP value

    private TextView levelText;      // Current level
    private TextView coinsText;
    private int level = 1;           // Current level value

    private int coins = 0;           // Current coins value

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habits);

        // Example habit names, these can be dynamically fetched or set by the user
        List<String> habitList = Arrays.asList("Drink Water", "Exercise", "Read a Book");
        LinearLayout habitContainer = findViewById(R.id.habitContainer);

        // Create habit cards
        createHabbit(habitList,habitContainer);

        // Get references to the progress bar and text view
        progressBar = findViewById(R.id.progressBar);
        progressBarXP = findViewById(R.id.progressBar2);
        progressText = findViewById(R.id.hp);
        xpProgressText = findViewById(R.id.xp);
        levelText = findViewById(R.id.user_level);
        coinsText = findViewById(R.id.coinstext);

        // Initialize HP values
        currentHp = 100;
        maximumHp = 100;

        // Initialize XP values
        currentXp = 0;
        maximumXp = (level-1)*12 +100;

        updateProgress(progressBar, progressText, currentHp, maximumHp);
        updateProgress(progressBarXP, xpProgressText, currentXp, maximumXp);
        setLevelText(level);
        setCoinsText(coins);


    }

    private void createHabbit(List habitList, LinearLayout habitContainer) {
        for (int i = 0; i < habitList.size(); i++) {
            View habitCard = getLayoutInflater().inflate(R.layout.habit, null);

            // Set dynamic margin
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 25); // Add margin bottom
            habitCard.setLayoutParams(params);


            // Find views within the habit card layout
            Button buttonPlus = habitCard.findViewById(R.id.buttonPlus);
            Button buttonMinus = habitCard.findViewById(R.id.buttonMinus);
            TextView habitName = habitCard.findViewById(R.id.habitName);
            TextView clickCounter = habitCard.findViewById(R.id.clickCounter);
            habitName.setText((CharSequence) habitList.get(i)); // Set the habit name dynamically


            // Initialize click counter
            int[] clickCount = {0}; // Initialize an array to store the click count

            habitCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Prompt user to delete habit
                    // open a dialog box to confirm deletion
                    new AlertDialog.Builder(Habits.this)
                            .setTitle("Delete Habit")
                            .setMessage("Are you sure you want to delete this habit?")
                            .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                // Remove habit card
                                habitContainer.removeView(habitCard);
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();

                }
            });
            buttonPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Call the increaseXp function with the desired increase amount
                    clickCount[0]++;
                    clickCounter.setText(String.valueOf(clickCount[0]));
                    increaseXp(10+(level-1)); // You can adjust the increase amount as needed
                }
            });

            buttonMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Call the decreaseXp function with the desired decrease amount
                    if (clickCount[0] > 0)
                        clickCount[0]--;
                    clickCounter.setText(String.valueOf(clickCount[0]));
                    decreaseXp(10+(level-1)); // You can adjust the decrease amount as needed
                }
            });

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
        // Calculate the progress percentage
        int progress = (int) ((float) currentValue / maximumValue * 100);

        // Update the progress bar
        pBar.setProgress(progress);

        // Update the progress text
        prText.setText(currentValue + "/" + maximumValue);
    }

    // Method to increase HP
    public void restoreHp() {
        
        currentHp = maximumHp;
        // Update the progress
        updateProgress(progressBar, progressText, currentHp, maximumHp);
    }

    // Method to increase XP
    public void increaseXp(int increaseAmount) {
        // Increase the current XP by the increase amount
        currentXp += increaseAmount;

        // Ensure the current XP does not exceed the maximum XP
        if (currentXp >= maximumXp) {
            increaseLevel(1);
        }

        // Update the progress
        updateProgress(progressBarXP, xpProgressText, currentXp, maximumXp);
    }

    // Method to increase level
    public void increaseLevel(int increaseAmount) {
        // Increase the current level by the increase amount
        level += increaseAmount;
        currentXp = 0;
        maximumXp = 100 + (level - 1)*25;
        maximumHp += 10 + (level - 1) * 1.5;
        coins += level*3 + 10;


        restoreHp();
        // Update the coins text
        setCoinsText(coins);
        // Update the level text
        setLevelText(level);
        // Update the progress
        updateProgress(progressBarXP, xpProgressText, currentXp, maximumXp);
    }

    // Method to decrease HP
    public void decreaseHp(int decreaseAmount) {
        // Decrease the current HP by the decrease amount
        if (currentHp - decreaseAmount >= 0)
            currentHp -= decreaseAmount;

        // Ensure the current HP does not go below 0
        else {
            decreaseCoins(5);
            currentHp = 0;
        }

        // Update the progress
        updateProgress(progressBar, progressText, currentHp, maximumHp);
    }

    public void decreaseXp(int decreaseAmount) {

        if (currentXp - decreaseAmount >= 0)
            currentXp -= decreaseAmount;

        else {
            currentXp = 0;
            decreaseHp(10 + (level - 1) * 2);
            updateProgress(progressBar, progressText, currentHp, maximumHp);
        }
        updateProgress(progressBarXP, xpProgressText, currentXp, maximumXp);
    }



    // Method to decrease coins
    public void decreaseCoins(int decreaseAmount) {
        // Decrease the current coins by the decrease amount
        if (coins  >= decreaseAmount) {
            coins -= decreaseAmount;
        }
        else {
            coins = 0;
        }
        setCoinsText(coins);
    }



    public void exitApp(View view) {
        finish(); // Finish the activity, thus exiting the app
    }



}
