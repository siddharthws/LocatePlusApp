package com.lplus.activities.JavaFiles;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.lplus.activities.Interfaces.UtteranceCompletedListener;

import java.util.HashMap;
import java.util.Locale;

public class TextToSpeechClass implements TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener {

    private TextToSpeech textToSpeech;
    private Context context;
    private UtteranceCompletedListener listenerr = null;
    HashMap<String, String> myHash;

    public void SetListener(UtteranceCompletedListener listener)
    {
        this.listenerr = listener;
    }

    public TextToSpeechClass(Context context, String text)
    {
        this.context = context;
        textToSpeech = new TextToSpeech(context,this);
    }

    public void convert(String toSay)
    {
        textToSpeech.speak(toSay, TextToSpeech.QUEUE_ADD, myHash);
    }

    @Override
    public void onInit(int status)
    {

        if (status == TextToSpeech.SUCCESS) {

            int result =    textToSpeech.setLanguage(Locale.ENGLISH);
                            textToSpeech.setSpeechRate(3.0f);


            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }
            else
            {
                myHash = new HashMap<>();
                myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"done");
                textToSpeech.setOnUtteranceCompletedListener(this);
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    public void closeSpeech()
    {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    @Override
    public void onUtteranceCompleted(String s) {

        if (s.equalsIgnoreCase("done")) {
            listenerr.onUtteranceCompleted();
        }
    }
}
