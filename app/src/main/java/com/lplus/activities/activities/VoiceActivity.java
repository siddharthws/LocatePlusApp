package com.lplus.activities.activities;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.lplus.R;
import com.lplus.activities.Extras.AppStatics;
import com.lplus.activities.Extras.CheckGPSOn;
import com.lplus.activities.Extras.TinyDB;
import com.lplus.activities.Interfaces.UtteranceCompletedListener;
import com.lplus.activities.JavaFiles.TextToSpeechClass;
import com.lplus.activities.Macros.Keys;

public class VoiceActivity extends AppCompatActivity implements View.OnClickListener, UtteranceCompletedListener, CheckGPSOn.CheckGPSInterface {

    private Button startButton;
    final int REQUEST_LOCATION = 199;
    private FusedLocationProviderClient mFusedLocationClient;
    private static LatLng currentLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);


        startButton = findViewById(R.id.startButton);

        //fetch the Text to sppech object
        if(AppStatics.textToSpeechClass == null)
        {
            AppStatics.textToSpeechClass = new TextToSpeechClass(this, "");
            AppStatics.textToSpeechClass.SetListener(this);
        }

        CheckGPSOn checkGPSOn = new CheckGPSOn(this);
        checkGPSOn.setListener(this);
        checkGPSOn.Init();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.startButton:
            {
                StringBuilder sb = new StringBuilder();
                sb.append("Welcome to Locate Plus. ").append("... ").append("... Please say a command");
                AppStatics.textToSpeechClass.convert(sb.toString());
                break;
            }
        }
    }

    @Override
    public void onUtteranceCompleted()
    {
        Intent intent = new Intent(VoiceActivity.this, SpeechToTextActivity.class);
        startActivity(intent);
    }

    @Override
    public void GPSResult(int statusCode, Status resultStatus) {
        switch (statusCode)
        {
            case 1: {getCurrentLocation(); break;}
            case 2:
            {
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    resultStatus.startResolutionForResult(
                            VoiceActivity.this,REQUEST_LOCATION);
                } catch (IntentSender.SendIntentException e) {
                    // Ignore the error.
                }
                break;
            }
            case 3: break;
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (mFusedLocationClient != null) {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        System.out.println("location: "+location.getLatitude());
                        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                    }
                }
            });
        }
        //initialise buttons
        startButton.setOnClickListener(this);

        //Save Current Location in TinyDb
        TinyDB tinyDB = TinyDB.Init(VoiceActivity.this);
        tinyDB.putInt("requestCodes", 101);
        tinyDB.putObject(Keys.VOICE_CURRENT_LOCATION, currentLocation);
    }
}
