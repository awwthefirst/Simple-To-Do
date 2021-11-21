package com.awwthefirst.todolist;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Item {
    public enum List {
        TODO,
        WORKING_ON,
        DONE
    }

    public List list;
    public String text;

    public Item(List list, String text) {
        this.list = list;
        this.text = text;
    }

    //Gets the corresponding List from a string
    private static List stringToList(String string) {
        switch (string) {
            case "TODO":
                return List.TODO;
            case "WORKING_ON":
                return List.WORKING_ON;
            case "DONE":
                return List.DONE;
        }
        return null;
    }

    //Returns a JsonObject which represents this Item
    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", list.toString());
        jsonObject.put("text", text);
        return jsonObject;
    }

    //Creates a Item from the data in the entered JSONObject
    public static Item fromJson(JSONObject itemJsonObject) throws JSONException {
        List list = stringToList(itemJsonObject.getString("list"));
        String text = itemJsonObject.getString("text");
        Item item = new Item(list, text);
        return item;
    }
}
