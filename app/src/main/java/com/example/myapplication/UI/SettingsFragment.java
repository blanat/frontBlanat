package com.example.myapplication.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar; // Import the correct Toolbar class

import androidx.fragment.app.Fragment;

import com.example.myapplication.ProfilFragment;
import com.example.myapplication.R;
import com.example.myapplication.UI.profile.changMdp;
import com.example.myapplication.retrofit.RetrofitService;
import com.example.myapplication.retrofit.UserApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends Fragment {

    private String email;
    private String password;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);


        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);

        // Set the title to the desired string
        toolbar.setTitle("Parametre");

        //final TextView backLink = view.findViewById(R.id.backID);
        final TextView modMdpLink = view.findViewById(R.id.modMdp);
        final TextView delAccLink = view.findViewById(R.id.delAcc);

        // Retrieve the email from arguments
        if (getArguments() != null && getArguments().containsKey("email")) {
            email = getArguments().getString("email");
        }

       /* backLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent = new Intent(requireContext(), ProfilFragment.class);
                signupIntent.putExtra("email", email);
                startActivity(signupIntent);
            }
        });*/

        modMdpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(requireContext(), changMdp.class);
                intent1.putExtra("email", email);
                intent1.putExtra("password", password);
                startActivity(intent1);
            }
        });

        delAccLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUser();
                Intent signupIntent = new Intent(requireContext(), MainActivity.class);
                startActivity(signupIntent);
            }
        });

        return view;
    }

    private void deleteUser() {
        // Get the email from wherever you have it stored (e.g., SharedPreferences, Intent extras)
        String userEmail = email;

        // Make the API call to delete the user
        RetrofitService retrofitService = new RetrofitService(requireContext());
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

        userApi.deleteUser(userEmail).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Handle successful response (user deletion)
                    Toast.makeText(requireContext(), "User deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle error response
                    Toast.makeText(requireContext(), "Failed to delete user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle network or other failures
                Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
