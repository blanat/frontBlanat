package com.example.myapplication.UI.details;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.model.listData;

public class DetailsDealActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_deal);

        // Receive Intent data
        Intent intent = getIntent();
        listData deal = intent.getParcelableExtra("deal");

        if (deal != null) {
            // Initialize and set up the top fragment with deal data
            DealDetailsFrag dealDetailsFrag = DealDetailsFrag.newInstance(deal);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerTop, dealDetailsFrag)
                    .commit();

            // Initialize and set up the bottom fragment for comments
            CommentListFrag commentListFrag = CommentListFrag.newInstance(deal.getDealID());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerBottom, commentListFrag)
                    .commit();
        }
    }

}