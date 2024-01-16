package com.example.myapplication.UI.Adapters;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.myapplication.MesdiscussionFragment;
import com.example.myapplication.MydealsFragment;
import com.example.myapplication.StatisticFragment;

public class ProfilePagerAdapter extends FragmentPagerAdapter {

    public ProfilePagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MydealsFragment();
            case 1:
                return new MesdiscussionFragment();
            case 2:
                return new StatisticFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3; // Number of tabs
    }

    @NonNull
    @Override
    public CharSequence getPageTitle(int position) {
        // Return tab titles
        switch (position) {
            case 0:
                return "Mes Deals";
            case 1:
                return "Mes Discussions";
            case 2:
                return "Statistics";
            default:
                return "";
        }
    }
}
