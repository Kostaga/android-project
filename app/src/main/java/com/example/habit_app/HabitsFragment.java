package com.example.habit_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.List;

public class HabitsFragment extends Fragment {

    private LinearLayout habitContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.habits, container, false);

        habitContainer = view.findViewById(R.id.habitContainer);

        // Example habit names, these can be dynamically fetched or set by the user
        List<String> habitList = Arrays.asList("Drink Water", "Exercise", "Read a Book");

        // Create habit cards
        createHabitCards(habitList);

        return view;
    }

    private void createHabitCards(List<String> habitList) {
        for (String habit : habitList) {
            View habitCard = getLayoutInflater().inflate(R.layout.habit, habitContainer, false);

            TextView habitNameTextView = habitCard.findViewById(R.id.habitName);
            habitNameTextView.setText(habit);

            habitContainer.addView(habitCard);
        }
    }
}
