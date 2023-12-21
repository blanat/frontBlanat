package com.example.myapplication.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.CreatDeals;
import com.example.myapplication.R;
import com.example.myapplication.model.JwtAuthenticationResponse;
import com.example.myapplication.model.User;
import com.example.myapplication.retrofit.RetrofitService;
import com.example.myapplication.retrofit.UserApi;

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

        final TextView linkSignUp = findViewById(R.id.linkSignUp);


        linkSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent = new Intent(LoginScreen.this, SignupScreen.class);
                startActivity(signupIntent);
            }
        });

        // Call the method to initialize components and set up the login logic
        initializeComponents();
    }

    private void initializeComponents() {
        final EditText etEmail = findViewById(R.id.etEmail);
        final EditText etPassword = findViewById(R.id.etPassword);
        Button buttonLogin = findViewById(R.id.btnLogin);

        RetrofitService retrofitService = new RetrofitService(this);
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

        buttonLogin.setOnClickListener(view -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Email validation
            if (!isValidEmail(email)) {
                etEmail.setError("Invalid email address");
                return;
            }
            User user = new User();
            user.setEmail(email);
            user.setPassword(password);

            // Call the login API
            userApi.signin(user).enqueue(new Callback<JwtAuthenticationResponse>() {
                @Override
                public void onResponse(Call<JwtAuthenticationResponse> call, Response<JwtAuthenticationResponse> response) {
                    if (response.isSuccessful()) {
                        JwtAuthenticationResponse jwtResponse = response.body();

                        // Extract the JWT token from the response
                        String jwtToken = extractTokenFromResponse(jwtResponse);
                        //Log.d("JwtToken", "Parsed JWT Token: " + jwtToken);

                        // Store the JWT token using SharedPreferences
                        saveToken(jwtToken);

                        Intent intent = new Intent(LoginScreen.this, CreatDeals.class);
                        // Optionally, you can pass data to the next activity using extras
                        intent.putExtra("email", email);
                        intent.putExtra("password", password);
                        startActivity(intent);
                    } else {
                        // Handle unsuccessful response
                        Toast.makeText(LoginScreen.this, "Échec de l'enregistrement!!!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JwtAuthenticationResponse> call, Throwable t) {
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

    // Method to parse JWT token from the response body
    private String parseJwtTokenFromResponse(String responseBody) {
        // Implement your logic to extract the JWT token from the response body
        // This will depend on the format of the response from your server
        // For example, if the token is in a JSON field named "token", you can use a JSON parser
        // Replace this with your actual logic
        return responseBody;
    }

    // Method to save JWT token to SharedPreferences
    private String extractTokenFromResponse(JwtAuthenticationResponse jwtResponse) {
        if (jwtResponse != null) {
            return jwtResponse.getToken();
        } else {
            Log.e("TokenExtraction", "Failed to extract token from response body: " + jwtResponse);
            return null;
        }
    }

    private void saveToken(String token) {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", token);
        editor.apply();
        Log.d("TokenDebug", "Token saved: " + token);
    }

}
