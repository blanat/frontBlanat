package com.example.myapplication.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.myapplication.R;

public class EntryPOINTactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if the token exists in SharedPreferences
        if (tokenExists()) {
            // Token exists, go to CreateDealsActivity
            Intent createDealsIntent = new Intent(this, navActivity.class);
            startActivity(createDealsIntent);
        } else {
            // Token doesn't exist, go to MainActivity
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
        }

        // Finish the current activity to prevent going back to it
        finish();
    }

    private boolean tokenExists() {
        // Retrieve the token from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = preferences.getString("token", "");

        // Check if the token exists
        return !TextUtils.isEmpty(token);
    }
}
