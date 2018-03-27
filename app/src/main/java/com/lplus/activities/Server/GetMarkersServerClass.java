package com.lplus.activities.Server;

import android.content.Context;
import android.content.SharedPreferences;

import com.lplus.activities.DBHelper.MarkersTable;
import com.lplus.activities.Extras.ServerParseStatics;
import com.lplus.activities.Interfaces.GetMarkerInteface;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Macros.UrlMappings;

import org.json.JSONArray;
import org.json.JSONException;

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
                JSONArray markers = null;
                try {
                    //delete the stored database
                    MarkersTable markersTable = new MarkersTable(context);
                    markersTable.DeleteAll();
                    markersTable.CloseConnection();

                    //Fetch new Records
                    markers = responseJson.getJSONArray(Keys.KEY_MARKERS);
                    //store data in markers
                    ServerParseStatics.parseMarkers(context, markers);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                listener.onMarkerFetchStatus(true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            listener.onMarkerFetchStatus(false);
        }

    }
}
