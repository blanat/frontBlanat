package com.example.myapplication.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.retrofit.RetrofitService;
import com.example.myapplication.retrofit.UserApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Parameter extends AppCompatActivity {
    private String email;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter);

        final TextView backLink = findViewById(R.id.backID);
        final TextView modMdpLink = findViewById(R.id.modMdp);
        final TextView delAccLink = findViewById(R.id.delAcc);
        Intent intent = getIntent();

        if (intent.hasExtra("email") && intent.hasExtra("password")) {
            email = intent.getStringExtra("email");
            password = intent.getStringExtra("password");
        }

        backLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent = new Intent(Parameter.this, Profile.class);
                signupIntent.putExtra("email", email);
                signupIntent.putExtra("password", password);
                startActivity(signupIntent);
            }
        });

        modMdpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent = new Intent(Parameter.this, changMdp.class);
                startActivity(signupIntent);
            }
        });

        delAccLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUser();
                Intent signupIntent = new Intent(Parameter.this, MainActivity.class);
                startActivity(signupIntent);
            }
        });
    }

    private void deleteUser() {
        // Get the email from wherever you have it stored (e.g., SharedPreferences, Intent extras)
        String userEmail = email;

        // Make the API call to delete the user
        RetrofitService retrofitService = new RetrofitService(this);
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

        userApi.deleteUser(userEmail).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Handle successful response (user deletion)
                    Toast.makeText(Parameter.this, "User deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle error response
                    Toast.makeText(Parameter.this, "Failed to delete user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle network or other failures
                Toast.makeText(Parameter.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}