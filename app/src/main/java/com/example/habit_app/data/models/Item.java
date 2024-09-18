package com.example.habit_app.data.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Item {

    private int id;
    private String name;
    private int price;

    // Stats
    private int STR;
    private int DEX;
    private int CON;
    private int INT;

    // Item state (locked/unlocked, equipped/unequipped)
    private boolean isUnlocked;
    private boolean isEquipped;
    private int equippedSlot;

    public static final String TABLE_NAME = "item_table";

    public Item(int id,String name, int price, int STR, int DEX, int CON, int INT, boolean isUnlocked, boolean isEquipped, int equippedSlot) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.STR = STR;
        this.DEX = DEX;
        this.CON = CON;
        this.INT = INT;
        this.isUnlocked = isUnlocked;
        this.isEquipped = isEquipped;
        this.equippedSlot = equippedSlot;

    }

    // Create Item table in the database
    public static void createTable(SQLiteDatabase db) {
        String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT,"
                + "price INTEGER,"
                + "STR INTEGER,"
                + "DEX INTEGER,"
                + "CON INTEGER,"
                + "INT INTEGER,"
                + "isUnlocked INTEGER,"
                + "isEquipped INTEGER,"
                + "equippedSlot INTEGER DEFAULT -1)"; // New equippedSlot field
        db.execSQL(CREATE_ITEMS_TABLE);
    }


    // Insert item data into the database
    public long insert(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        values.put("STR", STR);
        values.put("DEX", DEX);
        values.put("CON", CON);
        values.put("INT", INT);
        values.put("isUnlocked", isUnlocked ? 1 : 0);
        values.put("isEquipped", isEquipped ? 1 : 0);
        values.put("equippedSlot", equippedSlot);

        return db.insert(TABLE_NAME, null, values);
    }

    // Update item data in the database
    public int update(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("name", this.name);
        values.put("price", this.price);
        values.put("STR", this.STR);
        values.put("DEX", this.DEX);
        values.put("CON", this.CON);
        values.put("INT", this.INT);
        values.put("isUnlocked", this.isUnlocked ? 1 : 0);
        values.put("isEquipped", this.isEquipped ? 1 : 0);
        values.put("equippedSlot", this.equippedSlot);

        return db.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(id)});
    }



    // Retrieve an item from the database using a cursor
    public static Item fromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        int price = cursor.getInt(cursor.getColumnIndexOrThrow("price"));
        int STR = cursor.getInt(cursor.getColumnIndexOrThrow("STR"));
        int DEX = cursor.getInt(cursor.getColumnIndexOrThrow("DEX"));
        int CON = cursor.getInt(cursor.getColumnIndexOrThrow("CON"));
        int INT = cursor.getInt(cursor.getColumnIndexOrThrow("INT"));
        boolean isUnlocked = cursor.getInt(cursor.getColumnIndexOrThrow("isUnlocked")) == 1;
        boolean isEquipped = cursor.getInt(cursor.getColumnIndexOrThrow("isEquipped")) == 1;
        int equippedSlot = cursor.getInt(cursor.getColumnIndexOrThrow("equippedSlot"));

        return new Item(id,name, price, STR, DEX, CON, INT, isUnlocked, isEquipped, equippedSlot);
    }


    public static Item getItemInSlot(SQLiteDatabase db, int slot) {
        String selection = "equippedSlot = ?";
        String[] selectionArgs = { String.valueOf(slot) };

        Cursor cursor = db.query(
                TABLE_NAME,       // The table to query
                null,             // Return all columns
                selection,        // The columns for the WHERE clause
                selectionArgs,    // The values for the WHERE clause
                null,             // Don't group the rows
                null,             // Don't filter by row groups
                null              // The sort order
        );

        if (cursor != null && cursor.moveToFirst()) {
            Item item = Item.fromCursor(cursor);
            cursor.close();
            return item;
        } else {
            if (cursor != null) {
                cursor.close();
            }
            return null;  // No item found for the specified slot
        }
    }


    public int getEquippedSlot() {
        return equippedSlot;
    }

    public void setEquippedSlot(int slot) {
        this.equippedSlot = slot;
    }


    public boolean isUnlocked() {
        return isUnlocked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSTR() {
        return STR;
    }

    public void setSTR(int STR) {
        this.STR = STR;
    }

    public int getDEX() {
        return DEX;
    }

    public void setDEX(int DEX) {
        this.DEX = DEX;
    }

    public int getCON() {
        return CON;
    }

    public void setCON(int CON) {
        this.CON = CON;
    }

    public int getINT() {
        return INT;
    }

    public void setINT(int INT) {
        this.INT = INT;
    }

    public void setUnlocked(boolean unlocked) {
        isUnlocked = unlocked;
    }

    public boolean isEquipped() {
        return isEquipped;
    }

    public void setEquipped(boolean equipped) {
        isEquipped = equipped;
    }




}
