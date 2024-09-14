package com.example.habit_app.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.habit_app.logic.dao.CharacterDao;
import com.example.habit_app.data.models.Character;
import com.example.habit_app.logic.dao.HabitDao;
import com.example.habit_app.data.models.Habit;

public class HabitDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "habit_database";
    private static final int DATABASE_VERSION = 2;

    private static volatile HabitDatabase INSTANCE;

    private final HabitDao habitDao;
    private final CharacterDao characterDao;

    public HabitDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        habitDao = new HabitDao(context);
        characterDao = new CharacterDao(context);
    }

    public static HabitDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (HabitDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HabitDatabase(context.getApplicationContext());
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Habit.createTable(db);  // Create the Habit table using the createTable method in the Habit class
        Character.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + HabitDao.TABLE_NAME);
        onCreate(db);
    }

    public HabitDao habitDao() {
        return habitDao;
    }

    public CharacterDao characterDao() {
        return characterDao;
    }
}


