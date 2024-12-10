package com.example.mobileprogfinal;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

public class WarehouseAddItem extends AppCompatActivity {
    TextView addItemBtn;
    EditText itemName, itemQty;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_warehouse_add_item);
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

        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = itemName.getText().toString().toLowerCase();
                String qty = itemQty.getText().toString();

                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(qty)){
                    Toast.makeText(WarehouseAddItem.this, "Input item name and quantity", Toast.LENGTH_SHORT).show();
                }
                else{
                    addItem(name, qty);
                    Intent openWarehousePage = new Intent(WarehouseAddItem.this, WarehousePage.class);
                    openWarehousePage.putExtra("UserEmail", userEmail);
                    openWarehousePage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(openWarehousePage);
                    finish();
                }
            }
        });
    }

    public void addItem(String itemName, String itemQty){
        String userEmail = getIntent().getStringExtra("UserEmail");
        ItemList item = new ItemList();

        item.setName(itemName);
        item.setQuantity(itemQty);

        db.collection("Credentials").document(userEmail)
                .collection("Warehouse").document(itemName).set(item);
    }
}