package com.example.myapplication.UI.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;

public class Profile extends AppCompatActivity {
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();

        if (intent.hasExtra("email") && intent.hasExtra("password")) {
            email = intent.getStringExtra("email");
            password = intent.getStringExtra("password");
        }

        final TextView paramsLink = findViewById(R.id.paramId);

        paramsLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent = new Intent(Profile.this, Parameter.class);
                signupIntent.putExtra("email", email);
                signupIntent.putExtra("password", password);
                startActivity(signupIntent);
            }
        });
    }
}