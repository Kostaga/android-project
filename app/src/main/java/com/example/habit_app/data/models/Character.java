package com.example.habit_app.data.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Character {

    private int id;
    private int hp;
    private int xp;
    private int level;
    private int coins;

    // Constructors

    public Character() {
        this.id = 1;
        this.hp = 100;
        this.xp = 0;
        this.level = 1;
        this.coins = 0;
    }
    public Character(int id, int hp, int xp, int level, int coins) {
        this.id = id;
        this.hp = hp;
        this.xp = xp;
        this.level = level;
        this.coins = coins;
    }

    public Character(int hp, int xp, int level, int coins) {
        this.hp = hp;
        this.xp = xp;
        this.level = level;
        this.coins = coins;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public int getHp() {
        return hp;
    }


    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    // Method to create the Character table in the database
    public static void createTable(SQLiteDatabase db) {
        String createTable = "CREATE TABLE character_table (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "hp INTEGER, " +
                "xp INTEGER, " +
                "level INTEGER, " +
                "coins INTEGER)";
        db.execSQL(createTable);
    }

    // Insert character data into the database
    public long insert(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("hp", hp);
        values.put("xp", xp);
        values.put("level", level);
        values.put("coins", coins);

        return db.insert("character_table", null, values);
    }

    // Update character data in the database
    public int update(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("hp", hp);
        values.put("xp", xp);
        values.put("level", level);
        values.put("coins", coins);

        return db.update("character_table", values, "id = ?", new String[]{String.valueOf(id)});
    }

    // Delete character data from the database
    public int delete(SQLiteDatabase db) {
        return db.delete("character_table", "id = ?", new String[]{String.valueOf(id)});
    }

    // Method to retrieve a Character from a Cursor
    public static Character fromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        int hp = cursor.getInt(cursor.getColumnIndexOrThrow("hp"));
        int xp = cursor.getInt(cursor.getColumnIndexOrThrow("xp"));
        int level = cursor.getInt(cursor.getColumnIndexOrThrow("level"));
        int coins = cursor.getInt(cursor.getColumnIndexOrThrow("coins"));

        return new Character(id, hp, xp, level, coins);
    }

    public int getCurrentHp() {
        return hp;
    }

    public void setCurrentHp(int newHp) {
        hp = newHp;
    }

    public int getMaximumHp() {
        return 100;
    }

    public int getMaximumXp() {
        return (level - 1) * 12 + 100;
    }

    public void setMaximumXp(int newMaxXp) {
        xp = newMaxXp;
    }

    public void setMaximumHp(int newMaxHp) {
        hp = newMaxHp;
    }
}
