package com.lplus.activities.JavaFiles;

/**
 * Created by Sai_Kameswari on 20-03-2018.
 */


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Geocoding extends AsyncTask<Void, Integer, Void> {

    private static final String TAG = "Reverse Geocoding";
    private Geocoder geocoder;
    private double latitude, longitude;
    private Context context;
    private String result = null;
    private geocodingInterface listener = null;

    public interface geocodingInterface {
        void onAddressFetched(String result, double latitude, double longitude);
        void onAddressFetchFailed();
    }
    public void setListener(geocodingInterface listener)
    {
        this.listener = listener;
    }

    public Geocoding(Context context, double latitude, double longitude)
    {
        this.context = context;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    if(address.getAddressLine(i) != null)
                    {
                        sb.append(address.getAddressLine(i)).append(",");
                    }
                }
                if(address.getAdminArea() != null)
                {
                    sb.append(address.getAdminArea()).append(",");
                }
                if(address.getLocality() != null)
                {
                    sb.append(address.getLocality()).append(",");
                }
                if(address.getPostalCode() != null)
                {
                    sb.append(address.getPostalCode()).append(",");
                }
                if(address.getCountryName() != null)
                {
                    sb.append(address.getCountryName());
                }
                result = sb.toString();
            }
        } catch (IOException e) {
            Log.e(TAG, "Unable connect to Geocoder", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void results)
    {
        if(result != null)
        {
            listener.onAddressFetched(result, latitude, longitude);
        }
        else
        {
            listener.onAddressFetchFailed();
        }
    }
}