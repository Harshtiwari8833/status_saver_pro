package com.example.statussaverpro;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.example.statussaverpro.Utils.Common;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private static final String TAG="MainActivity";

    BottomNavigationView btm_nav;
    private long back_pressed;

    private static final int REQUEST_PERMISSIONS = 1234;
    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @SuppressLint("InlinedApi")
    private static final String[] NOTIFICATION_PERMISSION = {
            Manifest.permission.POST_NOTIFICATIONS
    };

    private static final int NOTIFICATION_REQUEST_PERMISSIONS = 4;

    private Context context;

    private int resourceId = R.id.img;

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {

                    Intent data = result.getData();

                    assert data != null;

                    context.getContentResolver().takePersistableUriPermission(
                            data.getData(),
                            Intent.FLAG_GRANT_READ_URI_PERMISSION |
                                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();

                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btm_nav = findViewById(R.id.btm_nav);


        btm_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.img) {
                    resourceId = R.id.img;
                    loadfrag(new img_Fragment(), false);
                } else if (id == R.id.vedio) {
                    resourceId = R.id.video;
                    loadfrag(new vedio_Fragment(), false);
                } else if (id == R.id.save) {
                    resourceId = R.id.save;
                    loadfrag(new saved_Fragment(), false);
                } else {
                    resourceId = R.id.setting;
                    loadfrag(new setting_Fragment(), false);
                }

                return true;
            }
        });

        btm_nav.setSelectedItemId(resourceId);
        Log.d(TAG, "onCreate: "+ resourceId);
    }


    public void loadfrag(Fragment fragment, boolean flag) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (flag) {
            ft.add(R.id.frame, fragment);
        } else {
            ft.replace(R.id.frame, fragment);
        }
        ft.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS && grantResults.length > 0) {
            if (arePermissionDenied()) {
                ((ActivityManager) Objects.requireNonNull(this.getSystemService(ACTIVITY_SERVICE))).clearApplicationUserData();
                recreate();
            }
        }
    }

    private boolean arePermissionDenied() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return getContentResolver().getPersistedUriPermissions().size() <= 0;
        }

        for (String permissions : PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), permissions) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && arePermissionDenied()) {

            // If Android 10+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                requestPermissionQ();
                return;
            }

            requestPermissions(PERMISSIONS, REQUEST_PERMISSIONS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(NOTIFICATION_PERMISSION,
                    NOTIFICATION_REQUEST_PERMISSIONS);
        }

        if (Common.APP_DIR == null || Common.APP_DIR.isEmpty()) {
            Common.APP_DIR = getExternalFilesDir("StatusDownloader").getPath();
            Log.d("App Path", Common.APP_DIR);
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void requestPermissionQ() {
        StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);

        Intent intent = sm.getPrimaryStorageVolume().createOpenDocumentTreeIntent();
        String startDir = "Android%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses";

        Uri uri = intent.getParcelableExtra("android.provider.extra.INITIAL_URI");

        String scheme = uri.toString();
        scheme = scheme.replace("/root/", "/document/");
        scheme += "%3A" + startDir;

        uri = Uri.parse(scheme);

        Log.d("URI", uri.toString());

        intent.putExtra("android.provider.extra.INITIAL_URI", uri);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);


        activityResultLauncher.launch(intent);
    }

    @Override
    protected final void onRestoreInstanceState(final Bundle inState) {
        Log.d(TAG, "onRestoreInstanceState: ");
        // Restore the saved variables.
        resourceId = inState.getInt("fragmentType", R.id.img);
        if (resourceId == R.id.img) {
            loadfrag(new img_Fragment(), false);
        } else if (resourceId == R.id.vedio) {
            loadfrag(new vedio_Fragment(), false);
        } else if (resourceId == R.id.save) {
            loadfrag(new saved_Fragment(), false);
        } else {
            loadfrag(new setting_Fragment(), true);
        }
        btm_nav.setSelectedItemId(resourceId);
    }

    @Override
    protected final void onSaveInstanceState(final Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        // Save the variables.
        outState.putInt("fragmentType", resourceId);
        super.onSaveInstanceState(outState);
    }


}


