package com.example.mobileprogfinal;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class StoreAddItem extends AppCompatActivity {
    TextView itemName, addItemBtn;
    EditText itemQty;
    FirebaseFirestore db;
    ArrayList<String> itemDoc;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_store_add_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String userEmail = getIntent().getStringExtra("UserEmail");

        itemName = findViewById(R.id.itemName);
        itemQty = findViewById(R.id.itemQty);
        addItemBtn = findViewById(R.id.addItemBtn);

        db = FirebaseFirestore.getInstance();

        itemDoc = new ArrayList<>();

        fetchItemData();

        itemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(StoreAddItem.this);
                dialog.setContentView(R.layout.dialog_searchable_spinner);

                dialog.getWindow().setLayout(1000, 1500);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();

                EditText editText = dialog.findViewById(R.id.editText);
                ListView listView = dialog.findViewById(R.id.listView);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(StoreAddItem.this, android.R.layout.simple_list_item_1, itemDoc);
                listView.setAdapter(adapter);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        adapter.getFilter().filter(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        itemName.setText((adapter.getItem(i)));
                        dialog.dismiss();;
                    }
                });
            }
        });

        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = itemName.getText().toString();
                String qty = itemQty.getText().toString();

                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(qty)){
                    Toast.makeText(StoreAddItem.this, "Input item name and quantity", Toast.LENGTH_SHORT).show();
                }
                else{
                    addItem(userEmail, name, qty);
                }
            }
        });
    }

    public void addItem(String userEmail, String itemName, String itemQty){
        DocumentReference doc = db.collection("Credentials").document(userEmail).collection("Warehouse").document(itemName);

        doc.get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            String warehouseStock = document.getString("quantity");
            Log.d("TestLog", "warehouse stock : " + warehouseStock
                    + "\nuser email : " + userEmail);

            int warehouseItemStock = Integer.parseInt(warehouseStock);
            int addStoreItem = Integer.parseInt(itemQty);

            if(addStoreItem <= warehouseItemStock){
                int updatedStock = warehouseItemStock - addStoreItem;

                doc.update("quantity", String.valueOf(updatedStock));

                addItemToStore(userEmail, itemName, itemQty);

                Intent openStorePage = new Intent(StoreAddItem.this, StorePage.class);
                openStorePage.putExtra("UserEmail", userEmail);
                openStorePage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(openStorePage);
                finish();
            }
            else{
                Toast.makeText(StoreAddItem.this, "Item quantity exceeds the available stock in the warehouse\nWarehouse Stock : " + warehouseStock
                        , Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fetchItemData() {
        String userEmail = getIntent().getStringExtra("UserEmail");
        db.collection("Credentials").document(userEmail).collection("Warehouse")
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String documentName = document.getId();
                        itemDoc.add(documentName);
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching documents", e));
    }

    public void addItemToStore(String userEmail, String itemName, String itemQty){
        ItemList item = new ItemList();

        item.setName(itemName);
        item.setQuantity(itemQty);

        db.collection("Credentials").document(userEmail)
                .collection("Store").document(itemName).set(item);
    }
}