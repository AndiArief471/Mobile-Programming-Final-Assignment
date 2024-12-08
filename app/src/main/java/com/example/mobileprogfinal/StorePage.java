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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class StorePage extends AppCompatActivity implements RecyclerViewInterface {
    TextView addItemBtn;
    RecyclerView recyclerView;
    FirebaseFirestore db;
    List<ItemList> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_store_page);
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
                Intent openStoreAddItem = new Intent(StorePage.this, StoreAddItem.class);
                openStoreAddItem.putExtra("UserEmail", userEmail);
                startActivity(openStoreAddItem);
            }
        });
    }

    private void fetchItemData(List<ItemList> itemList, String userEmail) {
        db.collection("Credentials").document(userEmail).collection("Store")
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

        recyclerView.setAdapter(new ItemAdapter(StorePage.this, itemLists, StorePage.this));
        recyclerView.setLayoutManager(new LinearLayoutManager(StorePage.this));

    }

    @Override
    public void onItemClick(int position) {
        String userEmail = getIntent().getStringExtra("UserEmail");

        Intent openStoreEditItem = new Intent(StorePage.this, StoreEditItem.class);
        openStoreEditItem.putExtra("ItemName", items.get(position).getName());
        openStoreEditItem.putExtra("ItemQuantity", items.get(position).getQuantity());
        openStoreEditItem.putExtra("UserEmail", userEmail);

        startActivity(openStoreEditItem);
    }
}