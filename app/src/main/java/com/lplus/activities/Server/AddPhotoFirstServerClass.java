package com.lplus.activities.Server;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.lplus.activities.Interfaces.AddPhotoInterface;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Macros.UrlMappings;
import com.lplus.activities.Objects.TempNewPhotoObject;

import org.json.JSONException;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by CHANDEL on 3/23/2018.
 */

public class AddPhotoFirstServerClass extends BaseServerClass {
    private Context context;
    private TempNewPhotoObject tempNewPhotoObject;
    BitmapFactory.Options bmOptions;

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
        bmOptions = new BitmapFactory.Options();
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
                    listener.onPhotoRecieve(true);
                }
                else
                {
                    listener.onPhotoRecieve(false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            listener.onPhotoRecieve(false);
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

            File f = new File(imagePaths.get(i));
            File file = ScaleImage(f);
            System.out.print("image "+imagePaths.get(i));
            /*File file = new File(imagePaths.get(i));*/
            //MultipartRequestBody fileBody = new MultipartRequestBody(path);
            RequestBody rq = RequestBody.create(MediaType.parse("image/jpeg"), file);
            mpartBuilder.addFormDataPart(Keys.AP_PHOTOSTREAM, imageUUIDs.get(i), rq);
        }
            requestBuilder.method("POST", mpartBuilder.build());


        // Validate response
        if (!IsResponseValid()) {
            return false;
        }

        return true;
    }

    private File ScaleImage(File file) {
        OutputStream os = null;
        try {
            //os = new BufferedOutputStream(new FileOutputStream(file));
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),bmOptions);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap = Bitmap.createScaledBitmap(bitmap,256,256,true);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            //os.flush();
            //os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
