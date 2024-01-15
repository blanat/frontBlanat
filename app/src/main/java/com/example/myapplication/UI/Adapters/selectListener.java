package com.example.myapplication.UI.Adapters;

import com.example.myapplication.model.listData;

public interface selectListener {

    void onItemClicked(listData listDealData);
    void onPlusButtonClicked(int position);
    void onMoinsButtonClicked(int position);
}
