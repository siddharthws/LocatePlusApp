package com.lplus.activities.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.widget.Toast;

import com.lplus.R;
import com.lplus.activities.Extras.Statics;
import com.lplus.activities.Interfaces.CategoryFetchInterface;
import com.lplus.activities.Interfaces.ServerStatusInterface;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Server.FilterServerClass;
import com.lplus.activities.Server.RegisterServerClass;

public class SplashActivity extends AppCompatActivity implements ServerStatusInterface, CategoryFetchInterface {

    private SharedPreferences app_sharePref;
    private FilterServerClass filterServerClass;
    private TextView tv_splash2;
    private int statusResponseGP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tv_splash2 = findViewById(R.id.tv_splash2);

        app_sharePref = getSharedPreferences(Keys.SHARED_PREF_NAME, MODE_PRIVATE);

        //Register User
        RegisterUser();
    }

    public void RegisterUser()
    {
            String imei = app_sharePref.getString(Keys.IMEI, "");
            if( imei.equals("")) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }

                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
                imei = telephonyManager.getDeviceId();

                SharedPreferences.Editor edit = app_sharePref.edit();
                edit.putString(Keys.IMEI, imei);
                edit.commit();
                System.out.println("IMEI: "+imei);
            }

            RegisterServerClass registerServerClass = new RegisterServerClass(this);
            registerServerClass.SetListener(this);
            registerServerClass.execute();
        }

    @Override
    public void onStatusSuccess(int statusResponseFC, int statusResponseGP) {

            int storedResponseFC = app_sharePref.getInt(Keys.STORED_RESPONSE_FC, -1);
            System.out.println("STORED RESPONSE CODE FC: " + storedResponseFC);

            this.statusResponseGP = statusResponseGP;

            tv_splash2.setText("Fetching App Data...");

            if(statusResponseFC <= storedResponseFC)
            {
                //no update needed
                tv_splash2.setText("Ready to Roll The App...");

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, PostSplashActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 2000);
            }
        else
        {
            //update shared preferences
            SharedPreferences.Editor edit = app_sharePref.edit();
            edit.putInt(Keys.STORED_RESPONSE_FC, statusResponseFC);
            edit.apply();

            //Update Needed & fetch categories from server
            filterServerClass = new FilterServerClass(this);
            filterServerClass.SetListener(this);
            filterServerClass.execute();
        }
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

    @Override
    public void onCatFetched()
    {
        int storedResponseGP = app_sharePref.getInt(Keys.STORED_RESPONSE_GP, -1);

        if(statusResponseGP <= storedResponseGP)
        {
            tv_splash2.setText("Ready to Roll The App...");

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        }
        else
        {
            //update shared preferences
            SharedPreferences.Editor edit = app_sharePref.edit();
            edit.putInt(Keys.STORED_RESPONSE_GP, statusResponseGP);
            edit.apply();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, PostSplashActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        }
    }

    @Override
    public void onCatNotFetched()
    {
        //update shared preferences
        SharedPreferences.Editor edit = app_sharePref.edit();
        edit.putInt(Keys.STORED_RESPONSE_FC, -1);
        edit.apply();

        //filterLoadingDialog.HideDialog();
        Statics.onFailInit();
        tv_splash2.setText("Could not fetch the Updates.....");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, PostSplashActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
