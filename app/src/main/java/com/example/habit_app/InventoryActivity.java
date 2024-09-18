package com.example.habit_app;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.habit_app.data.database.HabitDatabase;
import com.example.habit_app.data.models.Item;
import com.example.habit_app.logic.dao.CharacterDao;
import com.example.habit_app.data.models.Character;
import com.example.habit_app.logic.dao.ItemDao;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Objects;

public class InventoryActivity extends AppCompatActivity {

    private ImageView avatarImageView;
    private TextView nicknameTextView;
    private TextView coinsTextView;

    private int currentCoins;
    private CharacterDao characterDao;
    private ItemDao itemDao;
    private Character character;

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        HabitDatabase db = HabitDatabase.getInstance(this);
        database = db.getReadableDatabase();
        characterDao = db.characterDao();
        itemDao = db.itemDao();

        insertDefaultItems();


        // Initialize the UI elements
        avatarImageView = findViewById(R.id.profile_picture);
        nicknameTextView = findViewById(R.id.user_name);
        coinsTextView = findViewById(R.id.coinstext);

        // Retrieve character data from DB
        character = characterDao.getCharacterById(1); // Assuming character ID 1 for this example
        currentCoins = character.getCoins();
        coinsTextView.setText(String.valueOf(currentCoins));

        // Get the avatar and nickname from the Intent
        Intent intent = getIntent();
        String nickname = intent.getStringExtra("NICKNAME");
        int avatarId = intent.getIntExtra("AVATAR_ID", -1);

        // Set the avatar and nickname in the UI
        if (avatarId != -1) {
            avatarImageView.setImageResource(avatarId);
        }
        nicknameTextView.setText(nickname);

        // Set up the BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.inventory);

