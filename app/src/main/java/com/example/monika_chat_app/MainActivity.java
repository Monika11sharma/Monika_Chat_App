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
import com.google.firebase.auth.FirebaseAuthException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button register;
    EditText email;
    EditText password;
    TextView loginoption;
    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register= (Button) findViewById(R.id.registerbutton);
        email= findViewById(R.id.email);
        password= findViewById(R.id.password);
        loginoption=(TextView)findViewById(R.id.logintv);

        progressDialog = new ProgressDialog(this);

        firebaseAuth= FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null)
        {
            finish();
            startActivity(new Intent(MainActivity.this, ChatArea.class));
        }

        register.setOnClickListener(this);
        loginoption.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == register)
            registerUser();

        if(v == loginoption) {
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
    }

    private void registerUser() {
        String Email = email.getText().toString().trim();
        String Password = password.getText().toString().trim();

        if (TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password))
        {    Toast.makeText(this,"Please enter the email and password",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Regitered Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(MainActivity.this, ChatArea.class));
                } else {
                    FirebaseAuthException e = (FirebaseAuthException )task.getException();
                    Toast.makeText(MainActivity.this, "Failed to register"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        });

    }
}
