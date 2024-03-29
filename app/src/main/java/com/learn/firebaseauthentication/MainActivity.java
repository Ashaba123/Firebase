package com.learn.firebaseauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

    private static final String TAG ="firebase" ;
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

        //check if user is logged In
        if(firebaseAuth.getCurrentUser() != null){
            //start profile activity
            finish();
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));

        }

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
            startActivity(new Intent(this, LoginActivity.class));
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
        //Email and Passowrd Validations;
        if (passwordEt.getText().toString().length() < 6) {
            passwordEt.setError("password minimum contain 6 character");
            passwordEt.requestFocus();
            return;
        }
        if (passwordEt.getText().toString().equals("")) {
            passwordEt.setError("please enter password");
            passwordEt.requestFocus();
            return;
        }
        //Email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEt.setError("please enter valid email address");
            emailEt.requestFocus();
            return;
        }
        if (emailEt.getText().toString().equals("")) {
            emailEt.setError("please enter email address");
            emailEt.requestFocus();
            return;
        }


     /*   if (!emailEt.equals("") &&
                passwordEt.getText().toString().length() >= 6 &&
                !passwordEt.getText().toString().trim().equals("")
                && android.util.Patterns.EMAIL_ADDRESS.matcher(emailEt.getText().toString().trim()).matches()) {
            // do  your action
        }*/



        //All validations are okay ,now proceed,
        // use a progress bar to wait for internet to do its thing
        progressBar.setVisibility(View.VISIBLE);



        //firebase adds user
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressBar.setVisibility(View.INVISIBLE);

                        if (task.isSuccessful()) {
                            //user registered
                            //open profile page
                            Toast.makeText(MainActivity.this, "Registered Succesfully", Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "createUserWithEmail:success", task.getException());

                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));


                        } else {

                            Toast.makeText(MainActivity.this, "Failed to Register, Try again", Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "createUserWithEmail:success", task.getException());
                        }
                    }

                });

    }


}
