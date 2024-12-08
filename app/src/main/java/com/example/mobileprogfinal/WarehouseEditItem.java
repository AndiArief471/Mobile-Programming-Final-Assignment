package com.example.mobileprogfinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class WarehouseEditItem extends AppCompatActivity {
    TextView editItemBtn, deleteItemBtn;
    EditText itemName, itemQty;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_warehouse_edit_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String ItemName = getIntent().getStringExtra("ItemName");
        String ItemQuantity = getIntent().getStringExtra("ItemQuantity");
        String userEmail = getIntent().getStringExtra("UserEmail");

        itemName = findViewById(R.id.itemName);
        itemQty = findViewById(R.id.itemQty);
        editItemBtn = findViewById(R.id.editItemBtn);
        deleteItemBtn = findViewById(R.id.deleteItemBtn);

        db = FirebaseFirestore.getInstance();

        itemName.setText(ItemName);
        itemQty.setText(ItemQuantity);

        editItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatedName = itemName.getText().toString().toLowerCase();
                String updatedQty = itemQty.getText().toString();

                if(updatedName.equals(ItemName)){
                    if(updatedQty.equals(ItemQuantity)){
                        Toast.makeText(WarehouseEditItem.this, "Field not changed", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        db.collection("Credentials").document(userEmail)
                                .collection("Warehouse").document(ItemName).
                                update("quantity", updatedQty);
                    }
                }
                else{
                    checkStoreItemExist(userEmail, ItemName, updatedName);
                    addItem(userEmail, updatedName, updatedQty, "Warehouse");
                }

                changeActivity(userEmail);
            }
        });

        deleteItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItemDoc(userEmail, ItemName);
                changeActivity(userEmail);
            }
        });
    }

    public void changeActivity(String userEmail){
        Intent openWarehousePage = new Intent(WarehouseEditItem.this, WarehousePage.class);
        openWarehousePage.putExtra("UserEmail", userEmail);
        openWarehousePage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(openWarehousePage);
        finish();
    }

    public void deleteItemDoc(String userEmail, String itemName){
        db.collection("Credentials").document(userEmail)
                .collection("Warehouse").document(itemName).delete();

        db.collection("Credentials").document(userEmail)
                .collection("Store").document(itemName).delete();
    }

    public void checkStoreItemExist(String userEmail, String itemName, String updatedItemName){
        DocumentReference doc1 = db.collection("Credentials").document(userEmail);
        DocumentReference doc2 = doc1.collection("Store").document(itemName);
        doc2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String quantity = document.getString("quantity");

                        addItem(userEmail, updatedItemName, quantity, "Store");

                        deleteItemDoc(userEmail, itemName);
                    }
                }
            }
        });
    }

    public void addItem(String userEmail, String itemName, String itemQty, String storageType){
        ItemList item = new ItemList();

        item.setName(itemName);
        item.setQuantity(itemQty);

        db.collection("Credentials").document(userEmail)
                .collection(storageType).document(itemName).set(item);
    }
}