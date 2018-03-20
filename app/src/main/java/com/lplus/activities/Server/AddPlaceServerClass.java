package com.lplus.activities.Server;

import android.content.Context;

import com.lplus.activities.Interfaces.AddPlaceInterface;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Macros.UrlMappings;
import com.lplus.activities.Models.TempNewPlaceObject;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sai_Kameswari on 20-03-2018.
 */

public class AddPlaceServerClass extends BaseServerClass {

    private Context context;
    private TempNewPlaceObject tempNewPlaceObject;
    private JSONArray facilitiesJSON;

    private AddPlaceInterface listener = null;
    public void SetListener(AddPlaceInterface listener)
    {
        this.listener = listener;
    }

    public AddPlaceServerClass(Context context, TempNewPlaceObject tempNewPlaceObject)
    {
        super(context, UrlMappings.ADD_PLACE);
        this.context = context;
        this.tempNewPlaceObject = tempNewPlaceObject;
    }

    @Override
    public Void doInBackground (Void... params)
    {
        // Init Request JSON
        JSONObject requestJson = new JSONObject();
        toJSONArray();
        try
        {

            //put in jsonObject
            requestJson.put(Keys.AP_NAME,       tempNewPlaceObject.getName());
            requestJson.put(Keys.AP_ADDRESS,    tempNewPlaceObject.getAddress());
            requestJson.put(Keys.AP_CATEGORY,   tempNewPlaceObject.getCategory());
            requestJson.put(Keys.AP_LATITUDE,   tempNewPlaceObject.getLatitude());
            requestJson.put(Keys.AP_LONGITUDE,  tempNewPlaceObject.getLongitude());
            requestJson.put(Keys.AP_FACILITIES, facilitiesJSON);
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
                    listener.onPlaceAddSucces();
                }
                else
                {
                    listener.onPlaceAddFailed();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            listener.onPlaceAddFailed();
        }
    }

    private void toJSONArray()
    {
        JSONObject fac = new JSONObject();
        try {
            fac.put(Keys.FAC_ID, tempNewPlaceObject.getFacilities());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        facilitiesJSON = new JSONArray();
        facilitiesJSON.put(fac);
    }
}
