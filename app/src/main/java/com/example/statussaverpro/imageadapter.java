package com.example.statussaverpro;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import com.google.gson.Gson;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.statussaverpro.Models.Status;
import com.example.statussaverpro.Utils.Common;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class imageadapter extends RecyclerView.Adapter<imageadapter.ViewHolder> {
    private final List<Status> imagesList;
    private Context context;

    public imageadapter(List<Status> imagesList, Context context) {
        this.imagesList = imagesList;
        this.context = context;
    }

    @NonNull
    @Override
    public imageadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate( R.layout.imges_layout, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Status status = imagesList.get(position);
         holder.play.setVisibility(View.INVISIBLE);
         holder.share.setVisibility(View.INVISIBLE);
        if (status.isApi30()) {
//            holder.save.setVisibility(View.GONE);
            Glide.with(context).load(status.getDocumentFile().getUri()).into(holder.image_cardview);
        } else {
//            holder.save.setVisibility(View.VISIBLE);
            Glide.with(context).load(status.getFile()).into(holder.image_cardview);
        }

       holder.download.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Common.copyFile(status, context);
               Toast.makeText(context, "Image saved :)", Toast.LENGTH_SHORT).show();
           }
       });

        Intent intent = new Intent(context, OpenImageActivity.class);

        holder.image_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status.isApi30()) {
                    intent.putExtra("img", status.getDocumentFile().getUri().toString());
                    intent.putExtra("statusObject", status);

                } else {
                    intent.putExtra("img", status.getFile().toString());
                    intent.putExtra("download",status.toString());
                    intent.putExtra("statusObject", status);
                }
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder {
        ImageView image_cardview, download, play, share;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_cardview = itemView.findViewById(R.id.wall_img);
            download = itemView.findViewById(R.id.download);
            play  = itemView.findViewById(R.id.play);
            share = itemView.findViewById(R.id.share);

        }
    }
}
