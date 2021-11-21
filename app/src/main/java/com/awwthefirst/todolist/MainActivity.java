package com.awwthefirst.todolist;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.awwthefirst.todolist.recyclerview.ItemRecyclerViewAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.awwthefirst.todolist.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static MainActivity instance;

    private ActivityMainBinding binding;
    private RecyclerView recyclerView;
    private Item.List currentList = Item.List.TODO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        onItemsChanged();
    }

    public void createNewItem(View view) {
        //Creates a dialog box for adding a new item
        EditItemDialogFragment editItemDialogFragment = new EditItemDialogFragment();
        editItemDialogFragment.show(getSupportFragmentManager(), "addNewItem");
    }

    //Refreshes the recycler view
    public void onItemsChanged() {
        ItemRecyclerViewAdapter itemRecyclerViewAdapter = new
                ItemRecyclerViewAdapter(Items.getInstance(getApplicationContext()).getItemArray(currentList));
        recyclerView.setAdapter(itemRecyclerViewAdapter);
    }

    //Changes which list is displayed
    private boolean onNavigationItemSelected(MenuItem item) {
        Item.List previousList = currentList;
        switch (item.getItemId()) {
            case R.id.navigation_todo:
                currentList = Item.List.TODO;
                break;
            case R.id.navigation_working_on:
                currentList = Item.List.WORKING_ON;
                break;
            case R.id.navigation_done:
                currentList = Item.List.DONE;
                break;
        }
        if (previousList != currentList) {
            onItemsChanged();
        }
        return true;
    }

    public static MainActivity getInstance() {
        return instance;
    }

    //Called when a item in the recycler view is clicked
    //Opens a dialog to edit the items text
    public void onItemClick(Item item) {
        Log.d("TAG", "onItemClick: TEst");
        EditItemDialogFragment editItemDialogFragment = new EditItemDialogFragment(item);
        editItemDialogFragment.show(getSupportFragmentManager(), "editItem");
    }

    public void onItemLongClick(Item item) {
        MoveItemDialogFragment moveItemDialogFragment = new MoveItemDialogFragment(item);
        moveItemDialogFragment.show(getSupportFragmentManager(), "moveItem");
    }

    public Item.List getCurrentList() {
        return currentList;
    }
}