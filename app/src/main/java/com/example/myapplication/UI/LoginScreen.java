package com.example.myapplication.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

public class LoginScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);


        final EditText editTextEmail = findViewById(R.id.etEmail);
        final EditText editTextPassword = findViewById(R.id.etPassword);
        Button buttonLogin = findViewById(R.id.btnLogin);
        final TextView linkSignUp = findViewById(R.id.linkSignUp);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                Intent intent = new Intent(LoginScreen.this, MainActivity.class);

                // Optionally, you can pass data to the next activity using extras
                intent.putExtra("email", email);
                intent.putExtra("password", password);

                // Start the next activity
                startActivity(intent);
            }
        });

        linkSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent = new Intent(LoginScreen.this, SignupScreen.class);
                startActivity(signupIntent);
            }
        });
    }
}