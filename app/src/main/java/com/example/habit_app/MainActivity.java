package com.example.habit_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.habit_app.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private HabitsFragment habitsFragment;
    private AddFragment addFragment;
    private InventoryFragment inventoryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Αρχικοποίηση των Fragments
        habitsFragment = new HabitsFragment();
        addFragment = new AddFragment();
        inventoryFragment = new InventoryFragment();

        // Προβολή αρχικού Fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, habitsFragment, "HabitsFragment").commit();

        // Καθορισμός ακροατή για τις επιλογές του Bottom Navigation Bar
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            String tag = null;
            int itemId = item.getItemId();
            if (itemId == R.id.habbit) {
                selectedFragment = habitsFragment;
                tag = "HabitsFragment";
            } else if (itemId == R.id.addHabbit) {
                selectedFragment = addFragment;
                tag = "AddFragment";
            } else if (itemId == R.id.inventory) {
                selectedFragment = inventoryFragment;
                tag = "InventoryFragment";
            }
            // Εμφάνιση του επιλεγμένου Fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, selectedFragment, tag).addToBackStack(null).commit();
            return true;
        });
    }

    private void replaceFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment, tag); // Προσθήκη του τρίτου ορίσματος tag
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    // Μέθοδος για το κλείσιμο της εφαρμογής
    public void exitApp(View view) {
        finish();
    }
}
