package com.example.myapplication.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.User;
import com.example.myapplication.retrofit.RetrofitService;
import com.example.myapplication.retrofit.UserApi;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        initializeComponents();

        linkSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent = new Intent(LoginScreen.this, SignupScreen.class);
                startActivity(signupIntent);
            }
        });
    }

    private void initializeComponents() {
        final EditText etEmail = findViewById(R.id.etEmail);
        final EditText etPassword = findViewById(R.id.etPassword);
        Button buttonLogin = findViewById(R.id.btnLogin);

        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

        buttonLogin.setOnClickListener(view -> {
            String email = String.valueOf(etEmail.getText());
            String password = String.valueOf(etPassword.getText());

            // Email validation
            if (!isValidEmail(email)) {
                etEmail.setError("Invalid email address");
                return;
            }
            User user = new User();
            user.setEmail(email);
            user.setPassword(password);

            // Call the login API
            userApi.signin(user)
                    .enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                // Login successful
                                Toast.makeText(LoginScreen.this, "Login successful!", Toast.LENGTH_SHORT).show();
                            } else {
                                // Login failed
                                Toast.makeText(LoginScreen.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            // Handle failure
                            Toast.makeText(LoginScreen.this, "Login failed!", Toast.LENGTH_SHORT).show();
                            Logger.getLogger(LoginScreen.class.getName()).log(Level.SEVERE, "An error occurred", t);
                        }
                    });
        });
    }

    private boolean isValidEmail(String email) {
        // You can add your email validation logic here
        // For a simple check, you can use android.util.Patterns.EMAIL_ADDRESS
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


}