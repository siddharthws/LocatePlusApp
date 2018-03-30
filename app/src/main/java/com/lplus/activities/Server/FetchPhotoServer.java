package com.lplus.activities.Server;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.lplus.activities.DBHelper.AddPhotoTable;
import com.lplus.activities.DBHelper.DatabaseHelper;
import com.lplus.activities.Extras.CustomToast;
import com.lplus.activities.Extras.ServerParseStatics;
import com.lplus.activities.Interfaces.PhotoFetchStatusInterface;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Macros.UrlMappings;
import com.lplus.activities.Objects.MarkerObject;
import com.lplus.activities.Objects.PhotoObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import es.dmoral.toasty.Toasty;
import okhttp3.RequestBody;

/**
 * Created by Sai_Kameswari on 26-03-2018.
 */

public class FetchPhotoServer extends BaseServerClass
{
    private Context context;
    private MarkerObject markerObject;
    AddPhotoTable addPhotoTable;
    PhotoObject photoObject;

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
        addPhotoTable = new AddPhotoTable(context);
        photoObject = new PhotoObject();
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
        Log.e("ABC","its their in post");
        // Register user in preferences if server returned OK
        if (IsResponseValid())
        {
            System.out.println("Checking response of photo fetch");
            ArrayList<String> images_uuids = new ArrayList<>();
            ArrayList<Bitmap> images = new ArrayList<>();
            byte[] byteArray;
            Bitmap bmp1;
            try {
                System.out.println("json response "+responseJson.toString());
                JSONArray responseArray = responseJson.getJSONArray("photos");
                JSONObject innerObject;
                String innerJSONArray;
                if(addPhotoTable.Removeall(markerObject.getMarkerID())) {
                    System.out.println("Data removed");
                } else {
                    System.out.println("Data not available");
                }

                System.out.println("length = "+responseArray.length());
                for(int i =0; i< responseArray.length(); i++)
                {
                   innerObject = responseArray.getJSONObject(i);
                   innerJSONArray = innerObject.getString("photo");
                   String uuid = innerObject.getString("uuid");

                    byte[] decodedString = Base64.decode(innerJSONArray, Base64.NO_WRAP);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    System.out.println("uuid = "+uuid);

                   // byteArray =  Base64.decode(array.toString().getBytes(), Base64.NO_PADDING) ;
                    //String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    /*bmp1 = BitmapFactory.decodeByteArray(innerJSONArray, 0, innerJSONArray.length);*/

                    images.add(decodedByte);
                    images_uuids.add(uuid);
                }
                Log.e("ABC",images_uuids.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ArrayList<String> paths = ServerParseStatics.parsePhotos(context, images, images_uuids);
            Log.e("ABC",paths.toString());

            photoObject.setPlace_id(markerObject.getMarkerID());
            photoObject.setPhoto_uuids(images_uuids);
            photoObject.setPhoto_paths(paths);
            addPhotoTable.SavePhotos(photoObject);
            listener.onPhotoFetched(paths,images_uuids);
        }
        else
        {
            Log.e("ABC","its ");
            listener.onPhotoFetchFailed();
        }
    }

}
