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

    private final HabitRepository repository;
    private final LiveData<List<Habit>> getAllHabits;
    private final ExecutorService executorService;

    public HabitViewModel(@NonNull Application application) {
        super(application);
        HabitDatabase db = HabitDatabase.getInstance(application);
        repository = new HabitRepository(db.habitDao());
        getAllHabits = repository.getAllHabits();

        // Use a thread pool for background tasks
        executorService = Executors.newFixedThreadPool(2);
    }

    public LiveData<List<Habit>> getAllHabits() {
        return getAllHabits;
    }

    public void addHabit(Habit habit) {
        executorService.execute(() -> repository.addHabit(habit));
    }

    public void updateHabit(Habit habit) {
        executorService.execute(() -> repository.updateHabit(habit));
    }

    public void deleteHabit(Habit habit) {
        executorService.execute(() -> repository.deleteHabit(habit));
    }

    public void deleteAllHabits() {
        executorService.execute(repository::deleteAllHabits);
    }

    // New method to update click count
    public void updateHabitClickCount(Habit habit, int newClickCount) {
        habit.setClickCount(newClickCount);
        updateHabit(habit);  // Reuse the existing updateHabit method
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown(); // Shut down the executor service when ViewModel is cleared
    }
}
