package com.example.mobileprogfinal;

import android.content.Intent;
import android.os.Bundle;
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

public class StoreEditItem extends AppCompatActivity {
    TextView editItemBtn, deleteItemBtn, itemName;
    EditText itemQty;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_store_edit_item);
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

        // Haven't implement the addition of item/product if the edited quantity is lower than before
        editItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatedQty = itemQty.getText().toString();

                if(updatedQty.equals(ItemQuantity)){
                    Toast.makeText(StoreEditItem.this, "Quantity not changed", Toast.LENGTH_SHORT).show();
                }
                else{
                    db.collection("Credentials").document(userEmail)
                            .collection("Store").document(ItemName).
                            update("quantity", updatedQty);

                    changeActivity(userEmail);
                }
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

    public void deleteItemDoc(String userEmail, String itemName){
        db.collection("Credentials").document(userEmail)
                .collection("Store").document(itemName).delete();
    }

    public void changeActivity(String userEmail){
        Intent openStorePage = new Intent(StoreEditItem.this, StorePage.class);
        openStorePage.putExtra("UserEmail", userEmail);
        openStorePage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(openStorePage);
        finish();
    }
}