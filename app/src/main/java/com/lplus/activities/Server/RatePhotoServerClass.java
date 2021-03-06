package com.lplus.activities.Server;

import android.content.Context;

import com.lplus.activities.Interfaces.RatePhotosInterface;
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

public class RatePhotoServerClass extends BaseServerClass {
    private Context context;
    private MarkerObject markerObject;
    private ArrayList<String> photo_rate;
    private ArrayList<String> photo_uuid;

    private RatePhotosInterface listener = null;
    public void SetListener(RatePhotosInterface listener)
    {
        this.listener = listener;
    }

    public RatePhotoServerClass(Context context, MarkerObject markerObject, ArrayList<String> photo_rate, ArrayList<String> photo_uuid)
    {
        super(context, UrlMappings.RATE_PHOTOS);
        this.context = context;
        this.markerObject = markerObject;
        this.photo_rate = photo_rate;
        this.photo_uuid = photo_uuid;
    }

    @Override
    public Void doInBackground (Void... params)
    {
        // Init Request JSON
        JSONObject requestJson = new JSONObject();

        try
        {
            requestJson.put(Keys.MARKER_ID, markerObject.getMarkerID());
            requestJson.put(Keys.RATE_PHOTO, toPhotoJSONArray());
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
                    listener.onPhotoSent(true);
                }
                else
                {
                    listener.onPhotoSent(false);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            listener.onPhotoSent(false);
        }
    }

    private JSONArray toPhotoJSONArray()
    {
        JSONObject fac;
        JSONArray facilitiesJSON = new JSONArray();
        try {
            for(int i = 0;i<photo_uuid.size();i++)
            {
                fac = new JSONObject();
                fac.put(Keys.RATE_PHOTO_UUID, photo_uuid.get(i));
                fac.put(Keys.RATE_PHOTO_RATE, photo_rate.get(i));
                facilitiesJSON.put(fac);
            }
            System.out.println("json Photos "+facilitiesJSON.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return facilitiesJSON;
    }
}
