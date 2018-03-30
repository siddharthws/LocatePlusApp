package com.lplus.activities.Server;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.lplus.activities.DBHelper.DatabaseHelper;
import com.lplus.activities.Extras.ServerParseStatics;
import com.lplus.activities.Interfaces.PhotoFetchStatusInterface;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Macros.UrlMappings;
import com.lplus.activities.Objects.MarkerObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;

/**
 * Created by Sai_Kameswari on 26-03-2018.
 */

public class FetchPhotoServer extends BaseServerClass
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
            System.out.println("Checking response of photo fetch");
            ArrayList<Bitmap> images = new ArrayList<>();
            byte[] byteArray;
            Bitmap bmp1;
            try {
                JSONArray responseArray = responseJson.getJSONArray("photos");
                JSONObject innerObject;
                byte[] innerJSONArray;
                for(int i =0; i< responseArray.length(); i++)
                {
                   innerObject = responseArray.getJSONObject(i);
                   innerJSONArray = (byte[]) innerObject.get("photo");

                   // byteArray =  Base64.decode(array.toString().getBytes(), Base64.NO_PADDING) ;
                    //String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

                    bmp1 = BitmapFactory.decodeByteArray(innerJSONArray, 0, innerJSONArray.length);


                    images.add(bmp1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ArrayList<String> paths = ServerParseStatics.parsePhotos(context, images);
            listener.onPhotoFetched(paths);
        }
        else
        {
            listener.onPhotoFetchFailed();
        }
    }
}
