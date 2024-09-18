package com.example.habit_app.logic.repository;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.habit_app.data.models.Habit;
import com.example.habit_app.logic.dao.HabitDao;

import java.util.List;

public class HabitRepository {

    private HabitDao habitDao;
    private static LiveData<List<Habit>> getAllHabits;

    public HabitRepository(HabitDao habitDao) {
        this.habitDao = habitDao;
        getAllHabits = habitDao.getAllHabits();
    }

    public static LiveData<List<Habit>> getAllHabits() {
        return getAllHabits;
    }

    public void addHabit(Habit habit) {
        new AddHabitAsyncTask(habitDao).execute(habit);
    }

    public void updateHabit(Habit habit) {
        new UpdateHabitAsyncTask(habitDao).execute(habit);
    }

    public void deleteHabit(Habit habit) {
        new DeleteHabitAsyncTask(habitDao).execute(habit);
    }

    public void deleteAllHabits() {
        new DeleteAllHabitsAsyncTask(habitDao).execute();
    }

    // New method to update click count only
    public void updateHabitClickCount(Habit habit, int newClickCount) {
        habit.setClickCount(newClickCount);
        new UpdateHabitClickCountAsyncTask(habitDao).execute(habit);
    }

    // AsyncTask for adding a habit
    private static class AddHabitAsyncTask extends AsyncTask<Habit, Void, Void> {
        private HabitDao habitDao;

        private AddHabitAsyncTask(HabitDao habitDao) {
            this.habitDao = habitDao;
        }

        @Override
        protected Void doInBackground(Habit... habits) {
            habitDao.addHabit(habits[0]);
            return null;
        }
    }

    // AsyncTask for updating a habit
    private static class UpdateHabitAsyncTask extends AsyncTask<Habit, Void, Void> {
        private HabitDao habitDao;

        private UpdateHabitAsyncTask(HabitDao habitDao) {
            this.habitDao = habitDao;
        }

        @Override
        protected Void doInBackground(Habit... habits) {
            habitDao.updateHabit(habits[0]);
            return null;
        }
    }

    // New AsyncTask for updating click count only
    private static class UpdateHabitClickCountAsyncTask extends AsyncTask<Habit, Void, Void> {
        private HabitDao habitDao;

        private UpdateHabitClickCountAsyncTask(HabitDao habitDao) {
            this.habitDao = habitDao;
        }

        @Override
        protected Void doInBackground(Habit... habits) {
            habitDao.updateHabit(habits[0]);  // Update the habit with the new click count
            return null;
        }
    }

    // AsyncTask for deleting a habit
    private static class DeleteHabitAsyncTask extends AsyncTask<Habit, Void, Void> {
        private HabitDao habitDao;

        private DeleteHabitAsyncTask(HabitDao habitDao) {
            this.habitDao = habitDao;
        }

        @Override
        protected Void doInBackground(Habit... habits) {
            habitDao.deleteHabit(habits[0]);
            return null;
        }
    }

    // AsyncTask for deleting all habits
    private static class DeleteAllHabitsAsyncTask extends AsyncTask<Void, Void, Void> {
        private HabitDao habitDao;

        private DeleteAllHabitsAsyncTask(HabitDao habitDao) {
            this.habitDao = habitDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            habitDao.deleteAll();
            return null;
        }
    }
}
