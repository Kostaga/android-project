package com.example.habit_app.logic.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.habit_app.data.models.Habit;

import java.util.ArrayList;
import java.util.List;

public class HabitDao extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "habit_database";
    private static final int DATABASE_VERSION = 2; // Ensure this is set to the correct version

    public static final String TABLE_NAME = "habit_table";

    public HabitDao(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Habit.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN click_count INTEGER DEFAULT 0");
        }
    }

    // Add a habit
    public void addHabit(Habit habit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("habit_title", habit.getHabitTitle());
        values.put("click_count", habit.getClickCount());  // Include clickCount

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Update a habit
    public void updateHabit(Habit habit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("habit_title", habit.getHabitTitle());
        values.put("click_count", habit.getClickCount());  // Include clickCount

        db.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(habit.getId())});
        db.close();
    }

    // Update only the clickCount of a habit
    public void updateHabitClickCount(int habitId, int newCount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("click_count", newCount);

        db.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(habitId)});
        db.close();
    }

    // Delete a habit
    public void deleteHabit(Habit habit) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(habit.getId())});
        db.close();
    }

    // Get all habits
    public LiveData<List<Habit>> getAllHabits() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY id DESC", null);
        List<Habit> habitList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Habit habit = Habit.fromCursor(cursor);
                habitList.add(habit);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        MutableLiveData<List<Habit>> liveData = new MutableLiveData<>();
        liveData.setValue(habitList);
        return liveData;
    }

    // Delete all habits
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.close();
    }
}
