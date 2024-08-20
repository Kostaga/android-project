package com.example.habit_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.habit_app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set default screen
        startActivity(new Intent(MainActivity.this, Avatar.class));

//        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
//            if (item.getItemId() == R.id.habbit) {
//                startActivity(new Intent(MainActivity.this, HabitsActivity.class));
//            } else if (item.getItemId() == R.id.addHabbit) {
//                startActivity(new Intent(MainActivity.this, AddHabitActivity.class));
//            } else if (item.getItemId() == R.id.inventory) {
//                startActivity(new Intent(MainActivity.this, InventoryActivity.class));
//            }
//
//            return true;
//        });
    }

    // Method to close the app
    public void exitApp(View view) {
        finish();
    }
}
