package com.example.myapplication.UI.profile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.UI.LoginScreen;
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
                        updatePassword();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       Intent intent = new Intent(changMdp.this, Profile.class);
                       startActivity(intent);

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
        Intent receivedIntent = getIntent();
        String email = receivedIntent.getStringExtra("email");
        Call<User> updatePasswordCall = userApi.updatePassword(email , newPassword);
        updatePasswordCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getBaseContext(), "password changed successfully!", Toast.LENGTH_SHORT).show();
                    User updatedUser = response.body();
                } else {
                    Toast.makeText(getBaseContext(), "password can't be change try again!", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getBaseContext(), "password can't be change try again later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}