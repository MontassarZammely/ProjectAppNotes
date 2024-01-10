package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    EditText emailEditText;
    EditText passwordEditText;
    EditText confirm_passwordEditText;
    Button createAccountBtn;
    ProgressBar progressBar;
    TextView loginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confirm_passwordEditText = findViewById(R.id.confirm_password);
        createAccountBtn = findViewById(R.id.createaccountbutton);
        progressBar = findViewById(R.id.progressbar);
        loginBtn = findViewById(R.id.loginid);

        createAccountBtn.setOnClickListener (v-> createAccount ());

        loginBtn.setOnClickListener(v-> finish());

    }

    void createAccount(){
        String email=emailEditText.getText().toString();
        String password=passwordEditText.getText().toString();
        String confirm_pass=confirm_passwordEditText.getText().toString();
        boolean validation=validateData(email,password,confirm_pass);

        if (!validation) {
            return;
        }

        SignUpInFireBase(email,password);


    }
    // connexion avec FireBase

    void SignUpInFireBase(String email, String password){
            InCharging(true);

        // instance men firebase bech najmou nesta3mlou method "createUserWithEmailAndPassword"
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        // listener bech ycpati soit succees or failed
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SignUpActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override // test with "task"
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        InCharging(false);
                        if (task.isSuccessful()){
                            // done
                            Toast.makeText(SignUpActivity.this,"Account Successfully created, Check email to verify",Toast.LENGTH_SHORT).show();
                                // envoyer email pour verification
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();
                        }else {
                            // not done

                            Toast.makeText(SignUpActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();

                        }
                    }
                }
        );
    }

    void InCharging(boolean InProgress){
        if(!InProgress){
            progressBar.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            createAccountBtn.setVisibility(View.VISIBLE);
        }
    }


    boolean validateData(String email,String password, String confirm_pass){
        // validation des donnees email et password

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("email invalide");
            return false;
        }
        if(password.length()<8){
            passwordEditText.setError("password requires 8 chars");
            return false ;
        }
        if (!password.equals(confirm_pass)){
            confirm_passwordEditText.setError("passwords does not match");
            return false;
        }
        return true;
    }


}