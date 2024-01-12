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
import com.example.myapplication.model.JwtAuthenticationResponse;
import com.example.myapplication.model.User;
import com.example.myapplication.retrofit.RetrofitService;
import com.example.myapplication.retrofit.UserApi;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

            //password Validation
            if(!isValidPassword(password)){
                etPassword.setError("weak password");
                return;
            }

            // Password equality
            if (!passwordEquality(password, passwordVerif)) {
                etPassword.setError("Passwords do not match");
                etPasswordVerif.setError("Passwords do not match");
                return;
            }

            User user = new User();
            user.setUserName(name);
            user.setEmail(email);
            user.setPassword(password);

            userApi.signup(user)
                    .enqueue(new Callback<JwtAuthenticationResponse>() {
                        @Override
                        public void onResponse(Call<JwtAuthenticationResponse> call, Response<JwtAuthenticationResponse> response) {
                            if (response.isSuccessful()) {
                                // Parse the response body to get the JWT token
                                JwtAuthenticationResponse response1 = response.body();
                                String jwt = response1.getToken();
                                // Store the JWT token using SharedPreferences
                                saveJwtTokenToSharedPreferences(response1.getToken());

                                Intent intent = new Intent(SignupScreen.this, CreateDActivity.class);

                                etEmail.setText("");
                                etPasswordVerif.setText("");
                                etPassword.setText("");

                                startActivity(intent);
                                //Toast.makeText(SignupScreen.this, "Enregistrement réussi!", Toast.LENGTH_SHORT).show();
                            } else {
                                // Handle unsuccessful response
                                Toast.makeText(SignupScreen.this, "Échec de l'enregistrement!!!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<JwtAuthenticationResponse> call, Throwable t) {
                            Toast.makeText(SignupScreen.this, "Échec de l'enregistrement!!!", Toast.LENGTH_SHORT).show();
                            Logger.getLogger(SignupScreen.class.getName()).log(Level.SEVERE, "Une erreur s'est produite", t);
                        }
                    });
        });
    }




    // Method to save JWT token to SharedPreferences
    private void saveJwtTokenToSharedPreferences(String jwtToken) {
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("jwtToken", jwtToken);
        editor.apply();
    }


    public  boolean isValidPassword(String password) {
        String regex = "^(?=.*[A-Z])(?=.*[0-9]).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public  boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public  boolean isValidName(String name) {
        // The name should start with a character, have max length 21, and not contain special characters
        String regex = "^[A-Za-z][A-Za-z0-9]{0,20}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    public boolean passwordEquality(String password,String passwordVerification){
        return password.equals(passwordVerification);
    }

}

