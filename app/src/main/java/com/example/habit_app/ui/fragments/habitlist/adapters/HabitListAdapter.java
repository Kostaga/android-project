package com.example.habit_app.ui.fragments.habitlist.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habit_app.MainActivity;
import com.example.habit_app.R;
import com.example.habit_app.data.models.Habit;
import com.example.habit_app.logic.dao.CharacterDao;

import com.example.habit_app.ui.viewmodels.HabitViewModel;

import java.util.ArrayList;
import java.util.List;

public class HabitListAdapter extends RecyclerView.Adapter<HabitListAdapter.MyViewHolder> {

    private List<Habit> habitsList = new ArrayList<>();
    private OnHabitClickListener onHabitClickListener;

    HabitViewModel habitViewModel;
//    CharacterRepository characterRepository;
    CharacterDao characterDao;
    private MainActivity activity;




    // Constructor to accept CharacterRepository
    public HabitListAdapter(CharacterDao characterDao, MainActivity activity) {
        this.characterDao = characterDao;
        this.activity = activity;
    }


    // Interface for click listener
    public interface OnHabitClickListener {
        void onHabitClick(Habit habit);
    }

    // Setter for the click listener
    public void setOnHabitClickListener(OnHabitClickListener listener) {
        this.onHabitClickListener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView clickCounter;
        Button buttonPlus;
        Button buttonMinus;

        public MyViewHolder(@NonNull View itemView, final OnHabitClickListener listener) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.habitName);
            clickCounter = itemView.findViewById(R.id.clickCounter);
            buttonPlus = itemView.findViewById(R.id.buttonPlus);
            buttonMinus = itemView.findViewById(R.id.buttonMinus);

            // Setup click listener on the entire item view
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
        holder.titleTextView.setText(currentHabit.getHabitTitle());
        holder.clickCounter.setText(String.valueOf(currentHabit.getClickCount()));

        holder.itemView.setTag(currentHabit); // Set the habit as a tag for easy access in the listener

        // Increment button functionality
        holder.buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newCount = currentHabit.getClickCount() + 1;
                currentHabit.setClickCount(newCount);
                holder.clickCounter.setText(String.valueOf(newCount));

                characterDao.increaseXp(1, 25 + newCount * 2);
                activity.refreshUI();



            }
        });

        // Decrement button functionality
        holder.buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newCount = currentHabit.getClickCount() > 0 ? currentHabit.getClickCount() - 1 : 0;
                currentHabit.setClickCount(newCount);
                holder.clickCounter.setText(String.valueOf(newCount));
                characterDao.decreaseXp(1, 20);
                activity.refreshUI();
            }
        });
    }

    @Override
    public int getItemCount() {
        return habitsList.size();
    }

    public void setData(List<Habit> habits) {
        this.habitsList = habits;
        notifyDataSetChanged();
    }

    public void removeHabit(Habit habit) {
        int position = habitsList.indexOf(habit);
        if (position >= 0) {
            habitsList.remove(position);
            notifyItemRemoved(position);
        }
    }
}
