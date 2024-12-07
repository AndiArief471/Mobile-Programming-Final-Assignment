package com.example.mobileprogfinal;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.ArrayList;

public class SalesPage extends AppCompatActivity {
    TextView searchItem, warehouseBtn, storeBtn, userNameText;
    ArrayList<String> arrayList;
    Dialog dialog;



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
        String userName = getIntent().getStringExtra("UserName");
        String userEmail = getIntent().getStringExtra("UserEmail");

        searchItem = findViewById(R.id.searchItem);
        warehouseBtn = findViewById(R.id.warehouseBtn);
        userNameText = findViewById(R.id.userNameText);
        storeBtn = findViewById(R.id.storeBtn);

        arrayList = new ArrayList<>();

        arrayList.add("Apple");
        arrayList.add("banana");
        arrayList.add("Cherry");
        arrayList.add("Dragon Fruit");

        userNameText.setText(userName);

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
}