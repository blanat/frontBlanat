package com.example.myapplication.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.model.User;
import com.example.myapplication.retrofit.RetrofitService;
import com.example.myapplication.retrofit.UserApi;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);
        final TextView linkLogIn = findViewById(R.id.linkLogIn);
        initializeComponents();

        linkLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent = new Intent(SignupScreen.this, LoginScreen.class);
                startActivity(signupIntent);
            }
        });
    }

    private void initializeComponents() {
        final EditText etName = findViewById(R.id.etName);
        final EditText etEmail = findViewById(R.id.etEmail);
        final EditText etPassword = findViewById(R.id.etPassword);
        final EditText etPasswordVerif = findViewById(R.id.etPassword2);
        Button buttonLogin = findViewById(R.id.btnSignup);

        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

        buttonLogin.setOnClickListener(view -> {
            String name = String.valueOf(etName.getText());
            String email = String.valueOf(etEmail.getText());
            String password = String.valueOf(etPassword.getText());
            String passwordVerif = String.valueOf(etPasswordVerif.getText());

            // Email validation
            if (!isValidEmail(email)) {
                etEmail.setError("Invalid email address");
                return;
            }

            // Password validation
            if (!isValidPassword(password, passwordVerif)) {
                etPassword.setError("Passwords do not match");
                etPasswordVerif.setError("Passwords do not match");
                return;
            }

            User user = new User();
            user.setUserName(name);
            user.setEmail(email);
            user.setPassword(password);

            userApi.signup(user)
                    .enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            Toast.makeText(SignupScreen.this, "Enregistrement réussi!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(SignupScreen.this, "Échec de l'enregistrement!!!", Toast.LENGTH_SHORT).show();
                            Logger.getLogger(SignupScreen.class.getName()).log(Level.SEVERE, "Une erreur s'est produite", t);
                        }
                    });
        });
    }

    // Email validation method
    private boolean isValidEmail(String email) {
        // You can add your email validation logic here
        // For a simple check, you can use android.util.Patterns.EMAIL_ADDRESS
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Password validation method
    private boolean isValidPassword(String password, String passwordVerif) {
        // You can add your password validation logic here
        return password.equals(passwordVerif);
    }

}