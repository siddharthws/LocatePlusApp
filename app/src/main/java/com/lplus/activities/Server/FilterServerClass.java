package com.lplus.activities.Server;

import android.content.Context;
import android.content.SharedPreferences;

import com.lplus.activities.Extras.Statics;
import com.lplus.activities.Interfaces.CategoryFetchInterface;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Macros.UrlMappings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Sai_Kameswari on 19-03-2018.
 */

public class FilterServerClass extends BaseServerClass {

    private final String DUMMY_KEY = "dummy_key";
    private Context context;
    private String loading_msg = "Fetching Filter Types";
    private final String APP_ID = "app_id";
    private SharedPreferences app_sharePref;

    private CategoryFetchInterface listener = null;

    public void SetListener(CategoryFetchInterface listener) {
        this.listener = listener;
    }

    public FilterServerClass(Context context) {
        super(context, UrlMappings.FILTER_FETCH);
        this.context = context;
        app_sharePref = context.getSharedPreferences(Keys.SHARED_PREF_NAME, MODE_PRIVATE);

    }

    @Override
    public Void doInBackground(Void... params) {

        // Init Request JSON
        JSONObject requestJson = new JSONObject();

        try
        {
            requestJson.put(APP_ID, String.valueOf(app_sharePref.getInt(APP_ID, -1)));
        }
        catch (JSONException e)
        {
            return null;
        }
        // Call Super
        super.doInBackground(params);
        return null;
    }

    @Override
    public void onPostExecute(Void result) {
        // Register user in preferences if server returned OK
        if (IsResponseValid()) {
            try
            {
                System.out.println("Response JSON fetched");
                JSONArray categories = responseJson.getJSONArray(Keys.CATEGORIES);
                JSONArray facilities = responseJson.getJSONArray(Keys.FACILITIES);
                Statics.Init(context, categories, facilities);
                listener.onCatFetched();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            listener.onCatNotFetched();
        }

    }
}
