package com.example.statussaverpro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

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
        holder.download.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.delete));
        final Status status = imagesList.get(position);
        if (status.isVideo()) {

        }else {
            holder.play.setVisibility(View.INVISIBLE);

        }
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

        holder.image_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status.isVideo()) {



                    Intent intent = new Intent(context, SaveOpenVideoActivity.class);
                    if (status.isApi30()) {
                        intent.putExtra("Video", status.getDocumentFile().getUri().toString());
                        intent.putExtra("statusObject", status);

                    } else {
                        intent.putExtra("Video", status.getFile().toString());
                        intent.putExtra("download",status.toString());
                        intent.putExtra("statusObject", status);
                    }
                    context.startActivity(intent);


//                  LayoutInflater inflater = LayoutInflater.from(context);
//                  final View view2 = inflater.inflate(R.layout.view_video_full_screen, null);
//                    final AlertDialog.Builder alertDg = new AlertDialog.Builder(context);
//
//                    FrameLayout mediaControls = view2.findViewById(R.id.videoViewWrapper);
//
//                    if (view2.getParent() != null) {
//                        ((ViewGroup) view2.getParent()).removeView(view2);
//                    }
//
//                    alertDg.setView(view2);
//
//                    final VideoView videoView = view2.findViewById(R.id.video_full);
//
//                    final MediaController mediaController = new MediaController(context, false);
//
//                    videoView.setOnPreparedListener(mp -> {
//
//                        mp.start();
//                        mediaController.show(0);
//                        mp.setLooping(true);
//                    });
//
//                    videoView.setMediaController(mediaController);
//                    mediaController.setMediaPlayer(videoView);
//                    videoView.setVideoURI(Uri.fromFile(status.getFile()));
//                    videoView.requestFocus();
//
//                    ((ViewGroup) mediaController.getParent()).removeView(mediaController);
//
//                    if (mediaControls.getParent() != null) {
//                        mediaControls.removeView(mediaController);
//                    }
//
//                    mediaControls.addView(mediaController);
//
//                    final AlertDialog alert2 = alertDg.create();
//
//                    alert2.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;
//                    alert2.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//                    alert2.show();

                }else{
//                    Intent intent = new Intent(context, SaveOpenImageActivity.class);
//                    if (status.isApi30()) {
//                        intent.putExtra("img", status.getDocumentFile().getUri().toString());
//                        intent.putExtra("statusObject", status);
//
//                    } else {
//                        intent.putExtra("img", status.getFile().toString());
//                        intent.putExtra("download",status.toString());
//                        intent.putExtra("statusObject", status);
//                    }
//                    context.startActivity(intent);

                    final AlertDialog.Builder alertD = new AlertDialog.Builder(context);
                    LayoutInflater inflater1 = LayoutInflater.from(context);
                    View view1 = inflater1.inflate(R.layout.view_image_full_screen, null);
                    alertD.setView(view1);

                    ImageView imageView = view1.findViewById(R.id.img);
                    if (status.isApi30()) {
                        Glide.with(context).load(status.getDocumentFile().getUri()).into(imageView);
                    } else {
                        Glide.with(context).load(status.getFile()).into(imageView);
                    }

                    AlertDialog alert = alertD.create();
                    alert.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;
                    alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alert.show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_cardview, download, play;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_cardview = itemView.findViewById(R.id.wall_img);
            download = itemView.findViewById(R.id.download);
            play = itemView.findViewById(R.id.play);
        }
    }
}
