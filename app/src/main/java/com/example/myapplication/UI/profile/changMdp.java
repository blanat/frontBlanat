package com.example.myapplication.UI.profile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.model.User;
import com.example.myapplication.retrofit.RetrofitService;
import com.example.myapplication.retrofit.UserApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class changMdp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chang_mdp);

        final TextView backLink = findViewById(R.id.backID);
        Button btnSignup = findViewById(R.id.btnMod);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialog();
            }
        });


        backLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent = new Intent(changMdp.this, Parameter.class);
                startActivity(signupIntent);
            }
        });

    }
    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation")
                .setMessage("Are you sure you want to change the password?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Perform the action when the user clicks "Yes"
                        // You can put your logic for changing the password here
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked "No", do nothing or handle accordingly
                    }
                })
                .show();
    }

    private void updatePassword() {
        RetrofitService retrofitService = new RetrofitService(this);
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);
        final TextView oldpasword = findViewById(R.id.etPassword);
        final TextView newpassword = findViewById(R.id.newPassword);


        String newPassword = newpassword.getText().toString().trim();

        Call<User> updatePasswordCall = userApi.updatePassword("user@example.com", newPassword);
        updatePasswordCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    // Password updated successfully
                    User updatedUser = response.body();
                    // Handle the updated user
                } else {
                    // Handle the failure
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Handle the failure
            }
        });
    }
}