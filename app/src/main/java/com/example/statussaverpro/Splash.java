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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Objects;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //      this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



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
                 Toast.makeText(Splash.this, "hello", Toast.LENGTH_SHORT).show();
                 Intent  intent = new Intent(Splash.this, OnboardingActivity.class);
                 finish();
                 startActivity(intent);

             }
         }
     },1000);
    }
}




