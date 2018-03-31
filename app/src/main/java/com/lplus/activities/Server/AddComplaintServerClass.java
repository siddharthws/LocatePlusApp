package com.lplus.activities.Server;

import android.content.Context;

import com.lplus.activities.Interfaces.AddComplaintInterface;
import com.lplus.activities.Interfaces.AddPlaceInterface;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Macros.UrlMappings;
import com.lplus.activities.Objects.TempNewComplaintObject;
import com.lplus.activities.Objects.TempNewPlaceObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;

/**
 * Created by Siddharth on 31-03-2018.
 */

public class AddComplaintServerClass extends BaseServerClass {

    private Context context;
    private TempNewComplaintObject tco;

    private AddComplaintInterface listener = null;
    public void SetListener(AddComplaintInterface listener)
    {
        this.listener = listener;
    }

    public AddComplaintServerClass(Context context, TempNewComplaintObject tco)
    {
        super(context, UrlMappings.ADD_COMPLAINT);
        this.context = context;
        this.tco = tco;
    }

    @Override
    public Void doInBackground (Void... params)
    {
        // Init Request JSON
        JSONObject requestJson = new JSONObject();
        try
        {

            //put in jsonObject
            requestJson.put(Keys.AP_NAME,               tco.getName());
            requestJson.put(Keys.AP_ADDRESS,            tco.getAddress());
            requestJson.put(Keys.AP_LATITUDE,           tco.getLatitude());
            requestJson.put(Keys.AP_LONGITUDE,          tco.getLongitude());
            requestJson.put(Keys.AP_DESCRIPTION,        tco.getDescription());
            requestJson.put(Keys.AP_UUIDS,         toUUIDJSONArray());
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
            try {
                boolean success = responseJson.getBoolean(Keys.AP_RESPONSE);

                if(success)
                {
                    listener.onComplaintAddStatus(true);
                }
                else
                {
                    listener.onComplaintAddStatus(false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            listener.onComplaintAddStatus(false);
        }
    }

    private JSONArray toUUIDJSONArray()
    {
        JSONArray uuidJSON = new JSONArray();
        for(String id : tco.getUuids())
        {
            uuidJSON.put(id);
        }
        return uuidJSON;
    }
}
