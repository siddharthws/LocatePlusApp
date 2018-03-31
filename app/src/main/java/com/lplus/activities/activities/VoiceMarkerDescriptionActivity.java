package com.lplus.activities.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lplus.R;
import com.lplus.activities.Extras.AppStatics;
import com.lplus.activities.Extras.TinyDB;
import com.lplus.activities.Interfaces.UtteranceCompletedListener;
import com.lplus.activities.JavaFiles.TextToSpeechClass;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Objects.MarkerObject;

import java.util.ArrayList;

public class VoiceMarkerDescriptionActivity extends AppCompatActivity implements UtteranceCompletedListener {

    private String selected_category;
    private ArrayList<MarkerObject> selected_objects;
    private StringBuilder sb;
    private TinyDB tinyDB;
    private MarkerObject markerObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_marker_description);

        tinyDB = TinyDB.Init(this);

        markerObject = tinyDB.getObject(Keys.VOICE_SELECTED_MARKER, MarkerObject.class);


        sb = new StringBuilder();

        if(AppStatics.textToSpeechClass == null)
        {
            AppStatics.textToSpeechClass = new TextToSpeechClass(this, "");
        }
        AppStatics.textToSpeechClass.SetListener(this);
    }

    public void onMarkerDescription() {


            sb.append("Following is the description of the marker... ");

            sb.append("Place Name is... ");
            sb.append(markerObject.getMarkerName());

            sb.append("Place Address is... ");
            sb.append(markerObject.getMarkerAddress());

            sb.append("Place Description is... ");
            sb.append(markerObject.getMarkerDescription());


            sb.append("Please say command contact to call the given contact number");
            ToSpeak(sb);
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
