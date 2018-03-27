package com.lplus.activities.Server;

import android.content.Context;

import com.lplus.activities.Interfaces.RateFacillityInterface;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Macros.UrlMappings;
import com.lplus.activities.Objects.MarkerObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;

/**
 * Created by CHANDEL on 3/25/2018.
 */

public class RateFacilityServerClass extends BaseServerClass {
    private Context context;
    private ArrayList<String> fac_ids;
    private ArrayList<String> fac_rates;
    private MarkerObject markerObject;

    private RateFacillityInterface listener = null;
    public void SetListener(RateFacillityInterface listener)
    {
        this.listener = listener;
    }

    public RateFacilityServerClass(Context context, MarkerObject markerObject, ArrayList<String> fac_ids, ArrayList<String> fac_rates)
    {
        super(context, UrlMappings.RATE_FACILITY);
        this.context = context;
        this.fac_ids = fac_ids;
        this.markerObject = markerObject;
        this.fac_rates = fac_rates;
    }

    @Override
    public Void doInBackground (Void... params)
    {
        // Init Request JSON
        JSONObject requestJson = new JSONObject();

        try
        {
            requestJson.put(Keys.MARKER_ID, markerObject.getMarkerID());
            requestJson.put(Keys.RATE_FAC,         toFacJSONArray());
        }
        catch (JSONException e)
        {
            return null;
        }

        // Add data to request Builder
        requestBuilder.method("POST", RequestBody.create(JSON, requestJson.toString()));

        // Call Super
        super.doInBackground(params);
        return null;
    }

    @Override
    public void onPostExecute (Void result)
    {

        // Register user in preferences if server returned OK
        if (IsResponseValid()) {
            try {

                boolean sucess = responseJson.getBoolean(Keys.AP_RESPONSE);
                if(sucess)
                {
                    listener.onFacilityFetchStatus(true);
                }
                else
                {
                    listener.onFacilityFetchStatus(false);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            listener.onFacilityFetchStatus(false);
        }
    }

    private JSONArray toFacJSONArray()
    {
        JSONObject fac = null;
        JSONArray facilitiesJSON = new JSONArray();
        try {
            for(int i = 0;i<fac_ids.size();i++)
            {
                fac = new JSONObject();
                fac.put(Keys.RATE_FAC_ID, fac_ids.get(i));
                fac.put(Keys.RATE_FAC_RATE, fac_rates.get(i));
                facilitiesJSON.put(fac);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return facilitiesJSON;
    }
}
