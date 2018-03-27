package com.lplus.activities.Server;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.lplus.activities.Extras.InternetConnectivityCheck;

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
 * Created by Sai_Kameswari on 26-03-2018.
 */

public class BaseServerphotoClass extends AsyncTask<Void, Void, Void>
{

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    protected Context context = null;
    protected String URL = null;
    private static OkHttpClient okHttpClient = null;
    protected Request.Builder requestBuilder = null;
    protected Bitmap responseImage = null;
    private final String IMEI = "imei";
    private SharedPreferences app_sharePref;

    public BaseServerphotoClass(Context context, String URL)
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
            if (response.code() == HttpURLConnection.HTTP_OK)
            {
                InputStream inputStream = response.body().byteStream();
                responseImage = BitmapFactory.decodeStream(inputStream);
            }
            else {
                System.out.println("Response Code = " + response.code());
            }

            //close Connection
            if(response != null)
            {
                response.body().close();
                okHttpClient = null;
            }
        } catch (IOException e) {
            System.out.println("IO exception while making server request");
            e.printStackTrace();
        }
    }

    protected boolean IsResponseValid()
    {
        if (responseImage == null)
        {
            return false;
        }
        return true;
    }
}
