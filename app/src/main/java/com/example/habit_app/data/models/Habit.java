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

    public Habit(int id, String habitTitle, String habitDescription) {
        this.id = id;
        this.habitTitle = habitTitle;
        this.habitDescription = habitDescription;
    }

    public Habit(String habitTitle, String habitDescription) {
        this.habitTitle = habitTitle;
        this.habitDescription = habitDescription;
    }

    protected Habit(Parcel in) {
        id = in.readInt();
        habitTitle = in.readString();
        habitDescription = in.readString();
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

    public String getHabitDescription() {
        return habitDescription;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(habitTitle);
        parcel.writeString(habitDescription);
    }

    // SQLite Database Operations

    // Method to create a table for storing Habit data
    public static void createTable(SQLiteDatabase db) {
        String createTable = "CREATE TABLE habit_table (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "habit_title TEXT, " +
                "habit_description TEXT)";
        db.execSQL(createTable);
    }

    // Method to insert a Habit into the database
    public long insert(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("habit_title", habitTitle);
        values.put("habit_description", habitDescription);

        return db.insert("habit_table", null, values);
    }

    // Method to update a Habit in the database
    public int update(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("habit_title", habitTitle);
        values.put("habit_description", habitDescription);

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
        String habitDescription = cursor.getString(cursor.getColumnIndexOrThrow("habit_description"));

        return new Habit(id, habitTitle, habitDescription);
    }
}
