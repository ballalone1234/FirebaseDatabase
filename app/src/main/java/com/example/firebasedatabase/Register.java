package com.example.firebasedatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    private EditText inputEmailReg, inputPasswordReg;
    private Button btnRegister;
    private ProgressBar progressBarReg;
    private FirebaseAuth authReg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setTitle("Register B6301095");
// Define ColorDrawable object and parse color
// using parseColor method
// with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#5bf03a"));
// Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
        authReg = FirebaseAuth.getInstance();
        btnRegister = (Button) findViewById(R.id.reg_btn);
        inputEmailReg = (EditText) findViewById(R.id.editTextTextEmailAddressR);
        inputPasswordReg = (EditText) findViewById(R.id.editTextTextPasswordR);
        progressBarReg = (ProgressBar) findViewById(R.id.progressBarReg);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmailReg.getText().toString().trim();
                String password = inputPasswordReg.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBarReg.setVisibility(View.VISIBLE);
                authReg.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(Register.this, "createUserWithEmail:onComplete:" + task.isSuccessful(),
                                        Toast.LENGTH_SHORT).show();
                                progressBarReg.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),
                                                    "Registration successful!",
                                                    Toast.LENGTH_LONG)
                                            .show();
// hide the progress bar
                                    progressBarReg.setVisibility(View.GONE);
// if the user created intent to login activity
//                                    Intent intent
//                                            = new Intent(Register.this,
//                                            HomeActivity.class);
//                                    startActivity(intent);
                                }
                                else {
// Registration failed
                                    Toast.makeText(
                                                    getApplicationContext(),
                                                    "Registration failed!!"
                                                            + " Please try again later",
                                                    Toast.LENGTH_LONG)
                                            .show();
// hide the progress bar
                                    progressBarReg.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });
    }
}
