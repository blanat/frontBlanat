package com.example.myapplication.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
                Intent signupIntent = new Intent(SignupScreen.this, navActivity.class);
                startActivity(signupIntent);
            }
        });
    }

    private void initializeComponents() {
        final EditText etName = findViewById(R.id.etName);
        final EditText etEmail = findViewById(R.id.etEmail);
        final EditText etPassword = findViewById(R.id.etPassword);
        final EditText etPasswordVerif = findViewById(R.id.etPassword2);
        Button buttonSignup = findViewById(R.id.btnSignup);

        RetrofitService retrofitService = new RetrofitService(this);
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

        buttonSignup.setOnClickListener(view -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String passwordVerif = etPasswordVerif.getText().toString().trim();


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
                            if (response.isSuccessful()) {
                                // Parse the response body to get the JWT token
                                String responseBody = response.body().toString();
                                String jwtToken = parseJwtTokenFromResponse(responseBody);

                                // Store the JWT token using SharedPreferences
                                saveJwtTokenToSharedPreferences(jwtToken);

                                Intent intent = new Intent(SignupScreen.this, CreateDActivity.class);
                                startActivity(intent);
                                //Toast.makeText(SignupScreen.this, "Enregistrement réussi!", Toast.LENGTH_SHORT).show();
                            } else {
                                // Handle unsuccessful response
                                Toast.makeText(SignupScreen.this, "Échec de l'enregistrement!!!", Toast.LENGTH_SHORT).show();
                            }
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
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Password validation method
    private boolean isValidPassword(String password, String passwordVerif) {
        return password.equals(passwordVerif);
    }

    // Method to parse JWT token from the response body
    private String parseJwtTokenFromResponse(String responseBody) {
        // Implement your logic to extract the JWT token from the response body
        // This will depend on the format of the response from your server
        // For example, if the token is in a JSON field named "token", you can use a JSON parser
        // Replace this with your actual logic
        return responseBody;
    }

    // Method to save JWT token to SharedPreferences
    private void saveJwtTokenToSharedPreferences(String jwtToken) {
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("jwtToken", jwtToken);
        editor.apply();
    }
}
