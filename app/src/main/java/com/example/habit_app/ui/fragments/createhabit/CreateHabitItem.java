package com.example.habit_app.ui.fragments.createhabit;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.habit_app.R;
import com.example.habit_app.data.models.Habit;
import com.example.habit_app.ui.viewmodels.HabitViewModel;

public class CreateHabitItem extends Fragment {

    private EditText etHabitTitle;

    private HabitViewModel habitViewModel;

    public CreateHabitItem() {
        super(R.layout.fragment_create_habit_item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etHabitTitle = view.findViewById(R.id.et_habitTitle);

        habitViewModel = new ViewModelProvider(this).get(HabitViewModel.class);

        view.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHabitToDB();
            }
        });
    }

    private void addHabitToDB() {
        String title = etHabitTitle.getText().toString();

        // Validate input
        if (!title.isEmpty()) {
            // Create a new habit object with name and description only
            Habit habit = new Habit(title,0);

            // Insert habit into the database using ViewModel
            habitViewModel.addHabit(habit);

            Toast.makeText(getContext(), "Habit created successfully!", Toast.LENGTH_SHORT).show();

            // Navigate back to the habit list
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_createHabitItem_to_habitList);
        } else {
            Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
        }
    }
}
