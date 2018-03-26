package com.lplus.activities.Server;

import android.content.Context;

import com.lplus.activities.Interfaces.MarkerReviewInterface;
import com.lplus.activities.Interfaces.RatePlaceInterface;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Macros.UrlMappings;
import com.lplus.activities.Objects.MarkerObject;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;

/**
 * Created by CHANDEL on 3/25/2018.
 */

public class RatePlaceServerClass extends BaseServerClass {
    private Context context;
    private String ratePlace;
    private MarkerObject markerObject;

    private RatePlaceInterface listener = null;
    public void SetListener(RatePlaceInterface listener)
    {
        this.listener = listener;
    }

    public RatePlaceServerClass(Context context, MarkerObject markerObject, String ratePlace)
    {
        super(context, UrlMappings.RATE_PLACE);
        this.context = context;
        this.markerObject = markerObject;
        this.ratePlace = ratePlace;
    }

    @Override
    public Void doInBackground (Void... params)
    {
        // Init Request JSON
        JSONObject requestJson = new JSONObject();

        try
        {
            requestJson.put(Keys.MARKER_ID, markerObject.getMarkerID());
            requestJson.put(Keys.RATE_PLACE, ratePlace);
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
                    listener.onRatePlaceSuccess();
                }
                else
                {
                    listener.onRatePlaceFailed();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            listener.onRatePlaceFailed();
        }
    }
}
