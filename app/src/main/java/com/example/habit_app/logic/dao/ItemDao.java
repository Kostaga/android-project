package com.example.habit_app.logic.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.habit_app.data.database.HabitDatabase;
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
//        db = habitDatabase.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Item.createTable(db); // Assuming this creates the Item table
//        ItemDao.db = db;  // Store the db reference
        insertDefaultItems();
    }


    // Insert a new item into the database
//    public long insertItem(Item item) {
//        ContentValues values = new ContentValues();
//        values.put("name", item.getName());
//        values.put("price", item.getPrice());
//        values.put("STR", item.getSTR());
//        values.put("DEX", item.getDEX());
//        values.put("CON", item.getCON());
//        values.put("INT", item.getINT());
//        values.put("isUnlocked", item.isUnlocked() ? 1 : 0);
//        values.put("isEquipped", item.isEquipped() ? 1 : 0);
//
//        return db.insert("item_table", null, values);
//    }

    private void insertItem(String name, int price, int STR, int DEX, int CON, int INT, boolean isUnlocked, boolean isEquipped, int equippedSlot) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        values.put("STR", STR);
        values.put("DEX", DEX);
        values.put("CON", CON);
        values.put("INT", INT);
        values.put("isUnlocked", isUnlocked ? 1 : 0); // 1 for true, 0 for false
        values.put("isEquipped", isEquipped ? 1 : 0); // 1 for true, 0 for false
        values.put("equippedSlot", equippedSlot);

        db.insert(TABLE_NAME, null, values);
    }


    public void insertDefaultItems() {
        if (!hasItems()) {
            insertItem("Sword", 50, 10, 5, 0, 0, false, false,0);
            insertItem("Staff", 70, 0, 0, 5, 10, false, false,0);
            insertItem("Book", 40, 0, 0, 2, 7, false, false,0);
            insertItem("Armor", 100, 0, 10, 10, 0, false, false,0);
            insertItem("Helmet", 60, 5, 5, 5, 0, false, false,0);
            insertItem("Torch", 30, 2, 2, 2, 0, false, false,0);
            insertItem("Potion", 20, 0, 0, 5, 5, false, false,0);
            insertItem("Boots", 40, 5, 10, 0, 0, false, false,0);
        }
    }

    // Method to check if there are items already in the database
    public boolean hasItems() {
        String query = "SELECT COUNT(*) FROM item_table";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            return count > 0;
        }
        return false;
    }

    // Update an existing item in the database
    public static int updateItem(Item item) {
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

        return db.update("item_table", values, "id = ?", new String[]{String.valueOf(item.getId())});
    }

    // Delete an item from the database
    public int deleteItem(int itemId) {
        return db.delete("item_table", "id = ?", new String[]{String.valueOf(itemId)});
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
}
