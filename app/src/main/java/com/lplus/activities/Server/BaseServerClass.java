package com.lplus.activities.Server;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.lplus.activities.InternetConnectivityCheck;
import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Sai_Kameswari on 16-03-2018.
 */

public class BaseServerClass extends AsyncTask<Void, Integer, Void>
{
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    protected Context context = null;
    protected String URL = null;
    private static OkHttpClient okHttpClient = null;
    protected Request.Builder requestBuilder = null;
    protected JSONObject responseJson = null;
    private final String APP_ID = "app_id";
    private final String IMEI = "imei";
    private SharedPreferences app_sharePref;
    private int app_id;

    public BaseServerClass(Context context, String URL)
    {
        this.context = context;
        this.URL = URL;
        this.requestBuilder = new Request.Builder();
        app_sharePref = context.getSharedPreferences("app_details", MODE_PRIVATE);
    }

    @Override
    protected Void doInBackground(Void... voids)
    {
        if(!InternetConnectivityCheck.isConnectedToNetwork(context))
        {
          return null;
        }
        connectToServer();
        return null;
    }

    private void connectToServer()
    {
        CacheControl cc = new CacheControl.Builder().noCache().build();
        okHttpClient = new OkHttpClient();
        app_id = app_sharePref.getInt(APP_ID, -1);

        if(app_id == -1)
        {
            System.out.println("IMEI to be sent: "+app_sharePref.getString(IMEI, ""));
            requestBuilder.url(URL)
                    .cacheControl(cc)
                    .header(IMEI, app_sharePref.getString(IMEI, ""));
        }
        else
        {
            requestBuilder.url(URL)
                    .cacheControl(cc)
                    .header(APP_ID, String.valueOf(app_id));
        }

        try {
            Request request = requestBuilder.build();
            Response response = okHttpClient.newCall(request).execute();

            // Validate and parse reponse
            if (response.code() == HttpURLConnection.HTTP_OK) {
                responseJson = new JSONObject(response.body().string());
            } else {
                System.out.println("Response Code = " + response.code());
            }
        } catch (IOException e) {
            System.out.println("IO exception while making server request");
            e.printStackTrace();
        } catch (JSONException e) {
            System.out.println("JSON exception while making server request");
            e.printStackTrace();
        }
    }

    protected boolean IsResponseValid()
    {
        if (responseJson == null)
        {
            return false;
        }
        return true;
    }
}
