package com.example.mobileprogfinal;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

public class FrontPage extends AppCompatActivity {
    TextView signIn, signUp;
    Button test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_front_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        signIn = findViewById(R.id.signInbtn);
        signUp = findViewById(R.id.signUpbtn);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp.setBackground(getResources().getDrawable(R.drawable.border));
                signUp.setTextColor(Color.parseColor("#66068C"));

                signIn.setBackgroundColor(Color.parseColor("#66068C"));
                signIn.setTextColor(Color.parseColor("#FFFFFF"));


                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, SignIn.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn.setBackground(getResources().getDrawable(R.drawable.border));
                signIn.setTextColor(Color.parseColor("#66068C"));

                signUp.setBackgroundColor(Color.parseColor("#66068C"));
                signUp.setTextColor(Color.parseColor("#FFFFFF"));

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, SignUp.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();
            }
        });
    }
}