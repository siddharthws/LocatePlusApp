package com.lplus.activities.Server;

import android.content.Context;
import android.view.View;

import com.lplus.activities.Interfaces.RegisterUdidInterface;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Macros.UrlMappings;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;

/**
 * Created by Rohan on 3/31/2018.
 */

public class UdidRegisterServerClass extends BaseServerClass{
    private Context context;
    private String udid;
    private String name;

    private RegisterUdidInterface listener = null;
    public void SetListener(RegisterUdidInterface listener) { this.listener = listener; }

    public UdidRegisterServerClass(Context context, String udid, String name) {

        super(context, UrlMappings.REGISTER_UDID);
        this.context = context;
        this.udid = udid;
        this.name = name;
    }

    @Override
    public Void doInBackground (Void... params)
    {
        // Init Request JSON
        JSONObject requestJson = new JSONObject();

        try
        {
            requestJson.put(Keys.UDID, udid);
            requestJson.put(Keys.NAME, name);
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
            try {

                boolean sucess = responseJson.getBoolean(Keys.AP_RESPONSE);
                if(sucess)
                {
                    listener.onRegisterSuccess(true);
                }
                else
                {
                    listener.onRegisterSuccess(false);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            listener.onRegisterSuccess(false);
        }
    }
}
