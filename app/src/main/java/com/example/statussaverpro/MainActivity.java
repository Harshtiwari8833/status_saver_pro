package com.example.statussaverpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView btm_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btm_nav = findViewById(R.id.btm_nav);

        btm_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id==R.id.img){
                    loadfrag(new img_Fragment(),false);
                } else if (id == R.id.vedio) {
                    loadfrag(new vedio_Fragment(),false);
                } else if (id == R.id.save) {
                    loadfrag(new saved_Fragment(),false);
                }else {
                    loadfrag(new setting_Fragment(),true);
                }

                return true;
            }
        });

        btm_nav.setSelectedItemId(R.id.img);
    }

    public void loadfrag(Fragment fragment,boolean flag){

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(flag) {
            ft.add(R.id.frame, fragment);
        }
        else{
            ft.replace(R.id.frame, fragment);
        }
        ft.commit();
    }
}

