package com.example.monika_chat_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatArea extends AppCompatActivity {

    EditText editText;
    TextView textView;
    Button button, Logout;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessageDatabaseReference;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_area);

        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mMessageDatabaseReference = mFirebaseDatabase.getReference().child("messages");

        editText=findViewById(R.id.editText);
        textView=findViewById(R.id.textView);
        button=findViewById(R.id.button);
        Logout= findViewById(R.id.logout_button);

        firebaseAuth= FirebaseAuth.getInstance();

        if( firebaseAuth.getCurrentUser() == null ) {
            finish();
            startActivity( new Intent(getApplicationContext(), LoginActivity.class ));
        }

        FirebaseUser user= firebaseAuth.getCurrentUser();

        textView.setText(user.getEmail());

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(ChatArea.this, LoginActivity.class));
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=editText.getText().toString();
                editText.setText("");

                if(!TextUtils.isEmpty(msg))
                {
                    String id =mMessageDatabaseReference.push().getKey();
                    mMessageDatabaseReference.child(id).setValue(msg);
                    // Toast.makeText(this,"Message sent",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Empty message can't be sent", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
