package com.lplus.activities.Server;

import android.content.Context;
import android.content.SharedPreferences;

import com.lplus.activities.DBHelper.DatabaseHelper;
import com.lplus.activities.Interfaces.ServerStatusInterface;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Macros.UrlMappings;
import com.squareup.okhttp.RequestBody;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sai_Kameswari on 16-03-2018.
 */

public class RegisterServerClass extends BaseServerClass {

    private final String APP_ID = "app_id";
    private final String APP_UNAME = "name";
    private Context context;
    private String loading_msg = "Registering App";
    private  SharedPreferences app_sharePref;

    private ServerStatusInterface listener = null;
    public void SetListener(ServerStatusInterface listener)
    {
        this.listener = listener;
    }

    public RegisterServerClass(Context context)
    {
        super(context, UrlMappings.REGISTER_APP);
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
            requestJson.put(APP_ID, -1);
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
                int statusResponseFC = responseJson.getInt(Keys.SERVER_RESPONSE_FC);
                int statusResponseGP = responseJson.getInt(Keys.SERVER_RESPONSE_GP);

                listener.onStatusSuccess(statusResponseFC, statusResponseGP);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
           listener.onStatusFailure();
        }
    }
}
