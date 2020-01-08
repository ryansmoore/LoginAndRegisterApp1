package com.example.loginandregisterapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class Register extends AppCompatActivity {

    EditText mFullName, mEmail, mPassword, mPhone;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth; //this is provided by the firebase to register the user
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName = findViewById(R.id.fullName);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mPhone = findViewById(R.id.phone);
        mRegisterBtn = findViewById(R.id.loginBtn);
        mLoginBtn = findViewById(R.id.createText);

        //getting the current instance of the database from the firebase to preform verious operations on the database
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        //check is user is already logged in or created that account
        if(fAuth.getCurrentUser() != null){                 //user is already logged in
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getting values of email and password
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                //validate data saved in email and password
                if(TextUtils.isEmpty(email)){               //checks if no email is entered
                    mEmail.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(password)){            //checks if no password is entered
                    mPassword.setError("Password is Required");
                    return;
                }
                if(password.length() < 6) {                 //checks if the password is less than 6
                    mPassword.setError("Password must be at least 6 characters");
                }

                //display the progress bar to the user
                progressBar.setVisibility(View.VISIBLE);

                //register user in Firebase
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){            //created the user successfully
                            Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }else{
                            Toast.makeText(Register.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });


        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

    }
}
