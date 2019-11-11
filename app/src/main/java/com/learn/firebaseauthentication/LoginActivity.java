package com.learn.firebaseauthentication;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG ="firebaselogin" ;
    private Button btnSignIn;
    private EditText emailEt;
    private EditText passwordEt;
    private TextView signUp;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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

        btnSignIn = findViewById(R.id.btnSignIn);
        emailEt= findViewById(R.id.email);
        passwordEt = findViewById(R.id.password);
        signUp = findViewById(R.id.textViewSignUp);
        progressBar =findViewById(R.id.progressBar_login);

        btnSignIn.setOnClickListener(this);
        signUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnSignIn){
            userLogin();
        }

        if(v== signUp){
            //close this activity
            finish();
            startActivity(new Intent(this, MainActivity.class));

        }
    }

    private void userLogin() {
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


        //loading
          progressBar.setVisibility(View.VISIBLE);

        //sign in
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.INVISIBLE);

                        if(task.isSuccessful()){
                            //start profile activity
                            finish();
                            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                            Log.w(TAG, "Login:success", task.getException());

                        }else{
                            Log.w(TAG, "Login:Failed", task.getException());
                        }
                    }
                });


    }
}
