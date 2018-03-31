package com.lplus.activities.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.Toast;

import com.lplus.R;
import com.lplus.activities.DBHelper.MarkersTable;
import com.lplus.activities.Extras.AppStatics;
import com.lplus.activities.Extras.TinyDB;
import com.lplus.activities.Interfaces.UtteranceCompletedListener;
import com.lplus.activities.JavaFiles.TextToSpeechClass;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Objects.MarkerObject;

import java.util.ArrayList;
import java.util.Locale;

public class SpeechToTextActivity extends AppCompatActivity implements UtteranceCompletedListener {
    private final int REQ_CODE_SPEECH_INPUT                         = 101;
    private final int REQ_CODE_CATEGORY_INPUT                       = 102;
    private final int REQ_CODE_CATEGORY_SELECT                      = 103;
    private final int REQ_CODE_CATEGORY_SELECT_SYNTHESIZER          = 104;
    private final int REQ_CODE_SINGLE_MARKER_SELECT_SYNTHESIZER     = 105;
    private final int REQ_CODE_MARKER_SELECT_INPUT                  = 106;
    private final int REQ_CODE_MARKER_DISPLAY_INPUT                 = 107;
    private final int REQ_CODE_MARKER_DISPLAY_SYNTHESIZER           = 108;
    private final int REQ_CODE_MARKER_CONTACT_TEST                  = 108;

    private TinyDB tinyDB;
    private int requestCodes;

