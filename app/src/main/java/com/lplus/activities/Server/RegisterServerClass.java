package com.lplus.activities.Server;

import android.content.Context;
import android.content.SharedPreferences;

import com.lplus.activities.Dialogs.LoadingDialog;
import com.lplus.activities.Interfaces.ServerStatusInterface;
import com.lplus.activities.Macros.UrlMappings;
import com.squareup.okhttp.RequestBody;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Sai_Kameswari on 16-03-2018.
 */

public class RegisterServerClass extends BaseServerClass {

    private final String APP_ID = "app_id";
    private final String APP_UNAME = "name";
    private Context context;
    private String loading_msg = "Registering App";

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
                int appId = responseJson.getInt(APP_ID);
                String userName = responseJson.getString(APP_UNAME);

                SharedPreferences app_sharePref = context.getSharedPreferences("app_details", MODE_PRIVATE);
                SharedPreferences.Editor edit = app_sharePref.edit();
                edit.putInt(APP_ID, appId);
                edit.putString(APP_UNAME, userName);
                edit.commit();

                listener.onStatusSuccess();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
           listener.onStatusFailure();
        }
    }
}
