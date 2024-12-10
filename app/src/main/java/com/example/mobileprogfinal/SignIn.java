package com.example.mobileprogfinal;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignIn#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignIn extends Fragment {
    TextView loginBtn;
    EditText emailField, passwordField;

    FirebaseFirestore db;
    FirebaseAuth auth;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignIn() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignIn.
     */
    // TODO: Rename and change types and number of parameters
    public static SignIn newInstance(String param1, String param2) {
        SignIn fragment = new SignIn();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);

        loginBtn = v.findViewById(R.id.loginBtn);
        emailField = v.findViewById(R.id.emailField);
        passwordField = v.findViewById(R.id.passwordField);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(getContext(), "Input username and password", Toast.LENGTH_SHORT).show();
                }
                else{
                    userLogin(email, password);
                }
            }
        });

        return v;
    }

    private void userLogin(String email, String password){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    DocumentReference doc = db.collection("Credentials").document(email);

                    doc.get().addOnCompleteListener(tasks -> {
                        DocumentSnapshot document = tasks.getResult();
                        String userName = document.getString("userName");
                        String userEmail = document.getString("userEmail");

                        Preferences.setUserEmail(getContext(), userEmail);
                        Preferences.setUserName(getContext(), userName);
                        Preferences.setStatusLogin(getContext(), true);

                        Intent openSalesPage = new Intent(getContext(), SalesPage.class);
//                        openSalesPage.putExtra("UserName", userName);
//                        openSalesPage.putExtra("UserEmail", userEmail);
                        startActivity(openSalesPage);
                        getActivity().finish();
                    });
                }
                else{
                    String fail = task.getException().getMessage();
                    Toast.makeText(getContext(), fail, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}