package com.example.mobileprogfinal;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    TextView itemName, qty;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        itemName = itemView.findViewById(R.id.itemName);
        qty = itemView.findViewById(R.id.qty);
    }
}
