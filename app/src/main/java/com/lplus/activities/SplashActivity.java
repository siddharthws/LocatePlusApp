package com.lplus.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.lplus.R;
import com.lplus.activities.Dialogs.LoadingDialog;
import com.lplus.activities.Interfaces.RegisterClassInterface;
import com.lplus.activities.Server.RegisterServerClass;

public class SplashActivity extends AppCompatActivity implements RegisterClassInterface {

    private SharedPreferences app_sharePref;
    private final String APP_ID = "app_id";
    private final String IMEI = "imei";
    private final String APP_UNAME = "name";
    private final int INVALID = -1;
    private final int REQUEST_PERMISSION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        app_sharePref = getSharedPreferences("app_details", MODE_PRIVATE);


        //check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSION);
            return;
        }
        RegisterUser();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    RegisterUser();
                } else {
                    System.exit(1);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    @Override
    public void onRegisterSuccess() {
        int id = app_sharePref.getInt(APP_ID, -1);
        String name = app_sharePref.getString(APP_UNAME, "");

        System.out.println("ID: " + id);
        System.out.println("NAme: " + name);

        Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    public void onRegisterFailure() {
        Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                System.exit(1);
            }
        }, 2000);

    }

    @SuppressLint("HardwareIds")
    public void RegisterUser()
    {
            if(app_sharePref.getInt(APP_ID, -1) == INVALID)
            {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(this.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();

            SharedPreferences.Editor edit = app_sharePref.edit();
            edit.putString(IMEI, imei);
            edit.commit();

            System.out.println("IMEI: "+imei);

            RegisterServerClass registerServerClass = new RegisterServerClass(this);
            registerServerClass.SetListener(this);
            registerServerClass.execute();
        }
        else
        {
            int id = app_sharePref.getInt(APP_ID, -1);
            String name = app_sharePref.getString(APP_UNAME, "");

            System.out.println("ID: "+id);
            System.out.println("NAme: "+name);

            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }
}
