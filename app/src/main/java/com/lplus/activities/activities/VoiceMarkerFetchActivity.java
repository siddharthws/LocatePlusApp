package com.lplus.activities.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lplus.R;
import com.lplus.activities.DBHelper.MarkersTable;
import com.lplus.activities.Extras.AppStatics;
import com.lplus.activities.Extras.TinyDB;
import com.lplus.activities.Interfaces.GetMarkerInteface;
import com.lplus.activities.Interfaces.UtteranceCompletedListener;
import com.lplus.activities.JavaFiles.TextToSpeechClass;
import com.lplus.activities.Objects.MarkerObject;
import com.lplus.activities.Server.GetMarkersServerClass;

import java.util.ArrayList;

public class VoiceMarkerFetchActivity extends AppCompatActivity implements GetMarkerInteface, UtteranceCompletedListener {

    private String selected_category;
    private ArrayList<MarkerObject> selected_objects;
    private StringBuilder sb;
    private TinyDB tinyDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_marker_fetch);
        tinyDB = TinyDB.Init(this);

        Intent intent = getIntent();
        selected_category = intent.getStringExtra("selected_category");
        sb = new StringBuilder();


        if(AppStatics.textToSpeechClass == null)
        {
            AppStatics.textToSpeechClass = new TextToSpeechClass(this, "");
        }
        AppStatics.textToSpeechClass.SetListener(this);

        GetMarkersServerClass getMarkersServerClass = new GetMarkersServerClass(this);
        getMarkersServerClass.SetListener(this);
        getMarkersServerClass.execute();
    }

    @Override
    public void onMarkerFetchStatus(boolean status) {
        if (status)
        {
            MarkersTable markersTable = new MarkersTable(this);
            selected_objects = markersTable.GetMarkersByCategory(selected_category);
            sb.append("Fetched All markers for the selected Category... ");
            sb.append("The Total number of markers around you are... ");
            sb.append(String.valueOf(selected_objects.size()));
            sb.append("Please say the marker position to know the details of the Place");
            ToSpeak(sb);
        }
        else
        {
            sb.append("Sorry... ");
            sb.append("Cannot fetch the markers due to network connection failure... ");
            sb.append("Please try again... ");
            ToSpeak(sb);
        }
    }

    public void ToSpeak(StringBuilder sb)
    {
       //speak
        AppStatics.textToSpeechClass.convert(sb.toString());
    }

    @Override
    public void onUtteranceCompleted() {
        setResult(1);
        finish();
    }
}
