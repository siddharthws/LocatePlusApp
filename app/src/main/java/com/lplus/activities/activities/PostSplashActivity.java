package com.lplus.activities.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.lplus.R;
import com.lplus.activities.Dialogs.LoadingDialog;
import com.lplus.activities.Interfaces.GetMarkerInteface;
import com.lplus.activities.Server.GetMarkersServerClass;

public class PostSplashActivity extends AppCompatActivity implements GetMarkerInteface {

    private SharedPreferences app_sharePref;
    private LoadingDialog loadingDialog;
    private String loading_msg = "Fetching All Markers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_splash);

            loadingDialog = new LoadingDialog(PostSplashActivity.this, loading_msg);
            loadingDialog.ShowDialog();
            //Update Needed & fetch categories from server
            GetMarkersServerClass getMarkersServerClass = new GetMarkersServerClass(this);
            getMarkersServerClass.SetListener(this);
            getMarkersServerClass.execute();

    }

    @Override
    public void onMarkerFetched()
    {
        System.out.println("GP update complete");
        loadingDialog.HideDialog();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(PostSplashActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, 500);
    }

    @Override
    public void onMarkerFailed()
    {
        loadingDialog.HideDialog();
        Toast.makeText(this, "Failed to load Markers...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        loadingDialog.HideDialog();
        super.onPause();
    }

    @Override
    public void onResume() {
       loadingDialog.ShowDialog();
        super.onResume();
    }

}
