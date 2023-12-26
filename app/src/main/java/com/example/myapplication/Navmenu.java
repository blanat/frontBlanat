package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.myapplication.R;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.myapplication.UI.CreateDActivity;
import com.example.myapplication.databinding.ActivityNavmenuBinding;

public class Navmenu extends AppCompatActivity {

    ActivityNavmenuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNavmenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Log.d("Navmenu", "Item selected: " + item.getItemId());

            if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.add) {
                // Start the CreateDActivity
                startActivity(new Intent(Navmenu.this, CreateDActivity.class));
            } else if (item.getItemId() == R.id.profil) {
                replaceFragment(new ProfilFragment());
            }

            return true;
        });
    }


    private void replaceFragment(Fragment fragment) {
        Log.d("Navmenu", "Replacing fragment: " + fragment.getClass().getSimpleName());

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.commit();
    }

}