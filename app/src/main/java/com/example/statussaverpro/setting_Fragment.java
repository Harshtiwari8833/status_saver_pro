package com.example.statussaverpro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;


public class setting_Fragment extends Fragment {

    LinearLayout card_3,card_4,card_5,card_7;
    Switch switcher_1;
    boolean nightMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_setting_, container, false);


        card_3 = view.findViewById(R.id.card_3);
        card_4 = view.findViewById(R.id.card_4);
        card_5 = view.findViewById(R.id.card_5);
        card_7 = view.findViewById(R.id.card_7);

        switcher_1 = view.findViewById(R.id.switcher_1);

        /* settingViewModel = new ViewModelProvider(this).get(SettingViewModel.class);
         settingViewModel.getNightModeLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean nightMode) {
                // Update the UI elements here based on the night mode state
                if (nightMode) {
                    // Apply night mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    // Apply day mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                // Update the switch state in the adapter (if needed)
                settingViewModel.setNightMode(nightMode);
            }
        }); */



        sharedPreferences = getActivity().getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("night", false);

       if(nightMode){
           switcher_1.setChecked(true);
           AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
           switcher_1.setChecked(false);
           AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
       }

        switcher_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
try {
    if(nightMode){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        editor = sharedPreferences.edit();
        editor.putBoolean("night",false);


    }else{
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        editor = sharedPreferences.edit();
        editor.putBoolean("night", true);

    }

    editor.apply();

}catch (Exception e){
    e.printStackTrace();
    Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
}

            }
        });



        card_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent iprivacy = new Intent(getActivity(), Privacy_policy.class);
                startActivity(iprivacy);
            }
        });

        card_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent iShare = new Intent(Intent.ACTION_SEND);
                iShare.setType("text/plain");
                iShare.putExtra(Intent.EXTRA_TEXT,"Download this Amazing App, https://play.google.com/store/apps/details?id=one4studio.wallpaper.one4wal");
                startActivity(Intent.createChooser(iShare,"Share via"));

            }
        });

        card_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent iAbout = new Intent(getActivity(), about.class);
                startActivity(iAbout);
            }
        });

        card_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=one4studio.wallpaper.one4wall");

                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });
        return view;
    }
}