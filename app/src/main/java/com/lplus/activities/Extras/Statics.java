package com.lplus.activities.Extras;

import android.content.Context;
import android.content.SharedPreferences;

import com.lplus.activities.Macros.Keys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Sai_Kameswari on 19-03-2018.
 */

public class Statics {
    private static Set<String> categories_key;
    private static Set<String> facilities_key;
    private static Set<String> categories_value;
    private static Set<String> facilities_value;
    private static SharedPreferences app_sharePref;

    public static void onFailInit()
    {
        categories_key=new HashSet<>();
        categories_value = new HashSet<>();

        facilities_key=new HashSet<>();
        facilities_value = new HashSet<>();
    }

    public static void Init(Context context, JSONArray categoriesarray, JSONArray facilitiesarray)
    {
        app_sharePref = context.getSharedPreferences(Keys.SHARED_PREF_NAME, MODE_PRIVATE);
        setCategories(categoriesarray);
        setFacilities(facilitiesarray);
    }

    public static void setCategories(JSONArray categoriesarray)
    {
        categories_key=new HashSet<>();
        categories_value = new HashSet<>();

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
        SharedPreferences.Editor edit = app_sharePref.edit();
        edit.putStringSet(Keys.CATEGORY_KEY, categories_key);
        edit.putStringSet(Keys.CATEGORY_VALUE, categories_value);
        edit.commit();
    }

    public static void setFacilities(JSONArray facilitiesarray)
    {
        facilities_key=new HashSet<>();
        facilities_value = new HashSet<>();

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
        SharedPreferences.Editor edit = app_sharePref.edit();
        edit.putStringSet(Keys.FACILITIES_KEY, facilities_key);
        edit.putStringSet(Keys.FACILITIES_VALUE, facilities_value);
        edit.commit();
    }
}
