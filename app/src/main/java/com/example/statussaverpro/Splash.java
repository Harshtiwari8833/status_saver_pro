package com.example.statussaverpro;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.storage.StorageManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Objects;

public class Splash extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        try{
            sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
            boolean nightMode = sharedPreferences.getBoolean("night", false);
            if(nightMode==true){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }catch (Exception e)
        {
            e.printStackTrace();

        }

        SharedPreferences pref1 = getSharedPreferences("Onboardin", MODE_PRIVATE);
        boolean check = pref1.getBoolean("flag", false);
     new Handler().postDelayed(new Runnable() {
         @Override
         public void run() {
             if(check){
                 Intent  intent = new Intent(Splash.this, MainActivity.class);
                 finish();
                 startActivity(intent);

             }
             else{
                 Intent  intent = new Intent(Splash.this, OnboardingActivity.class);
                 finish();
                 startActivity(intent);

             }
         }
     },1000);
    }
}




