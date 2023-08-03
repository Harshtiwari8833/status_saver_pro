package com.example.statussaverpro;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.statussaverpro.Models.Status;
import com.example.statussaverpro.Utils.Common;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class saved_Fragment extends Fragment {

    private RecyclerView recyclerView;
    private final List<Status> savedFilesList = new ArrayList<>();
    private SwipeRefreshLayout refresh;
    private FilesAdapter filesAdapter;
    private final Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_, container, false);
        recyclerView = view.findViewById(R.id.recycler);
        refresh = view.findViewById(R.id.refresh);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFiles();
                refresh.setRefreshing(false);
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        getFiles();

        return  view;
    }

    private void getFiles() {

        final File app_dir = new File(Common.APP_DIR);

        if (app_dir.exists() ||
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

//            no_files_found.setVisibility(View.GONE);

            new Thread(() -> {
                File[] savedFiles;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    File f = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DCIM
                    ) + File.separator + "status_saver");
                    savedFiles = f.listFiles();
                } else {
                    savedFiles = app_dir.listFiles();
                }
                savedFilesList.clear();

                if (savedFiles != null && savedFiles.length > 0) {

                    Arrays.sort(savedFiles);
                    for (File file : savedFiles) {
                        Status status = new Status(file, file.getName(), file.getAbsolutePath());

                        savedFilesList.add(status);
                    }

                    handler.post(() -> {

                        filesAdapter = new FilesAdapter(savedFilesList, getContext());
                        recyclerView.setAdapter(filesAdapter);
                        filesAdapter.notifyDataSetChanged();
//                        progressBar.setVisibility(View.GONE);
                    });

                } else {

                    handler.post(() -> {
//                        progressBar.setVisibility(View.GONE);
//                        no_files_found.setVisibility(View.VISIBLE);
                        if(isAdded()){
                            Toast.makeText(getContext(), "Dir doest not exists!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                refresh.setRefreshing(false);
            }).start();

        } else {
//            no_files_found.setVisibility(View.VISIBLE);
//            progressBar.setVisibility(View.GONE);
        }

    }
}