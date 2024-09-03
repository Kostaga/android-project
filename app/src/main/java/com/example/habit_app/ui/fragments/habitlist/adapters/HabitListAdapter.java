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

// Inside HabitListAdapter
public class HabitListAdapter extends RecyclerView.Adapter<HabitListAdapter.MyViewHolder> {

    private List<Habit> habitsList = new ArrayList<>();
    private OnHabitClickListener onHabitClickListener;

    public interface OnHabitClickListener {
        void onHabitClick(Habit habit);
    }

    public void setOnHabitClickListener(OnHabitClickListener listener) {
        this.onHabitClickListener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView, final OnHabitClickListener listener) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onHabitClick((Habit) v.getTag());
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_habit_item, parent, false);
        return new MyViewHolder(view, onHabitClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Habit currentHabit = habitsList.get(position);
        TextView titleTextView = holder.itemView.findViewById(R.id.habitName);
        titleTextView.setText(currentHabit.getHabitTitle());

        holder.itemView.setTag(currentHabit); // Set the habit as a tag for easy access in the listener
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
