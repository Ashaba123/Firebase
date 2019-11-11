package com.learn.firebaseauthentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private TextView userEmail;
    private Button btnLogOut;

    private DatabaseReference databaseReference;
    private EditText etNames, etAddress;
    private Button btnSave;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        InitUi();

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();
        userEmail.setText(getString(R.string.welcome,user.getEmail()));


    }

    private void InitUi() {

        databaseReference = FirebaseDatabase.getInstance().getReference();
        etAddress =findViewById(R.id.etAddress);
        etNames =findViewById(R.id.etNames);
        btnSave =findViewById(R.id.btnSave);


        firebaseAuth = FirebaseAuth.getInstance();
        userEmail = findViewById(R.id.tvUserEmail);
        btnLogOut = findViewById(R.id.btnLogout);

        btnLogOut.setOnClickListener(LogOut());
        btnSave.setOnClickListener(SaveInfo());
    }

    private View.OnClickListener SaveInfo() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             saveUserInformation();
             clearEditText();


            }
        };
    }

    private void clearEditText() {
        etNames.getText().clear();
        etAddress.getText().clear();
    }

    private void saveUserInformation() {

        String name = etNames.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        UserInformation userInformation = new UserInformation(name,address);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null) {
        databaseReference.child(user.getUid()).setValue(userInformation);

        Toast.makeText(getApplicationContext(), "Information saved", Toast.LENGTH_LONG).show();

        }else{
            Log.d("TAG","user is null");
        }


    }

    private View.OnClickListener LogOut() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class
                ));

            }
        };
    }


}
