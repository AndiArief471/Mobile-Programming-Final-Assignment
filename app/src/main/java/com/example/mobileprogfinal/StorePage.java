package com.example.mobileprogfinal;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class StorePage extends AppCompatActivity {
    TextView pageName;
    ArrayList<String> listItems = new ArrayList<String>();
    ListView listView;
    String fruitList [] = {"Apple", "Banana", "Cherry", "Dragon Fruit"};

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

        pageName = findViewById(R.id.textView3);
        listView = findViewById(R.id.listView);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fruitList);
        listView.setAdapter(arrayAdapter);

        listItems.add("Apple");
        listItems.add("Banana");
        listItems.add("Cherry");
        listItems.add("Dragon Fruit");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedItem = listView.getItemAtPosition(position).toString();
                Toast.makeText(getBaseContext(), "item = " + selectedItem, Toast.LENGTH_LONG).show();
            }
        });
//
    }
    public void testButton(View v){
        pageName.setText("Changed");
        Toast.makeText(StorePage.this, "Button Pressed", Toast.LENGTH_SHORT).show();
    }
}