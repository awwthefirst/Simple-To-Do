package com.awwthefirst.todolist;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Items {

    private static Items instance;

    private final ArrayList<Item> toDoList, workingOnList, doneList;
    private static Context currentContext;

    private Items() {
        toDoList = new ArrayList<>();
        workingOnList = new ArrayList<>();
        doneList = new ArrayList<>();

        try {
            loadLists();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Items getInstance(Context context) {
        currentContext = context;

        if (instance == null) {
            instance = new Items();
        }

        return instance;
    }

    //Adds the item to the correct list and saves it to disk
    public void addItem(Item item) {
        getCorrespondingList(item.list).add(item);
        saveItem(item);
    }

    //Returns a array from the the corresponding ArrayList
    public Item[] getItemArray(Item.List list) {
        ArrayList<Item> items = getCorrespondingList(list);
        return items.toArray(new Item[items.size()]);
    }

    //Moves the entered item to the the entered list
    public void moveItem(Item item, Item.List newList) {
        deleteItem(item);
        item.list = newList;
        addItem(item);
    }

    //Removes the entered item from it's list and deletes it from disk
    public void deleteItem(Item item) {
        getCorrespondingList(item.list).remove(item);
        String fileName = item.list + item.text + ".json";
        currentContext.deleteFile(fileName);
        MainActivity.getInstance().onItemsChanged();
    }

    //Returns the corresponding ArrayList
    private ArrayList<Item> getCorrespondingList(Item.List list) {
        switch (list) {
            case TODO:
                return toDoList;
            case WORKING_ON:
                return workingOnList;
            case DONE:
                return doneList;
        }
        return null;
    }

    //Loads all Items which are saved to disk
    private void loadLists() throws FileNotFoundException {
        String[] fileNames = currentContext.fileList();
        for (String fileName : fileNames) {
            FileInputStream fis = currentContext.openFileInput(fileName);
            InputStreamReader inputStreamReader =
                    new InputStreamReader(fis, StandardCharsets.UTF_8);
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String line = reader.readLine();
                while (line != null) {
                    stringBuilder.append(line).append('\n');
                    line = reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                String contents = stringBuilder.toString();
                try {
                    JSONObject itemJsonObject = new JSONObject(contents);
                    Item item = Item.fromJson(itemJsonObject);
                    getCorrespondingList(item.list).add(item);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //Saves the entered Item to disk
    private void saveItem(Item item) {
        JSONObject jsonObject = null;
        try {
            jsonObject = item.toJson();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String fileName = item.list + item.text + ".json";
        try (FileOutputStream fos = currentContext.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            fos.write(jsonObject.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
