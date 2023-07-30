package com.example.statussaverpro;

import android.content.Context;
import android.content.Intent;
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

import java.util.List;

public class videoAdapter extends  RecyclerView.Adapter<videoAdapter.ViewHolder> {
    private final List<Status> videoList;
    private Context context;

    public videoAdapter(List<Status> videoList, Context context) {
        this.videoList = videoList;
        this.context = context;
    }

    @NonNull
    @Override
    public videoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate( R.layout.imges_layout, parent,false);
        return new videoAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull videoAdapter.ViewHolder holder, int position) {
        final Status status = videoList.get(position);
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
                Toast.makeText(context, "Video saved :)", Toast.LENGTH_SHORT).show();
            }
        });
        Intent intent = new Intent(context, OpenVideoActivity.class);
        holder.image_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status.isApi30()) {
                    intent.putExtra("Video", status.getDocumentFile().getUri().toString());
                    intent.putExtra("statusObject", status);

                } else {
                    intent.putExtra("Video", status.getFile().toString());
                    intent.putExtra("download",status.toString());
                    intent.putExtra("statusObject", status);
                }
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        ImageView image_cardview, download;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_cardview = itemView.findViewById(R.id.wall_img);
            download = itemView.findViewById(R.id.download);


        }
    }
}
