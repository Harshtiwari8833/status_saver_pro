package com.example.statussaverpro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.statussaverpro.Models.Status;

import java.util.List;

public class FilesAdapter extends  RecyclerView.Adapter<FilesAdapter.ViewHolder> {
    private final List<Status> imagesList;
    private Context context;

    public FilesAdapter(List<Status> imagesList, Context context) {
        this.imagesList = imagesList;
        this.context = context;
    }

    @NonNull
    @Override
    public FilesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate( R.layout.imges_layout, parent,false);
        return new FilesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FilesAdapter.ViewHolder holder, int position) {
        holder.download.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_delete_24));
        final Status status = imagesList.get(position);
        if (status.isApi30()) {
            Glide.with(context).load(status.getDocumentFile().getUri()).into(holder.image_cardview);
        } else {
            Glide.with(context).load(status.getFile()).into(holder.image_cardview);
        }
        holder.download.setOnClickListener(view -> {
            if (status.getFile().delete()) {
                imagesList.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "File Deleted", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(context, "Unable to Delete File", Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_cardview, download;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_cardview = itemView.findViewById(R.id.wall_img);
            download = itemView.findViewById(R.id.download);
        }
    }
}
