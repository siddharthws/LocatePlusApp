package com.lplus.activities.Server;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.lplus.activities.Extras.InternetConnectivityCheck;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import okhttp3.CacheControl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Sai_Kameswari on 16-03-2018.
 */

public class BasePhotoServerClass extends AsyncTask<Void, Integer, Void>
{
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    protected Context context = null;
    protected String URL = null;
    private static OkHttpClient okHttpClient = null;
    protected Request.Builder requestBuilder = null;
    protected InputStream inputStream = null;
    private final String IMEI = "imei";
    private SharedPreferences app_sharePref;
    protected Bitmap bmp = null;

    public BasePhotoServerClass(Context context, String URL)
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

        System.out.println("IMEI to be sent: "+app_sharePref.getString(IMEI, ""));
        requestBuilder.url(URL)
                .cacheControl(cc)
                .header(IMEI, app_sharePref.getString(IMEI, ""));


        try {
            Request request = requestBuilder.build();
            Response response = okHttpClient.newCall(request).execute();

            // Validate and parse reponse
            if (response.code() == HttpURLConnection.HTTP_OK) {
                inputStream = response.body().byteStream();
                bmp = BitmapFactory.decodeStream(inputStream);
                Log.e("ABC", "Fetched...." + bmp.getByteCount());
            } else {
                System.out.println("Response Code = " + response.code());
            }

        } catch (IOException e) {
            System.out.println("IO exception while making server request");
            e.printStackTrace();
        }
    }

    protected boolean IsResponseValid()
    {
        return bmp != null & bmp.getByteCount() > 0;
    }
}
