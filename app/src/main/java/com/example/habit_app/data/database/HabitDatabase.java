package com.example.habit_app.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.habit_app.logic.dao.CharacterDao;
import com.example.habit_app.logic.dao.HabitDao;
import com.example.habit_app.logic.dao.ItemDao;
import com.example.habit_app.data.models.Character;
import com.example.habit_app.data.models.Habit;
import com.example.habit_app.data.models.Item;

public class HabitDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "habit_database";
    private static final int DATABASE_VERSION = 2;

    private static volatile HabitDatabase INSTANCE;

    private final HabitDao habitDao;
    private final CharacterDao characterDao;
    private final ItemDao itemDao;

    public HabitDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        habitDao = new HabitDao(context);
        characterDao = new CharacterDao(context);
        itemDao = new ItemDao(context);

    }

    public static HabitDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (HabitDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HabitDatabase(context.getApplicationContext());
                }
            }
        }

//        INSTANCE.getWritableDatabase();  // Force database initialization
        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Habit.createTable(db);  // Create the Habit table
        Character.createTable(db);  // Create the Character table
        Item.createTable(db);  // Create the Item table
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + HabitDao.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CharacterDao.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ItemDao.TABLE_NAME);
        onCreate(db);
    }

    public HabitDao habitDao() {
        return habitDao;
    }

    public CharacterDao characterDao() {
        return characterDao;
    }

    public ItemDao itemDao() {
        return itemDao;
    }

    public SQLiteDatabase getReadableDatabaseInstance() {
        return this.getReadableDatabase();
    }

    public SQLiteDatabase getWritableDatabaseInstance() {
        return this.getWritableDatabase();
    }
}
