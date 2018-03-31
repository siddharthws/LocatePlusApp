package com.lplus.activities.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
    private StringBuilder sb;
    private ArrayList<String> categories;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_cat_fetch);

        listView =  findViewById(R.id.lvitems);

        categories = new ArrayList<>();

        //fetch the Text to sppech object
        if(AppStatics.textToSpeechClass == null)
        {
            AppStatics.textToSpeechClass = new TextToSpeechClass(this, "");
        }
        AppStatics.textToSpeechClass.SetListener(this);
        tinyDB = TinyDB.Init(this);
       categories = tinyDB.getListString(Keys.CATEGORY_VALUE);
        if (categories.size() <= 0)
        {
            //get all categories
            FilterServerClass filterServerClass = new FilterServerClass(this);
            filterServerClass.SetListener(this);
            filterServerClass.execute();
        }
        else
        {
            //maintain a single string
            sb = new StringBuilder();
            sb.append("Following are the list of Categories.");
            for(String item : categories)
            {
                sb.append(item).append("... ");
            }
            sb.append("Please select a category.");
            ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);
            listView.setAdapter(itemsAdapter);
            ToSpeak(sb);
        }
    }

    @Override
    public void onCatStatus(boolean status)
    {
        if (status)
        {
            categories = tinyDB.getListString(Keys.CATEGORY_VALUE);
            System.out.println("dekho categories: "+categories.toString());

            //maintain a single string
            sb = new StringBuilder();
            sb.append("Following are the list of Categories.");
            for(String item : categories)
            {
                sb.append(item).append("... ");
            }
            sb.append("Please select a category.");
            ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);
            listView.setAdapter(itemsAdapter);
            ToSpeak(sb);
        }
        else
        {
            sb = new StringBuilder();
            sb.append("Unable to fetch the Categories from Server..Try again Later");
            ToSpeak(sb);
        }

    }

    public void ToSpeak(StringBuilder sb)
    {
        System.out.println("reaching");

        AppStatics.textToSpeechClass.convert(sb.toString());
    }

    @Override
    public void onUtteranceCompleted() {
        System.out.println("yet to change the request codes");
        setResult(1);
        finish();
    }
}
