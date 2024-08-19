package com.example.habit_app.logic.repository;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.habit_app.data.models.Habit;
import com.example.habit_app.logic.dao.HabitDao;

import java.util.List;

public class HabitRepository {

    private HabitDao habitDao;
    private LiveData<List<Habit>> getAllHabits;

    public HabitRepository(HabitDao habitDao) {
        this.habitDao = habitDao;
        this.getAllHabits = habitDao.getAllHabits();
    }

    public LiveData<List<Habit>> getAllHabits() {
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
