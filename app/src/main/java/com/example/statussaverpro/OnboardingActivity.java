package com.example.statussaverpro;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.statussaverpro.Models.OnboardingItem;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OnboardingActivity extends AppCompatActivity {
    private OnboardingAdapter onboardingAdapter;
    private LinearLayout layoutOnboardingIndicators ;

    private MaterialButton buttonOnboardingAction;

    private static final int REQUEST_PERMISSIONS = 1234;
    private static final String[] PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private Context context;

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {

                    Intent data = result.getData();

                    assert data != null;

                    Log.d("HEY: ", data.getData().toString());

                    context.getContentResolver().takePersistableUriPermission(
                            data.getData(),
                            Intent.FLAG_GRANT_READ_URI_PERMISSION |
                                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();

                }
            }
    );

    private final Handler handler = new Handler();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //      this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        buttonOnboardingAction = findViewById(R.id.buttonOnboardingAction);

        layoutOnboardingIndicators=findViewById(R.id.layoutOnboardingIndicators);


        setupOnboardingItems();

        ViewPager2 onboardingViewPager =findViewById(R.id.onboardingViewPager);
        onboardingViewPager.setAdapter(onboardingAdapter);

        setupOnboardingIndicators();

        setCurrentOnboardingIndicator(0);

        onboardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnboardingIndicator(position);
            }
        });

        buttonOnboardingAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onboardingViewPager.getCurrentItem()+1 < onboardingAdapter.getItemCount())
                {
                    onboardingViewPager.setCurrentItem(onboardingViewPager.getCurrentItem()+1);
                }
                else
                {
//                    SharedPreferences pref3 = getSharedPreferences("onboarding", MODE_PRIVATE);
//                    SharedPreferences.Editor editor = pref3.edit();
//                    editor.putBoolean("flag4", true);
//                    editor.apply();
//                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                    finish();


                    context = getApplicationContext();

                    if (!arePermissionDenied()) {
                        next();
                        return;
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && arePermissionDenied()) {

                        // If Android 10+
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            requestPermissionQ();
                            return;
                        }

                        requestPermissions(PERMISSIONS, REQUEST_PERMISSIONS);
                    }

                }
            }
        });
    }

    private void setupOnboardingItems() {
        List<OnboardingItem> onboardingItems= new ArrayList<>();

        OnboardingItem itemPayOnline= new OnboardingItem();
//        itemPayOnline.setTitle("Medicare In Your Pocket");
//        itemPayOnline.setDescription("Get Prescription From Well-Known Doctors Across The World");
        itemPayOnline.setImage(R.drawable.oneimg);

        OnboardingItem itemOnTheWay = new OnboardingItem();
//        itemOnTheWay.setTitle("Unlock Your Beauty Secrets");
//        itemOnTheWay.setDescription("Enhance Your Beauty, Effortlessly. Your Aesthetic Journey Starts Here");
        itemOnTheWay.setImage(R.drawable.twoimg);

        OnboardingItem itemEatTogether = new OnboardingItem();
//        itemEatTogether.setTitle("Embrace Beauty, Inside and Out");
//        itemEatTogether.setDescription("Cosmetic Care, Reinvented. Unlock the Best Version of You with Our App.");
        itemEatTogether.setImage(R.drawable.threeimg);

        onboardingItems.add(itemPayOnline);
        onboardingItems.add(itemOnTheWay);
        onboardingItems.add(itemEatTogether);

        onboardingAdapter=new OnboardingAdapter(onboardingItems);
    }

    private void setupOnboardingIndicators(){
        ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8,0,8,0);

        for(int i=0;i<indicators.length;i++)
        {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplication(),
                    R.drawable.onboarding_indicator_active
            ));
            indicators[i].setLayoutParams(layoutParams);
            layoutOnboardingIndicators.addView(indicators[i]);
        }
    }
    private void setCurrentOnboardingIndicator(int index){
        int childCount = layoutOnboardingIndicators.getChildCount();

        for(int i=0;i< childCount;i++)
        {
            ImageView imageView = (ImageView) layoutOnboardingIndicators.getChildAt(i);
            if(i==index)
            {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_indicator_active)
                );
            }
            else
            {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_indicator_inactive)
                );
            }
        }
        if(index == onboardingAdapter.getItemCount() - 1)
        {
            buttonOnboardingAction.setText("Start");
        }
        else {
            buttonOnboardingAction.setText("Next");
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        if (!arePermissionDenied()) {
            next();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS && grantResults.length > 0) {
            if (arePermissionDenied()) {
                // Clear Data of Application, So that it can request for permissions again
                ((ActivityManager) Objects.requireNonNull(this.getSystemService(ACTIVITY_SERVICE))).clearApplicationUserData();
                recreate();
            } else {

                   next();
            }
        }
    }

    private void next() {

        SharedPreferences pref1 = getSharedPreferences("Onboardin", MODE_PRIVATE);
        pref1.getBoolean("flag", false);
        SharedPreferences.Editor editor = pref1.edit();
        editor.putBoolean("flag",true);
        editor.apply();
        Toast.makeText(context, "harsh", Toast.LENGTH_SHORT).show();

        handler.postDelayed(() -> {
            startActivity(new Intent(OnboardingActivity.this,MainActivity.class));
            finish();
        }, 0000);
    }


    private boolean arePermissionDenied() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return getContentResolver().getPersistedUriPermissions().size() <= 0;
        }

        for (String permissions : PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), permissions) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }
}
