package com.example.habit_app.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.example.habit_app.data.database.HabitDatabase;
import com.example.habit_app.data.models.Habit;
import com.example.habit_app.logic.repository.HabitRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HabitViewModel extends AndroidViewModel {

    private HabitRepository repository;
    private LiveData<List<Habit>> getAllHabits;
    private ExecutorService executorService;

    public HabitViewModel(@NonNull Application application) {
        super(application);
        HabitDatabase db = HabitDatabase.getInstance(application);
        repository = new HabitRepository(db.habitDao());
        getAllHabits = repository.getAllHabits();
        executorService = Executors.newFixedThreadPool(2); // Use a thread pool for background tasks
    }

    public LiveData<List<Habit>> getAllHabits() {
        return getAllHabits;
    }

    public void addHabit(final Habit habit) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                repository.addHabit(habit);
            }
        });
    }

    public void updateHabit(final Habit habit) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                repository.updateHabit(habit);
            }
        });
    }

    public void deleteHabit(final Habit habit) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                repository.deleteHabit(habit);
            }
        });
    }

    public void deleteAllHabits() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                repository.deleteAllHabits();
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown(); // Shut down the executor service when ViewModel is cleared
    }
}
