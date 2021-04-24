package com.example.finder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.text.Editable;
import android.text.TextWatcher;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText usernameText, passwordText;
    private SharedPreferences sp,sp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp = getSharedPreferences("login", MODE_PRIVATE);
        sp2 = getSharedPreferences("username", MODE_PRIVATE);
        if(sp.getBoolean("logged",true) == true)
        {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        usernameText = (EditText) findViewById(R.id.username);
        passwordText = (EditText) findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
    }

    public void login(View view) {
        if(dataIsCorrect() == true) {
            mAuth.signInWithEmailAndPassword(usernameText.getText().toString(), passwordText.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                sp.edit().putBoolean("logged",true).apply();
                                sp2.edit().putString("username", usernameText.getText().toString()).apply();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else
            Toast.makeText(LoginActivity.this, "Wrong credentials!",
                    Toast.LENGTH_SHORT).show();
    }

    public void register(View view) {
        if(dataIsCorrect() == true) {
            mAuth.createUserWithEmailAndPassword(((EditText)findViewById(R.id.username)).getText().toString(), ((EditText)findViewById(R.id.password)).getText().toString())
                    .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                sp.edit().putBoolean("logged",true).apply();
                                sp2.edit().putString("username", usernameText.getText().toString()).apply();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Register failed." + ((EditText)findViewById(R.id.username)).getText().toString()+ ((EditText)findViewById(R.id.password)).getText().toString(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else
            Toast.makeText(LoginActivity.this, "Wrong credentials!",
                Toast.LENGTH_SHORT).show();
    }

    private boolean dataIsCorrect() {
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

}