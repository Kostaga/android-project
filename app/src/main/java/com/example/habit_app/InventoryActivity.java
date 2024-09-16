package com.example.habit_app;

import android.content.Intent;
import android.os.Bundle;
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

public class InventoryActivity extends AppCompatActivity {

    private ImageView avatarImageView;
    private TextView nicknameTextView;
    private TextView coinsTextView;

    private int currentCoins;
    private CharacterDao characterDao;
    private ItemDao itemDao;
    private Character character;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        HabitDatabase db = HabitDatabase.getInstance(this);
        characterDao = db.characterDao();
        itemDao = db.itemDao();

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

        // Load and display items
        loadItems();
    }

    private void loadItems() {
        // Fetch all items from the database
        List<Item> items = itemDao.getAllItems();

        // For each item, set the appropriate UI elements, such as locked/unlocked state, and purchase logic
        for (Item item : items) {
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

        if (item.isEquipped()) {
            String strBonus = getString(R.string.Strength, item.getSTR());
            strTextView.setText(strBonus);

            String dexBonus = getString(R.string.Dexterity, item.getDEX());
            dexTextView.setText(dexBonus);

            String conBonus = getString(R.string.Constitution, item.getCON());
            conTextView.setText(conBonus);

            String intBonus = getString(R.string.Intelligence, item.getINT());
            intTextView.setText(intBonus);
        }

        // If item is locked, show price and lock icon, otherwise show the item as unlocked
        if (!item.isUnlocked()) {
//            itemImageView.setImageResource(R.drawable.lock_icon);
            itemImageView.setOnClickListener(v -> showPurchaseDialog(item));
        } else {
//            itemImageView.setImageResource(item.getImageId()); // Set the item's image if unlocked
            itemImageView.setOnClickListener(v -> showEquipDialog(item));
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
            characterDao.updateCharacter(character);
            itemDao.updateItem(item);

            // Update the UI
            coinsTextView.setText(String.valueOf(currentCoins));
            setupItemUI(item);

            Toast.makeText(this, item.getName() + " purchased!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Not enough coins!", Toast.LENGTH_SHORT).show();
        }
    }

    // Show a dialog to choose a slot to equip the item
    private void showEquipDialog(Item item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Equip Item")
                .setItems(new String[]{"Slot 1", "Slot 2", "Slot 3", "Slot 4"}, (dialog, which) -> {
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
                    }
                })
                .create()
                .show();
    }

    private void equipItem(Item item, int slot) {
        // Equip logic: mark item as equipped and update the database
        item.setEquipped(true);
        item.setEquippedSlot(slot);
        itemDao.updateItem(item);

        // Update UI to show equipped item in the chosen slot (you should have ImageViews for these slots)
        ImageView slotImageView = getSlotImageView(slot);
        switch (item.getName()) {
            case "Sword":
                slotImageView.setImageResource(R.drawable.sword); // Refers to sword drawable resource
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
        }

        Toast.makeText(this, item.getName() + " equipped in Slot " + slot, Toast.LENGTH_SHORT).show();
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
}
