package com.lplus.activities.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.Toast;

import com.lplus.R;
import com.lplus.activities.Extras.AppStatics;
import com.lplus.activities.Interfaces.UtteranceCompletedListener;
import com.lplus.activities.JavaFiles.TextToSpeechClass;

import java.util.ArrayList;
import java.util.Locale;

public class SpeechToTextActivity extends AppCompatActivity implements UtteranceCompletedListener {
    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        promptSpeechInput();
    }

    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //get the text
                        if(result.get(0).matches("(.*)categor(.*)"))
                        {
                            //Fetch Categories
                            Intent intent = new Intent(SpeechToTextActivity.this, voiceCatFetchActivity.class);
                            startActivityForResult(intent, 1);
                            finish();
                        }
                        else
                        {
                            //fetch the Text to sppech object
                            if(AppStatics.textToSpeechClass == null)
                            {
                                AppStatics.textToSpeechClass = new TextToSpeechClass(this, "");
                                AppStatics.textToSpeechClass.SetListener(this);
                            }
                            String error = "Not a valid command... Please try again... ";
                            AppStatics.textToSpeechClass.convert(error);
                        }
                    }
                break;
                }

            case 1:
            {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                Toast.makeText(SpeechToTextActivity.this, result.get(0), Toast.LENGTH_LONG).show();
                break;
            }

        }
    }

    @Override
    public void onUtteranceCompleted()
    {
       promptSpeechInput();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
