package com.example.statussaverpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.statussaverpro.Models.Status;

public class SaveOpenImageActivity extends AppCompatActivity {
ImageView image;
    Button share;
    Status status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_open_image);
        image = findViewById(R.id.image);
        share = findViewById(R.id.share);


        //share
        Intent intent = getIntent();
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = intent.getParcelableExtra("statusObject");
                Intent shareIntent = new Intent(Intent.ACTION_SEND);

                shareIntent.setType("image/jpg");

                if (status.isApi30()) {
                    shareIntent.putExtra(Intent.EXTRA_STREAM, status.getDocumentFile().getUri());
                } else {
//                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + status.getFile().getAbsolutePath()));
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(getIntent().getStringExtra("img")));
                }

                startActivity(Intent.createChooser(shareIntent, "Share image"));
            }
        });
        status = intent.getParcelableExtra("statusObject");


        if (status.isApi30()) {
            Glide.with(this).load(status.getDocumentFile().getUri()).into(image);
        } else {
            Glide.with(this).load(status.getFile()).into(image);
        }

//        Glide.with(this).load(status.getDocumentFile().getUri()).into(image);

    }
}