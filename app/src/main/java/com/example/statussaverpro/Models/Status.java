package com.example.statussaverpro.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.documentfile.provider.DocumentFile;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;

public class Status implements Parcelable {

    private File file;
    private String title;
    private String path;
    private boolean isVideo, isApi30;
    private DocumentFile documentFile;

    public Status(File file, String title, String path) {
        this.file = file;
        this.title = title;
        this.path = path;
        String MP4 = ".mp4";
        this.isApi30 = false;
        this.isVideo = file.getName().endsWith(MP4);
    }

    public Status(DocumentFile documentFile) {
        this.isApi30 = true;
        this.documentFile = documentFile;
        String MP4 = ".mp4";
        this.isVideo = Objects.requireNonNull(documentFile.getName()).endsWith(MP4);
    }

    public DocumentFile getDocumentFile() {
        return documentFile;
    }

    public void setDocumentFile(DocumentFile docFile) {
        this.documentFile = docFile;
    }

    public boolean isApi30() {
        return isApi30;
    }

    public void setApi30(boolean api30) {
        this.isApi30 = api30;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }
    public Status(/* Your constructor parameters here */) {
        // Initialization of class members here
    }

    // Parcelable implementation
    protected Status(Parcel in) {
        // Read data from the parcel and assign it to the class members
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // Write class data to the parcel
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Parcelable CREATOR
    public static final Parcelable.Creator<Status> CREATOR = new Parcelable.Creator<Status>() {
        @Override
        public Status createFromParcel(Parcel in) {
            return new Status(in);
        }

        @Override
        public Status[] newArray(int size) {
            return new Status[size];
        }
    };
}

