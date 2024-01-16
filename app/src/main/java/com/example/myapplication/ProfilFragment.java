package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.UI.Adapters.SavedDealsAdapter;
import com.example.myapplication.UI.Adapters.selectListener;
import com.example.myapplication.UI.CreateDiscFragment;
import com.example.myapplication.UI.DeleteDiscussionFragment;
import com.example.myapplication.UI.MainActivity;
import com.example.myapplication.UI.SettingsFragment;
import com.example.myapplication.UI.details.DetailsDealActivity;
import com.example.myapplication.UI.profile.Parameter;
import com.example.myapplication.UI.profile.Profile;
import com.example.myapplication.model.User;
import com.example.myapplication.model.listData;
import com.example.myapplication.retrofit.DealApi;
import com.example.myapplication.retrofit.RetrofitService;
import com.example.myapplication.retrofit.UserApi;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;
import com.example.myapplication.UI.Adapters.ProfilePagerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfilFragment extends Fragment implements selectListener {

    private RetrofitService retrofitService;
    private UserApi userApi;
    private String email;
    //private RecyclerView recyclerView;
    private EditText editText;
    private ArrayAdapter<listData> SavedDealsAdapter;
    private List<listData> dealslist;

    public ProfilFragment() {
        // Required empty public constructor
    }

    public static ProfilFragment newInstance() {
        ProfilFragment fragment = new ProfilFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        ImageView parametre = view.findViewById(R.id.parametre);
        ImageView logout = view.findViewById(R.id.logout);
        //=================================
        dealslist = new ArrayList<>();

        //final TextView paramsLink = view.findViewById(R.id.paramId);
        final ImageView profileImage = view.findViewById(R.id.profile_image);
        final TextView username = view.findViewById(R.id.username);

        // Initialize your recyclerView and editText here


        // Initialize ViewPager and TabLayout
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);

        // Set up the ViewPager with the sections adapter.
        ProfilePagerAdapter profilePagerAdapter = new ProfilePagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(profilePagerAdapter);

        // Connect the TabLayout with the ViewPager.
        tabLayout.setupWithViewPager(viewPager);


        retrofitService = new RetrofitService(requireContext());

        RetrofitService retrofitService = new RetrofitService(requireContext());
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

        String jwtToken = retrieveToken();


        userApi.fromToke("Bearer " + jwtToken)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            User user = response.body();
                            Uri fileUri = Uri.fromFile(new File(user.getProfileFilePath()));
                            String imageUrl = fileUri.toString();

                            Log.d("TAG", Uri.decode(imageUrl));
                            Picasso.get()
                                    .load(Uri.decode(imageUrl)) // Replace with the actual method to get the image URL
                                    .placeholder(R.drawable.default_image) // You can set a placeholder image while loading
                                    .error(R.drawable.default_image) // You can set an error image if the loading fails
                                    .into(profileImage);



                            username.setText(user.getUserName());
                            email = user.getEmail();
                            Log.d("UserApiResponse", "User data: " + user.toString());
                        } else {
                            // Log the error
                            Log.e("UserApiResponse", "Error: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        // Log the failure
                        Log.e("UserApiFailure", "Failure: " + t.getMessage());
                    }
                });


        parametre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SettingsFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeJwtTokenFromSharedPreferences();

                // Create a new task and clear the existing task (activity stack)
                Intent intent = new Intent(requireContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                // Finish the current activity
                requireActivity().finish();
            }
        });



        return view;
    }



    private String retrieveToken() {
        // Retrieve the token from SharedPreferences using the Activity's context
        SharedPreferences preferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String jwtToken = preferences.getString("jwtToken", "");
        // Log the token for debugging
        Log.d("Token", "Retrieved JWT Token: " + jwtToken);
        return jwtToken;
    }




    public void onItemClicked(listData deal) {
        Intent intent = new Intent(requireContext(), DetailsDealActivity.class);
        intent.putExtra("deal", deal);
        startActivity(intent);
    }

    @Override
    public void onPlusButtonClicked(int position) {

    }

    @Override
    public void onMoinsButtonClicked(int position) {

    }


    //=====================================================

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        // Find the menu items
        MenuItem addItem = menu.findItem(R.id.action_add);
        MenuItem deleteItem = menu.findItem(R.id.action_delete);

        // Hide the first two items and show the last two
        addItem.setVisible(false);
        deleteItem.setVisible(false);

        MenuItem parametre = menu.findItem(R.id.parametre);
        MenuItem logout = menu.findItem(R.id.logout);

        parametre.setVisible(true);
        logout.setVisible(true);
        requireActivity().invalidateOptionsMenu();

    }


    //=================================================
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.parametre) {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ParametreFragment())
                    .addToBackStack(null)
                    .commit();
            return true;
        } else if (itemId == R.id.logout) {
            removeJwtTokenFromSharedPreferences();

            // Create a new task and clear the existing task (activity stack)
            Intent intent = new Intent(requireContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            // Finish the current activity
            requireActivity().finish();
            Log.d("NavActivity", "Logging out");
            Toast.makeText(requireContext(), "Logout!", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Log.w("NavActivity", "Unhandled item ID: " + itemId);
        }

        return super.onOptionsItemSelected(item);
    }


    private void removeJwtTokenFromSharedPreferences() {
        SharedPreferences preferences = requireContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("jwtToken");
        editor.apply();
    }


// Other methods and code for your fragment
/*

    private boolean shouldShowButton() {
        // Implement your conditions here
        // For example, return true if you want to show the "ADD" button, false otherwise
        return true;
    }
*/



}
