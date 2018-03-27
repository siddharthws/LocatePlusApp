package com.lplus.activities.Server;

import android.content.Context;

import com.lplus.activities.DBHelper.DatabaseHelper;
import com.lplus.activities.Interfaces.PhotoFetchStatusInterface;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Macros.UrlMappings;
import com.lplus.activities.Objects.MarkerObject;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;

/**
 * Created by Sai_Kameswari on 26-03-2018.
 */

public class FetchPhotoServer extends BaseServerphotoClass
{
    private Context context;
    private MarkerObject markerObject;

    private PhotoFetchStatusInterface listener = null;
    public void SetListener(PhotoFetchStatusInterface listener)
    {
        this.listener = listener;
    }

    public FetchPhotoServer(Context context, MarkerObject markerObject)
    {
        super(context, UrlMappings.GET_PHOTOS);
        this.markerObject = markerObject;
        this.context = context;
    }

    @Override
    public Void doInBackground (Void... params)
    {

        //Initialize Database
        DatabaseHelper.Init(context);

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
        if (IsResponseValid())
        {
            listener.onPhotoFetched(responseImage);
        }
        else
        {
            listener.onPhotoFetchFailed();
        }
    }
}
