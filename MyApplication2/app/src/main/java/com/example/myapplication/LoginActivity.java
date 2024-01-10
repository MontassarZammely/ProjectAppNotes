package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText;
    EditText passwordEditText;
    Button loginBtn;
    ProgressBar progressBar;
    TextView CreateAccountButtonTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id. email);
        passwordEditText = findViewById(R.id. password);
        loginBtn = findViewById(R.id. Loginbtnid);
        progressBar = findViewById(R.id. progressbar);
        CreateAccountButtonTextView = findViewById(R.id. createaccountid);

        loginBtn.setOnClickListener((v) -> loginUser());
        CreateAccountButtonTextView.setOnClickListener((v) -> startActivity(new Intent(LoginActivity.this,SignUpActivity.class)));


    }

    void loginUser(){
        String email=emailEditText.getText().toString();
        String password=passwordEditText.getText().toString();
        boolean validation=validateData(email,password);

        if (!validation) {
            return;
        }

        loginInFireBase(email,password);

    }

    void loginInFireBase(String email,String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        InCharging(true);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                InCharging(false);
                if (task.isSuccessful()){
                    // login success
                    startActivity(new Intent(  LoginActivity. this, MainActivity.class));
                    finish();
                }else {
                    // login failed
                    Toast.makeText(LoginActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();

                }

            }
        });
    }
    void InCharging(boolean InProgress){
        if(!InProgress){
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }


    boolean validateData(String email,String password){
        // validation des donnees email et password

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("email invalide");
            return false;
        }
        if(password.length()<8){
            passwordEditText.setError("password requires 8 chars");
            return false ;
        }
        return true;
    }
}