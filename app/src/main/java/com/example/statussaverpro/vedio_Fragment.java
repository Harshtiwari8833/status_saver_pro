package com.example.statussaverpro;

import android.content.UriPermission;
import android.os.Build;
import android.os.Bundle;

import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;


public class vedio_Fragment extends Fragment {
RecyclerView recycler;
videoAdapter adapter;
TextView messageTextView;
SwipeRefreshLayout refresh;
    private final List<Status> videoList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vedio_, container, false);

         recycler = view.findViewById(R.id.recycler);
        refresh = view.findViewById(R.id.refresh);
        messageTextView = view.findViewById(R.id.no);

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
        recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        getStatus();
        return view;
    }
    private void getStatus() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            executeNew();

        } else if (Common.STATUS_DIRECTORY.exists()) {

            executeOld();

        } else {
            messageTextView.setVisibility(View.VISIBLE);
//            messageTextView.setText(R.string.cant_find_whatsapp_dir);
            Toast.makeText(getContext(), "Cant find whatsapp directory!", Toast.LENGTH_SHORT).show();
            refresh.setRefreshing(false);
        }

    }
    private void executeNew() {
        Executors.newSingleThreadExecutor().execute(() -> {
            Handler mainHandler = new Handler(Looper.getMainLooper());
            try {  List<UriPermission> list = requireActivity().getContentResolver().getPersistedUriPermissions();

                DocumentFile file = DocumentFile.fromTreeUri(requireActivity(), list.get(0).getUri());

                videoList.clear();
                if (file == null) {
                    mainHandler.post(() -> {
//                    progressBar.setVisibility(View.GONE);
                        messageTextView.setVisibility(View.VISIBLE);
//                    messageTextView.setText(R.string.no_files_found);
                        Toast.makeText(getContext(), "No file found!", Toast.LENGTH_SHORT).show();
                        refresh.setRefreshing(false);
                    });
                    return;
                }
                DocumentFile[] statusFiles = file.listFiles();
                if (statusFiles.length <= 0) {
                    mainHandler.post(() -> {
//                    progressBar.setVisibility(View.GONE);
                        messageTextView.setVisibility(View.VISIBLE);
//                    messageTextView.setText(R.string.no_files_found);
                        Toast.makeText(getActivity(), "No file found!", Toast.LENGTH_SHORT).show();
                        refresh.setRefreshing(false);
                    });
                    return;
                }
                for (DocumentFile documentFile : statusFiles) {
                    Status status = new Status(documentFile);

                    if (status.isVideo()) {
                        videoList.add(status);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }










            mainHandler.post(() -> {

                if (videoList.size() <= 0) {
                    messageTextView.setVisibility(View.VISIBLE);
//                    messageTextView.setText(R.string.no_files_found);
                } else {
                    messageTextView.setVisibility(View.GONE);
//                    messageTextView.setText("");
                }

                adapter = new videoAdapter(videoList, getContext());
                recycler.setAdapter(adapter);
                adapter.notifyItemRangeChanged(0, videoList.size());
//                progressBar.setVisibility(View.GONE);
            });

            refresh.setRefreshing(false);

        });
    }
    private void executeOld() {

        Executors.newSingleThreadExecutor().execute(() -> {
            Handler mainHandler = new Handler(Looper.getMainLooper());

            File[] statusFiles = Common.STATUS_DIRECTORY.listFiles();
            videoList.clear();

            if (statusFiles != null && statusFiles.length > 0) {

                Arrays.sort(statusFiles);
                for (File file : statusFiles) {
                    Status status = new Status(file, file.getName(), file.getAbsolutePath());

                    if (status.isVideo()) {
                        videoList.add(status);
                    }

                }

                mainHandler.post(() -> {

                    if (videoList.size() <= 0) {
                        messageTextView.setVisibility(View.VISIBLE);
//                        messageTextView.setText(R.string.no_files_found);
                    } else {
                        messageTextView.setVisibility(View.GONE);
//                        messageTextView.setText("");
                    }

                    adapter = new videoAdapter(videoList, getContext());
                    recycler.setAdapter(adapter);
                    adapter.notifyItemRangeChanged(0, videoList.size());
//                    progressBar.setVisibility(View.GONE);
                });

            } else {

                mainHandler.post(() -> {
//                    progressBar.setVisibility(View.GONE);
                    messageTextView.setVisibility(View.VISIBLE);
//                    messageTextView.setText(R.string.no_files_found);
                    Toast.makeText(getContext(), "No file found!", Toast.LENGTH_SHORT).show();
                });

            }
            refresh.setRefreshing(false);

        });

    }
}