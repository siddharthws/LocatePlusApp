package com.lplus.activities.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.lplus.R;
import com.lplus.activities.Extras.AppStatics;
import com.lplus.activities.Interfaces.UtteranceCompletedListener;
import com.lplus.activities.JavaFiles.TextToSpeechClass;

public class VoiceActivity extends AppCompatActivity implements View.OnClickListener, UtteranceCompletedListener {

    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);

        //fetch the Text to sppech object
        if(AppStatics.textToSpeechClass == null)
        {
            AppStatics.textToSpeechClass = new TextToSpeechClass(this, "");
            AppStatics.textToSpeechClass.SetListener(this);
        }

        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
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
}
