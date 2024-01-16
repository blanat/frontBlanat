package com.example.myapplication.UI.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
        final TextView entredOldpasword = findViewById(R.id.etPassword);
        final TextView entredpaswordverif = findViewById(R.id.verifPassword);
        final TextView newpassword = findViewById(R.id.newPassword);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation")
                .setMessage("Are you sure you want to change the password?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent receivedIntent = getIntent();
                        String passwordOld = receivedIntent.getStringExtra("password");
                        String oldPasswordEnteredByUser = entredOldpasword.getText().toString().trim();
                        String oldPasswordEnteredByUserVerif = entredpaswordverif.getText().toString().trim();

//                        if(!passwordOld.equals(oldPasswordEnteredByUser)){
//                            entredOldpasword.setText("old password incorrect");
//                            Toast.makeText(getBaseContext(), "old password incorrect!", Toast.LENGTH_SHORT).show();
//                        }

//                        if (!oldPasswordEnteredByUserVerif.equals(oldPasswordEnteredByUser)){
//                            Toast.makeText(getBaseContext(), "Entered password do not match!", Toast.LENGTH_SHORT).show();
//                        }
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
        final TextView entredOldpasword1 = findViewById(R.id.etPassword);
        final TextView entredpaswordverif1 = findViewById(R.id.verifPassword);
        final TextView newpassword1 = findViewById(R.id.newPassword);

        RetrofitService retrofitService = new RetrofitService(this);
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);


        String newPassword = newpassword1.getText().toString().trim();
        Intent receivedIntent = getIntent();
        String email = receivedIntent.getStringExtra("email");
        String passwordOld = receivedIntent.getStringExtra("password");


        Call<User> updatePasswordCall = userApi.updatePassword(email , newPassword);
        updatePasswordCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getBaseContext(), "password changed successfully!", Toast.LENGTH_SHORT).show();
                    User updatedUser = response.body();
                    entredpaswordverif1.setText("");
                    newpassword1.setText("");
                    entredOldpasword1.setText("");
                    Intent intent1 = new Intent(getBaseContext(), Parameter.class);
                    startActivity(intent1);
                    
                } else {
                    Toast.makeText(getBaseContext(), "password can't be change try again!", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                entredpaswordverif1.setText("");
                newpassword1.setText("");
                entredOldpasword1.setText("");
                Intent intent1 = new Intent(getBaseContext(), Parameter.class);
                startActivity(intent1);
                //Toast.makeText(getBaseContext(), "password password failed to changed !!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
