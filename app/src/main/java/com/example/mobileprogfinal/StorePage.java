package com.example.mobileprogfinal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
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

public class StorePage extends AppCompatActivity implements RecyclerViewInterface{
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

        recyclerView = findViewById(R.id.recyclerView);
        db = FirebaseFirestore.getInstance();

        fetchItemData(items);

    }

    private void fetchItemData(List<ItemList> itemList) {
        db.collection("Credentials").document("arief@gmail.com").collection("Store")
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    for(DocumentSnapshot document: queryDocumentSnapshots){
                        ItemList item = document.toObject(ItemList.class);
                        if(item != null){
                            itemList.add(item);
                        }
                    }
                    handleItemList(items);
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching documents", e));
    }

    private void handleItemList(List<ItemList> itemLists) {

        recyclerView.setAdapter(new ItemAdapter(StorePage.this,itemLists, StorePage.this));
        recyclerView.setLayoutManager(new LinearLayoutManager(StorePage.this));

    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(StorePage.this,
                items.get(position).getName() + ", " + items.get(position).getQuantity(),
                Toast.LENGTH_SHORT).show();
    }
}


        //  Old Code
//        db.collection("Credentials").document("arief@gmail.com").collection("Store")
//                .get().addOnSuccessListener(queryDocumentSnapshots -> {
//                    for(DocumentSnapshot document: queryDocumentSnapshots){
//                        ItemList itemList = document.toObject(ItemList.class);
//                        if(itemList != null){
//                            items.add(itemList);
//                        }
//                    }
//                    handleItemList(items);
//
//                });

//        db.collection("Credentials").document("arief@gmail.com").collection("Store")
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        List<ItemList> items = new ArrayList<>();
//                        if(task.isSuccessful()){
//                            for(QueryDocumentSnapshot document : task.getResult()){
//                                String itemName = document.getString("name");
//                                String itemQty = document.getString("quantity");
//
//                                items.add(new ItemList(itemName, itemQty));
//
////                                Log.d("TestLog",document.getId() + " => " + document.getData());
////                                Log.d("TestLog", "Name = " + itemName + ", Qty = " +itemQty);
//                            }
//
//                            recyclerView.setAdapter(new ItemAdapter(StorePage.this,items, StorePage.this));
//                        }
//                    }
//                });