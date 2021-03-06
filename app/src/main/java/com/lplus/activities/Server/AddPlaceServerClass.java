package com.lplus.activities.Server;

import android.content.Context;

import com.lplus.activities.Interfaces.AddPlaceInterface;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Macros.UrlMappings;
import com.lplus.activities.Objects.TempNewPlaceObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;

/**
 * Created by Sai_Kameswari on 20-03-2018.
 */

public class AddPlaceServerClass extends BaseServerClass {

    private Context context;
    private TempNewPlaceObject tempNewPlaceObject;

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
        JSONArray facilitiesJSON = toJSONArray();
        JSONArray uuidJSON = toUUIDJSONArray();
        try
        {

            System.out.println("Facilities: "+facilitiesJSON.toString());
            System.out.println("Facilities: "+toUUIDJSONArray().toString());
            System.out.println("contact info = ok "+tempNewPlaceObject.getContact());
            //put in jsonObject
            requestJson.put(Keys.AP_NAME,               tempNewPlaceObject.getName());
            requestJson.put(Keys.AP_ADDRESS,            tempNewPlaceObject.getAddress());
            requestJson.put(Keys.AP_CATEGORY,           tempNewPlaceObject.getCategory());
            requestJson.put(Keys.AP_LATITUDE,           tempNewPlaceObject.getLatitude());
            requestJson.put(Keys.AP_LONGITUDE,          tempNewPlaceObject.getLongitude());
            requestJson.put(Keys.AP_DESCRIPTION,        tempNewPlaceObject.getDescription());
            requestJson.put(Keys.AP_FACILITIES,         facilitiesJSON);
            requestJson.put(Keys.AP_UUIDS,              toUUIDJSONArray());
            requestJson.put(Keys.AP_CONTACT,            tempNewPlaceObject.getContact());
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
                    listener.onPlaceAddStatus(true);
                }
                else
                {
                    listener.onPlaceAddStatus(false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            listener.onPlaceAddStatus(false);
        }
    }

    private JSONArray toJSONArray()
    {
        JSONObject fac = null;
        JSONArray facilitiesJSON = new JSONArray();
        try {
            for(String id : tempNewPlaceObject.getFacilities())
            {
                fac = new JSONObject();
                fac.put(Keys.FAC_ID, id);
                facilitiesJSON.put(fac);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return facilitiesJSON;
    }

    private JSONArray toUUIDJSONArray()
    {
        JSONArray uuidJSON = new JSONArray();
        for(String id : tempNewPlaceObject.getUuids())
        {
            uuidJSON.put(id);
        }
        return uuidJSON;
    }
}
