package com.example.habit_app.logic.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.habit_app.data.models.Character;

import java.util.ArrayList;
import java.util.List;

public class CharacterDao extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "habit_database";
    private static final int DATABASE_VERSION = 2; // Adjust version as needed

    public static final String TABLE_NAME = "character_table";
    // LiveData for Character
    private final MutableLiveData<Character> characterLiveData = new MutableLiveData<>();
    public CharacterDao(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Character.createTable(db); // Create Character table when the database is created
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < DATABASE_VERSION) {
                db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN click_count INTEGER DEFAULT 0");
            }
        }

    // LiveData getter method
    public LiveData<Character> getCharacterLiveData() {
        return characterLiveData;
    }


    // Get character by ID
    // Get character by ID and update LiveData
    public Character getCharacterById(int characterId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE id = ?", new String[]{String.valueOf(characterId)});
        Character character = null;

        if (cursor.moveToFirst()) {
            character = Character.fromCursor(cursor);
            // Update LiveData when character is fetched
            characterLiveData.postValue(character);
        }
        cursor.close();
        return character;
    }

    // Method to increase HP and save to database
    // Method to update character's HP and refresh LiveData
    public void increaseHp(int characterId, int amount) {
        Character character = getCharacterById(characterId);
        if (character != null) {
            int newHp = Math.min(character.getCurrentHp() + amount, character.getMaxXp());
            character.setCurrentHp(newHp);
            saveCharacterHp(characterId, newHp);

            // Update LiveData after change
            characterLiveData.postValue(character);
        }
    }




    // Method to decrease HP and save to database
    public void decreaseHp(int characterId, int amount) {
        Character character = getCharacterById(characterId);
        if (character != null) {
            int newHp = Math.max(character.getCurrentHp() - amount, 0);
            character.setCurrentHp(newHp);
            saveCharacterHp(characterId, newHp);
            if (newHp == 0) {
                decreaseCoins(characterId, 10); // Apply coin penalty if HP drops to 0
            }

            // Update LiveData after change
            characterLiveData.postValue(character);
        }
    }

    // Method to increase XP and save to database, handle leveling up
    public void increaseXp(int characterId, int amount) {
        Character character = getCharacterById(characterId);
        if (character != null) {
            int newXp = character.getXp() + amount;
            if (newXp >= character.getMaxXp()) {
                newXp = 0;
                increaseLevel(characterId, 1); // Handle leveling up
            }
            character.setXp(newXp);
            saveCharacterXp(characterId, character.getXp());


            // Update LiveData after XP change
            characterLiveData.postValue(character);
        }
    }

    // Method to decrease XP and apply penalties, update database
    public void decreaseXp(int characterId, int amount) {
        Character character = getCharacterById(characterId);
        if (character != null) {
            int newXp = character.getXp() - amount;
            if (newXp < 0) {
                newXp = 0;
                decreaseHp(characterId, 10 + (character.getLevel() - 1) * 2); // Apply HP penalty if XP drops below 0
            }
            character.setXp(newXp);
            saveCharacterXp(characterId, newXp);
        }

        // Update LiveData after XP change
        characterLiveData.postValue(character);
    }

    // Method to increase coins and save to database
    public void increaseCoins(int characterId, int amount) {
        Character character = getCharacterById(characterId);
        if (character != null) {
            int newCoins = character.getCoins() + amount;
            character.setCoins(newCoins);
            saveCharacterCoins(characterId, newCoins);

            // Update LiveData after coins change
            characterLiveData.postValue(character);
        }
    }

    // Method to decrease coins and save to database
    public void decreaseCoins(int characterId, int amount) {
        Character character = getCharacterById(characterId);
        if (character != null) {
            int newCoins = Math.max(character.getCoins() - amount, 0); // Ensure coins don't drop below 0
            character.setCoins(newCoins);
            saveCharacterCoins(characterId, newCoins);

            // Update LiveData after coins change
            characterLiveData.postValue(character);
        }
    }

    // Method to increase level, adjust stats, and save to database
    public void increaseLevel(int characterId, int amount) {
        Character character = getCharacterById(characterId);
        if (character != null) {
            int newLevel = character.getLevel() + amount;
            character.setLevel(newLevel);

            // Reset XP and adjust maximum XP and HP
            character.setXp(0);
            character.setMaximumHp(100 + (newLevel - 1) * 10);
            character.setMaximumXp(100 + (newLevel - 1) * 25);

            // Increase coins based on level
            int newCoins = character.getCoins() + newLevel * 3 + 10;
            character.setCoins(newCoins);

            // Restore HP to maximum after leveling up
            character.setCurrentHp(character.getMaxHp());

            // Save all updated stats
            saveCharacterStats(characterId, newLevel, character.getXp(), character.getMaxXp(), character.getMaxHp(), newCoins, character.getHp());

            // Update LiveData after level up
            characterLiveData.postValue(character);
        }
    }


    // Method to increase HP for a character
    private void saveCharacterHp(int characterId, int newHp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("hp", newHp);
        db.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(characterId)});
        db.close();
    }

    // Method to decrease HP for a character
    private void saveCharacterXp(int characterId, int newXp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("xp", newXp);
        db.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(characterId)});
        db.close();
    }

    // Method to save coins for a character
    private void saveCharacterCoins(int characterId, int newCoins) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("coins", newCoins);
        db.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(characterId)});
        db.close();
    }

    // Method to save all character stats
    private void saveCharacterStats(int characterId, int level, int xp, int maxXp, int maxHp, int coins, int hp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", characterId);
        values.put("level", level);
        values.put("xp", xp);
        values.put("max_xp", maxXp);
        values.put("max_hp", maxHp);
        values.put("coins", coins);
        values.put("hp", hp);
        db.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(characterId)});
        db.close();
    }


    // Add a character
    public void addCharacter(Character character) {
        SQLiteDatabase db = this.getWritableDatabase();
        character.insert(db);
        db.close();
    }

    // Update a character
    public void updateCharacter(Character character) {
        SQLiteDatabase db = this.getWritableDatabase();
        character.update(db);
        db.close();
    }





    public Character getCharacter() {
        return characterLiveData.getValue();
    }
}
