package com.lplus.activities.Extras;

import android.content.Context;

import com.lplus.activities.DBHelper.AddCategoryTable;
import com.lplus.activities.DBHelper.AddFacilityTable;
import com.lplus.activities.DBHelper.MarkersTable;
import com.lplus.activities.DBHelper.ReviewsTable;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Objects.CategoryObject;
import com.lplus.activities.Objects.FacilityObject;
import com.lplus.activities.Objects.MarkerObject;
import com.lplus.activities.Objects.ReviewsObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sai_Kameswari on 19-03-2018.
 */

public class ServerParseStatics {
    private static ArrayList<String> categories_key;
    private static ArrayList<String> facilities_key;
    private static ArrayList<String> categories_value;
    private static ArrayList<String> facilities_value;
    private static Context context;
    private static TinyDB tinyDB;

    public static TinyDB getTinyDBObject(Context context)
    {
        tinyDB = TinyDB.Init(context);
        return tinyDB;
    }

    public static void onFailInit()
    {
        categories_key=new ArrayList<>();
        categories_value = new ArrayList<>();

        facilities_key=new ArrayList<>();
        facilities_value = new ArrayList<>();
    }

    public static void Init(Context contexts, JSONArray categoriesarray, JSONArray facilitiesarray)
    {
        context = contexts;
        tinyDB = TinyDB.Init(context);
        setCategories(categoriesarray);
        setFacilities(facilitiesarray);
    }

    public static void setCategories(JSONArray categoriesarray)
    {
        categories_key=new ArrayList<>();
        categories_value = new ArrayList<>();

       for(int i=0; i<categoriesarray.length();i++)
       {
           try {
               JSONObject cat = categoriesarray.getJSONObject(i);
               System.out.println("Cat ID: "+cat.getInt(Keys.CAT_ID));
               System.out.println("Cat: "+cat.getString(Keys.CAT_NAME));
               categories_key.add(cat.getString(Keys.CAT_ID));
               categories_value.add(cat.getString(Keys.CAT_NAME));

           } catch (JSONException e) {
               e.printStackTrace();
           }
       }
        System.out.println("Set in shared preferences: "+categories_value.toString());

       //save in Object
        CategoryObject categoryObject = new CategoryObject();
        categoryObject.setCategory_ids(categories_key);
        categoryObject.setCategory_values(categories_value);

        //save in DB
        AddCategoryTable addCategoryTable = new AddCategoryTable(context);
        addCategoryTable.SaveRecords(categoryObject);
        addCategoryTable.CloseConnection();

        //Save in TinyDb
        tinyDB.putListString(Keys.CATEGORY_KEY, categories_key);
        tinyDB.putListString(Keys.CATEGORY_VALUE, categories_value);
    }

    public static void setFacilities(JSONArray facilitiesarray)
    {
        facilities_key=new ArrayList<>();
        facilities_value = new ArrayList<>();

        for(int i=0; i<facilitiesarray.length();i++)
        {
            try {
                JSONObject fac = facilitiesarray.getJSONObject(i);
                System.out.println("Cat ID: "+fac.getInt(Keys.FAC_ID));
                System.out.println("Cat: "+fac.getString(Keys.FAC_NAME));
                facilities_key.add(fac.getString(Keys.FAC_ID));
                facilities_value.add(fac.getString(Keys.FAC_NAME));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Set in shared preferences facilities: "+facilities_value.toString());

       //Save in Object
        FacilityObject facilityObject = new FacilityObject();
        facilityObject.setFacility_ids(facilities_key);
        facilityObject.setFacility_values(facilities_value);

        //save in Db
        AddFacilityTable addFacilityTable = new AddFacilityTable(context);
        addFacilityTable.SaveRecords(facilityObject);
        addFacilityTable.CloseConnection();

        tinyDB.putListString(Keys.FACILITIES_KEY, facilities_key);
        tinyDB.putListString(Keys.FACILITIES_VALUE, facilities_value);

    }


    public static void parseMarkers(Context contexts, JSONArray markers)
    {

        //Initialise the cache variable
        if(CacheData.cacheMarkers == null)
        {
            CacheData.cacheMarkers = new ArrayList<>();
        }
        MarkersTable markersTable = new MarkersTable(context);
        context = contexts;
        tinyDB = TinyDB.Init(context);
        System.out.println("Reaching the statics part");
        MarkerObject markerObject;
        for(int i=0; i<markers.length();i++)
        {
            try {
                JSONObject marker = markers.getJSONObject(i);
                markerObject = new MarkerObject();
                markerObject.setMarkerID(marker.getString(Keys.MARKER_ID));
                markerObject.setMarkerName(marker.getString(Keys.MARKER_NAME));
                markerObject.setMarkerAddress(marker.getString(Keys.MARKER_ADDRESS));

                //fetch category object
                JSONObject categoryObject = marker.getJSONObject(Keys.MARKER_CATEGORY);
                markerObject.setMarkerCategory(categoryObject.getString(Keys.CAT_NAME));
                markerObject.setMarkerLatitude(marker.getDouble(Keys.MARKER_LATITUDE));
                markerObject.setMarkerLongitude(marker.getDouble(Keys.MARKER_LONGITUDE));

                //fetch marker facilities through jsonarray
                JSONArray marker_facilities = marker.getJSONArray(Keys.MARKER_FACILITIES);
                ArrayList<String> marker_facilities_list = new ArrayList<>();
                for(int j=0; j<marker_facilities.length(); j++)
                {
                    JSONObject marker_fac_object = marker_facilities.getJSONObject(j);
                    marker_facilities_list.add(marker_fac_object.getString(Keys.FAC_NAME));
                }

                //set marker facilities
                markerObject.setMarkerFacilities(marker_facilities_list);
                markersTable.SaveRecord(markerObject);
                CacheData.cacheMarkers.add(markerObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        markersTable.CloseConnection();
    }

    public static void parseReviews(Context contexts, String placeId, JSONArray reviews)
    {
        //Initialise the cache variable
        if(CacheData.cacheAllReviews == null)
        {
            CacheData.cacheAllReviews = new ArrayList<>();
        }
        ReviewsTable reviewsTable = new ReviewsTable(context);
        context = contexts;
        System.out.println("Reaching the statics part of reviews");
        ReviewsObject reviewsObject = new ReviewsObject();
        reviewsObject.setPlaceId(placeId);
        ArrayList<String> reviews_array = new ArrayList<>();
        for(int i=0; i< reviews.length();i++)
        {
            try {
                reviews_array.add(reviews.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            reviewsObject.setReviews(reviews_array);
            reviewsTable.SaveRecord(reviewsObject);
            System.out.println("Statcis Review: "+reviews_array.toString());
            CacheData.cacheAllReviews.add(reviewsObject);
            reviewsTable.CloseConnection();
        }
    }
}