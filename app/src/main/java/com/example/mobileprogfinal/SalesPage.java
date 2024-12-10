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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SalesPage extends AppCompatActivity {
    TextView searchItem, sellBtn, warehouseBtn, storeBtn, userNameText;
    EditText itemQty;
    ArrayList<String> arrayList;
    Dialog dialog;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sales_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        String userName = getIntent().getStringExtra("UserName");
//        String userEmail = getIntent().getStringExtra("UserEmail");

        String userName = Preferences.getUserName(SalesPage.this);
        String userEmail = Preferences.getUserEmail(SalesPage.this);

        userNameText = findViewById(R.id.userNameText);
        searchItem = findViewById(R.id.searchItem);
        itemQty = findViewById(R.id.itemQty);
        sellBtn = findViewById(R.id.sellBtn);
        warehouseBtn = findViewById(R.id.warehouseBtn);
        storeBtn = findViewById(R.id.storeBtn);

        db = FirebaseFirestore.getInstance();

        userNameText.setText(userName);

        arrayList = new ArrayList<>();

        fetchItemData(userEmail);

        userNameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openUserPage = new Intent(SalesPage.this, UserPage.class);
                openUserPage.putExtra("UserEmail", userEmail);
                openUserPage.putExtra("UserName", userName);
                startActivity(openUserPage);
            }
        });

        searchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(SalesPage.this);
                dialog.setContentView(R.layout.dialog_searchable_spinner);

                dialog.getWindow().setLayout(1000, 1500);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();

                EditText editText = dialog.findViewById(R.id.editText);
                ListView listView = dialog.findViewById(R.id.listView);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(SalesPage.this, android.R.layout.simple_list_item_1, arrayList);
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
                        searchItem.setText((adapter.getItem(i)));
                        dialog.dismiss();;
                    }
                });
            }
        });

        sellBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ItemName = searchItem.getText().toString();
                String ItemQty = itemQty.getText().toString();

                if(!TextUtils.isEmpty(ItemName) || !TextUtils.isEmpty(ItemQty)){
                    DocumentReference doc = db.collection("Credentials")
                            .document(userEmail).collection("Store").document(ItemName);

                    doc.get().addOnCompleteListener(task -> {
                        DocumentSnapshot document = task.getResult();
                        String storeStock = document.getString("quantity");

                        int warehouseItemStock = Integer.parseInt(storeStock);
                        int soldStoreItem = Integer.parseInt(ItemQty);

                        if(soldStoreItem <= warehouseItemStock){
                            int updatedStock = warehouseItemStock - soldStoreItem;

                            doc.update("quantity", String.valueOf(updatedStock));

                            searchItem.setText("");
                            itemQty.setText("0");
                        }
                        else{
                            Toast.makeText(SalesPage.this, "Item quantity exceeds the available stock in the Store\nStore Stock : " + storeStock
                                    , Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else{
                    Toast.makeText(SalesPage.this, "Input Item name and quantity", Toast.LENGTH_LONG).show();
                }
            }
        });

        storeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openStorePage = new Intent(SalesPage.this, StorePage.class);
                openStorePage.putExtra("UserEmail", userEmail);
                startActivity(openStorePage);
            }
        });

        warehouseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openWarehousePage = new Intent(SalesPage.this, WarehousePage.class);
                openWarehousePage.putExtra("UserEmail", userEmail);
                startActivity(openWarehousePage);
            }
        });
    }

    private void fetchItemData(String userEmail) {
        db.collection("Credentials").document(userEmail).collection("Store")
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String documentName = document.getId();
                        arrayList.add(documentName);
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching documents", e));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Preferences.clearLoggedUser(SalesPage.this);
        startActivity(new Intent(SalesPage.this, FrontPage.class));
        finish();
    }
}