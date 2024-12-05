package com.example.mobileprogfinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    Context context;
    List<itemList> items;

    public ItemAdapter(Context context, List<itemList> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.itemName.setText(items.get(position).getName());
        holder.qty.setText(items.get(position).getQuantity());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
