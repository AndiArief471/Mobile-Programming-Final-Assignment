package com.example.mobileprogfinal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class WarehousePage extends AppCompatActivity implements RecyclerViewInterface{
    TextView addItemBtn;
    RecyclerView recyclerView;
    FirebaseFirestore db;
    List<ItemList> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_warehouse_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String userEmail = getIntent().getStringExtra("UserEmail");

        addItemBtn = findViewById(R.id.addItemBtn);
        recyclerView = findViewById(R.id.recyclerView);

        db = FirebaseFirestore.getInstance();

        fetchItemData(items, userEmail);

        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openStoreAddItem = new Intent(WarehousePage.this, WarehouseAddItem.class);
                openStoreAddItem.putExtra("UserEmail", userEmail);
                startActivity(openStoreAddItem);
            }
        });
    }

    private void fetchItemData(List<ItemList> itemList, String userEmail) {
        db.collection("Credentials").document(userEmail).collection("Warehouse")
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        ItemList item = document.toObject(ItemList.class);
                        if (item != null) {
                            itemList.add(item);
                        }
                    }
                    handleItemList(items);
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching documents", e));
    }

    private void handleItemList(List<ItemList> itemLists) {

        recyclerView.setAdapter(new ItemAdapter(WarehousePage.this, itemLists, WarehousePage.this));
        recyclerView.setLayoutManager(new LinearLayoutManager(WarehousePage.this));

    }

    @Override
    public void onItemClick(int position) {
        String userEmail = getIntent().getStringExtra("UserEmail");

        Intent openWarehouseEditItem = new Intent(WarehousePage.this, WarehouseEditItem.class);
        openWarehouseEditItem.putExtra("ItemName", items.get(position).getName());
        openWarehouseEditItem.putExtra("ItemQuantity", items.get(position).getQuantity());
        openWarehouseEditItem.putExtra("UserEmail", userEmail);

        startActivity(openWarehouseEditItem);
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(WarehousePage.this, SalesPage.class));
        finish();
    }
}