    private static int currentRequestCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentRequestCode = REQ_CODE_SPEECH_INPUT;
        promptSpeechInput(currentRequestCode);
    }

    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput(int requestCode) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        try {
            startActivityForResult(intent, requestCode);
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
            case REQ_CODE_SPEECH_INPUT:
                {

                    if (AppStatics.textToSpeechClass == null) {
                        AppStatics.textToSpeechClass = new TextToSpeechClass(this, "");
                    }
                    AppStatics.textToSpeechClass.SetListener(this);


                    if (resultCode == RESULT_OK && null != data)
                    {

                        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        //get the text
                            if(result.get(0).matches("(.*)categor(.*)"))
                            {
                                //Fetch Categories
                                Intent intent = new Intent(SpeechToTextActivity.this, voiceCatFetchActivity.class);
                                startActivityForResult(intent, REQ_CODE_CATEGORY_INPUT);
                            }
                            else if(result.get(0).matches("(.*)exit(.*)") || result.get(0).matches("(.*)stop(.*)"))
                                {
                                    //Exit App
                                    String error = "Thank you for using the Voice Control of Locate Plus... App is Exiting... ";
                                    currentRequestCode = 5;
                                    AppStatics.textToSpeechClass.convert(error);
                                }
                                else if(result.get(0).matches("(.*)back(.*)") || result.get(0).matches("(.*)previous(.*)"))
                                 {
                                    //Exit App
                                    String error = "Going back to previous screen... ";
                                    currentRequestCode = REQ_CODE_SPEECH_INPUT;
                                    AppStatics.textToSpeechClass.convert(error);
                                 }
                                 else
                                {
                                    String error = "Not a valid command... Please try again... ";
                                    currentRequestCode = REQ_CODE_SPEECH_INPUT;
                                    AppStatics.textToSpeechClass.convert(error);
                                }
                    }
                    break;
                }

            case REQ_CODE_CATEGORY_INPUT:
            {
                //call to synthesizer
                promptSpeechInput(REQ_CODE_CATEGORY_SELECT_SYNTHESIZER);
                break;
            }

            //all call to synthesizer
            case REQ_CODE_CATEGORY_SELECT_SYNTHESIZER:
            {
                if (AppStatics.textToSpeechClass == null) {
                    AppStatics.textToSpeechClass = new TextToSpeechClass(this, "");
                }
                AppStatics.textToSpeechClass.SetListener(this);

                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                Toast.makeText(this, result.get(0), Toast.LENGTH_LONG).show();
                //fetch all categories list
                tinyDB = TinyDB.Init(this);
                ArrayList<String> categories = tinyDB.getListString(Keys.CATEGORY_VALUE);
                int flag = 0;
                int i;
                String item;
                for (i = 0; i < categories.size(); i++)
                {
                    item = categories.get(i);
                    if(item.equalsIgnoreCase(result.get(0)))
                    {
                        flag = 1;
                        break;
                    }
                }
                    if(flag == 1)
                    {
                        Intent intents = new Intent(SpeechToTextActivity.this, VoiceMarkerFetchActivity.class);
                        intents.putExtra("selected_category", categories.get(i));
                        startActivityForResult(intents, REQ_CODE_MARKER_SELECT_INPUT);
                    }
                    else if(result.get(0).matches("(.*)exit(.*)") || result.get(0).matches("(.*)stop(.*)"))
                    {
                        //Exit App
                        String error = "Thank you for using the Voice Control of Locate Plus... App is Exiting... ";
                        currentRequestCode = 5;
                        AppStatics.textToSpeechClass.convert(error);
                    }
                    else if(result.get(0).matches("(.*)back(.*)") || result.get(0).matches("(.*)previous(.*)"))
                    {
                        //Exit App
                        String error = "Going back to previous screen... ";
                        currentRequestCode = REQ_CODE_CATEGORY_SELECT_SYNTHESIZER;
                        AppStatics.textToSpeechClass.convert(error);
                    }
                    else
                    {
                        String error = "Not a valid command... Please try again... ";
                        currentRequestCode = REQ_CODE_CATEGORY_SELECT_SYNTHESIZER;
                        AppStatics.textToSpeechClass.convert(error);
                    }
                break;
             }

            case REQ_CODE_MARKER_SELECT_INPUT:
            {
                //call to synthesizer
                promptSpeechInput(REQ_CODE_SINGLE_MARKER_SELECT_SYNTHESIZER);
                break;
            }

            //all call to synthesizer
            case REQ_CODE_SINGLE_MARKER_SELECT_SYNTHESIZER:
            {
                if (AppStatics.textToSpeechClass == null) {
                    AppStatics.textToSpeechClass = new TextToSpeechClass(this, "");
                }
                AppStatics.textToSpeechClass.SetListener(this);


                ArrayList<String> selected_marker = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                int selected_marker_index = Integer.parseInt(selected_marker.get(0));

                //fetch all categories list
                MarkersTable markersTable = new MarkersTable(this);
                ArrayList<MarkerObject> selected_objects = markersTable.GetMarkersByCategory(selected_marker.get(0));

               if(selected_marker_index <= selected_objects.size())
               {
                   //speak place description
                   Intent intents = new Intent(SpeechToTextActivity.this, VoiceMarkerDescriptionActivity.class);
                  tinyDB = TinyDB.Init(this);
                  MarkerObject markerObject = selected_objects.get(selected_marker_index-1);
                  tinyDB.putObject(Keys.VOICE_SELECTED_MARKER, markerObject);
                   startActivityForResult(intents, REQ_CODE_MARKER_DISPLAY_INPUT);
               }
               else if(selected_marker.get(0).matches("(.*)exit(.*)") || selected_marker.get(0).matches("(.*)stop(.*)"))
               {
                   //Exit App
                   String error = "Thank you for using the Voice Control of Locate Plus... App is Exiting... ";
                   currentRequestCode = 5;
                   AppStatics.textToSpeechClass.convert(error);
               }
               else if(selected_marker.get(0).matches("(.*)back(.*)") || selected_marker.get(0).matches("(.*)previous(.*)"))
               {
                   //Exit App
                   String error = "Going back to previous screen... ";
                   currentRequestCode = REQ_CODE_SINGLE_MARKER_SELECT_SYNTHESIZER;
                   AppStatics.textToSpeechClass.convert(error);
               }
               else
               {
                   String error = "Not a valid command... Please try again... ";
                   currentRequestCode = REQ_CODE_SINGLE_MARKER_SELECT_SYNTHESIZER;
                   AppStatics.textToSpeechClass.convert(error);
               }
                break;
            }

            case REQ_CODE_MARKER_DISPLAY_INPUT:
            {
                //call to synthesizer
                promptSpeechInput(REQ_CODE_MARKER_DISPLAY_SYNTHESIZER);
                break;
            }

            //all call to synthesizer
            case REQ_CODE_MARKER_DISPLAY_SYNTHESIZER:
            {
                if (AppStatics.textToSpeechClass == null) {
                    AppStatics.textToSpeechClass = new TextToSpeechClass(this, "");
                }
                AppStatics.textToSpeechClass.SetListener(this);


                ArrayList<String> marker_contact = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);


              /*  //fetch all categories list
                MarkersTable markersTable = new MarkersTable(this);
                ArrayList<MarkerObject> selected_objects = markersTable.GetMarkersByCategory(selected_marker.get(0));*/

                if(marker_contact.get(0).equalsIgnoreCase("(.*)contact(.*)"))
                {
                   /* //speak place description
                    Intent intents = new Intent(SpeechToTextActivity.this, VoiceContactActivity.class);
                    tinyDB = TinyDB.Init(this);
                    MarkerObject markerObject = selected_objects.get(selected_marker_index-1);
                    tinyDB.putObject(Keys.VOICE_SELECTED_MARKER, markerObject);
                    startActivityForResult(intents, REQ_CODE_MARKER_DISPLAY_INPUT);*/
                }
                else if(marker_contact.get(0).matches("(.*)exit(.*)") || marker_contact.get(0).matches("(.*)stop(.*)"))
                {
                    //Exit App
                    String error = "Thank you for using the Voice Control of Locate Plus... App is Exiting... ";
                    currentRequestCode = 5;
                    AppStatics.textToSpeechClass.convert(error);
                }
                else if(marker_contact.get(0).matches("(.*)back(.*)") || marker_contact.get(0).matches("(.*)previous(.*)"))
                {
                    //Exit App
                    String error = "Going back to previous screen... ";
                    currentRequestCode = REQ_CODE_MARKER_CONTACT_TEST;
                    AppStatics.textToSpeechClass.convert(error);
                }
                else
                {
                    String error = "Not a valid command... Please try again... ";
                    currentRequestCode = REQ_CODE_MARKER_CONTACT_TEST;
                    AppStatics.textToSpeechClass.convert(error);
                }
                break;
            }
        }
    }

    @Override
    public void onUtteranceCompleted()
    {
        if(currentRequestCode == 5)
        {
            AppStatics.textToSpeechClass.closeSpeech();
            System.exit(1);
        }
        else
        {
            promptSpeechInput(currentRequestCode);
        }
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
