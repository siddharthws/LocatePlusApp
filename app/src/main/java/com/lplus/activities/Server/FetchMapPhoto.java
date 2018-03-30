package com.lplus.activities.Server;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.lplus.activities.DBHelper.AddPhotoTable;
import com.lplus.activities.DBHelper.DatabaseHelper;
import com.lplus.activities.Extras.GetMapImage;
import com.lplus.activities.Extras.ServerParseStatics;
import com.lplus.activities.Interfaces.FetchGooglePhoto;
import com.lplus.activities.Interfaces.PhotoFetchStatusInterface;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Macros.UrlMappings;
import com.lplus.activities.Objects.MarkerObject;
import com.lplus.activities.Objects.PhotoObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;

/**
 * Created by CHANDEL on 3/30/2018.
 */

public class FetchMapPhoto extends BasePhotoServerClass {
    private Context context;
    private MarkerObject markerObject;

    private FetchGooglePhoto listener = null;
    public void SetListener(FetchGooglePhoto listener)
    {
        this.listener = listener;
    }

    public FetchMapPhoto(Context context, MarkerObject markerObject,String color)
    {
        super(context, GetMapImage.MapURL(String.valueOf(markerObject.getMarkerLatitude()), String.valueOf(markerObject.getMarkerLongitude()),color));
        this.markerObject = markerObject;
        this.context = context;
    }

    @Override
    public Void doInBackground (Void... params)
    {

        //Initialize Database
        DatabaseHelper.Init(context);

        // Call Super
        super.doInBackground(params);
        return null;
    }

    @Override
    public void onPostExecute (Void result)
    {
        Log.e("ABC","its their in post");
        // Register user in preferences if server returned OK
        if (IsResponseValid())
        {
            listener.onMapPhotoFetched(bmp);
        }
        else
        {
            Log.e("ABC","its ");
            listener.onMapPhotoFailed();
        }
    }
}
