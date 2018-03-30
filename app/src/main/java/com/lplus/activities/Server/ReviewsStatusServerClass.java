package com.lplus.activities.Server;

import android.content.Context;

import com.lplus.activities.DBHelper.AddRateTable;
import com.lplus.activities.Interfaces.ReviewsStatusInterface;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Macros.UrlMappings;
import com.lplus.activities.Objects.MarkerObject;
import com.lplus.activities.Objects.RateObject;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;

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
                    System.out.println("json object"+ responseJson.toString());
                    AddRateTable addRateTable = new AddRateTable(context);
                   int reviewResponse = responseJson.getInt(Keys.REVIEW_UPDATE_RESPONSE);
                   int photoResponse = responseJson.getInt(Keys.PHOTO_UPDATE_RESPONSE);
                   double rate = responseJson.getDouble(Keys.RATE_PLACE);
                   int users = responseJson.getInt(Keys.RATE_USERS);



                    boolean isRateAvailable = addRateTable.isRateAvailable(markerObject.getMarkerID());
                    //Fetch new Records
                    //store data in tinyDB
                    RateObject rateObject = new RateObject(markerObject.getMarkerID(),rate, users);
                    if(isRateAvailable) {
                        boolean update = addRateTable.UpdateRateByID(rateObject);
                    }
                    else {
                        addRateTable.SaveRecord(rateObject);
                    }
                    addRateTable.close();
                   listener.onUpdateRequired(reviewResponse, photoResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {listener.onUpdateNotRequired();}
    }
}
