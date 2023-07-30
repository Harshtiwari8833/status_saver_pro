package com.example.statussaverpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.statussaverpro.Models.Status;
import com.example.statussaverpro.Utils.Common;
import com.example.statussaverpro.Utils.SingleMediaScanner;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OpenVideoActivity extends AppCompatActivity {
VideoView video;
Button downloadbtn;
    Status status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_video);
        video = findViewById(R.id.video);
        downloadbtn = findViewById(R.id.button2);

        final MediaController mediaController = new MediaController(OpenVideoActivity.this, false);

        video.setOnPreparedListener(mp -> {

            mp.start();
            mediaController.show(0);
            mp.setLooping(true);
        });
        video.setMediaController(mediaController);
        mediaController.setMediaPlayer(video);

        video.setVideoURI(Uri.parse(getIntent().getStringExtra("Video")));



        //download video

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("statusObject")) {
            status = intent.getParcelableExtra("statusObject");
            downloadbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File file = new File(Common.APP_DIR);
                    if (!file.exists()) {
                        if (!file.mkdirs()) {

                        }
                    }

                    String fileName;

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                    String currentDateTime = sdf.format(new Date());

                    if (status.isVideo()) {
                        fileName = "VID_" + currentDateTime + ".mp4";
                    } else {
                        fileName = "IMG_" + currentDateTime + ".jpg";
                    }

                    File destFile = new File(file + File.separator + fileName);

                    try {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                            ContentValues values = new ContentValues();

                            Uri destinationUri;

                            values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                            values.put(MediaStore.MediaColumns.RELATIVE_PATH,
                                    Environment.DIRECTORY_DCIM + "/status_saver");

                            Uri collectionUri;
                            if (status.isVideo()) {
                                values.put(MediaStore.MediaColumns.MIME_TYPE, "video/*");
                                collectionUri = MediaStore.Video.Media.getContentUri(
                                        MediaStore.VOLUME_EXTERNAL_PRIMARY);
                            } else {
                                values.put(MediaStore.MediaColumns.MIME_TYPE, "image/*");
                                collectionUri = MediaStore.Images.Media.getContentUri(
                                        MediaStore.VOLUME_EXTERNAL_PRIMARY);
                            }

                            destinationUri = getContentResolver().insert(collectionUri, values);

                            InputStream inputStream = getContentResolver().openInputStream(Uri.parse(getIntent().getStringExtra("Video")));
                            OutputStream outputStream = getContentResolver().openOutputStream(destinationUri);
                            IOUtils.copy(inputStream, outputStream);



                        } else {
                            org.apache.commons.io.FileUtils.copyFile(status.getFile(), destFile);
                            //noinspection ResultOfMethodCallIgnored
                            destFile.setLastModified(System.currentTimeMillis());
                            new SingleMediaScanner(OpenVideoActivity.this, file);


                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(OpenVideoActivity.this, "Video Downloaded :)", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else{
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        }
    }
}