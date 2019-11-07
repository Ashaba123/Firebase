package com.learn.firebaseauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button registerBtn;
    private EditText emailEt;
    private EditText passwordEt;
    private TextView signIn;


    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntitUI();



    }

    private void IntitUI() {
       firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progress_circular);
        registerBtn = findViewById(R.id.btnRegister);
        emailEt= findViewById(R.id.email);
        passwordEt = findViewById(R.id.password);
        signIn = findViewById(R.id.textViewSignin);

        registerBtn.setOnClickListener(this);
        signIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == registerBtn){
            registerUser();
        }

        if(v== signIn){
            //open login activity
        }

    }

    private void registerUser() {
        String email = emailEt.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this,"Enter email",Toast.LENGTH_SHORT).show();

            //stop executing futher
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Enter password",Toast.LENGTH_SHORT).show();

            return;
        }

        //all validations are okay ,now proceed,
        // use a progress bar to wait for internet to do its thing
        progressBar.setVisibility(View.VISIBLE);



        //firebase adds user
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                   if(task.isSuccessful()){
                       //user registered
                       //open profile page
                       Toast.makeText(MainActivity.this,"Registered Succesfully",Toast.LENGTH_SHORT).show();
                       progressBar.setVisibility(View.INVISIBLE);

                   }else {

                       Toast.makeText(MainActivity.this,"Failed to Register, Try again",Toast.LENGTH_SHORT).show();
                   }

                    }
                });

    }
}
