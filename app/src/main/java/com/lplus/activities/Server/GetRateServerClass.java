package com.lplus.activities.Server;

import android.content.Context;

import com.lplus.activities.DBHelper.AddRateTable;
import com.lplus.activities.DBHelper.ReviewsTable;
import com.lplus.activities.Extras.ServerParseStatics;
import com.lplus.activities.Extras.TinyDB;
import com.lplus.activities.Interfaces.GetRateInterface;
import com.lplus.activities.Interfaces.GetReviewsInterface;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Macros.UrlMappings;
import com.lplus.activities.Objects.MarkerObject;
import com.lplus.activities.Objects.RateObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;

/**
 * Created by CHANDEL on 3/27/2018.
 */

public class GetRateServerClass extends BaseServerClass {
    private Context context;
    private MarkerObject markerObject;
    private GetRateInterface listener = null;
    TinyDB tinyDB;
    public void SetListener(GetRateInterface listener){this.listener = listener;}

    public GetRateServerClass(Context context, MarkerObject markerObject)
    {
        super(context, UrlMappings.GET_REVIEWS);
        this.context = context;
        this.markerObject = markerObject;
        tinyDB = TinyDB.Init(context);
    }

    @Override
    public Void doInBackground (Void... params)
    {
        // Init Request JSON
        JSONObject requestJson = new JSONObject();

        try
        {

            System.out.println("Place ID sending: "+markerObject.getMarkerID());
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
            AddRateTable addRateTable = new AddRateTable(context);

            Double rate = 0.0;
            try {
                boolean isRateAvailable = addRateTable.isRateAvailable(markerObject.getMarkerID());
                //Fetch new Records
                String placeID = responseJson.getString(Keys.MARKER_ID);
                rate = responseJson.getDouble(Keys.RATE_PLACE);
                //store data in tinyDB
                if(isRateAvailable) {
                    RateObject rateObject = new RateObject(markerObject.getMarkerID(),rate);
                    boolean update = addRateTable.UpdateRateByID(rateObject);
                    if(update) {
                        listener.onRateReceiveSuccess();
                    }
                }
                else {
                    RateObject rateObject = new RateObject(markerObject.getMarkerID(),rate);
                    addRateTable.SaveRecord(rateObject);
                    listener.onRateReceiveSuccess();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {listener.onRateReceiveFailed();}
    }
}
