package com.lplus.activities.Server;

import android.content.Context;

import com.lplus.activities.Interfaces.ReviewsStatusInterface;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Macros.UrlMappings;
import com.lplus.activities.Objects.MarkerObject;
import okhttp3.RequestBody;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sai_Kameswari on 23-03-2018.
 */

public class ReviewsStatusServerClass extends BaseServerClass {

    private Context context;
    private MarkerObject markerObject;
    private ReviewsStatusInterface listener = null;
    public void SetListener(ReviewsStatusInterface listener) {this.listener = listener;}

    public ReviewsStatusServerClass(Context context, MarkerObject markerObject)
    {
        super(context, UrlMappings.GET_RP_STATUS);
        this.context = context;
        this.markerObject = markerObject;
    }

    @Override
    public Void doInBackground (Void... params)
    {
        // Init Request JSON
        JSONObject requestJson = new JSONObject();

        try
        {
            requestJson.put(Keys.MARKER_ID, markerObject.getMarkerID());
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
                   int reviewResponse = responseJson.getInt(Keys.REVIEW_UPDATE_RESPONSE);
                   int photoResponse = responseJson.getInt(Keys.PHOTO_UPDATE_RESPONSE);

                   listener.onUpdateRequired(reviewResponse, photoResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {listener.onUpdateNotRequired();}
    }
}
