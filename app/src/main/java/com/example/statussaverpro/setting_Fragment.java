package com.example.statussaverpro;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class setting_Fragment extends Fragment {

    CardView card_3,card_4;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_setting_, container, false);


        card_3 = view.findViewById(R.id.card_3);
        card_4 = view.findViewById(R.id.card_4);

        card_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent iShare = new Intent(Intent.ACTION_SEND);
                iShare.setType("text/plain");
                iShare.putExtra(Intent.EXTRA_TEXT,"Download this Amazing App, https://play.google.com/store/apps/details?id=one4studio.wallpaper.one4wal");
                startActivity(Intent.createChooser(iShare,"Share via"));

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