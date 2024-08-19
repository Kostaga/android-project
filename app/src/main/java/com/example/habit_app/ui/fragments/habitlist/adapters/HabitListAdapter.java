package com.example.habit_app.ui.fragments.habitlist.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habit_app.R;
import com.example.habit_app.data.models.Habit;

import java.util.ArrayList;
import java.util.List;

public class HabitListAdapter extends RecyclerView.Adapter<HabitListAdapter.MyViewHolder> {

    private List<Habit> habitsList = new ArrayList<>();

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            // No click listener for updating habit
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_habit_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Habit currentHabit = habitsList.get(position);

        // Set habit title and description
        TextView titleTextView = holder.itemView.findViewById(R.id.habitName);
        titleTextView.setText(currentHabit.getHabitTitle());
    }

    @Override
    public int getItemCount() {
        return habitsList.size();
    }

    public void setData(List<Habit> habits) {
        this.habitsList = habits;
        notifyDataSetChanged();
    }
}
