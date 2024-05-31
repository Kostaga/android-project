package com.example.habit_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class AddFragment extends Fragment {

    private EditText editTextHabitName;

    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        editTextHabitName = view.findViewById(R.id.editTextHabitName);
        Button buttonAddHabit = view.findViewById(R.id.buttonAddHabit);

        buttonAddHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                addHabitToHabitsFragment(editTextHabitName.getText().toString());
//                editTextHabitName.setText(""); // Clear EditText after adding habit

                String habitName = editTextHabitName.getText().toString().trim();
                if (!habitName.isEmpty()) {
                    // Αποκτήστε πρόσβαση στο HabitsFragment
                    HabitsFragment habitsFragment = (HabitsFragment) getParentFragmentManager().findFragmentByTag("HabitsFragment");
                    if (habitsFragment != null) {
                        // Προσθέστε τη νέα συνήθεια
                        Toast.makeText(getActivity(), "Habit added successfully: " + habitName, Toast.LENGTH_SHORT).show();
                        habitsFragment.addHabit(habitName);
                    } else {
                        Toast.makeText(getActivity(), "Habit not added: Fragment not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void addHabitToHabitsFragment(String habitname) {
        HabitsFragment habitsFragment = (HabitsFragment) requireActivity().getSupportFragmentManager().findFragmentByTag("HabitsFragment");

        if (habitsFragment != null) {
            Toast.makeText(getActivity(), "Habit added successfully: " + habitname, Toast.LENGTH_SHORT).show();
            habitsFragment.addHabit(habitname);
        } else {
            Toast.makeText(getActivity(), "Habit not added: Fragment not found", Toast.LENGTH_SHORT).show();
        }
    }

}