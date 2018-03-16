package com.lplus.activities.Server;

import android.content.Context;
import android.content.SharedPreferences;

import com.squareup.okhttp.RequestBody;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Sai_Kameswari on 16-03-2018.
 */

public class RegisterServerClass extends BaseServerClass {

    private final String APP_ID = "app_id";
    private final String APP_UNAME = "userName";

    public RegisterServerClass(Context context, String URL) {
        super(context, URL);
    }

    @Override
    public void onPreExecute() {

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

            } catch (JSONException e) {
               e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void onPostExecute (Void result)
    {
        if (IsResponseValid())
        {

        }
        else
        {

        }
    }
}
