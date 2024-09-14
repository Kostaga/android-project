package com.example.habit_app.data.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

public class Habit implements Parcelable {

    private int id;
    private String habitTitle;
    private String habitDescription;
    private int clickCount;

    // Getter and Setter for clickCount
    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    // Constructors
    public Habit(int id, String habitTitle, int clickCount) {
        this.id = id;
        this.habitTitle = habitTitle;
        this.clickCount = clickCount;
    }

    public Habit(String habitTitle, int clickCount) {
        this.habitTitle = habitTitle;
        this.clickCount = clickCount;
    }

    protected Habit(Parcel in) {
        id = in.readInt();
        habitTitle = in.readString();
        clickCount = in.readInt();
    }

    public static final Creator<Habit> CREATOR = new Creator<Habit>() {
        @Override
        public Habit createFromParcel(Parcel in) {
            return new Habit(in);
        }

        @Override
        public Habit[] newArray(int size) {
            return new Habit[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getHabitTitle() {
        return habitTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(habitTitle);
        parcel.writeInt(clickCount);
    }

    // SQLite Database Operations

    // Method to create a table for storing Habit data
    public static void createTable(SQLiteDatabase db) {
        String createTable = "CREATE TABLE habit_table (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "habit_title TEXT, " +
                "click_count INTEGER DEFAULT 0)";  // Adding clickCount to the table
        db.execSQL(createTable);
    }

    // Method to insert a Habit into the database
    public long insert(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("habit_title", habitTitle);
        values.put("click_count", clickCount);

        return db.insert("habit_table", null, values);
    }

    // Method to update a Habit in the database
    public int update(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("habit_title", habitTitle);
        values.put("click_count", clickCount);

        return db.update("habit_table", values, "id = ?", new String[]{String.valueOf(id)});
    }

    // Method to delete a Habit from the database
    public int delete(SQLiteDatabase db) {
        return db.delete("habit_table", "id = ?", new String[]{String.valueOf(id)});
    }

    // Static method to retrieve a Habit from a Cursor
    public static Habit fromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        String habitTitle = cursor.getString(cursor.getColumnIndexOrThrow("habit_title"));
        int clickCount = cursor.getInt(cursor.getColumnIndexOrThrow("click_count"));

        return new Habit(id, habitTitle, clickCount);
    }
}
