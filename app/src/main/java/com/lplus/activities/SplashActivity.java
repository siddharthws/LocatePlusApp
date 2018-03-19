package com.lplus.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.lplus.R;
import com.lplus.activities.Dialogs.LoadingDialog;
import com.lplus.activities.Interfaces.CategoryFetchInterface;
import com.lplus.activities.Interfaces.ServerStatusInterface;
import com.lplus.activities.Server.FilterServerClass;
import com.lplus.activities.Server.RegisterServerClass;

public class SplashActivity extends AppCompatActivity implements ServerStatusInterface, CategoryFetchInterface {

    private SharedPreferences app_sharePref;
    private final String APP_ID = "app_id";
    private final String IMEI = "imei";
    private final String APP_UNAME = "name";
    private final int INVALID = -1;
    private static final int REQUEST_PERMISSION = 5;
    private FilterServerClass filterServerClass;
    private LoadingDialog loadingDialog, filterLoadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

      //  loadingDialog = new LoadingDialog(this, "Registering App");
      //  filterLoadingDialog = new LoadingDialog(this, "Fetching Categories");
        app_sharePref = getSharedPreferences("app_details", MODE_PRIVATE);
        checkForPermissions();
    }

    @Override
    public void onStatusSuccess() {
       // loadingDialog.HideDialog();

      //  filterLoadingDialog.ShowDialog();
        int id = app_sharePref.getInt(APP_ID, -1);
        String name = app_sharePref.getString(APP_UNAME, "");

        System.out.println("ID: " + id);
        System.out.println("NAme: " + name);

        Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();

        //fetch categories from server
        filterServerClass = new FilterServerClass(this);
        filterServerClass.SetListener(this);
        filterServerClass.execute();
    }

    @Override
    public void onStatusFailure() {
       // loadingDialog.HideDialog();
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

          //  loadingDialog.ShowDialog();

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

           // filterLoadingDialog.ShowDialog();
            Toast.makeText(this, "About to fetch", Toast.LENGTH_SHORT).show();
            //fetch categories from server
            filterServerClass = new FilterServerClass(this);
            filterServerClass.SetListener(this);
            filterServerClass.execute();
        }
    }

    public void checkForPermissions()
    {
        System.out.println("Checking for permissions.......");
        //check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            // request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{   Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
            return;
        }
        onPermissionSuccess();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("permissions granted.......");
                    onPermissionSuccess();

                } else {
                    onPermissionFailure();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    public void onPermissionSuccess() {
        RegisterUser();
    }

    public void onPermissionFailure() {
        System.exit(1);
    }

    @Override
    public void onCatFetched()
    {
        System.out.println("Categories loaded");
       // filterLoadingDialog.HideDialog();
        Toast.makeText(this, "Fetched categories", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCatNotFetched()
    {
        //filterLoadingDialog.HideDialog();
        Statics.onFailInit();
        Toast.makeText(this, "Failed to fetch categories", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
