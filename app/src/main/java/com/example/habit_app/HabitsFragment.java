package com.example.habit_app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.List;

public class HabitsFragment extends Fragment {

    private ProgressBar progressBar;
    private ProgressBar progressBarXP;
    private TextView progressText;
    private TextView xpProgressText;
    private TextView levelText;
    private TextView coinsText;

    private LinearLayout habitContainer;
    private int currentHp;  // Current HP value
    private int maximumHp;  // Maximum HP value
    private int currentXp = 0;  // Current XP value
    private int maximumXp = 100;  // Maximum XP value
    private int level = 1;           // Current level value
    private int coins = 0;           // Current coins value

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.habits, container, false);

        habitContainer = view.findViewById(R.id.habitContainer);

        // Example habit names, these can be dynamically fetched or set by the user
        List<String> habitList = Arrays.asList("Drink Water", "Exercise", "Read a Book");

        // Initialize views
        progressBar = view.findViewById(R.id.progressBar);
        progressBarXP = view.findViewById(R.id.progressBar2);
        progressText = view.findViewById(R.id.hp);
        xpProgressText = view.findViewById(R.id.xp);
        levelText = view.findViewById(R.id.user_level);
        coinsText = view.findViewById(R.id.coinstext);

        // Initialize HP values
        currentHp = 100;
        maximumHp = 100;

        // Initialize XP values
        currentXp = 0;
        maximumXp = (level - 1) * 12 + 100;

        // Initialize UI
        updateProgress(progressBar, progressText, currentHp, maximumHp);
        updateProgress(progressBarXP, xpProgressText, currentXp, maximumXp);
        setLevelText(level);
        setCoinsText(coins);

        // Create habit cards
        createHabitCards(habitList);

        return view;
    }

    private void createHabitCards(List<String> habitList) {
        for (String habit : habitList) {
            View habitCard = getLayoutInflater().inflate(R.layout.habit, habitContainer, false);

            TextView habitNameTextView = habitCard.findViewById(R.id.habitName);
            habitNameTextView.setText(habit);

            // Find views within the habit card layout
            Button buttonPlus = habitCard.findViewById(R.id.buttonPlus);
            Button buttonMinus = habitCard.findViewById(R.id.buttonMinus);
            TextView clickCounter = habitCard.findViewById(R.id.clickCounter);

            // Initialize click counter
            int[] clickCount = {0}; // Initialize an array to store the click count

            habitCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Prompt user to delete habit
                    // open a dialog box to confirm deletion
                    new AlertDialog.Builder(requireContext())
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
                    increaseXp(10 + (level - 1)); // You can adjust the increase amount as needed
                }
            });

            buttonMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Call the decreaseXp function with the desired decrease amount
                    if (clickCount[0] > 0)
                        clickCount[0]--;
                    clickCounter.setText(String.valueOf(clickCount[0]));
                    decreaseXp(10 + (level - 1)); // You can adjust the decrease amount as needed
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

    // Method to increase XP
    private void increaseXp(int increaseAmount) {
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
    private void increaseLevel(int increaseAmount) {
        // Increase the current level by the increase amount
        level += increaseAmount;
        currentXp = 0;
        maximumXp = 100 + (level - 1) * 25;
        maximumHp += 10 + (level - 1) * 1.5;
        coins += level * 3 + 10;

        restoreHp();
        // Update the coins text
        setCoinsText(coins);
        // Update the level text
        setLevelText(level);
        // Update the progress
        updateProgress(progressBarXP, xpProgressText, currentXp, maximumXp);
    }

    // Method to decrease HP
    private void decreaseHp(int decreaseAmount) {
        // Decrease the current HP by the decrease amount
        if (currentHp - decreaseAmount >= 0)
            currentHp -= decreaseAmount;
        else {
            decreaseCoins(5);
            currentHp = 0;
        }

        // Update the progress
        updateProgress(progressBar, progressText, currentHp, maximumHp);
    }

    // Method to decrease XP
    private void decreaseXp(int decreaseAmount) {
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
    private void decreaseCoins(int decreaseAmount) {
        // Decrease the current coins by the decrease amount
        if (coins >= decreaseAmount) {
            coins -= decreaseAmount;
        } else {
            coins = 0;
        }
        setCoinsText(coins);
    }

    // Method to restore HP
    private void restoreHp() {
        currentHp = maximumHp;
        // Update the progress
        updateProgress(progressBar, progressText, currentHp, maximumHp);
    }
}
