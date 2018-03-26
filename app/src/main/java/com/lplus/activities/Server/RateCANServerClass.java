package com.lplus.activities.Server;

import android.content.Context;

import com.lplus.activities.Interfaces.MarkerReviewInterface;
import com.lplus.activities.Interfaces.RateCANInterface;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Macros.UrlMappings;
import com.lplus.activities.Objects.MarkerObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;

/**
 * Created by CHANDEL on 3/25/2018.
 */

public class RateCANServerClass extends BaseServerClass {
    private Context context;
    private MarkerObject markerObject;
    private ArrayList<String> canRate;

    private RateCANInterface listener = null;
    public void SetListener(RateCANInterface listener)
    {
        this.listener = listener;
    }

    public RateCANServerClass(Context context, MarkerObject markerObject, ArrayList<String> canRate)
    {
        super(context, UrlMappings.RATE_CAN);
        this.context = context;
        this.markerObject = markerObject;
        this.canRate = canRate;
    }

    @Override
    public Void doInBackground (Void... params)
    {
        // Init Request JSON
        JSONObject requestJson = new JSONObject();

        try
        {
            requestJson.put(Keys.MARKER_ID, markerObject.getMarkerID());
            requestJson.put(Keys.RATE_CAN_NAME, canRate.get(0));
            requestJson.put(Keys.RATE_CAN_CATEGORY, canRate.get(1));
            requestJson.put(Keys.RATE_CAN_ADDRESS, canRate.get(2));
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
                    listener.onCANSuccess();
                }
                else
                {
                    listener.onCANFailed();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            listener.onCANFailed();
        }
    }
}
