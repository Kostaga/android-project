package com.example.habit_app.logic.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.habit_app.data.models.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemDao extends SQLiteOpenHelper {

    private static SQLiteDatabase db;

    private static final String DATABASE_NAME = "habit_database";
    private static final int DATABASE_VERSION = 2; // Adjust version as needed

    public static final String TABLE_NAME = "item_table";

    // Constructor to initialize the database
    public ItemDao(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        HabitDatabase habitDatabase = HabitDatabase.getInstance(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Item.createTable(db); // Assuming this creates the Item table
//        insertDefaultItems();
    }



    public void insertItem(Item item) {
        if (db == null) {
            throw new IllegalStateException("Database is not initialized");
        }
        ContentValues values = new ContentValues();
        values.put("name", item.getName());
        values.put("price", item.getPrice());
        values.put("STR", item.getSTR());
        values.put("DEX", item.getDEX());
        values.put("CON", item.getCON());
        values.put("INT", item.getINT());
        values.put("isUnlocked", item.isUnlocked()); // 1 for true, 0 for false
        values.put("isEquipped", item.isEquipped()); // 1 for true, 0 for false
        values.put("equippedSlot", item.getEquippedSlot());

        db.insert(TABLE_NAME, null, values);
    }

//
//    public void insertDefaultItems() {
//            Log.d("ItemDao", "Inserting default items...");
//            if (!hasItems()) {
//                insertItem("Sword", 50, 10, 5, 0, 0, false, false, 0);
//                insertItem("Staff", 70, 0, 0, 5, 10, false, false, 0);
//                insertItem("Book", 40, 0, 0, 2, 7, false, false, 0);
//                insertItem("Armor", 100, 0, 10, 10, 0, false, false, 0);
//                insertItem("Helmet", 60, 5, 5, 5, 0, false, false, 0);
//                insertItem("Torch", 30, 2, 2, 2, 0, false, false, 0);
//                insertItem("Potion", 20, 0, 0, 5, 5, false, false, 0);
//                insertItem("Boots", 40, 5, 10, 0, 0, false, false, 0);
//            }
//
//    }
//
//    // Method to check if there are items already in the database
//    public boolean hasItems() {
//        SQLiteDatabase db = getReadableDatabase();
//        if (db == null) {
//            throw new IllegalStateException("Database is not initialized");
//        }
//
//        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
//        cursor.moveToFirst();
//        int count = cursor.getInt(0);
//        cursor.close();
//        return count > 0;
//    }


    // Update an existing item in the database
    public static void updateItem(Item item) {
        ContentValues values = new ContentValues();
        values.put("name", item.getName());
        values.put("price", item.getPrice());
        values.put("STR", item.getSTR());
        values.put("DEX", item.getDEX());
        values.put("CON", item.getCON());
        values.put("INT", item.getINT());
        values.put("isUnlocked", item.isUnlocked() ? 1 : 0);
        values.put("isEquipped", item.isEquipped() ? 1 : 0);
        values.put("equippedSlot", item.getEquippedSlot());

        // Return the number of rows affected
        db.update("item_table", values, "id = ?", new String[]{String.valueOf(item.getId())});
    }




    // Get an item by its ID
    public Item getItemById(int id) {
        Cursor cursor = db.query("item_table", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Item item = Item.fromCursor(cursor);
            cursor.close();
            return item;
        }
        return null;
    }

    // Get all items in the database
    public List<Item> getAllItems() {
        if (db == null) {
            db = getReadableDatabase();
        }
        List<Item> items = new ArrayList<>();
        Cursor cursor = db.query("item_table", null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Item item = Item.fromCursor(cursor);
                items.add(item);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return items;
    }

    // Get all unlocked items
    public List<Item> getUnlockedItems() {
        List<Item> items = new ArrayList<>();
        Cursor cursor = db.query("item_table", null, "isUnlocked = 1", null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Item item = Item.fromCursor(cursor);
                items.add(item);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return items;
    }

    // Get all equipped items
    public List<Item> getEquippedItems() {
        List<Item> items = new ArrayList<>();
        Cursor cursor = db.query("item_table", null, "isEquipped = 1", null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Item item = Item.fromCursor(cursor);
                items.add(item);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return items;
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < DATABASE_VERSION) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN isEquipped INTEGER DEFAULT 0");
        }
    }

    public Item getItemInSlot(int slot) {

        Cursor cursor = db.query("item_table", null, "equippedSlot = ?", new String[]{String.valueOf(slot)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Log.d("ItemDao", "Found item in slot: " + slot);
            Item item = Item.fromCursor(cursor);
            cursor.close();
            return item;
        }
        return null;
    }

    public static void logTableInfo() {
        // Execute the PRAGMA query to get table info
        Cursor cursor = db.rawQuery("PRAGMA table_info(item_table);", null);

        // Iterate through the cursor to log each column's information
        if (cursor.moveToFirst()) {
            do {
                String columnName = cursor.getString(cursor.getColumnIndex("name"));
                String columnType = cursor.getString(cursor.getColumnIndex("type"));
                boolean isPrimaryKey = cursor.getInt(cursor.getColumnIndex("pk")) > 0;

                Log.d("TableInfo", "Column Name: " + columnName + ", Type: " + columnType + ", Primary Key: " + isPrimaryKey);
            } while (cursor.moveToNext());
        }

        // Close the cursor after usage
        cursor.close();
    }




}
