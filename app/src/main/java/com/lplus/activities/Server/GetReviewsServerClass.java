package com.lplus.activities.Server;

import android.content.Context;

import com.lplus.activities.DBHelper.ReviewsTable;
import com.lplus.activities.Extras.ServerParseStatics;
import com.lplus.activities.Interfaces.GetReviewsInterface;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Macros.UrlMappings;
import com.lplus.activities.Objects.MarkerObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;

/**
 * Created by Sai_Kameswari on 23-03-2018.
 */

public class GetReviewsServerClass extends BaseServerClass{

    private Context context;
    private MarkerObject markerObject;
    private GetReviewsInterface listener = null;
    public void SetListener(GetReviewsInterface listener){this.listener = listener;}

    public GetReviewsServerClass(Context context, MarkerObject markerObject)
    {
        super(context, UrlMappings.GET_REVIEWS);
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
            JSONArray reviews = null;
            try {

                //delete the stored database
                ReviewsTable reviewsTable = new ReviewsTable(context);
                reviewsTable.DeleteAll();
                reviewsTable.CloseConnection();

                //Fetch new Records
                String placeID = responseJson.getString(Keys.MARKER_ID);
                reviews = responseJson.getJSONArray(Keys.ALL_REVIEWS);
                //store data in markers
                ServerParseStatics.parseReviews(context, placeID, reviews);
                listener.onReviewFetched();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {listener.onReviewNotFetched();}
    }
}
