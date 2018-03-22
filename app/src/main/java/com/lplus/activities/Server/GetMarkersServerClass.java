package com.lplus.activities.Server;

import android.content.Context;
import android.content.SharedPreferences;

import com.lplus.activities.Extras.Statics;
import com.lplus.activities.Interfaces.GetMarkerInteface;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Macros.UrlMappings;

import org.json.JSONArray;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Sai_Kameswari on 21-03-2018.
 */

public class GetMarkersServerClass extends BaseServerClass {

    private Context context;
    private String loading_msg = "Fetching All Markers";
    private SharedPreferences app_sharePref;

    private GetMarkerInteface listener = null;

    public void SetListener(GetMarkerInteface listener) {
        this.listener = listener;
    }

    public GetMarkersServerClass(Context context) {
        super(context, UrlMappings.GET_MARKERS);
        this.context = context;
        app_sharePref = context.getSharedPreferences(Keys.SHARED_PREF_NAME, MODE_PRIVATE);
    }

    @Override
    public Void doInBackground(Void... params) {

        // Call Super
        super.doInBackground(params);
        return null;
    }

    @Override
    public void onPostExecute(Void result) {
        // Register user in preferences if server returned OK
        if (IsResponseValid()) {
            try
            {
                System.out.println("Response JSON fetched for markers");
                JSONArray markers =  responseJson.getJSONArray(Keys.KEY_MARKERS);
                boolean markerUpdateRequired = responseJson.getBoolean(Keys.MARKER_UPDATE_REQUIRED);

                System.out.println("Marker update required: "+markerUpdateRequired);

                if(markerUpdateRequired)
                {
                    //store data in markers
                    Statics.parseMarkers(markers);

                    app_sharePref = context.getSharedPreferences(Keys.SHARED_PREF_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor edit = app_sharePref.edit();
                    edit.putBoolean(Keys.MARKER_UPDATE_REQUIRED, markerUpdateRequired);
                    edit.commit();
                }
                listener.onMarkerFetched();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            listener.onMarkerFailed();
        }

    }
}
