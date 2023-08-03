package com.example.statussaverpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.statussaverpro.Models.Status;

public class SaveOpenVideoActivity extends AppCompatActivity {

    VideoView video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_open_video);

        video = findViewById(R.id.video);


        Intent intent = getIntent();

        final MediaController mediaController = new MediaController(SaveOpenVideoActivity.this, false);

        video.setOnPreparedListener(mp -> {

            mp.start();
            mediaController.show(0);
            mp.setLooping(true);
        });
        video.setMediaController(mediaController);
        mediaController.setMediaPlayer(video);

        video.setVideoURI(Uri.parse(getIntent().getStringExtra("Video")));

    }
}