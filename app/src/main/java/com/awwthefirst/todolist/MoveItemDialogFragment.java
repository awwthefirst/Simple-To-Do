package com.awwthefirst.todolist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MoveItemDialogFragment extends DialogFragment {

    private Item item;
    private Item.List[] lists = {Item.List.TODO, Item.List.WORKING_ON, Item.List.DONE};
    private List<Item.List> moveTo;

    public MoveItemDialogFragment(Item item) {
        this.item = item;

        moveTo = new ArrayList(Arrays.asList(lists));
        moveTo.remove(item.list);
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle(R.string.move_item_title);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_move_item, null);
        dialogBuilder.setView(view);

        ((Button)view.findViewById(R.id.move_button_1)).setText(getButtonText(moveTo.get(0)));
        ((Button)view.findViewById(R.id.move_button_2)).setText(getButtonText(moveTo.get(1)));

        view.findViewById(R.id.move_button_1).setOnClickListener(i -> moveItem(moveTo.get(0)));
        view.findViewById(R.id.move_button_2).setOnClickListener(i -> moveItem(moveTo.get(1)));

        view.findViewById(R.id.delete_button).setOnClickListener(this::deleteItem);

        return dialogBuilder.create();
    }

    public void moveItem(Item.List list) {
        Items.getInstance(getContext()).moveItem(item, list);
        this.dismiss();
    }

    public void deleteItem(View view) {
        Items.getInstance(getContext()).deleteItem(item);
        this.dismiss();
    }

    //Returns the name of the button for the entered Item.List
    public String getButtonText(Item.List list) {
        String result = "Move To ";
        switch (list) {
            case TODO:
                return result + "To Do";
            case WORKING_ON:
                return result + "Working On";
            case DONE:
                return result + "Done";
        }
        return null;
    }
}
