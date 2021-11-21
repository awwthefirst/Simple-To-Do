package com.awwthefirst.todolist.recyclerview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.awwthefirst.todolist.EditItemDialogFragment;
import com.awwthefirst.todolist.Item;
import com.awwthefirst.todolist.MainActivity;
import com.awwthefirst.todolist.R;

import org.jetbrains.annotations.NotNull;

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter {
    private Item[] dataSet;

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView textView;
        private Item item;

        public ItemViewHolder(@NonNull @NotNull View view) {
            super(view);

            textView = (TextView) view.findViewById(R.id.textView);
            itemView.setOnClickListener(this);
            view.setOnLongClickListener(i -> {
                MainActivity.getInstance().onItemLongClick(item);
                return true;
            });
        }

        public void setItem(Item item) {
            this.item = item;
            textView.setText(item.text);
        }

        @Override
        public void onClick(View v) {
            MainActivity.getInstance().onItemClick(item);
        }
    }

    public ItemRecyclerViewAdapter(Item[] dataSet) {
        this.dataSet = dataSet;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_row_item, parent,
                false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder)holder).setItem(dataSet[position]);
    }

    @Override
    public int getItemCount() {
        return dataSet.length;
    }
}
