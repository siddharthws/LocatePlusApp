package com.lplus.activities.Server;

import android.content.Context;

import com.lplus.activities.Interfaces.MarkerReviewInterface;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Macros.UrlMappings;
import com.lplus.activities.Objects.MarkerObject;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;

/**
 * Created by Sai_Kameswari on 22-03-2018.
 */

public class MarkerReviewServerClass extends BaseServerClass {

    private Context context;
    private String review;
    private MarkerObject markerObject;

    private MarkerReviewInterface listener = null;
    public void SetListener(MarkerReviewInterface listener)
    {
        this.listener = listener;
    }

    public MarkerReviewServerClass(Context context, MarkerObject markerObject, String review)
    {
        super(context, UrlMappings.ADD_REVIEWS);
        this.context = context;
        this.markerObject = markerObject;
        this.review = review;
    }

    @Override
    public Void doInBackground (Void... params)
    {
        // Init Request JSON
        JSONObject requestJson = new JSONObject();

        try
        {
            requestJson.put(Keys.MARKER_ID, markerObject.getMarkerID());
            requestJson.put(Keys.MARKER_REVIEW, review);
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
                    listener.onReviewSentStatus(true);
                }
                else
                {
                    listener.onReviewSentStatus(false);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            listener.onReviewSentStatus(false);
        }
    }
}
