package com.example.nmi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;



import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class SplashScreen extends AppCompatActivity {

    public static final int MULTIPLE_PERMISSIONS = 10;
    List<String> listPermissionsNeeded = new ArrayList<>();
    ProgressBar pro;

    String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA};  //ADD WHATEVER PERMISSIONS YOU NEEDED


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

       try {
           pro = findViewById(R.id.proId);
           for (int i = 20; i <= 100; i = i + 20) {
               pro.setProgress(i);
           }
           call_permissions();


       }
       catch ( Exception e){

       }


    }
    private void call_permissions() {
        int result;

        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getApplicationContext(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);

            } else{


            }

        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
        }
        else {
            Intent in = new Intent(getApplicationContext(), SignIn.class);
            startActivity(in);
        }
        return;

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length >= 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED  &&grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // permissions granted.
                    Intent in = new Intent(getApplicationContext(), SignIn.class);
                   startActivity(in);
                } else {
                    // no permissions granted.
                    ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                            (new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
                }
                return;
            }
        }
    }
}
