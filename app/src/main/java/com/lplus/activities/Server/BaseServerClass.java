package com.lplus.activities.Server;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

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
    SharedPreferences app_sharePref = context.getSharedPreferences("app_details", MODE_PRIVATE);

    public BaseServerClass(Context context, String URL)
    {
        this.context = context;
        this.URL = URL;
        okHttpClient = new OkHttpClient();
    }

    @Override
    protected Void doInBackground(Void... voids) {


        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        // Validate network info and connection status and return result
        if ((networkInfo == null)  && !networkInfo.isConnected())
        {
            return null;
        }
        connectToServer();
        return null;
    }

    private boolean connectToServer()
    {
        boolean connectionResult = false;
        CacheControl cc = new CacheControl.Builder().noCache().build();
        requestBuilder.url(URL)
                .cacheControl(cc)
                .header(APP_ID, String.valueOf(app_sharePref.getInt(APP_ID, -1)));

        try {
            Request request = requestBuilder.build();
            Response response = okHttpClient.newCall(request).execute();

            // Validate and parse reponse
            if (response.code() == HttpURLConnection.HTTP_OK) {
                responseJson = new JSONObject(response.body().string());
                connectionResult = true;
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
        return connectionResult;
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