        // Handle BottomNavigationView item selection
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
                homeIntent.putExtra("NICKNAME", nickname);
                homeIntent.putExtra("AVATAR_ID", avatarId);
                startActivity(homeIntent);
                finish();
                return true;
            } else if (item.getItemId() == R.id.inventory) {
                // We're already in InventoryActivity, no need to do anything
                return true;
            }
            return false;
        });

        TextView strTextView = findViewById(R.id.strengthAttribute);
        TextView dexTextView = findViewById(R.id.dexterityAttribute);
        TextView conTextView = findViewById(R.id.constitutionAttribute);
        TextView intTextView = findViewById(R.id.intelligenceAttribute);

        // Initialize to +0 for each attribute when no equipment is equipped
        String strBonus = getString(R.string.Strength, 0);
        strTextView.setText(strBonus);

        String dexBonus = getString(R.string.Dexterity, 0);
        dexTextView.setText(dexBonus);

        String conBonus = getString(R.string.Constitution, 0);
        conTextView.setText(conBonus);

        String intBonus = getString(R.string.Intelligence, 0);
        intTextView.setText(intBonus);


        // If there are no items in the database, insert the default items
        if (itemDao.getAllItems().isEmpty()) {
            insertDefaultItems();
        }
        // Otherwise load the items from the database
        else {
            loadItems();
        }

    }

    private void insertDefaultItems() {
        if (!itemDao.getAllItems().isEmpty()) {
            return;
        }
        Item sword = new Item(0,"Sword", 50, 6, 0, 0, 0, false, false, -1);
        Item staff = new Item(1,"Staff", 50, 0, 2, 0, 6, false, false, -1);
        Item book = new Item(2,"Book", 25, 0, 0, 2, 4, false, false, -1);
        Item armor = new Item(3,"Armor", 45, 2, 0, 3, 0, false, false, -1);
        Item helmet = new Item(4,"Helmet", 30, 1, 0, 2, 2, false, false, -1);
        Item torch = new Item(5,"Torch", 25, 0, 3, 0, 2, false, false, -1);
        Item potion = new Item(6,"Potion", 45, 1, 1, 2, 2, false, false, -1);
        Item boots = new Item(7,"Boots", 45, 0, 6, 1, 0, false, false, -1);

//        sword.insert(database);
//        staff.insert(database);
//        book.insert(database);
//        armor.insert(database);
//        helmet.insert(database);
//        torch.insert(database);
//        potion.insert(database);
//        boots.insert(database);

        itemDao.insertItem(sword);
        itemDao.insertItem(staff);
        itemDao.insertItem(book);
        itemDao.insertItem(armor);
        itemDao.insertItem(helmet);
        itemDao.insertItem(torch);
        itemDao.insertItem(potion);
        itemDao.insertItem(boots);


        setupItemUI(sword);
        setupItemUI(staff);
        setupItemUI(book);
        setupItemUI(armor);
        setupItemUI(helmet);
        setupItemUI(torch);
        setupItemUI(potion);
        setupItemUI(boots);
    }

    private void loadItems() {
        // Fetch all items from the database
        List<Item> items = itemDao.getAllItems();
        // For each item, set the appropriate UI elements, such as locked/unlocked state, and purchase logic
        for (Item item : items) {
            // Load and display items
            setupItemUI(item);
        }
    }

    private void setupItemUI(Item item) {

        TextView strTextView = findViewById(R.id.strengthAttribute);
        TextView dexTextView = findViewById(R.id.dexterityAttribute);
        TextView conTextView = findViewById(R.id.constitutionAttribute);
        TextView intTextView = findViewById(R.id.intelligenceAttribute);

        // Assuming your layout has item ImageViews corresponding to each item
        ImageView itemImageView = findViewById(getItemImageViewId(item));
        TextView itemPriceTextView = findViewById(getItemPriceTextViewId(item)); // TextView for price

        // If the item is equipped, apply its stats to attributes
        if (item.isEquipped()) {
            String strBonus = getString(R.string.Strength, item.getSTR());
            strTextView.setText(strBonus);


            String dexBonus = getString(R.string.Dexterity, item.getDEX());
            dexTextView.setText(dexBonus);

            String conBonus = getString(R.string.Constitution, item.getCON());
            conTextView.setText(conBonus);

            String intBonus = getString(R.string.Intelligence, item.getINT());
            intTextView.setText(intBonus);
            updateSlotImageView(getSlotImageView(item.getEquippedSlot()), item.getName());
        }

        // If the item is locked, show the price and lock icon
        if (!item.isUnlocked()) {
            // Show price for locked items
            String price = getString(R.string.price, item.getPrice());
            itemPriceTextView.setText(price);  // Correctly display price
//        itemImageView.setImageResource(R.drawable.lock_icon);  // Set the lock icon if available
            itemImageView.setOnClickListener(v -> showPurchaseDialog(item));  // Handle purchase
        } else {
            // For unlocked items, clear the price display and show the item's image
            itemPriceTextView.setText("0");
//        itemImageView.setImageResource(item.getImageId());
            itemImageView.setOnClickListener(v -> showEquipDialog(item));  // Handle equip
        }
    }


    private int getItemPriceTextViewId(Item item) {
        switch (item.getName()) {
            case "Sword":
                return R.id.itemPrice1;
            case "Staff":
                return R.id.itemPrice2;
            case "Book":
                return R.id.itemPrice3;

            case "Armor":
                return R.id.itemPrice4;
            case "Helmet":
                return R.id.itemPrice5;
            case "Torch":
                return R.id.itemPrice6;
            case "Potion":
                return R.id.itemPrice7;
            case "Boots":
                return R.id.itemPrice8;
            default:
                throw new IllegalArgumentException("Unknown item: " + item.getName());
        }
    }

    private int getItemImageViewId(Item item) {
        // This method returns the corresponding ImageView ID for each item
        // You'll need to map your item to the correct ImageView (Sword, Staff, etc.)
        switch (item.getName()) {
            case "Sword":
                return R.id.swordImageView;
            case "Staff":
                return R.id.staffImageView;
            case "Book":
                return R.id.bookImageView;
            // Add other cases for other items
            case "Armor":
                return R.id.armorImageView;
            case "Helmet":
                return R.id.helmetImageView;
            case "Torch":
                return R.id.torchImageView;
            case "Potion":
                return R.id.potionImageView;
            case "Boots":
                return R.id.bootsImageView;
            default:
                throw new IllegalArgumentException("Unknown item: " + item.getName());
        }
    }

    // Show a dialog to purchase the item if locked
    private void showPurchaseDialog(Item item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Purchase " + item.getName())
                .setMessage("Do you want to purchase this item for " + item.getPrice() + " coins?")
                .setPositiveButton("Buy", (dialog, which) -> purchaseItem(item))
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void purchaseItem(Item item) {
        if (currentCoins >= item.getPrice()) {
            currentCoins -= item.getPrice();
            item.setUnlocked(true);
            character.setCoins(currentCoins);

            // Update the database
            characterDao.updateCharacter(character);
            itemDao.updateItem(item);

            // Update the UI
            coinsTextView.setText(String.valueOf(currentCoins));
            setupItemUI(item);  // Re-run setup to reflect unlocked state

            Toast.makeText(this, item.getName() + " purchased!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Not enough coins!", Toast.LENGTH_SHORT).show();
        }
    }

    // Show a dialog to choose a slot to equip the item
    private void showEquipDialog(Item item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Equip Item")
                .setItems(new String[]{"Slot 1", "Slot 2", "Slot 3", "Slot 4", "Remove"}, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            equipItem(item, 1);
                            break;
                        case 1:
                            equipItem(item, 2);
                            break;
                        case 2:
                            equipItem(item, 3);
                            break;
                        case 3:
                            equipItem(item, 4);
                            break;
                        case 4:
                            unequipItem(item.getEquippedSlot());
                    }
                })
                .create()
                .show();
    }

    public void equipItem(Item item, int slot) {
        // Check if the item is already equipped in the same slot
        if (item.isEquipped() && item.getEquippedSlot() == slot && Objects.equals(getItemInSlot(slot).getName(), item.getName())) {
            Toast.makeText(this, item.getName() + " is already equipped in Slot " + slot, Toast.LENGTH_SHORT).show();
            return;  // No need to re-equip
        }

        // If the item is equipped in a different slot, unequip it first
        if (item.isEquipped() && item.getEquippedSlot() != slot) {
            unequipItem(item.getEquippedSlot());
        }

        // Check if the slot already has another equipped item
        Item equippedItemInSlot = itemDao.getItemInSlot(slot);
        if (equippedItemInSlot != null) {
            // Unequip the item currently in the slot (e.g. if replacing a Sword with a Staff)
            unequipItem(slot);  // Ensure stats are subtracted correctly
        }



        // Equip the new item
        item.setEquipped(true);
        item.setEquippedSlot(slot);  // Set the slot where the item is equipped
        itemDao.updateItem(item);    // Update in database

        // Update the UI to reflect the newly equipped item
        ImageView slotImageView = getSlotImageView(slot);
        updateSlotImageView(slotImageView, item.getName());

        // Add the new item's stats to the character's attributes
        updateAttributes(item.getSTR(), item.getDEX(), item.getCON(), item.getINT());

        // Confirmation message
        Toast.makeText(this, item.getName() + " equipped in Slot " + slot, Toast.LENGTH_SHORT).show();
    }



    private void updateSlotImageView(ImageView slotImageView, String itemName) {
        if (slotImageView == null) return;

        switch (itemName) {
            case "Sword":
                slotImageView.setImageResource(R.drawable.sword);
                break;
            case "Staff":
                slotImageView.setImageResource(R.drawable.staff);
                break;
            case "Armor":
                slotImageView.setImageResource(R.drawable.iron_armor);
                break;
            case "Helmet":
                slotImageView.setImageResource(R.drawable.iron_helmet);
                break;
            case "Torch":
                slotImageView.setImageResource(R.drawable.torch);
                break;
            case "Potion":
                slotImageView.setImageResource(R.drawable.large_full_flask);
                break;
            case "Boots":
                slotImageView.setImageResource(R.drawable.leather_boots);
                break;
            case "Book":
                slotImageView.setImageResource(R.drawable.book);
                break;
            default:
                slotImageView.setImageResource(0); // Clear if no match
                break;
        }
    }


    public void unequipItem(int slot) {
        // Get the currently equipped item in the slot
        if (slot == -1) {
            return;
        }
        Item equippedItem =  getItemInSlot(slot);

        if (equippedItem == null) {
            Toast.makeText(this, "No item equipped in Slot " + slot, Toast.LENGTH_SHORT).show();
            return;
        }

        // Un-equip the item
        equippedItem.setEquipped(false);
        equippedItem.setEquippedSlot(-1);  // Reset the equipped slot
        itemDao.updateItem(equippedItem);  // Update in database

        // Clear the slot's ImageView
        clearSlotImageView(slot);

        // Subtract the item's stats from the character's attributes
        updateAttributes(-equippedItem.getSTR(), -equippedItem.getDEX(), -equippedItem.getCON(), -equippedItem.getINT());

        // Confirmation message
        Toast.makeText(this, equippedItem.getName() + " unequipped from Slot " + slot, Toast.LENGTH_SHORT).show();
    }





    // Method to update the character's attributes and UI
    private void updateAttributes(int str, int dex, int con, int intelligence) {
        TextView strTextView = findViewById(R.id.strengthAttribute);
        TextView dexTextView = findViewById(R.id.dexterityAttribute);
        TextView conTextView = findViewById(R.id.constitutionAttribute);
        TextView intTextView = findViewById(R.id.intelligenceAttribute);

        // Parse the current attribute values from the UI
        int currentStr = Integer.parseInt(strTextView.getText().toString().replace("STR: ", ""));
        int currentDex = Integer.parseInt(dexTextView.getText().toString().replace("DEX: ", ""));
        int currentCon = Integer.parseInt(conTextView.getText().toString().replace("CON: ", ""));
        int currentInt = Integer.parseInt(intTextView.getText().toString().replace("INT: ", ""));

        // Update the attributes by adding or subtracting the item stats
        currentStr += str;
        currentDex += dex;
        currentCon += con;
        currentInt += intelligence;

        // Update the TextViews with the new values
        strTextView.setText(getString(R.string.Strength, currentStr));
        dexTextView.setText(getString(R.string.Dexterity, currentDex));
        conTextView.setText(getString(R.string.Constitution, currentCon));
        intTextView.setText(getString(R.string.Intelligence, currentInt));
    }


    // Helper method to clear the image from the slot when unequipped
    private void clearSlotImageView(int slot) {
        ImageView slotImageView = getSlotImageView(slot);
        if (slotImageView != null) {
            slotImageView.setImageResource(0);
        }
    }




    private ImageView getSlotImageView(int slot) {
        // Map the slot number to the correct ImageView in the layout
        switch (slot) {
            case 1:
                return findViewById(R.id.slot1ImageView);
            case 2:
                return findViewById(R.id.slot2ImageView);
            case 3:
                return findViewById(R.id.slot3ImageView);
            case 4:
                return findViewById(R.id.slot4ImageView);
            default:
                throw new IllegalArgumentException("Unknown slot: " + slot);
        }
    }



    public Item getItemInSlot(int slot) {

        Cursor cursor = database.query("item_table", null, "equippedSlot = ?", new String[]{String.valueOf(slot)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Log.d("ItemDao", "Found item in slot: " + slot);
            Item item = Item.fromCursor(cursor);
            cursor.close();
            return item;
        }
        return null;
    }



}
