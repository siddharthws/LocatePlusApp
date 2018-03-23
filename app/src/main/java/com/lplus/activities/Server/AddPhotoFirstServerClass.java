package com.lplus.activities.Server;

import android.content.Context;

import com.lplus.activities.Interfaces.AddPhotoInterface;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Macros.UrlMappings;
import com.lplus.activities.Objects.TempNewPhotoObject;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by CHANDEL on 3/23/2018.
 */

public class AddPhotoFirstServerClass extends BaseServerClass {
    private Context context;
    private TempNewPhotoObject tempNewPhotoObject;

    private AddPhotoInterface listener = null;
    public void SetListener(AddPhotoInterface listener)
    {
        this.listener = listener;
    }

    public AddPhotoFirstServerClass(Context context, TempNewPhotoObject tempNewPhotoObject)
    {
        super(context, UrlMappings.ADD_PHOTOS);
        this.context = context;
        this.tempNewPhotoObject = tempNewPhotoObject;
    }

    @Override
    public Void doInBackground (Void... params)
    {
        // Init Request JSON
        SyncImage(tempNewPhotoObject.getPhotoPath(),tempNewPhotoObject.getPhotoUID());
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
            try {
                boolean success = responseJson.getBoolean(Keys.AP_PHOTO_RESPONSE);

                if(success)
                {
                    listener.onPhotoAddSucces();
                }
                else
                {
                    listener.onPhotoAddFailed();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            listener.onPhotoAddFailed();
        }
    }

    private boolean SyncImage(ArrayList<String> imagePaths, ArrayList<String> imageUUIDs) {

        // Set response to null
        responseJson = null;

        // Check if image exists

        for(String path : imagePaths) {
            if (!new File(path).exists()) {
                return false;
            }
        }

        // Create Multipart request body
        MultipartBody.Builder mpartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for(int i = 0;i<imagePaths.size();i++) {

            //MultipartRequestBody fileBody = new MultipartRequestBody(path);
            RequestBody rq = RequestBody.create(MediaType.parse("image/jpeg"), new File(imagePaths.get(i)));
            mpartBuilder.addFormDataPart(Keys.AP_PHOTOSTREAM, imageUUIDs.get(i), rq);
        }
        requestBuilder.method("POST", mpartBuilder.build());


        // Validate response
        if (!IsResponseValid()) {
            return false;
        }

        return true;
    }
}
