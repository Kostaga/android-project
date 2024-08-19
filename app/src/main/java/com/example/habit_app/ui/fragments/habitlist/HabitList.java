package com.example.habit_app.ui.fragments.habitlist;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;
import com.example.habit_app.R;
import com.example.habit_app.data.models.Habit;
import com.example.habit_app.ui.fragments.habitlist.adapters.HabitListAdapter;
import com.example.habit_app.ui.viewmodels.HabitViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.util.List;

public class HabitList extends Fragment {

    private List<Habit> habitList;
    private HabitViewModel habitViewModel;
    private HabitListAdapter adapter;
    private RecyclerView rvHabits;
    private FloatingActionButton fabAdd;
    private SwipeRefreshLayout swipeToRefresh;
    private View tvEmptyView;

    public HabitList() {
        super(R.layout.fragment_habit_list);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new HabitListAdapter();
        rvHabits = view.findViewById(R.id.rv_habits);
        fabAdd = view.findViewById(R.id.fab_add);
        swipeToRefresh = view.findViewById(R.id.swipeToRefresh);
        tvEmptyView = view.findViewById(R.id.tv_emptyView);

        rvHabits.setAdapter(adapter);
        rvHabits.setLayoutManager(new LinearLayoutManager(getContext()));

        // Instantiate and create ViewModel observers
        viewModels();

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(HabitList.this)
                        .navigate(R.id.action_habitList_to_createHabitItem);
            }
        });

        // Setup MenuProvider
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.nav_main, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.nav_delete) {
                    habitViewModel.deleteAllHabits();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.setData(habitList);
                swipeToRefresh.setRefreshing(false);
            }
        });
    }

    private void viewModels() {
        habitViewModel = new ViewModelProvider(this).get(HabitViewModel.class);

        habitViewModel.getAllHabits().observe(getViewLifecycleOwner(), new Observer<List<Habit>>() {
            @Override
            public void onChanged(List<Habit> habits) {
                adapter.setData(habits);
                habitList = habits;

                if (habits.isEmpty()) {
                    rvHabits.setVisibility(View.GONE);
                    tvEmptyView.setVisibility(View.VISIBLE);
                } else {
                    rvHabits.setVisibility(View.VISIBLE);
                    tvEmptyView.setVisibility(View.GONE);
                }
            }
        });
    }
}