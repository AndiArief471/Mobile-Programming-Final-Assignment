package com.example.mobileprogfinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

public class StoreEditItem extends AppCompatActivity {
    TextView editItemBtn;
    EditText itemName, itemQty;
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

        db = FirebaseFirestore.getInstance();

        itemName.setText(ItemName);
        itemQty.setText(ItemQuantity);

        editItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openStorePage = new Intent(StoreEditItem.this, StorePage.class);
                openStorePage.putExtra("UserEmail", userEmail);
                openStorePage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(openStorePage);
                finish();
            }
        });
    }
}