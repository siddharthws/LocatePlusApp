package com.lplus.activities.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lplus.R;
import com.lplus.activities.Extras.AppStatics;
import com.lplus.activities.Extras.TinyDB;
import com.lplus.activities.Interfaces.CategoryFetchInterface;
import com.lplus.activities.Interfaces.UtteranceCompletedListener;
import com.lplus.activities.JavaFiles.TextToSpeechClass;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Server.FilterServerClass;

import java.util.ArrayList;

public class voiceCatFetchActivity extends AppCompatActivity implements UtteranceCompletedListener, CategoryFetchInterface {
    private TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_cat_fetch);

        //fetch the Text to sppech object
        if(AppStatics.textToSpeechClass == null)
        {
            AppStatics.textToSpeechClass = new TextToSpeechClass(this, "");
            AppStatics.textToSpeechClass.SetListener(this);
        }

        ArrayList<String> categories = tinyDB.getListString(Keys.CATEGORY_VALUE);
        if (categories.size() <= 0)
        {
            //get all categories
            FilterServerClass filterServerClass = new FilterServerClass(this);
            filterServerClass.SetListener(this);
            filterServerClass.execute();
        }
        else
        {
            ToSpeak(categories);
        }
    }

    @Override
    public void onCatStatus(boolean status)
    {
        tinyDB = TinyDB.Init(this);
        ArrayList<String> categories = tinyDB.getListString(Keys.CATEGORY_VALUE);
        System.out.println("dekho categories: "+categories.toString());

        ToSpeak(categories);
    }

    public void ToSpeak(ArrayList<String> categories)
    {
        //maintain a single string
        StringBuilder sb = new StringBuilder();
        sb.append("Following are the list of Categories.");
        for(String item : categories)
        {
            sb.append(item).append("... ");
        }
        sb.append("Please select a category.");

        //speak
        AppStatics.textToSpeechClass.convert(sb.toString());
    }

    @Override
    public void onUtteranceCompleted() {
        setResult(1);
    }
}
