package com.example.monika_chat_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button loginbutton;
    EditText email;
    EditText password;
    TextView signup;
    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginbutton = (Button) findViewById(R.id.loginbutton);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signup = (TextView)findViewById(R.id.signuptv);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null)
        {
            finish();
            startActivity(new Intent(LoginActivity.this, ChatArea.class));
        }

        loginbutton.setOnClickListener(this);
        signup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == loginbutton)
            loginUser();

        if (v == signup) {
            finish();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    private void loginUser() {
        String Email = email.getText().toString().trim();
        String Password = password.getText().toString().trim();

        if (TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password)) {
            Toast.makeText(LoginActivity.this, "Please enter the email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Logging in...Please wait");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if(task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(),"You are logged in successfully", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(LoginActivity.this, ChatArea.class));
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"Failed to login",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
