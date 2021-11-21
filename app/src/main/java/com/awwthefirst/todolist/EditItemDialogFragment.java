package com.awwthefirst.todolist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

public class EditItemDialogFragment extends DialogFragment {

    private Item existingItem;

    public EditItemDialogFragment(Item existingItem) {
        this.existingItem = existingItem;
    }
    public EditItemDialogFragment() {
        this.existingItem = null;
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_item, null);

        dialogBuilder.setView(view);

        EditText input = view.findViewById(R.id.item_text_input);

        if (existingItem == null) {
            dialogBuilder.setTitle(R.string.add_new_item_title);
            dialogBuilder.setPositiveButton(R.string.comfirm_text, (dialog, which) ->
                    createItem(dialog, which, input));
        } else {
            dialogBuilder.setTitle(R.string.edit_item_title);
            input.setText(existingItem.text);
            dialogBuilder.setPositiveButton(R.string.comfirm_text, (dialog, which) ->
                    editItem(dialog, which, input));
        }

        dialogBuilder.setNegativeButton(R.string.cancel_text, (dialog, which) -> dialog.cancel());

        return dialogBuilder.create();
    }

    /*Called when the positive button is pressed. Creates a new item calls
     MainActivity.onItemsChanged to refresh the recycler view.
     */
    private void createItem(DialogInterface dialog, int which, EditText input) {
        String text = input.getText().toString();
        if (!text.isEmpty()) {
            Item item = new Item(MainActivity.getInstance().getCurrentList(), text);
            Items.getInstance(getContext()).addItem(item);
            MainActivity.getInstance().onItemsChanged();
        }
    }

    private void editItem(DialogInterface dialog, int which, EditText input) {
        existingItem.text = input.getText().toString();
        MainActivity.getInstance().onItemsChanged();
    }
}
