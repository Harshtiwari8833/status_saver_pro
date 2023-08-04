package com.example.statussaverpro;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriPermission;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.statussaverpro.Models.Status;
import com.example.statussaverpro.Utils.Common;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;


public class img_Fragment extends Fragment {

    RecyclerView recycler;
    imageadapter adapter;
    TextView no_files_found;
    boolean nightMode;

    private final List<Status> imagesList = new ArrayList<>();
    SwipeRefreshLayout refresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_img_, container, false);
        refresh = v.findViewById(R.id.refresh);
        recycler = v.findViewById(R.id.recycler);
        no_files_found = v.findViewById(R.id.no);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());






 refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
     @Override
     public void onRefresh() {
         getStatus();
         refresh.setRefreshing(false);
     }
 });
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));

        getStatus();
        return v;
    }


    private void getStatus() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            executeNew();

        } else if (Common.STATUS_DIRECTORY.exists()) {

            executeOld();

        } else {
            no_files_found.setVisibility(View.VISIBLE);
//            messageTextView.setText(R.string.cant_find_whatsapp_dir);
//            Toast.makeText(getActivity(), getString(R.string.cant_find_whatsapp_dir), Toast.LENGTH_SHORT).show();
            refresh.setRefreshing(false);
        }

    }


    private void executeOld() {

        Executors.newSingleThreadExecutor().execute(() -> {

            Handler mainHandler = new Handler(Looper.getMainLooper());

            File[] statusFiles;
            statusFiles = Common.STATUS_DIRECTORY.listFiles();
            imagesList.clear();

            if (statusFiles != null && statusFiles.length > 0) {

                Arrays.sort(statusFiles);
                for (File file : statusFiles) {

                    if (file.getName().contains(".nomedia"))
                        continue;

                    Status status = new Status(file, file.getName(), file.getAbsolutePath());

                    if (!status.isVideo() && status.getTitle().endsWith(".jpg")) {
                        imagesList.add(status);
                    }

                }

                mainHandler.post(() -> {

                    if (imagesList.size() <= 0) {
                        no_files_found.setVisibility(View.VISIBLE);
//                        messageTextView.setText(R.string.no_files_found);
                    } else {
                        no_files_found.setVisibility(View.GONE);
//                        messageTextView.setText("");
                    }

                    adapter = new imageadapter(imagesList, getContext());
                    recycler.setAdapter(adapter);
                    adapter.notifyItemRangeChanged(0, imagesList.size());
//                    progressBar.setVisibility(View.GONE);
                });

            } else {

                mainHandler.post(() -> {
//                    progressBar.setVisibility(View.GONE);
                    no_files_found.setVisibility(View.VISIBLE);
//                    messageTextView.setText(R.string.no_files_found);
//                    Toast.makeText(getActivity(), getString(R.string.no_files_found), Toast.LENGTH_SHORT).show();
                });

            }
            refresh.setRefreshing(false);

        });
    }

    private void executeNew() {

        Executors.newSingleThreadExecutor().execute(() -> {
            Handler mainHandler = new Handler(Looper.getMainLooper());
            try {
                List<UriPermission> list = requireActivity().getContentResolver().getPersistedUriPermissions();
                DocumentFile file = DocumentFile.fromTreeUri(requireActivity(), list.get(0).getUri());

                imagesList.clear();

                if (file == null) {
                    mainHandler.post(() -> {
//                        progressBar.setVisibility(View.GONE);
                        no_files_found.setVisibility(View.VISIBLE);
//                        messageTextView.setText(R.string.no_files_found);

                        refresh.setRefreshing(false);
                    });
                    return;
                }
                DocumentFile[] statusFiles = file.listFiles();
                if (statusFiles.length <= 0) {
                    mainHandler.post(() -> {
//                    progressBar.setVisibility(View.GONE);
                        no_files_found.setVisibility(View.VISIBLE);
//                    messageTextView.setText(R.string.no_files_found);
                        Toast.makeText(getActivity(), "no file found", Toast.LENGTH_SHORT).show();
                        refresh.setRefreshing(false);
                    });
                    return;
                }

                for (DocumentFile documentFile : statusFiles) {

                    if (Objects.requireNonNull(documentFile.getName()).contains(".nomedia"))
                        continue;

                    Status status = new Status(documentFile);

                    if (!status.isVideo()) {
                        imagesList.add(status);
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }



            mainHandler.post(() -> {

                if (imagesList.size() <= 0) {
                    no_files_found.setVisibility(View.VISIBLE);
//                    messageTextView.setText(R.string.no_files_found);
                } else {
                    no_files_found.setVisibility(View.GONE);
//                    messageTextView.setText("");
                }

                adapter = new imageadapter(imagesList, getContext());
                recycler.setAdapter(adapter);
                adapter.notifyItemRangeChanged(0, imagesList.size());
//                progressBar.setVisibility(View.GONE);
            });

        });
    }

}












//    private void getData() {
//          ImageModel model;
//          String targetPath = Environment.getExternalStorageDirectory().toString()+ "/Android/media/com.whatsapp/WhatsApp/Media/.Statuses";
//          File targetDirector = new File(targetPath);
//
//          File[] allFiles = targetDirector.listFiles();
//        Toast.makeText(getContext(), allFiles.toString(), Toast.LENGTH_SHORT).show();
//        String targetPathBusiness = Environment.getExternalStorageDirectory().getAbsolutePath()+"/WhatsApp Business/Media/.Statuses";
//        File targetDirectorBusiness = new File(targetPathBusiness);
//        File[] allFilesBusiness = targetDirectorBusiness .listFiles();
//
//        Arrays.sort(allFiles, ((o1,o2)->{
//           if(o1.lastModified() > o2.lastModified()) return -1;
//           else if(o1.lastModified() < o2.lastModified())  return +1;
//           else return 0;
//        }));
//
//        for(int i =0; i< allFiles.length; i++ ){
//            File file  = allFiles[i];
//            if (Uri.fromFile(file).toString().endsWith(".png")||Uri.fromFile(file).toString().endsWith(".jpg")){
//                model= new ImageModel("Whats "+i,
//                        file.getName(), allFiles[i].getAbsolutePath(), Uri.fromFile(file) );
//                list.add(model);
//
//            }
//
//        }



//        Arrays.sort(allFilesBusiness, ((o1,o2)->{
//            if(o1.lastModified() > o2.lastModified()) return -1;
//            else if(o1.lastModified() < o2.lastModified())  return +1;
//            else return 0;
//        }));
//
//        for(int i =0; i< allFilesBusiness.length; i++ ){
//            File file  = allFilesBusiness[i];
//            if (Uri.fromFile(file).toString().endsWith(".png")||Uri.fromFile(file).toString().endsWith(".jpg")){
//                model= new ImageModel("WhatsBusiness "+i,
//                        file.getName(), allFilesBusiness[i].getAbsolutePath(), Uri.fromFile(file) );
//                list.add(model);
//
//            }
//        }



