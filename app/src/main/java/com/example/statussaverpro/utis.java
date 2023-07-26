package com.example.statussaverpro;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;

public class utis {
    public  static  Boolean isPermissionGranted(Context context){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
            return Environment.isExternalStorageManager();

        }else{
            int readExtStorgae = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
            return readExtStorgae == PackageManager.PERMISSION_GRANTED;
        }
    }
}
