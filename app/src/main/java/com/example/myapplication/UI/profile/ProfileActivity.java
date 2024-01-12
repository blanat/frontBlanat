package com.example.myapplication.UI.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.myapplication.R;

public class ProfileActivity extends AppCompatActivity {
    TextView usernameTv;
    TextView dateJoinTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String username = findViewById(R.id.usernameTextView).toString();
        String dateJoin = findViewById(R.id.dateJoinedTextView).toString();

        usernameTv.setText(username);
    }

}